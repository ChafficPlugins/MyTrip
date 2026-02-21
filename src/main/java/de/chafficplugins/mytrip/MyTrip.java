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
import io.github.chafficui.CrucialLib.Utils.Server;
import io.github.chafficui.CrucialLib.Utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class MyTrip extends JavaPlugin {
    private static MyTrip instance;
    public final Logger logger = Logger.getLogger("MyTrip");
    public FileManager fileManager;
    public CustomMessages customMessages;

    public static MyTrip getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        Crucial.init();
    }

    @Override
    public void onEnable() {
        try {
            if(Crucial.connect()) {
                if (!Server.checkCompatibility("1.21", "1.20", "1.19", "1.18", "1.17", "1.16", "1.15")) {
                    warning("Unsupported server version, there may be some issues with this version. Please use a supported version.");
                    warning("This is NOT a bug. Do NOT report this!");
                }
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
        getConfig().addDefault(ConfigStrings.DISABLE_DRUG_SET, false);
        getConfig().addDefault(ConfigStrings.ADDICTION_EFFECTS, List.of("CONFUSION:0"));
        getConfig().addDefault(ConfigStrings.OVERDOSE_EFFECTS, List.of("BLINDNESS:0", "NAUSEA:0", "SLOW:0", "SLOW_DIGGING:0", "WEAKNESS:0"));
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

    public List<String> getConfigStringList(String path) {
        return getConfig().getStringList(path);
    }

    public void log(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.severe(message);
    }

    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if(fileManager != null && Crucial.isIsConnected()) {
            if(!fileManager.saveFiles()) {
                error("Failed to save files.");
            }
        }
    }
}
