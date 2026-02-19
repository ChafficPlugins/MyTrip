# Commands & Permissions

The main command is `/mytrip` (alias: `/mt`).

## Commands

| Command | Description | Required Permission |
|---|---|---|
| `/mytrip help` | Shows a list of all available commands. | `mytrip.*` or `mytrip.help` |
| `/mytrip info` | Shows information about the plugin. | None |
| `/mytrip list` | Lists all consumable drugs added to the game, including their recipes. | `mytrip.*` or `mytrip.list` |
| `/mytrip recover [player]` | Removes all active drug effects from yourself or another player. | `mytrip.*` / `mytrip.recover` / `mytrip.recover.self` / `mytrip.recover.others` |
| `/mytrip give <drugname> [player]` | Gives a drug with the specified quality to yourself or another player. | `mytrip.*` / `mytrip.give` / `mytrip.give.self` / `mytrip.give.others` |
| `/mytrip create <drugname>` | Opens the drug creation GUI to create a new drug. | `mytrip.*` or `mytrip.create` |
| `/mytrip addictions clear [player]` | Removes all addictions from yourself or a specified player. | `mytrip.*` / `mytrip.addictions` / `mytrip.addictions.clear` / `mytrip.addictions.clear.self` / `mytrip.addictions.clear.others` |
| `/mytrip addictions list [player]` | Lists all addictions of yourself or a specified player. | `mytrip.*` / `mytrip.addictions` / `mytrip.addictions.list` / `mytrip.addictions.list.self` / `mytrip.addictions.list.others` |
| `/mytrip addictions add <drugname> [player]` | Adds an addiction to yourself or a specified player. | `mytrip.*` / `mytrip.addictions` / `mytrip.addictions.add` / `mytrip.addictions.add.self` / `mytrip.addictions.add.others` |

> **Note:** When `[player]` is omitted, the command targets the executing player. Using it on others requires the `.others` permission variant.

---

## Permissions

By default, permissions are **disabled** (`settings.permissions: false` in `config.yml`). This means only OP players can use admin commands, but **all players** can craft and consume drugs.

When permissions are enabled, the following nodes are available:

### General

| Permission | Description |
|---|---|
| `mytrip.*` | Grants access to all commands and all drug crafting/usage. |

### Commands

| Permission | Description |
|---|---|
| `mytrip.help` | Allows use of `/mytrip help`. |
| `mytrip.list` | Allows use of `/mytrip list`. |
| `mytrip.recover` | Allows use of `/mytrip recover` (self and others). |
| `mytrip.recover.self` | Allows use of `/mytrip recover` on self only. |
| `mytrip.recover.others` | Allows use of `/mytrip recover` on other players. |
| `mytrip.give` | Allows use of `/mytrip give` (self and others). |
| `mytrip.give.self` | Allows use of `/mytrip give` on self only. |
| `mytrip.give.others` | Allows use of `/mytrip give` on other players. |
| `mytrip.create` | Allows use of `/mytrip create`. |
| `mytrip.addictions` | Allows use of all `/mytrip addictions` subcommands. |
| `mytrip.addictions.clear` | Allows use of `/mytrip addictions clear` (self and others). |
| `mytrip.addictions.clear.self` | Allows clearing own addictions. |
| `mytrip.addictions.clear.others` | Allows clearing other players' addictions. |
| `mytrip.addictions.list` | Allows use of `/mytrip addictions list` (self and others). |
| `mytrip.addictions.list.self` | Allows listing own addictions. |
| `mytrip.addictions.list.others` | Allows listing other players' addictions. |
| `mytrip.addictions.add` | Allows use of `/mytrip addictions add` (self and others). |
| `mytrip.addictions.add.self` | Allows adding addictions to self. |
| `mytrip.addictions.add.others` | Allows adding addictions to other players. |

### Item Permissions

| Permission | Description |
|---|---|
| `mytrip.use.*` | Allows use (consumption) of all items. |
| `mytrip.craft.*` | Allows crafting of all items. |
| `mytrip.craft.drugset` | Allows crafting the Drug Set. |
| `mytrip.use.drugset` | Allows using the Drug Set. |
| `mytrip.craft.antitoxin` | Allows crafting Anti-Toxin. |
| `mytrip.use.antitoxin` | Allows using Anti-Toxin. |
| `mytrip.craft.drugtest` | Allows crafting the Drug Test. |
| `mytrip.use.drugtest` | Allows using the Drug Test. |
| `mytrip.craft.<drugname>` | Allows crafting a specific drug (replace `<drugname>` with the drug's name). |
| `mytrip.use.<drugname>` | Allows consuming a specific drug (replace `<drugname>` with the drug's name). |
