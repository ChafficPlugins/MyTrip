package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import io.github.chafficui.CrucialAPI.Utils.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static de.chafficplugins.mytrip.utils.ConfigStrings.CRUCIAL_API_VERSION;

public class Crucial {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);
    private static boolean isConnected = false;

    public static void download() {
        if (plugin.getServer().getPluginManager().getPlugin("CrucialAPI") == null) {
            try {
                plugin.log("Downloading CrucialAPI");
                URL website = new URL("https://github.com/Chafficui/CrucialAPI/releases/download/v" + CRUCIAL_API_VERSION + "/CrucialAPI-v" + CRUCIAL_API_VERSION + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/CrucialAPI.jar");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                plugin.log(ChatColor.GREEN + "Downloaded successfully.");
                Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialAPI.jar"));
            } catch (IOException e) {
                plugin.error("Error 24: Failed to download CrucialAPI");
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                Bukkit.getPluginManager().disablePlugin(plugin);
            } catch (InvalidDescriptionException | org.bukkit.plugin.InvalidPluginException e) {
                plugin.error("Error 25: Failed to load CrucialAPI.");
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    public static boolean connect() throws IOException {
        org.bukkit.plugin.Plugin crucialAPI = plugin.getServer().getPluginManager().getPlugin("CrucialAPI");
        if (crucialAPI != null) {
            if (!crucialAPI.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(crucialAPI);
            }
            plugin.log(ChatColor.GREEN + "Successfully connected to CrucialAPI.");
            isConnected = true;
            if (!crucialAPI.getDescription().getVersion().startsWith(CRUCIAL_API_VERSION.substring(0,3))) {
                plugin.error("Error 24: Please update to CrucialAPI " + CRUCIAL_API_VERSION);
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                return false;
            }
            if (!Server.checkCompatibility("1.18", "1.17", "1.16", "1.15")) {
                plugin.error("Error 3: Wrong server version. Please use a supported version.");
                plugin.log("This is NOT a bug. Do NOT report this!");
                Bukkit.getPluginManager().disablePlugin(plugin);
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