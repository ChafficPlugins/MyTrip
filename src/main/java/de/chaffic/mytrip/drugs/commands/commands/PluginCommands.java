package de.chaffic.mytrip.drugs.commands.commands;

import de.chaffic.mytrip.MyTrip;
import org.bukkit.command.CommandSender;

public class PluginCommands {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    public static void performCommand(CommandSender sender, String command) {
        switch (command) {
            case "reload":
                reloadPlugin(sender);
                break;
            case "help":
                //TODO: Help
                break;
            case "info", "version", "about":
                pluginInfo(sender);
                break;
            default:
                sender.sendMessage("§cUnknown command"); //TODO: Localization
        }
    }

    private static void reloadPlugin(CommandSender sender) {
        sender.sendMessage("§aReloading plugin"); //TODO: Localization
        plugin.reloadConfig();
        if(plugin.fileManager.reload()) {
            return;
        }
        sender.sendMessage("§cFailed to reload plugin. Disabling..."); //TODO: Localization
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    private static void pluginInfo(CommandSender sender) {
        sender.sendMessage("§aMyTrip"); //TODO: Localization
        sender.sendMessage("§aVersion: §e" + plugin.getDescription().getVersion()); //TODO: Localization
        sender.sendMessage("§aAuthor: §e" + plugin.getDescription().getAuthors()); //TODO: Localization
    }
}
