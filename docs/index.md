# MyTrip

**Add Drugs to Minecraft**

MyTrip is a Spigot plugin that adds craftable drugs to Minecraft. Players can consume them and receive various potion effects fitting the effects of real-life drugs. Developed by [ChafficPlugins](https://github.com/ChafficPlugins) since April 2020 and open-source since April 2021.

## Key Features

- **Unlimited Custom Drugs** — Create as many drugs as you want with infinite potion effects via the in-game creation GUI or by editing `drugs.json` directly.
- **Drug Set** — A custom crafting station exclusively for crafting drugs. Place it as a block and interact with it to open the drug crafting menu. Can be toggled on/off in `config.yml`.
- **Addiction System** — Players can become addicted to drugs over time. Addicted players receive periodic damage and negative effects, escalating with intensity. Addictions persist across sessions.
- **Anti-Toxin** — A craftable item that removes all active drug effects from a player and has a chance to reduce or remove addiction.
- **Overdose** — Taking too many concurrent doses triggers overdose effects (configurable). Players receive blindness, nausea, slowness, and weakness by default.
- **Drug Test** — A craftable item to test whether a player has drugs in their system.
- **Permissions** — Fine-grained permission nodes for crafting, consuming, and administering drugs.
- **Developer API** — Event-based API for other plugins to hook into drug consumption, crafting, and tool usage.

## Links

- [SpigotMC Resource Page](https://www.spigotmc.org/resources/mytrip-amazing-drugs-in-minecraft.76816/)
- [GitHub Repository](https://github.com/ChafficPlugins/MyTrip)
- [Discord Server](https://discord.gg/RYFamQzkcB)
- [Wiki](https://github.com/ChafficPlugins/MyTrip/wiki)

## Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| Server | Spigot, Paper, or compatible Bukkit fork 1.21+ |
| CrucialLib | v3.0.1+ — required dependency ([GitHub](https://github.com/ChafficPlugins/CrucialLib)) |

## Quick Start

1. Download [CrucialLib v3.0.1+](https://github.com/ChafficPlugins/CrucialLib/releases) and place it in `plugins/`
2. Download [MyTrip](https://github.com/ChafficPlugins/MyTrip/releases/latest) and place it in `plugins/`
3. Restart the server
4. Use `/mt create <name>` to create your first drug, or `/mt list` to see the defaults

See [Installation](installation.md) for detailed setup instructions.

## License

MyTrip is licensed under the [GPL-3.0 License](https://github.com/ChafficPlugins/MyTrip/blob/master/LICENSE).
