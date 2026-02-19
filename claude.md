# MyTrip

MyTrip is a Spigot/Bukkit plugin for Minecraft that adds a custom drug system to the game. It is developed by ChafficPlugins (since April 1, 2020) and has been open-source under the GPL-3.0 license since April 19, 2021.

## Overview

MyTrip allows server administrators to create custom drugs with configurable potion effects, crafting recipes, addiction mechanics, and overdose thresholds. Players can consume drugs, become addicted, overdose, and use tools like drug tests and antitoxins. The plugin provides both in-game GUI interfaces and chat commands for drug management.

**Version:** 0.7.6
**API Version:** Spigot 1.15+ (compatible with 1.15 through 1.20)
**Java:** 16+
**License:** GPL-3.0
**Build System:** Maven

## Dependencies

| Dependency | Version | Scope |
|---|---|---|
| Spigot API | 1.20.1-R0.1-SNAPSHOT | provided |
| CrucialAPI | 02caf0c41f (requires 2.2.0+) | provided (soft dependency) |
| JetBrains Annotations | 19.0.0 | compile |

CrucialAPI is a required runtime dependency that provides:
- `CrucialItem` - base class for custom items (drugs and tools)
- `Page` - inventory GUI system
- `Json` - JSON serialization/deserialization
- `LocalizedFromYaml` - message localization
- `Server` - server version compatibility checking
- `Stats` - bStats metrics integration
- `VisualEffects` - player visual effects

## Building

```bash
mvn clean package
```

Output: `target/MyTrip-v0.7.6.jar`

The build uses maven-shade-plugin to bundle compile-scope dependencies and maven-compiler-plugin targeting Java 14. JitPack CI is configured via `jitpack.yml` using OpenJDK 17.

## Project Structure

```
src/main/java/de/chafficplugins/mytrip/
├── MyTrip.java                          # Main plugin entry point (JavaPlugin)
├── api/                                 # Public API for third-party plugins
│   ├── APICaller.java                   # Event listener registry
│   ├── DrugAPIEvents.java               # Drug consumption events
│   ├── DrugSetAPIEvents.java            # Drug set interaction events
│   ├── DrugToolAPIEvents.java           # Tool interaction events (abstract)
│   └── MyTripEvent.java                 # Base event class
├── drugs/
│   ├── commands/
│   │   ├── CommandListener.java         # CommandExecutor for /mytrip
│   │   └── Commands.java                # Command implementations
│   ├── events/
│   │   ├── DrugToolEvents.java          # Drug test, drug set, antitoxin listeners
│   │   ├── FeatureEvents.java           # Death and join event listeners
│   │   └── InteractionEvents.java       # Drug consumption and crafting listeners
│   ├── inventories/
│   │   ├── DrugEdit.java                # GUI for editing drug properties
│   │   ├── DrugList.java                # GUI listing all registered drugs
│   │   ├── DrugShow.java                # GUI showing drug details (recipe, effects, info)
│   │   └── drugCreation/
│   │       ├── DrugCraft.java           # GUI for setting drug recipe (3x3 grid + result)
│   │       └── DrugEffects.java         # GUI for selecting potion effects
│   └── objects/
│       ├── Addiction.java               # Addiction model (intensity, damage loop)
│       ├── DrugPlayer.java              # Per-player drug state (dose, addictions)
│       ├── DrugTool.java                # Tool items (drug set, test kit, antitoxin)
│       └── MyDrug.java                  # Drug definition (effects, recipe, properties)
├── io/
│   ├── FileManager.java                 # JSON file loading/saving, initial file download
│   └── MessagesYaml.java               # messages.yml creation with defaults
└── utils/
    ├── ConfigStrings.java               # All constants: permissions, config keys, UUIDs, messages
    ├── Crucial.java                     # CrucialAPI connection and version checking
    ├── CustomMessages.java              # Localization wrapper (extends LocalizedFromYaml)
    ├── MathUtils.java                   # Random number utility
    └── PlayerUtils.java                 # Permission checking utility
```

## Plugin Lifecycle

**Entry point:** `de.chafficplugins.mytrip.MyTrip` (extends `JavaPlugin`)

### onLoad
- Calls `Crucial.init()` to verify CrucialAPI is present and the correct version (2.2.0+)

### onEnable
1. Calls `Crucial.connect()` to enable CrucialAPI
2. Checks server version compatibility (1.15-1.20)
3. Loads `config.yml` with defaults
4. Creates `FileManager` which sets up the `do not edit/` data directory and downloads default `drugs.json` and `tools.json` from Google Drive if missing
5. Loads all data: `MyDrug.loadAll()`, `DrugTool.loadAll()`, `DrugPlayer.loadAll()`
6. Initializes `CustomMessages` and creates `messages.yml`
7. Registers event listeners: `InteractionEvents`, `DrugToolEvents`, `FeatureEvents`
8. Registers the `/mytrip` command with `CommandListener`
9. Initializes bStats metrics (ID: 7038)

