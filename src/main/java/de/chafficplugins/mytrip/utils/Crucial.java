package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import static de.chafficplugins.mytrip.utils.ConfigStrings.CRUCIAL_LIB_VERSION;

public class Crucial {
    private static final MyTrip plugin = MyTrip.getInstance();
    private static boolean isConnected = false;

    public static void init() {
        Plugin crucialLib = plugin.getServer().getPluginManager().getPlugin("CrucialLib");
        if (crucialLib == null) {
            plugin.log("CrucialLib not found.");
            plugin.log("Please download version " + CRUCIAL_LIB_VERSION + " or higher from: https://github.com/ChafficPlugins/CrucialLib");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else if (!checkVersion(crucialLib)) {
            plugin.error("Error 26: Wrong version of CrucialLib.");
            plugin.getServer().getPluginManager().disablePlugin(crucialLib);
            plugin.log("CrucialLib not found.");
            plugin.log("Please download version " + CRUCIAL_LIB_VERSION + " or higher from: https://github.com/ChafficPlugins/CrucialLib");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private static boolean checkVersion(Plugin crucialLib) {
        String version = crucialLib.getDescription().getVersion();
        if (version.equals(CRUCIAL_LIB_VERSION)) return true;
        String[] subVersions = version.split("\\.");
        String[] subVersions2 = CRUCIAL_LIB_VERSION.split("\\.");
        if (subVersions[0].equals(subVersions2[0]) && subVersions[1].equals(subVersions2[1])) {
            return Integer.parseInt(subVersions[2]) >= Integer.parseInt(subVersions2[2]);
        }
        return false;
    }

    public static boolean connect() {
        org.bukkit.plugin.Plugin crucialLib = plugin.getServer().getPluginManager().getPlugin("CrucialLib");
        if (crucialLib != null) {
            if (!crucialLib.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(crucialLib);
            }
            plugin.log(ChatColor.GREEN + "Successfully connected to CrucialLib.");
            isConnected = true;
            if (!crucialLib.getDescription().getVersion().startsWith(CRUCIAL_LIB_VERSION.substring(0, 3))) {
                plugin.error("Error 24: Please update to CrucialLib " + CRUCIAL_LIB_VERSION + " or higher.");
                plugin.log("Please download it from: https://github.com/ChafficPlugins/CrucialLib");
                return false;
            }
            return true;
        }
        plugin.error("Error 26: Failed to connect to CrucialLib.");
        return false;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }
}
