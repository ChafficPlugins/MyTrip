[![User Wiki](https://img.shields.io/badge/Wiki-Users-blue)](https://chafficplugins.github.io/docs/mytrip/) [![](https://jitpack.io/v/ChafficPlugins/MyTrip.svg)](https://jitpack.io/#ChafficPlugins/MyTrip) [![Discord](https://img.shields.io/badge/Discord-Join-blue)](https://discord.gg/RYFamQzkcB)

# MyTrip

MyTrip is a Spigot/Paper plugin that adds drugs to the world of Minecraft. Players can craft custom substances, consume them for potion effects, develop addictions, and overdose. Developed by ChafficPlugins since April 2020 and open-source since April 2021.

## Features

- **Custom Drugs** — Create unlimited drugs with custom recipes, potion effects, overdose thresholds, and addiction probabilities
- **Drug Set** — A portable crafting station exclusively for drug crafting
- **Addiction System** — Players develop addictions with escalating intensity (1-8), causing periodic damage and negative effects
- **Overdose** — Configurable overdose threshold and effects per drug
- **Drug Test** — Test whether a player has drugs in their system
- **Anti-Toxin** — Consumable item that removes all drug effects
- **5 Default Drugs** — Weed, Cocaine, LSD, Heroin, and Ecstasy come pre-configured with recipes and effects
- **Developer API** — Event-based API for other plugins to hook into drug actions
- **Permissions** — Fine-grained permission nodes for every action

## Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| Server | Spigot, Paper, or compatible fork 1.21+ |
| [CrucialLib](https://github.com/ChafficPlugins/CrucialLib) | v3.0.1+ |

## Quick Start

1. Download [CrucialLib v3.0.1+](https://github.com/ChafficPlugins/CrucialLib/releases) and place it in `plugins/`
2. Download [MyTrip](https://github.com/ChafficPlugins/MyTrip/releases/latest) and place it in `plugins/`
3. Restart the server
4. Use `/mt list` to see the 5 default drugs, or `/mt create <name>` to create your own

See the [full documentation](https://chafficplugins.github.io/docs/mytrip/) for detailed setup, configuration, and usage.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Open a Pull Request and allow edits from maintainers
4. Await review from a maintainer

## Dependencies

All dependencies are managed by Maven.

| Dependency | Version | Purpose |
|---|---|---|
| [Paper API](https://papermc.io/) | 1.21.4-R0.1-SNAPSHOT | Server API |
| [CrucialLib](https://github.com/ChafficPlugins/CrucialLib) | v3.0.1 | Custom items, GUI, localization, JSON I/O |

## Building

```bash
mvn clean package    # Build the plugin JAR
mvn test             # Run tests (395 tests)
```

Output: `target/MyTrip-v0.8.1.jar`

## License

[GPL-3.0](LICENSE)