### onDisable
1. Cancels all scheduled Bukkit tasks
2. Saves all data files via `FileManager.saveFiles()`

## Configuration

### config.yml

| Key | Type | Default | Description |
|---|---|---|---|
| `features.heal_on_death` | boolean | `true` | Clear addictions when a player dies |
| `settings.permissions` | boolean | `false` | Enable permission-based access control |
| `settings.update-alerts` | boolean | `true` | Show update notifications to admins |
| `disable_drug_set` | boolean | `false` | Disable the drug set tool (forced `true` in offline mode) |
| `addiction_effects` | string list | `["CONFUSION:0"]` | Potion effects applied at high addiction (intensity > 5) |
| `overdose_effects` | string list | `["BLINDNESS:0", "NAUSEA:0", "SLOW:0", "SLOW_DIGGING:0", "WEAKNESS:0"]` | Potion effects applied on overdose |

When the server runs in offline mode, `disable_drug_set` is forced to `true` and the drug set cannot be used.

### messages.yml

All player-facing messages are configurable. The file is auto-created with English defaults. Messages support `{0}`, `{1}` placeholders for dynamic values. Key messages include:

- `only_players_cmd` - "Only players can use this command!"
- `no_permission` - "You don't have the permission to use this command."
- `recoverd` - "You have been recovered!"
- `drug_not_exist` - "The drug {0} does not exist"
- `given_drug` - "You have been given {0}!"
- `addiction_kicks` - "Your addiction to {0} kicks in!"
- `effects_start_in` - "The drugs effects will start in {0} seconds!"
- `is_high` - "{0} is high!"
- `is_not_high` - "{0} is not high!"
- `was_deleted` - "{0} was deleted!"

(See `MessagesYaml.java` for the full list of 28 message keys.)

## Data Storage

All data is persisted as JSON in the `plugins/MyTrip/do not edit/` directory using CrucialAPI's `Json` utility.

### drugs.json
Array of `MyDrug` objects (extends `CrucialItem`). Each drug has:
- `id` (UUID), `name`, `material`, `recipe` (9-element string array of material names)
- `effects` (array of `[PotionEffectType, strength]` pairs)
- `commands` (array of command strings)
- `duration` (seconds), `effectDelay` (seconds), `overdose` (threshold 0-99), `addict` (probability 0-100%)
- `particle`, `isRegistered`

### playerdata.json
Array of `DrugPlayer` objects:
- `uuid` (player UUID), `dose` (double, 0.0 to 1.0+), `addictions` (array of `Addiction` objects)

### tools.json
Array of `DrugTool` objects (extends `CrucialItem`). Three hardcoded tools exist with fixed UUIDs:
- **Drug Set** (`2e116c45-8bd6-4297-a8c1-98041c08d39c`) - opens a crafting workbench for drug recipes
- **Drug Test** (`764d1358-32d9-4f8b-af6c-c5d64de2bfd0`) - right-click a player to check if they are under the influence
- **Antitoxin** (`8000f544-c0db-4af2-aea5-80fa8bc53aaa`) - consumable that clears all effects, addictions, and dose

## Commands

Base command: `/mytrip` (alias: `/mt`)
Requires `mytrip.admin` permission for the base command.

| Command | Description | Permission |
|---|---|---|
| `/mytrip help` | Show command help | `mytrip.help` |
| `/mytrip info` | Show plugin version, author, website | none |
| `/mytrip list` | Open drug list GUI (aliases: `drugs`, `ls`, `show`) | `mytrip.list` |
| `/mytrip create <name>` | Create a new drug (opens recipe builder GUI) | `mytrip.create` |
| `/mytrip recover [player]` | Clear potion effects and reset dose | `mytrip.recover.self` / `mytrip.recover.other` |
| `/mytrip give <drug> [player]` | Give a drug item to a player | `mytrip.give.self` / `mytrip.give.others` |
| `/mytrip addictions clear [player]` | Clear all addictions | `mytrip.addictions.clear.self` / `mytrip.addictions.clear.others` |
| `/mytrip addictions list [player]` | List a player's addictions with intensity | `mytrip.addictions.list.self` / `mytrip.addictions.list.others` |
| `/mytrip addictions add <drug> [player]` | Manually add an addiction | `mytrip.addictions.add.self` / `mytrip.addictions.add.others` |

