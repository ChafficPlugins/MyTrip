# CLAUDE.md - MyTrip Plugin

## Project Overview

MyTrip is a Spigot plugin that adds drugs to Minecraft. Players can craft custom substances via a drug set (portable crafting table), consume them to receive potion effects, develop addictions, and overdose. Admins can create new drugs through an in-game GUI, manage player addictions, and configure all behavior through config files.

## Dependencies

- **Spigot API**: 1.21.4-R0.1-SNAPSHOT
- **CrucialLib** (plugin.yml softdepend: `CrucialAPI`): v3.0.0 — provides custom items (`CrucialItem`), GUI pages, localization, JSON I/O, and visual effects
- **Java**: 21
- **Gson** (transitive via CrucialLib): JSON serialization for drug/tool/player data

## Build & Test

```bash
mvn clean package    # Build the plugin JAR
mvn test             # Run unit tests
mvn clean verify     # Full build + test cycle
```

Output JAR: `target/MyTrip-v0.7.6.jar`

## Project Structure

```
src/main/java/de/chafficplugins/mytrip/
├── MyTrip.java                          # Plugin main class (onLoad, onEnable, onDisable)
├── api/                                 # Developer API for external plugin integration
│   ├── APICaller.java                   # Event registry for API consumers
│   ├── MyTripEvent.java                 # Base event marker class
│   ├── DrugAPIEvents.java              # Hooks for drug start/end
│   ├── DrugToolAPIEvents.java          # Hooks for drug test, antitoxin, craft
│   └── DrugSetAPIEvents.java           # Hooks for drug set opening
├── drugs/
│   ├── commands/
│   │   ├── CommandListener.java         # /mytrip command router (implements CommandExecutor)
│   │   └── Commands.java               # Command implementations (help, list, give, recover, create, addictions)
│   ├── events/
│   │   ├── InteractionEvents.java       # Drug consumption (right-click) and crafting validation
│   │   ├── DrugToolEvents.java          # Drug test, drug set, and antitoxin interactions
│   │   └── FeatureEvents.java           # Player death (heal on death) and join (reset dose, start addiction loops)
│   ├── inventories/
│   │   ├── DrugList.java                # GUI page listing all registered drugs
│   │   ├── DrugShow.java               # GUI page showing a single drug's recipe, effects, and stats
│   │   ├── DrugEdit.java               # GUI page for editing drug duration, delay, overdose, addiction
│   │   └── drugCreation/
│   │       ├── DrugCraft.java           # GUI page for setting drug crafting recipe + material
│   │       └── DrugEffects.java         # GUI page for selecting drug potion effects
│   └── objects/
│       ├── MyDrug.java                  # Drug model (extends CrucialItem) — effects, recipe, duration, overdose, etc.
│       ├── DrugPlayer.java              # Per-player drug state — dose tracking, addictions list
│       ├── DrugTool.java                # Tool model (drug set, drug test, antitoxin) — extends CrucialItem
│       └── Addiction.java               # Addiction model — intensity, periodic damage loop
├── io/
│   ├── FileManager.java                 # JSON file I/O for drugs, tools, player data
│   └── MessagesYaml.java               # messages.yml default generation and loading
└── utils/
    ├── ConfigStrings.java               # All config keys, message keys, permission strings, UUIDs
    ├── Crucial.java                     # CrucialLib connection and version checking
    ├── CustomMessages.java              # Localization wrapper (CrucialLib LocalizedFromYaml)
    ├── MathUtils.java                   # Random int utility
    └── PlayerUtils.java                 # Permission checking utility
```

## Key Classes

