# Architecture

## High-Level Overview

```
┌────────────────────────────────────────────────────────────┐
│                     Minecraft Server                        │
│  ┌──────────────┐    ┌─────────────────────────────────┐   │
│  │  CrucialLib   │◄───│           MyTrip Plugin          │   │
│  │  (dependency) │    │                                   │   │
│  │  - CrucialItem│    │  ┌───────────┐  ┌────────────┐  │   │
│  │  - Page/GUI   │    │  │  Commands  │  │   Events    │  │   │
│  │  - Json I/O   │    │  │  /mytrip   │  │ Interact/   │  │   │
│  │  - Localizer  │    │  │  subcommands│ │ Craft/Death │  │   │
│  │  - Stats      │    │  └─────┬──────┘  └──────┬──────┘  │   │
│  └──────────────┘    │        │                  │         │   │
│                       │  ┌─────▼──────────────────▼──────┐ │   │
│                       │  │       Drug System Core         │ │   │
│                       │  │  MyDrug, DrugPlayer, Addiction │ │   │
│                       │  │  DrugTool, GUI Inventories     │ │   │
│                       │  └──────────────┬────────────────┘ │   │
│                       │                 │                   │   │
│                       │  ┌──────────────▼────────────────┐ │   │
│                       │  │     Persistence (JSON)         │ │   │
│                       │  │  drugs.json, tools.json,       │ │   │
│                       │  │  playerdata.json, config.yml   │ │   │
│                       │  └───────────────────────────────┘ │   │
│                       └─────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────┘
```

## Plugin Lifecycle

### onLoad()
1. `Crucial.init()` — checks if CrucialLib plugin is present and version is compatible
2. If missing/incompatible, disables the plugin immediately

### onEnable()
1. `Crucial.connect()` — establishes connection to CrucialLib, enables it if needed
2. `Server.checkCompatibility()` — warns if server version is unsupported
3. `loadConfig()` — sets defaults in config.yml, handles offline mode
4. `FileManager()` — creates data directories, downloads default drugs/tools JSON if absent
5. `fileManager.loadFiles()` — deserializes drugs, tools, and player data from JSON
6. `CustomMessages()` + `MessagesYaml.create()` — initializes localization system
7. Registers event listeners: `InteractionEvents`, `DrugToolEvents`, `FeatureEvents`
8. Registers command: `/mytrip` → `CommandListener`
9. Initializes bStats metrics

### Runtime
- Event-driven: Bukkit events trigger drug consumption, crafting, testing, antitoxin use
- Scheduled tasks: Addiction loops via `BukkitRunnable.runTaskTimer()`
- GUI interactions: CrucialLib `Page` system handles inventory click events

### onDisable()
1. Cancels all scheduled tasks
2. Saves drugs, tools, and player data to JSON files

## Package Structure

### `de.chafficplugins.mytrip`
Main plugin class. Entry point for lifecycle management.

### `de.chafficplugins.mytrip.api`
Developer-facing API. External plugins can register `MyTripEvent` implementations via `APICaller` to hook into drug consumption, tool usage, and drug set events. Currently supports:
- `DrugAPIEvents` — `onDrugStart()`, `onDrugEnd()`
- `DrugToolAPIEvents` — `onDrugTest()`, `onAntiToxin()`, `onDrugCraftPrepare()`
- `DrugSetAPIEvents` — `onDrugSetOpen()`

### `de.chafficplugins.mytrip.drugs.objects`
Core data models. All extend `CrucialItem` (from CrucialLib) which provides UUID-based item identification, custom skull textures, recipe storage, and registration lifecycle.

- **MyDrug**: Drug definition with effects, recipe, timing, overdose/addiction thresholds
- **DrugPlayer**: Per-player drug state. Static `playerData` list is the in-memory store.
- **DrugTool**: Special tools (drug set, drug test, antitoxin) identified by hardcoded UUIDs
- **Addiction**: Models addiction to a specific drug with intensity-based damage loops

### `de.chafficplugins.mytrip.drugs.commands`
Command handling. Single `/mytrip` command with subcommands routed by `CommandListener`:
- `help` — list commands
- `info` — plugin version info
- `list` / `drugs` / `ls` / `show` — open drug list GUI
- `create <name>` — open drug creation GUI
- `recover [player]` — remove active drug effects
- `give <drug> [player]` — give drug item
- `addictions clear|list|add <drug> [player]` — manage addictions

