package de.chafficplugins.mytrip;

import de.chafficplugins.mytrip.drugs.commands.CommandListener;
import de.chafficplugins.mytrip.drugs.events.DrugToolEvents;
import de.chafficplugins.mytrip.drugs.events.FeatureEvents;
import de.chafficplugins.mytrip.drugs.events.InteractionEvents;
import de.chafficplugins.mytrip.io.FileManager;
import de.chafficplugins.mytrip.io.MessagesYaml;
import de.chafficplugins.mytrip.utils.ConfigStrings;
import de.chafficplugins.mytrip.utils.Crucial;
import de.chafficplugins.mytrip.utils.CustomMessages;
import io.github.chafficui.CrucialAPI.Utils.Server;
import io.github.chafficui.CrucialAPI.Utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class MyTrip extends JavaPlugin {
    public Logger logger = Logger.getLogger("MyTrip");
    public FileManager fileManager;
    public CustomMessages customMessages;

    @Override
    public void onLoad() {
        Crucial.init();
    }

    @Override
    public void onEnable() {
        try {
            if (!Server.checkCompatibility("1.19", "1.18", "1.17", "1.16", "1.15")) {
                error("Unsupported server version, there may be some issues with this version. Please use a supported version.");
                error("This is NOT a bug. Do NOT report this!");
            }
            if(Crucial.connect()) {
                loadConfig();
                fileManager = new FileManager();
                if(!fileManager.loadFiles()) {
                    error("Failed to load files. Disabling plugin.");
                    throw new IOException();
                }

                //init localizations
                customMessages = new CustomMessages();
                MessagesYaml.create();

                registerEvents(new InteractionEvents(), new DrugToolEvents(), new FeatureEvents());
                registerCommand("mytrip", new CommandListener());
                new Stats(this, ConfigStrings.BSTATS_ID);
                log(ChatColor.DARK_GREEN + getDescription().getName() + " is now enabled (Version: " + getDescription().getVersion() + ") made by "
                        + ChatColor.AQUA + getDescription().getAuthors() + ".");
            }
        } catch (IOException e) {
            error("Failed to startup " + getDescription().getName() + " (Version: " + getDescription().getVersion() + ")");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if(command != null) {
            command.setExecutor(executor);
        }
    }

    public void loadConfig() {
        getConfig().addDefault(ConfigStrings.FEATURE_HEAL_ON_DEATH, true);
        getConfig().addDefault(ConfigStrings.SETTING_PERMISSIONS, false);
        getConfig().addDefault(ConfigStrings.SETTING_ALERTS, true);
        getConfig().options().header("MyTrip config file");
        getConfig().set("version", getDescription().getVersion());
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public boolean getConfigBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public String getConfigString(String path) {
        return getConfig().getString(path);
    }

    public void log(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if(!Crucial.isIsConnected() || !fileManager.saveFiles()) {
            error("Failed to save files. Disabling plugin.");
        }
    }
}