| Class | Responsibility |
|-------|---------------|
| `MyTrip` | Plugin lifecycle — loads CrucialLib, config, files, registers events/commands |
| `MyDrug` | Drug data model: name, material, recipe[9], effects[], duration, effectDelay, overdose threshold, addiction probability |
| `DrugPlayer` | Per-player state: UUID, addictions list, current dose level |
| `Addiction` | Addiction to a specific drug: intensity (1-8), periodic damage + effects via BukkitRunnable loop |
| `DrugTool` | Special tools (drug set, drug test, antitoxin) — identified by hardcoded UUIDs |
| `CommandListener` | Routes `/mytrip <subcommand>` to the appropriate handler |
| `Commands` | Implements all subcommands with permission checks |
| `InteractionEvents` | Handles right-click drug consumption and crafting table recipe validation |
| `DrugToolEvents` | Handles drug test (entity right-click), drug set (block right-click), antitoxin (consume) |
| `FeatureEvents` | Heal-on-death clearing and join-time initialization |
| `FileManager` | Downloads default drug/tool JSON from Google Drive, saves/loads via CrucialLib's Json utility |
| `ConfigStrings` | Central constants for config keys, permissions, message keys, UUIDs |

## How Drugs Work

### Drug Definition
Drugs are instances of `MyDrug extends CrucialItem` with:
- `effects`: ArrayList of `String[]{effectName, strength}` — PotionEffectType names
- `duration`: seconds the effects last
- `effectDelay`: seconds before effects start after consumption
- `overdose`: number of concurrent doses before overdose triggers
- `addict`: percentage probability of developing/increasing addiction per use
- `recipe[9]`: 3x3 crafting grid of Material names
- `material`: the Material used as the drug's item appearance

### Drug Consumption Flow
1. Player right-clicks with a registered drug item → `InteractionEvents.onDrugConsume()`
2. Permission check → `MyDrug.doDrug(player, itemStack)`
3. Item amount decremented, drink sound played
4. `DrugPlayer.consume()` tracks dose and addiction probability
5. If dose >= 1.0 (overdose), apply overdose effects from config
6. After `effectDelay` seconds, apply drug's potion effects for `duration` seconds
7. After effects expire, `DrugPlayer.subDose()` reduces dose

### Addiction System
- On consumption, random roll against `addictionProbability` — may create new `Addiction` or increase existing intensity
- `Addiction.loop()` runs a repeating BukkitRunnable: deals damage, applies config-defined addiction effects (if intensity > 5), sends title/action bar messages
- Loop interval = `16000 / intensity` ticks (shorter interval = more severe)
- Addictions persist across sessions via JSON serialization

### Crafting
- If drug set is enabled (default): drugs can only be crafted while holding the drug set item, which opens a workbench
- If drug set is disabled (offline mode or config): drugs can be crafted in any crafting table
- Recipe validation happens in `InteractionEvents.onDrugCraft()` (PrepareItemCraftEvent)

## Configuration

### config.yml
```yaml
features.heal_on_death: true      # Clear addictions on death
settings.permissions: false        # Enable permission checks (default: off = OP only for commands)
settings.update-alerts: true       # Update notifications
disable_drug_set: false            # Disable the drug set tool (auto-disabled in offline mode)
addiction_effects:                  # Effects applied when addiction intensity > 5
  - "CONFUSION:0"
overdose_effects:                  # Effects applied on overdose
  - "BLINDNESS:0"
  - "NAUSEA:0"
  - "SLOW:0"
  - "SLOW_DIGGING:0"
  - "WEAKNESS:0"
```

### Data Files (in `plugins/MyTrip/do not edit/`)
- `drugs.json` — serialized ArrayList of MyDrug
- `tools.json` — serialized ArrayList of DrugTool
- `playerdata.json` — serialized ArrayList of DrugPlayer

## Important Behaviors

- CrucialLib is checked in `onLoad()` and connected in `onEnable()`. If CrucialLib is missing or wrong version, the plugin disables itself.
- The drug set is automatically disabled when the server runs in offline mode (no UUID-based player identification).
- Default drug and tool data is downloaded from Google Drive on first run if not present.
- Milk bucket consumption is cancelled if the player has an active drug dose (prevents easy effect removal).
- Player dose resets to 0 on join, but addictions persist and their loops restart.
- The `PotionEffectType.getByName()` calls use old Bukkit effect names (CONFUSION, SLOW, etc.) for backward compatibility with existing configs and stored drug data.

## CI

CI must pass before merge. The GitHub Actions workflow runs `mvn -B verify` on Java 21.

## Wiki

User-facing documentation is at [github.com/ChafficPlugins/MyTrip/wiki](https://github.com/ChafficPlugins/MyTrip/wiki) covering setup, commands, permissions, drug creation, and features.
