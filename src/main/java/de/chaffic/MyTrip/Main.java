package de.chaffic.MyTrip;

import com.google.gson.reflect.TypeToken;
import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.API.FileAPI;
import de.chaffic.MyTrip.API.GUIs.InventoryManager;
import de.chaffic.MyTrip.API.LanguageAPI;
import de.chaffic.MyTrip.API.Objects.DrugPlayer;
import de.chaffic.MyTrip.API.Objects.DrugTool;
import de.chaffic.MyTrip.API.Objects.Key;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.API.UpdateCheckerAPI;
import de.chaffic.MyTrip.Commands.CommandHandler;
import de.chaffic.MyTrip.Events.DrugEvents;
import de.chaffic.MyTrip.Events.HighEvents;
import de.chaffic.MyTrip.Events.MenuEvents;
import de.chaffic.MyTrip.Events.OtherEvents;
import io.github.chafficui.CrucialAPI.Utils.Server;
import io.github.chafficui.CrucialAPI.Utils.Stats;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import io.github.chafficui.CrucialAPI.io.Json;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    /** Public. */
    public UpdateCheckerAPI uc = new UpdateCheckerAPI(this, 76816);
    public ChatColor fine = ChatColor.GREEN;
    public FileAPI fc;
    public InventoryManager GUIAPI;
    public static DrugTool[] tools = new DrugTool[3];

    /** Private. */
    private final PluginDescriptionFile pdf = getDescription();
    private final File f1 = new File("plugins/MyTrip/do not edit/");
    private final Logger logger = Logger.getLogger("MyTrip");
    private final String v = pdf.getVersion();

    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("CrucialAPI") == null) {
            try {
                this.logger.info("Downloading CrucialAPI");
                String CAPIVERSION = "2.0";
                URL website = new URL("https://github.com/Chafficui/CrucialAPI/releases/download/v" + CAPIVERSION + "/CrucialAPI-v" + CAPIVERSION + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/CrucialAPI.jar");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                this.logger.info(ChatColor.GREEN + "Downloaded successfully.");
                Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialAPI.jar"));
            } catch (IOException e) {
                this.logger.severe("Error 24: Failed to download CrucialAPI");
                this.logger.warning("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                Bukkit.getPluginManager().disablePlugin(this);
            } catch (InvalidDescriptionException | org.bukkit.plugin.InvalidPluginException e) {
                this.logger.severe("Error 25: Failed to load CrucialAPI.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    @Override
    public void onEnable() {
        if(getServer().getPluginManager().getPlugin("CrucialAPI") != null) {
            if (!getServer().getPluginManager().getPlugin("CrucialAPI").isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("CrucialAPI"));
                logger.info(ChatColor.GREEN + "Successfully connected to CrucialAPI.");
            }
            if (!Server.checkCompatibility(new String[]{"1.17", "1.16", "1.15"})) {
                logger.severe("Error 003: Wrong server version. Please use a supported version.");
                logger.severe("This is NOT a bug. Do NOT report this!");
                Bukkit.getPluginManager().disablePlugin(this);
            } else {
                fc = new FileAPI();
                GUIAPI = new InventoryManager(this);

                //creates MyTrip and update folder
                createFolder(f1);

                enableConfigs();
                try {
                    enableMetrics();
                } catch (CrucialException e) {
                    e.printStackTrace();
                }
                update();
                try {
                    createItems();
                } catch (Exception e) {
                    e.printStackTrace();
                    getPluginLoader().disablePlugin(this);
                    return;
                }
                GUIAPI.init();
                getCommand("mytrip").setExecutor(new CommandHandler());
                getServer().getPluginManager().registerEvents(new DrugEvents(), this);
                getServer().getPluginManager().registerEvents(new OtherEvents(), this);
                getServer().getPluginManager().registerEvents(new MenuEvents(), this);
                getServer().getPluginManager().registerEvents(new HighEvents(), this);

                File json = new File(getDataFolder() + "/do not edit/playerdata.json");
                if(json.exists()) {
                    Json.fromJson(getDataFolder().getPath() + "/do not edit/playerdata.json", new TypeToken<ArrayList<DrugPlayer>>() {
                    }.getType());
                }
                logger.info(ChatColor.DARK_GREEN + pdf.getName() + " is activated (Version: " + v + ") made by "
                        + ChatColor.AQUA + pdf.getAuthors());
                if (v.contains("alpha")) {
                    logger.severe("Alpha versions are NOT recommended. Please report all bugs!");
                }
            }
        } else {
            logger.severe("Error 26: Failed to connect to CrucialAPI.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void createFolder(File fx) {
        if(fx.exists()) {
            if(fx.mkdir()) logger.severe("Error 019:");
        }
    }

    private void enableConfigs() {
        try {
            logger.info("Loading config files");

            //load Configs
            fc.setup();
            fc.getLanguage().options().header("Current language presets: GERMAN,ENGLISH,FRENCH,SPANISH,\n" +
                    "LITHUANIAN,ARABIC,BRAZILIAN,RUSSIAN,DUTCH,POLISH(unfinished),KOREAN(unfinished), ITALIAN.");
            //print Changelog
            String v2 = "version";
            if(!pdf.getVersion().equals(getConfig().getString(v2))) {
                logger.info(fine + "Successfully updated to v" + pdf.getVersion());
                //edit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                logger.info(ChatColor.DARK_PURPLE + "--- Changelog ---");
                logger.info(ChatColor.WHITE + "[+] Introduced a new addiction system");
                logger.info(ChatColor.WHITE + "[+] Introduced a new Overdose system");
                logger.info(ChatColor.WHITE + "[+] Added Drug edit");
                logger.info(ChatColor.WHITE + "[+] Improved spanish translation");
                logger.info(ChatColor.WHITE + "[+] 1.17 support");
                logger.info(ChatColor.WHITE + "[+] Fixed drugs craftable in crafting table bug");
                logger.info(ChatColor.WHITE + "[+] Fixed drugs not craftable bug");
                logger.info(ChatColor.WHITE + "[+] Fixed many bugs");
                logger.info(ChatColor.WHITE + "[-] Removed drug quality system");
                logger.info(ChatColor.WHITE + "[-] Autoupdater is not supported for this version");
                //logger.info(ChatColor.WHITE + "[+] ");
                getConfig().set(v2, v);
            }

            enableConfig();
            LanguageAPI languageAPI = new LanguageAPI();
            languageAPI.setLang(getConfig().getString("settings.language"), logger);
            saveConfig();

            logger.info(fine + "Config files loaded.");
        } catch (IllegalStateException ignored){}
    }

    private void enableConfig() {
        //config.yml defualts
        getConfig().addDefault("settings.permissions", false);
        getConfig().addDefault("settings.language", "ENGLISH");
        getConfig().addDefault("settings.updater", true);
        getConfig().addDefault("settings.alerts", true);
        getConfig().addDefault("features.addiction", true);
        getConfig().addDefault("version", pdf.getVersion());
        getConfig().set("settings.drugpresets", null);
        getConfig().addDefault("features.heal_on_death", true);
        getConfig().options().header("MyTrip Config");
        getConfig().options().copyDefaults(true);
        if(getConfig().getString("settings.heal_on_death") != null){
            getConfig().set("features.heal_on_death", getConfig().getBoolean("settings.heal_on_death"));
            getConfig().set("settings.heal_on_death", null);
        }
    }

    private void enableMetrics() throws CrucialException {
        int pluginId = 7038;
        Stats.setMetrics(this, pluginId);
        Stats.addPieChart("autoupdater", getConfig().getString("settings.updater"));
        Stats.addPieChart("updatealerts", getConfig().getString("settings.upalertsdater"));
    }

    private void update() {
        new UpdateCheckerAPI(this, 76816).getVersion(version -> {
            String v2 = getDescription().getVersion();
            char[] onver = new char[3];
            char[] ofver = new char[3];

            onver[0] = version.charAt(0);
            ofver[0] = v2.charAt(0);
            if(ofver[0] > onver[0]) {
                logger.info(fine + "Plugin is up to date.");
            } else {
                try {
                    onver[1] = version.charAt(2);
                } catch(Exception e) {
                    onver[1] = 0;
                }
                try {
                    ofver[1] = v2.charAt(2);
                } catch(Exception e) {
                    ofver[1] = 0;
                }
                if(ofver[1] > onver[1]) {
                    logger.info(fine + "Plugin is up to date.");
                } else {
                    try {
                        onver[2] = version.charAt(4);
                    } catch(Exception e) {
                        onver[2] = 0;
                    }
                    try {
                        ofver[2] = v2.charAt(4);
                    } catch(Exception e) {
                        ofver[2] = 0;
                    }
                    if(ofver[2] >= onver[2]) {
                        logger.info(fine + "Plugin is up to date.");
                    } else {
                        logger.warning("There is a new update available.");
                    }
                }
            }
        });
    }

    private void createItems() throws Exception {
        List<MyDrug> myDrugs = Json.fromJson(getDataFolder().getPath() + "/do not edit/drugs.json", new TypeToken<ArrayList<MyDrug>>() {
        }.getType());

        ArrayList<DrugTool> drugTools = Json.fromJson(getDataFolder().getPath() + "/do not edit/tools.json", new TypeToken<ArrayList<DrugTool>>(){}.getType());
        for (DrugTool item:drugTools) {
            String key = item.getKey();
            if (key.equals(Key.DRUGSET.key())) {
                item.setName(getWord("drug set"));
                tools[0] = item;
            } else if (key.equals(Key.DRUTEST.key())) {
                item.setName(getWord("drug test"));
                tools[1] = item;
            } else if (key.equals(Key.ANTITOXIN.key())) {
                item.setName(getWord("anti toxin"));
                tools[2] = item;
            } else {
                throw new Exception("Unknown Drug Tool!");
            }
        }

        for (DrugTool tool : tools) {
            tool.unregister();
            tool.register();
        }

        for (MyDrug item : myDrugs) {
            item.unregister();
            item.register();
        }
    }

    public String getWord(String string) {
        return fc.getLanguage().getString(string);
    }

    public void setWord(String string, Object set) {
        fc.getLanguage().set(string, set);
    }

    @Override
    public void onDisable() {
        File json = new File(getDataFolder() + "/do not edit/playerdata.json");
        if(json.exists()) {
            Json.saveFile(Json.toJson(DrugAPI.playerDatas), getDataFolder().getPath() + "/do not edit/playerdata.json");
        }
        fc.saveItems();
        try {
            Bukkit.getScheduler().cancelTasks(this);
        } catch (IllegalPluginAccessException ignored){}
        logger.info(fine + "Disabled MyTrip");
    }
}