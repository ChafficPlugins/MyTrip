# Drugs

## Overview

Drugs are custom consumable items that apply potion effects to players. Each drug has a crafting recipe, a set of effects, timing properties, and configurable overdose and addiction thresholds.

## Drug Properties

| Property | Description | Range |
|---|---|---|
| **Name** | Display name of the drug | Any string |
| **Material** | The Minecraft item used as the drug's appearance | Any valid Material |
| **Recipe** | 3x3 crafting grid of materials | 9 Material slots |
| **Effects** | List of potion effects applied on consumption | PotionEffectType + strength |
| **Duration** | How long effects last (seconds) | 0+ |
| **Effect Delay** | Seconds before effects kick in after consumption | 0+ |
| **Overdose** | Number of concurrent doses before overdose triggers | 0–99 |
| **Addiction Probability** | Percent chance of developing/increasing addiction per use | 0–100 |

## Creating Drugs

### In-Game GUI (Recommended)

1. Run `/mt create <drugname>` as an OP player (or with `mytrip.create` permission)
2. **Recipe screen**: Place ingredient items in the 3x3 crafting grid and the result item in the output slot
3. Click **"Continue"** to proceed to effect selection
4. **Effects screen**: Click potion effects to add them to the drug (click again to remove)
5. Click **"CREATE"** to register the drug
6. **Edit properties**: Use `/mt list` → click the drug → click **"EDIT"** to adjust duration, delay, overdose threshold, and addiction probability

### Editing Existing Drugs

1. `/mt list` — opens the drug list GUI
2. Click on a drug to view its details (recipe, effects, stats)
3. Click **"EDIT"** to modify properties:
   - Click the clock to increase/decrease **duration**
   - Click the hopper to increase/decrease **effect delay**
   - Click the redstone to increase/decrease **overdose** threshold
   - Click the potion to increase/decrease **addiction probability**
4. Click **"DELETE"** to remove a drug permanently

### Via JSON (Advanced)

Drug data is stored in `plugins/MyTrip/do not edit/drugs.json`. You can edit this file while the server is stopped:

```json
[
  {
    "name": "ExampleDrug",
    "material": "SUGAR",
    "recipe": ["AIR", "SUGAR", "AIR", "AIR", "WHEAT", "AIR", "AIR", "SUGAR", "AIR"],
    "effects": [["SPEED", "2"], ["JUMP", "1"]],
    "duration": 60,
    "effectDelay": 3,
    "overdose": 5,
    "addict": 15
  }
]
```

> **Warning:** Only edit JSON files while the server is stopped. The plugin overwrites these files on shutdown.

---

## Consumption

When a player right-clicks while holding a drug item:

1. **Permission check** — if permissions are enabled, the player needs `mytrip.use.<drugname>` or `mytrip.use.*`
2. **Item consumed** — one item is removed from the stack
3. **Drink sound** plays
4. **Dose tracked** — the player's dose level increases by `1 / overdose_threshold`
5. **Addiction roll** — random chance (based on drug's addiction probability) to develop or worsen addiction
6. If dose reaches **100% (overdose)** — overdose effects from config are applied immediately
7. After **effect delay** seconds — the drug's potion effects are applied
8. After **duration** seconds — effects expire and dose decreases

### Dose System

Each drug has an overdose threshold (e.g., 5). Each consumption adds `1/threshold` to the player's dose level. When dose reaches 1.0 (100%), the player overdoses.

- Dose resets to 0 when the player joins the server
- Dose decreases after each drug's effects expire
- Multiple different drugs all contribute to the same dose pool

### Milk Blocking

While a player has an active dose (> 0), milk bucket consumption is blocked. This prevents players from easily clearing drug effects with milk. Use the Anti-Toxin item instead.

---

## Addiction

### How Addiction Develops

Each time a player consumes a drug, there's a random chance (equal to the drug's `addict` percentage) that they develop an addiction to that specific drug. If already addicted, the same roll may increase addiction intensity.

### Intensity Levels

Addiction intensity ranges from **1 to 8**:

| Intensity | Damage per Tick | Extra Effects | Loop Interval |
|---|---|---|---|
| 1–5 | Equal to intensity | None | 16000/intensity ticks |
| 6–8 | Equal to intensity | Config `addiction_effects` applied | 16000/intensity ticks |

Higher intensity means:
- More damage per addiction tick
- Shorter intervals between ticks (more frequent)
- At intensity > 5, additional negative effects from `addiction_effects` config

### Addiction Loop

The addiction system runs a repeating timer for each active addiction:
1. Action bar message warns the player
2. Player takes damage equal to intensity
3. If intensity > 5, configured addiction effects are applied
4. Title message shown: "Addiction" / "You are addicted to {drug}"
5. Loop reschedules itself

### Persistence

- Addictions are saved to `playerdata.json` and persist across server restarts
- Addiction loops restart when the player joins the server
- Addictions are cleared on death if `features.heal_on_death` is `true` in config
- Admins can manage addictions with `/mt addictions clear|list|add`

---

## Overdose

When a player's dose reaches 100%, overdose effects from `overdose_effects` in config.yml are applied. By default:

- Blindness
- Nausea
- Slowness
- Mining Fatigue
- Weakness

These effects last 100 seconds (2000 ticks) at the configured amplifier level. Overdose effects stack with the drug's normal effects.

---

## Crafting

### With Drug Set (Default)

When the Drug Set is enabled (default), drugs can **only** be crafted through the Drug Set:

1. Craft the Drug Set item
2. Place it as a block in the world
3. Right-click the block to open the crafting interface
4. Place ingredients matching a drug's recipe
5. The drug item appears in the result slot

Permission required: `mytrip.use.drugset` (if permissions enabled)

### Without Drug Set

When the Drug Set is disabled (`disable_drug_set: true` or offline-mode server), drugs can be crafted in **any regular crafting table**. The plugin validates recipes in the `PrepareItemCraftEvent`.

Permission required: `mytrip.craft.<drugname>` (if permissions enabled)

---

## Tools

### Drug Set

A portable crafting station for drugs. Place as a block and right-click to open.

- Identified by a hardcoded UUID
- Automatically disabled on offline-mode servers
- Can be toggled via `disable_drug_set` config option

### Drug Test

Right-click on another player to test if they have drugs in their system (dose > 0). The result is displayed as a chat message.

### Anti-Toxin

A consumable item that:
- Removes all active potion effects from the player
- Grants temporary Resistance, Saturation, and Luck
- Removes any visual blood effects (via CrucialLib VisualEffects)
