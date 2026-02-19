# MyTrip

**Add Drugs to Minecraft**

MyTrip is a Spigot plugin that adds craftable drugs to Minecraft. Players can consume them and receive various potion effects fitting the effects of real-life drugs. Developed by [ChafficPlugins](https://github.com/ChafficPlugins) since April 2020 and open-source since April 2021.

## Key Features

- **Unlimited Custom Drugs** — Create as many drugs as you want with infinite potion effects via the in-game creation GUI or by editing `drugs.json` directly.
- **Drug Set** — A custom crafting station exclusively for crafting drugs. Place it as a block and interact with it to open the drug crafting menu. Can be toggled on/off in `config.yml`.
- **Addiction System** — Players can become addicted to drugs over time. Addicted players receive negative effects periodically until they consume the drug again or take anti-toxin.
- **Anti-Toxin** — A craftable item that removes all active drug effects from a player and has a chance to reduce or remove addiction.
- **Overdose** — Taking too many drugs applies a wither effect that will kill the player unless they take anti-toxin.
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
| Java | 16+ |
| Server | Spigot, Paper, or compatible Bukkit fork 1.18+ |
| CrucialAPI | Required dependency ([GitHub](https://github.com/Chafficui/CrucialAPI)) |

## License

MyTrip is licensed under the [GPL-3.0 License](https://github.com/ChafficPlugins/MyTrip/blob/master/LICENSE).