When `[player]` is omitted, the command targets the caller. All commands are player-only (no console support).

## Permissions

Permissions are **disabled by default** (`settings.permissions: false`). When disabled, all players can use all features. When enabled:

| Permission | Description |
|---|---|
| `mytrip.*` | Full admin access (bypasses all checks) |
| `mytrip.use.*` | Use any drug or tool |
| `mytrip.use.<name>` | Use a specific drug or tool |
| `mytrip.craft.*` | Craft any drug or tool |
| `mytrip.craft.<name>` | Craft a specific drug or tool |
| `mytrip.help` | Use `/mytrip help` |
| `mytrip.list` | Use `/mytrip list` |
| `mytrip.create` | Use `/mytrip create` |
| `mytrip.recover` | Recover self or others |
| `mytrip.recover.self` | Recover self only |
| `mytrip.recover.other` | Recover other players |
| `mytrip.give` | Give drugs to self or others |
| `mytrip.give.self` | Give drugs to self only |
| `mytrip.give.others` | Give drugs to other players |
| `mytrip.addictions` | Full addictions command access |
| `mytrip.addictions.clear` | Clear addictions (self or others) |
| `mytrip.addictions.clear.self` | Clear own addictions |
| `mytrip.addictions.clear.others` | Clear others' addictions |
| `mytrip.addictions.list` | List addictions (self or others) |
| `mytrip.addictions.list.self` | List own addictions |
| `mytrip.addictions.list.others` | List others' addictions |
| `mytrip.addictions.add` | Add addictions (self or others) |
| `mytrip.addictions.add.self` | Add addiction to self |
| `mytrip.addictions.add.others` | Add addiction to others |

OP players and players with `mytrip.*` always bypass permission checks. Permission checking is done in `PlayerUtils.hasOnePermissions()`.

## Core Mechanics

### Drug Consumption
1. Player right-clicks while holding a registered `MyDrug` item
2. `InteractionEvents.onDrugConsume` fires, checks `mytrip.use.*` / `mytrip.use.<drug>` permission
3. `MyDrug.doDrug()` is called:
   - Plays honey bottle drink sound
   - Shows action bar message with effect delay countdown
   - Decrements item stack by 1
   - Calls `DrugPlayer.consume()` to update dose and check for overdose/addiction
   - After `effectDelay` seconds (scheduled task), applies all configured potion effects for `duration` seconds
   - If player already has the same effect, durations stack
   - After `duration` seconds, reduces the player's dose back down

### Dose and Overdose
- Each drug consumption adds `1 / overdose_threshold` to the player's dose
- When dose reaches >= 1.0, the player overdoses
- Overdose applies all effects from `overdose_effects` config for 100 seconds
- Dose decreases by the same amount after the drug's duration expires
- Player dose resets to 0 on join

### Addiction System
- On drug consumption, there is an `addict`% chance of becoming addicted (default 5%)
- If already addicted to that drug, consuming it has an `addict`% chance to increase addiction intensity by 1
- Addiction intensity ranges from 1 to 8 (capped)
- The addiction loop runs as a repeating Bukkit task at an interval of `16000 / intensity` ticks
- Each loop tick:
  - Deals damage equal to the intensity value
  - Shows an action bar message: "Your addiction to [drug] kicks in!"
  - Displays a title screen with "Addiction" / "You are addicted to [drug]!"
  - If intensity > 5, applies configured `addiction_effects` potion effects for `60 * intensity` ticks
- Addictions persist across sessions (saved in `playerdata.json`)
- Addictions resume their loops when a player joins

### Drug Crafting
- When `disable_drug_set` is `false`: drugs can only be crafted in a regular crafting table while holding the Drug Set item in the main hand
- When `disable_drug_set` is `true` (or offline mode): drugs can be crafted in any crafting table without the Drug Set
- Drug tools (Drug Set, Drug Test, Antitoxin) can always be crafted in a regular crafting table
- Crafting respects `mytrip.craft.*` / `mytrip.craft.<name>` permissions
- Milk buckets are blocked from being consumed while a player has an active dose (prevents clearing effects)

### Drug Tools
- **Drug Set**: Right-click to open a virtual crafting workbench. Required to craft drugs (unless disabled).
- **Drug Test**: Right-click on another player to check if their dose > 0. Reports whether the target "is high" or "is not high".
- **Antitoxin**: Consumable item that clears all potion effects, removes all addictions, resets dose to 0, and grants temporary DAMAGE_RESISTANCE, SATURATION, and LUCK effects (6 seconds each at level 2).

### Feature Events
- **Death**: If `features.heal_on_death` is `true`, all addictions are cleared on death
- **Join**: Player data is initialized/loaded, dose is reset to 0, and addiction loops are restarted