### `de.chafficplugins.mytrip.drugs.events`
Bukkit event listeners:
- **InteractionEvents**: `PlayerInteractEvent` (drug consumption on right-click), `PrepareItemCraftEvent` (recipe validation with drug set logic)
- **DrugToolEvents**: `PlayerInteractEntityEvent` (drug test on entity), `PlayerInteractEvent` (drug set opens workbench), `PlayerItemConsumeEvent` (antitoxin clears effects, milk blocked while high)
- **FeatureEvents**: `PlayerDeathEvent` (optional heal-on-death), `PlayerJoinEvent` (init DrugPlayer, restart addiction loops)

### `de.chafficplugins.mytrip.drugs.inventories`
CrucialLib `Page` subclasses for in-game GUIs:
- **DrugList**: 6-row inventory listing all registered drugs
- **DrugShow**: Shows recipe grid, effects, and stats for a single drug; has edit/delete buttons
- **DrugEdit**: Increment/decrement duration, delay, overdose, addiction probability
- **DrugCraft** (creation): Drag-and-drop recipe grid + result slot
- **DrugEffects** (creation): Click to select potion effects for new drug

### `de.chafficplugins.mytrip.io`
File persistence:
- **FileManager**: Manages `do not edit/` directory. Downloads defaults from Google Drive. Delegates to CrucialLib's `Json` utility for serialization.
- **MessagesYaml**: Creates and populates `messages.yml` with default localized strings.

### `de.chafficplugins.mytrip.utils`
Utilities:
- **ConfigStrings**: Central constants — config keys, permission strings, message keys, UUIDs, URLs
- **Crucial**: CrucialLib presence/version checking and connection
- **CustomMessages**: Wraps CrucialLib's `LocalizedFromYaml` for message localization
- **MathUtils**: `randomInt(min, max)` utility
- **PlayerUtils**: `hasOnePermissions()` — permission check respecting admin override and config toggle

## Drug System

### Data Flow: Drug Consumption
```
Player right-clicks drug item
    │
    ▼
InteractionEvents.onDrugConsume()
    │ Permission check via PlayerUtils
    ▼
MyDrug.doDrug(player, itemStack)
    │ Decrement item, play sound
    │ DrugPlayer.consume(drug) → track dose + addiction
    │
    ├── If dose >= 1.0: apply overdose effects from config
    │
    ▼ (after effectDelay ticks)
doEffects()
    │ Apply each potion effect for duration
    │
    ▼ (after duration ticks)
DrugPlayer.subDose(drug) → reduce dose
```

### Data Flow: Addiction
```
DrugPlayer.consume(drug)
    │ Random roll vs drug.addictionProbability
    │
    ├── New addiction: Addiction(drugId, intensity=1, playerId)
    │   └── Starts loop()
    │
    └── Existing addiction: consumed() → may increase intensity
        └── loop() already running

Addiction.loop() [repeating BukkitRunnable]
    │ Every (16000/intensity) ticks:
    │ - Action bar message
    │ - player.damage(intensity)
    │ - If intensity > 5: apply addiction effects from config
    │ - Show addiction title
    │ - Reschedule loop
```

## CrucialLib Integration Points

| CrucialLib Class | Used By | Purpose |
|-----------------|---------|---------|
| `CrucialItem` | `MyDrug`, `DrugTool` | UUID-based custom item with recipe, name, material |
| `Page` | All inventory classes | GUI framework with click handlers |
| `InventoryItem` | All inventory classes | GUI slot items with click callbacks |
| `Stack` | `DrugShow` | ItemStack builder with display name and lore |
| `Json` | `FileManager` | Gson-based JSON file serialization |
| `LocalizedFromYaml` | `CustomMessages` | YAML-based message localization |
| `Localizer` | `CustomMessages` | Static localized string retrieval |
| `Server` | `MyTrip` | Server version compatibility check |
| `Stats` | `MyTrip` | bStats metrics |
| `VisualEffects` | `DrugToolEvents` | Remove blood visual effect on antitoxin |

## Config System

`config.yml` is managed by Bukkit's standard `getConfig()`:
1. `loadConfig()` sets defaults via `addDefault()`
2. `options().copyDefaults(true)` writes defaults to file
3. Accessed at runtime via `getConfigBoolean()`, `getConfigString()`, `getConfigStringList()`
4. Offline mode auto-disables drug set feature

Drug/tool data is stored as JSON via CrucialLib's `Json` class (Gson underneath). Player data is a separate JSON file.
