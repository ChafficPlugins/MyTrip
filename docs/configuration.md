# Configuration

## config.yml

The main configuration file is located at `plugins/MyTrip/config.yml`. It is generated on first startup with default values.

### Settings

| Option | Type | Default | Description |
|---|---|---|---|
| `settings.permissions` | boolean | `false` | Enable permission-based access control. When `false`, all players can craft and use drugs; only OP players can use admin commands. When `true`, permissions are enforced for all actions. |
| `settings.alerts` | boolean | `true` | Enable admin alerts (e.g., notifications when players use drugs). |

### Features

| Option | Type | Default | Description |
|---|---|---|---|
| `features.heal_on_death` | boolean | `true` | Remove all drug effects from a player when they die. |
| `features.disable_drug_set` | boolean | `false` | Disable the Drug Set crafting station. When `true`, drugs can only be obtained via commands. |

### Addiction Effects

The `addiction.effects` list defines the potion effects applied to addicted players periodically. Format: `EFFECT_NAME:AMPLIFIER`.

```yaml
addiction:
  effects:
    - "CONFUSION:0"
```

### Overdose Effects

The `overdose.effects` list defines the potion effects applied when a player overdoses. Format: `EFFECT_NAME:AMPLIFIER`.

```yaml
overdose:
  effects:
    - "BLINDNESS:0"
    - "NAUSEA:0"
    - "SLOW:0"
    - "SLOW_DIGGING:0"
    - "WEAKNESS:0"
```

### Example config.yml

```yaml
settings:
  permissions: false
  alerts: true

features:
  heal_on_death: true
  disable_drug_set: false

addiction:
  effects:
    - "CONFUSION:0"

overdose:
  effects:
    - "BLINDNESS:0"
    - "NAUSEA:0"
    - "SLOW:0"
    - "SLOW_DIGGING:0"
    - "WEAKNESS:0"
```

> **Warning:** Do NOT manually edit `playerdata.yml`. This file is managed by the plugin and stores player-specific data (addictions, active effects, etc.).

---

## Creating Custom Drugs

### Recommended: In-Game GUI

1. Run `/mt create <drugname>` in-game.
2. Follow the creation GUI to set up the drug's recipe, effects, and properties.
3. (Optional) Fine-tune the drug by editing `drugs.json` afterward.
4. Restart the server to apply changes.

### Advanced: Editing drugs.json

The drugs file is located at `plugins/MyTrip/drugs.json` and is formatted in JSON. You can edit it directly if you are comfortable with JSON syntax.

> **Warning:** Only edit `drugs.json` manually if you understand the format. Incorrect edits can prevent drugs from loading. Use the in-game GUI (`/mt create`) instead if you are unsure.

---

## Drug Set

The Drug Set is a custom crafting station block where drugs are crafted. Players craft the Drug Set item, place it in the world, and right-click it to open the drug crafting interface.

The Drug Set can be disabled by setting `features.disable_drug_set` to `true` in `config.yml`.

**Crafting recipe:**

![Drug Set Recipe](https://i.ibb.co/FHRfZtD/Drugset.png)

---

## Anti-Toxin

Anti-toxin is a consumable item that removes all active drug effects from a player. It also has a chance to reduce or remove addiction.

**Crafting recipe:**

![Anti-Toxin Recipe](https://i.ibb.co/G01CVv5/Antitoxin.png)

---

## Drug Test

The Drug Test is an item used to test whether a player currently has drugs in their system.

**Crafting recipe:**

![Drug Test Recipe](https://i.ibb.co/N7cG27p/drug-test.png)

---

## Viewing Drug Recipes

Use `/mt list` (or `/mytrip list`) in-game to see all registered drugs and their crafting recipes.