## GUI System

All inventory GUIs extend `Page` from CrucialAPI and use 6-row (54-slot) inventories with white stained glass pane backgrounds.

### Drug List (`/mytrip list`)
- Shows all registered drugs as clickable items
- Left-click a drug to open its DrugShow page
- Singleton pattern: reused across players

### Drug Show
- Displays the drug's 3x3 crafting recipe (slots 10-12, 19-21, 28-30)
- Shows the result item (slot 24)
- Shows effects list (slot 48, lingering potion icon)
- Shows information: overdose threshold, addiction probability, duration, effect delay (slot 50, clock icon)
- EDIT button (slot 52) opens DrugEdit
- DELETE button (slot 53) permanently removes the drug
- BACK button (slot 45) returns to DrugList

### Drug Edit
- Editable properties with -1/+1 red wool/green wool buttons:
  - **Duration** (slots 3-5, clock icon)
  - **Effect Delay** (slots 12-14, comparator icon)
  - **Overdose** (slots 21-23, wither skeleton skull icon)
  - **Addiction Probability** (slots 30-32, spider eye icon)
- APPLY button (slot 53) saves changes and returns to DrugShow

### Drug Creation (`/mytrip create <name>`)

**Step 1 - DrugCraft**: Place ingredients in a 3x3 grid (slots 10-12, 19-21, 28-30) and a result item (slot 24). Items are movable. Click Continue (slot 44) to proceed. Requires at least one ingredient and a result item.

**Step 2 - DrugEffects**: Select potion effects from 28 available effect buttons (slots 0-32). Clicking an effect adds it with strength 1 and removes the button. RESET (slot 40) clears selections. BACK (slot 45) returns to recipe. CREATE (slot 53) finalizes the drug with default values: duration=100s, effectDelay=2s, addictionProbability=5%, overdose=5.

Available effects: ABSORPTION, BLINDNESS, CONFUSION, DAMAGE_RESISTANCE, FAST_DIGGING, FIRE_RESISTANCE, GLOWING, HARM, HEAL, HEALTH_BOOST, HUNGER, INCREASE_DAMAGE, INVISIBILITY, JUMP, LEVITATION, LUCK, NIGHT_VISION, POISON, REGENERATION, SATURATION, SLOW, SLOW_DIGGING, SLOW_FALLING, UNLUCK, WATER_BREATHING, WEAKNESS, WITHER, SPEED.

## Developer API

Third-party plugins can listen to MyTrip events by extending event classes and registering them.

### Registration

```java
APICaller.registerEvent(new MyCustomDrugEvent());
APICaller.unregisterEvent(myEvent); // only DrugToolAPIEvents can be unregistered
```

### Event Classes

**DrugAPIEvents** (extend this class):
- `boolean onDrugStart(Player p, MyDrug drug)` - called before drug effects are applied. Return `true` to cancel.
- `void onDrugEnd(Player p, MyDrug drug)` - called after drug effects expire.

**DrugSetAPIEvents** (extend this class):
- `boolean onDrugSetOpen(Player p, BlockState state)` - called when a player opens the drug set workbench. Return `true` to cancel.

**DrugToolAPIEvents** (extend this abstract class):
- `boolean onDrugTest(Player tester, DrugPlayer tested, boolean isPositive)` - called when a drug test is used. Return `true` to cancel.
- `boolean onAntiToxin(Player player, Collection<PotionEffect> activePotionEffects)` - called when antitoxin is consumed. Return `true` to cancel.
- `boolean onDrugCraftPrepare(LivingEntity entity, BlockState drugSetBlock, CrucialItem result, CraftingInventory inventory)` - called during craft preparation. Return `true` to cancel.

### Maven Dependency (via JitPack)

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.ChafficPlugins</groupId>
    <artifactId>MyTrip</artifactId>
    <version>TAG</version>
    <scope>provided</scope>
</dependency>
```

## Key Implementation Details

- All chat messages use the prefix `§7MyTrip §8» ` (gray "MyTrip" + dark gray arrow)
- Drug items display with white text when registered, red text when unregistered (in creation)
- Sound effects: lever click for UI interactions, honey bottle drink for consumption, ambient cave for effect onset, note block bell for edits, bass note for errors, level up for drug creation
- The plugin uses Gson (via CrucialAPI) for JSON serialization with `TypeToken` for generic type handling
- bStats metrics tracking ID: 7038
- Default drug/tool data files are downloaded from Google Drive on first setup
- Unregistered drugs (mid-creation) are tracked in a separate `Set<MyDrug>` and removed upon registration
