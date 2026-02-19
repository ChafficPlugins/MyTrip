# Configuration

## config.yml

The main configuration file is located at `plugins/MyTrip/config.yml`. It is generated on first startup with default values.

### Settings

| Option | Type | Default | Description |
|---|---|---|---|
| `settings.permissions` | boolean | `false` | Enable permission-based access control. When `false`, all players can craft and use drugs; only OP players can use admin commands. When `true`, permissions are enforced for all actions. |
| `settings.update-alerts` | boolean | `true` | Enable update notifications for admins on join. |

### Features

| Option | Type | Default | Description |
|---|---|---|---|
| `features.heal_on_death` | boolean | `true` | Clear all addictions and drug effects when a player dies. |
| `disable_drug_set` | boolean | `false` | Disable the Drug Set crafting station. When `true`, drugs can be crafted in any crafting table. Automatically set to `true` on offline-mode servers. |

### Addiction Effects

The `addiction_effects` list defines the potion effects applied to addicted players when their addiction intensity exceeds 5. Format: `EFFECT_NAME:AMPLIFIER`.

```yaml
addiction_effects:
  - "CONFUSION:0"
```

### Overdose Effects

The `overdose_effects` list defines the potion effects applied when a player overdoses (dose reaches 100%). Format: `EFFECT_NAME:AMPLIFIER`.

```yaml
overdose_effects:
  - "BLINDNESS:0"
  - "NAUSEA:0"
  - "SLOW:0"
  - "SLOW_DIGGING:0"
  - "WEAKNESS:0"
```

### Full Default config.yml

```yaml
# MyTrip config file
version: 0.8.0
features:
  heal_on_death: true
settings:
  permissions: false
  update-alerts: true
disable_drug_set: false
addiction_effects:
  - "CONFUSION:0"
overdose_effects:
  - "BLINDNESS:0"
  - "NAUSEA:0"
  - "SLOW:0"
  - "SLOW_DIGGING:0"
  - "WEAKNESS:0"
```

> **Note:** Effect names use legacy Bukkit PotionEffectType names for backward compatibility (e.g., `CONFUSION` instead of `NAUSEA`, `SLOW` instead of `SLOWNESS`). These are resolved automatically.

> **Warning:** Do NOT manually edit files in the `plugins/MyTrip/do not edit/` directory. These files (`drugs.json`, `tools.json`, `playerdata.json`) are managed by the plugin.

---

## Data Files

All persistent data is stored in `plugins/MyTrip/do not edit/`:

| File | Contents |
|---|---|
| `drugs.json` | All registered drug definitions (name, material, recipe, effects, duration, overdose, addiction probability) |
| `tools.json` | Drug Set, Drug Test, and Anti-Toxin tool definitions |
| `playerdata.json` | Per-player data: UUID, addictions list, current dose level |

---

## Drug Set

The Drug Set is a custom crafting station block where drugs are crafted. Players craft the Drug Set item, place it in the world, and right-click it to open the drug crafting interface.

The Drug Set can be disabled by setting `disable_drug_set` to `true` in `config.yml`. It is also automatically disabled on offline-mode servers.

**Crafting recipe:**

![Drug Set Recipe](https://i.ibb.co/FHRfZtD/Drugset.png)

---

## Anti-Toxin

Anti-toxin is a consumable item that removes all active drug effects from a player. It also grants temporary resistance, saturation, and luck effects.

**Crafting recipe:**

![Anti-Toxin Recipe](https://i.ibb.co/G01CVv5/Antitoxin.png)

---

## Drug Test

The Drug Test is an item used to test whether a player currently has drugs in their system. Right-click on a player to test them.

**Crafting recipe:**

![Drug Test Recipe](https://i.ibb.co/N7cG27p/drug-test.png)

---

## Viewing Drug Recipes

Use `/mt list` (or `/mytrip list`) in-game to see all registered drugs. Click on a drug to view its crafting recipe, effects, and properties.
