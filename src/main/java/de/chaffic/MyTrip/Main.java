package de.chaffic.MyTrip;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.google.gson.reflect.TypeToken;
import de.chaffic.MyTrip.API.*;
import de.chaffic.MyTrip.API.GUIs.*;
import de.chaffic.MyTrip.API.Objects.DrugPlayer;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.*;
import io.github.chafficui.CrucialAPI.Crucial;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import de.chaffic.MyTrip.Commands.MyTripCommands;
import de.chaffic.MyTrip.Events.DrugEvents;
import de.chaffic.MyTrip.Events.HighEvents;
import de.chaffic.MyTrip.Events.MenuEvents;
import de.chaffic.MyTrip.Events.OtherEvents;

public class Main extends JavaPlugin {

    /** Public. */
    public UpdateCheckerAPI uc = new UpdateCheckerAPI(this, 76816);
    public ChatColor fine = ChatColor.GREEN;
    public FileAPI fc;
    public InventoryManager GUIAPI;

    /** Private. */
    private PluginDescriptionFile pdf = getDescription();
    private File f1 = new File("plugins/MyTrip/do not edit/");
    private Logger logger = Logger.getLogger("MyTrip");
    private String v = pdf.getVersion();
    private LanguageAPI languageAPI;

    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("CrucialAPI") == null) {
            try {
                this.logger.info("Downloading CrucialAPI");
                URL website = new URL("https://github.com/Chafficui/CrucialAPI/releases/download/v0.1.2/CrucialAPI-v0.1.2.jar");
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
            if(!getServer().getPluginManager().getPlugin("CrucialAPI").isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("CrucialAPI"));
                logger.info(ChatColor.GREEN + "Successfully connected to CrucialAPI.");
            }
            if (!Server.checkVersion(new String[]{"1.16", "1.15"})) {
                logger.severe("Error 003: Wrong server version. Please use a supported version.");
                logger.severe("This is NOT a bug. Do NOT report this!");
                Bukkit.getPluginManager().disablePlugin(this);
            } else {
                String APIVERSION = "1.1";
                if(Bukkit.getPluginManager().getPlugin("CrucialAPI").getDescription().getVersion().equals("0.1.1")) {
                    logger.severe("Please download Crucial API v" + APIVERSION + " from https://www.spigotmc.org/resources/crucialapi.86380/history!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                } else {
                    Crucial.getVersion(APIVERSION, this);
                }
                fc = new FileAPI();
                GUIAPI = new InventoryManager(this);

                //creates MyTrip and update folder
                createFolder(f1);

                enableConfigs();
                enableMetrics();
                update();
                createItems();
                GUIAPI.init();
                getCommand("mytrip").setExecutor(new MyTripCommands());
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
                        + ChatColor.AQUA + pdf.getAuthors() + ChatColor.DARK_GREEN + " a " + ChatColor.BOLD + ChatColor.RED + "Crucial "
                        + ChatColor.WHITE + "Games " + ChatColor.RESET + ChatColor.DARK_GREEN + "Project!");
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
            logger.info(ChatColor.WHITE + "[+] Added an early alpha of drug editing");
            logger.info(ChatColor.WHITE + "[+] removed drug qualities");
            logger.info(ChatColor.WHITE + "[+] fixed async task bugs");
            //logger.info(ChatColor.WHITE + "[+] ");
            getConfig().set(v2, v);
        }

        enableConfig();
        languageAPI = new LanguageAPI();
        languageAPI.setLang(getConfig().getString("settings.language"), logger);
        saveConfig();

        logger.info(fine + "Config files loaded.");
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

    private void enableMetrics() {
        int pluginId = 7038;
        Stats.addChart(pluginId, this, "autoupdater", getConfig().getString("settings.updater"));
        Stats.addChart(pluginId, this, "updatealerts", getConfig().getString("settings.upalertsdater"));
    }

    private void update() {
        new UpdateCheckerAPI(this, 76816).getVersion(version -> {
            String va = version;
            String v2 = getDescription().getVersion();
            char[] onver = new char[3];
            char[] ofver = new char[3];

            onver[0] = va.charAt(0);
            ofver[0] = v2.charAt(0);
            if(ofver[0] > onver[0]) {
                logger.info(fine + "Plugin is up to date.");
            } else {
                try {
                    onver[1] = va.charAt(2);
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
                        onver[2] = va.charAt(4);
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
                        if(getConfig().getBoolean("settings.updater")) {
                            logger.info(ChatColor.YELLOW + "Downloading latest file.");
                            int ID = 76816;
                            Updater.update(this, ID, getFile());
                        } else {
                            logger.warning("Automated updates are turned off");
                        }
                    }
                }
            }
        });
    }

    public String[] fixIngredients(String ing1, String ing2, String ing3){
        if (!ing1.contains(","))
            ing1 = "AIR, " + ing1 + ", AIR";
        if (!ing2.contains(","))
            ing2 = "AIR, " + ing2 + ", AIR";
        if (!ing3.contains(","))
            ing3 = "AIR, " + ing3 + ", AIR";
        String[] row1 = ing1.split(", ");
        String[] row2 = ing2.split(", ");
        String[] row3 = ing3.split(", ");

        return new String[]{row1[0],row1[1],row1[2],row2[0],row2[1],row2[2],row3[0],row3[1],row3[2]};
    }

    private void createItems() {
        File json = new File(getDataFolder() + "/do not edit/drugs.json");
        if(!json.exists()){
            System.out.println("Translating...");
            FileConfiguration drugscfg = Files.setupYaml(getDataFolder(), "do not edit/", "drugs.yml");
            for(int i = 1; drugscfg.getInt(i + ".drugnumber") == i; i++) {

                String ing1 = drugscfg.getString(i + ".crafting.row1");
                String ing2 = drugscfg.getString(i + ".crafting.row2");
                String ing3 = drugscfg.getString(i + ".crafting.row3");

                boolean bloody = drugscfg.getBoolean(i + ".effects.bloody");
                int effectdelay = drugscfg.getInt(i + ".consume.effectdelay");
                int duration = drugscfg.getInt(i + ".consume.duration");
                int overdose = drugscfg.getInt(i + ".consume.overdose");
                int addictionpercentage = drugscfg.getInt(i + ".consume.addictionpercentage");

                String material = drugscfg.getString(i + ".information.material");
                String name = drugscfg.getString(i + ".information.Displayname");


                ArrayList<String[]> effects = new ArrayList<>();
                for(int o = 1; drugscfg.getString(i + ".effects." + o) != null; o++) {
                    effects.add(drugscfg.getString(i + ".effects." + o).split(", "));
                }

                CItem.addCrucialItem(MyDrug.drugBuilder().material(material).name(name).crafting(fixIngredients(ing1, ing2, ing3))
                        .effects(effects).isBloody(bloody).effectDelay(effectdelay).duration(duration).overdose(overdose)
                        .addict(addictionpercentage).build());
                logger.info("Successfully created " + name);
            }

            String ing1 = drugscfg.getString("drugset.row1");
            String ing2 = drugscfg.getString("drugset.row2");
            String ing3 = drugscfg.getString("drugset.row3");
            String l = drugscfg.getString("drugset.lore");
            String name = getWord("drug set");
            String m =  "HEAD:JWQuantum";
            CItem.addCrucialItem(CrucialItem.builder().material(m).name(name).lore(Arrays.asList(l))
                    .crafting(fixIngredients(ing1, ing2, ing3)).type("DRUG TOOL").build());

            ing1 = drugscfg.getString("drugtest.row1");
            ing2 = drugscfg.getString("drugtest.row2");
            ing3 = drugscfg.getString("drugtest.row3");
            String l3 = drugscfg.getString("durgtest.lore");
            String name3 = getWord("drug test");
            String m3 = "STICK";
            CItem.addCrucialItem(CrucialItem.builder().material(m3).name(name3).lore(Arrays.asList(l3))
                    .crafting(fixIngredients(ing1, ing2, ing3)).type("DRUG TOOL").build());

            String material = drugscfg.getString("antitoxin.material");
            Material m22 = Material.getMaterial(material);
            ing1 = drugscfg.getString("antitoxin.row1");
            ing2 = drugscfg.getString("antitoxin.row2");
            ing3 = drugscfg.getString("antitoxin.row3");
            String l2 = drugscfg.getString("antitoxin.lore");
            String name2 =getWord("anti toxin");
            if(!m22.isEdible()) {
                material = "HONEY_BOTTLE";
                getLogger().severe("[IC] Error 001: " + "Material (" + material + ") of " + name2 + " is not edible. Choosing default (" + material + ") instead.");
            }
            CItem.addCrucialItem(CrucialItem.builder().material(material).name(name2).lore(Arrays.asList(l2))
                    .crafting(fixIngredients(ing1, ing2, ing3)).type("DRUG TOOL").build());


            for (CrucialItem cItem:CItem.getCrucialItems()) {
                cItem.register();
            }
            fc.saveItems();
        } else {
            CItem.addCrucialItems(Json.fromJson(getDataFolder().getPath() + "/do not edit/drugs.json", new TypeToken<ArrayList<MyDrug>>(){}.getType()));
            CItem.addCrucialItems(Json.fromJson(getDataFolder().getPath() + "/do not edit/tools.json", new TypeToken<ArrayList<CrucialItem>>(){}.getType()));
            for (CrucialItem item:CItem.getCrucialItems()) {
                item.register();
            }
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
        System.out.println(Json.toJson(DrugAPI.playerDatas));
        Json.saveFile(Json.toJson(DrugAPI.playerDatas), getDataFolder().getPath() + "/do not edit/playerdata.json");
        Bukkit.getScheduler().cancelTasks(this);
        logger.info(fine + "Disabled MyTrip");
    }
}