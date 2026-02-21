# Installation

## Download

- **GitHub Releases**: [Latest Release](https://github.com/ChafficPlugins/MyTrip/releases/latest)
- **SpigotMC**: [MyTrip Resource Page](https://www.spigotmc.org/resources/mytrip-amazing-drugs-in-minecraft.76816/)

## Video Tutorial

A video tutorial is available: [Watch on YouTube](https://youtu.be/aSUYqZ5cUVY)

## Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| Server | Spigot, Paper, or compatible Bukkit fork 1.21+ |
| CrucialLib | v3.0.1+ ([download](https://github.com/ChafficPlugins/CrucialLib/releases)) |

## Step-by-Step Setup

1. **Install CrucialLib** — Download [CrucialLib v3.0.1+](https://github.com/ChafficPlugins/CrucialLib/releases) and place it in your server's `plugins/` folder. MyTrip requires this dependency to function.

2. **Install MyTrip** — Place the MyTrip `.jar` file into your server's `plugins/` folder.

3. **(Optional) Install a permissions plugin** — If you want to control who can craft and use drugs, install a permissions plugin such as [LuckPerms](https://luckperms.net/) and set `settings.permissions` to `true` in `config.yml`.

4. **Restart the server** — Restart (not reload) your server to generate the default configuration files.

5. **Edit config.yml** — Customize the plugin settings in `plugins/MyTrip/config.yml`. See the [Configuration](configuration.md) page for details. **Do NOT edit files in the `do not edit/` directory manually.**

6. **(Optional) Create custom drugs** — Use `/mt create <drugname>` in-game to open the drug creation GUI. See [Drugs](drugs.md) for details on how drugs work.

7. **Restart the server again** — Restart the server to load your configuration changes and custom drugs.

8. **(Optional) Assign permissions** — Use your permissions plugin to assign MyTrip permission nodes to players and groups. See [Commands & Permissions](commands.md) for a full list.

9. **Verify** — Run `/mt list` in-game to check that all drugs are loaded correctly. Drugs marked in **red** indicate registration errors — check the server console for details.

10. **Start crafting** — Craft a [Drug Set](configuration.md#drug-set) (if enabled) and place it to open the drug crafting menu. Use `/mt list` to view all registered drugs and their ingredients.

## Updating

1. Download the latest version from [GitHub Releases](https://github.com/ChafficPlugins/MyTrip/releases/latest) or [SpigotMC](https://www.spigotmc.org/resources/mytrip-amazing-drugs-in-minecraft.76816/).
2. **Also update CrucialLib** if a new version is required — check the release notes.
3. Replace the old `.jar` file in `plugins/` with the new one.
4. Restart the server.
5. Check the changelog for any configuration changes that may be required.

> **Note:** When updating from versions that used CrucialAPI (pre-v0.8.0), you must replace CrucialAPI with [CrucialLib v3.0.1+](https://github.com/ChafficPlugins/CrucialLib/releases). Existing drug data, player data, and configs are compatible.

## Troubleshooting

### Plugin fails to enable

- Check that CrucialLib v3.0.1+ is installed in `plugins/`
- Ensure you are running Java 21 or higher (`java -version`)
- Ensure your server is Spigot/Paper 1.21 or higher

### "CrucialLib not found" error

Download CrucialLib from [GitHub](https://github.com/ChafficPlugins/CrucialLib/releases) and place the JAR in your `plugins/` folder.

### "Wrong version of CrucialLib" error

You have an outdated CrucialLib. Download v3.0.1 or higher.

### Drug Set automatically disabled

This happens when the server runs in offline mode (`online-mode=false` in `server.properties`). The Drug Set requires online mode for UUID-based player identification. Drugs can still be crafted in regular crafting tables when the Drug Set is disabled.

If you encounter other issues, join the [Discord server](https://discord.gg/RYFamQzkcB) for support.
