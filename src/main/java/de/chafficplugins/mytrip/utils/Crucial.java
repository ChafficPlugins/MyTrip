package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

import static de.chafficplugins.mytrip.utils.ConfigStrings.CRUCIAL_API_VERSION;

public class Crucial {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);
    private static boolean isConnected = false;

    public static void init() {
        Plugin crucialAPI = plugin.getServer().getPluginManager().getPlugin("CrucialAPI");
        if (crucialAPI == null) {
            plugin.log("CrucialAPI not found.");
            plugin.log("Please download version " + CRUCIAL_API_VERSION + " or higher from: https://www.spigotmc.org/resources/crucialapi.86380/");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else if (!checkVersion(crucialAPI)) {
            plugin.error("Error 26: Wrong version of CrucialAPI.");
            plugin.getServer().getPluginManager().disablePlugin(crucialAPI);
            plugin.log("CrucialAPI not found.");
            plugin.log("Please download version " + CRUCIAL_API_VERSION + " or higher from: https://www.spigotmc.org/resources/crucialapi.86380/");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private static boolean checkVersion(Plugin crucialAPI) {
        String version = crucialAPI.getDescription().getVersion();
        if (version.equals(CRUCIAL_API_VERSION)) return true;
        String[] subVersions = version.split("\\.");
        String[] subVersions2 = CRUCIAL_API_VERSION.split("\\.");
        if (subVersions[0].equals(subVersions2[0]) && subVersions[1].equals(subVersions2[1])) {
            return Integer.parseInt(subVersions[2]) >= Integer.parseInt(subVersions2[2]);
        }
        return false;
    }

    public static boolean connect() throws IOException {
        org.bukkit.plugin.Plugin crucialAPI = plugin.getServer().getPluginManager().getPlugin("CrucialAPI");
        if (crucialAPI != null) {
            if (!crucialAPI.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(crucialAPI);
            }
            plugin.log(ChatColor.GREEN + "Successfully connected to CrucialAPI.");
            isConnected = true;
            if (!crucialAPI.getDescription().getVersion().startsWith(CRUCIAL_API_VERSION.substring(0, 3))) {
                plugin.error("Error 24: Please update to CrucialAPI " + CRUCIAL_API_VERSION + " or higher.");
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                return false;
            }
            return true;
        }
        plugin.error("Error 26: Failed to connect to CrucialAPI.");
        return false;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }
}