# Installation

## Download

- **SpigotMC**: [MyTrip Resource Page](https://www.spigotmc.org/resources/mytrip-amazing-drugs-in-minecraft.76816/)
- **GitHub Releases**: [Latest Release](https://github.com/ChafficPlugins/MyTrip/releases/latest)

## Video Tutorial

A video tutorial is available: [Watch on YouTube](https://youtu.be/aSUYqZ5cUVY)

## Step-by-Step Setup

1. **Install CrucialAPI** — Download [CrucialAPI](https://github.com/Chafficui/CrucialAPI) and place it in your server's `plugins/` folder. MyTrip requires this dependency to function.

2. **Install MyTrip** — Place the MyTrip `.jar` file into your server's `plugins/` folder.

3. **(Optional) Install a permissions plugin** — If you want to control who can craft and use drugs, install a permissions plugin such as [LuckPerms](https://www.spigotmc.org/resources/luckperms-an-advanced-permissions-plugin.28140/) and set `settings.permissions` to `true` in `config.yml`.

4. **Restart the server** — Restart (not reload) your server to generate the default configuration files.

5. **Edit config.yml** — Customize the plugin settings in `plugins/MyTrip/config.yml`. See the [Configuration](configuration.md) page for details. **Do NOT edit `playerdata.yml` manually.**

6. **(Optional) Create custom drugs** — Use `/mt create <drugname>` in-game to open the drug creation GUI, or edit `drugs.json` directly. See [Configuration > Creating Drugs](configuration.md#creating-custom-drugs) for details.

7. **Restart the server again** — Restart the server to load your configuration changes and custom drugs.

8. **(Optional) Assign permissions** — Use your permissions plugin to assign MyTrip permission nodes to players and groups. See [Commands & Permissions](commands.md) for a full list.

9. **Verify** — Run `/mt list` in-game to check that all drugs are loaded correctly. Drugs marked in red indicate errors — check the server console for details.

10. **Start crafting** — Craft a [Drug Set](configuration.md#drug-set) (if enabled) and place it to open the drug crafting menu. Use `/mt list` to view all registered drugs and their ingredients.

## Updating

1. Download the latest version from [SpigotMC](https://www.spigotmc.org/resources/mytrip-amazing-drugs-in-minecraft.76816/) or [GitHub Releases](https://github.com/ChafficPlugins/MyTrip/releases/latest).
2. Replace the old `.jar` file in `plugins/` with the new one.
3. Restart the server.
4. Check the changelog for any configuration changes that may be required.

## Troubleshooting

If you encounter issues, join the [Discord server](https://discord.gg/RYFamQzkcB) for support.
