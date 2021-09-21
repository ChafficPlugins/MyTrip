package de.chaffic.MyTrip.API;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import de.chaffic.MyTrip.API.Objects.DrugTool;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.Files;
import io.github.chafficui.CrucialAPI.io.Json;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import de.chaffic.MyTrip.Main;


public class FileAPI {
    public static String drugSetKey;
    public static String drugTestKey;
    public static String antiToxinKey;
    private static final Main plugin = Main.getPlugin(Main.class);


    //files & file configs
    public FileConfiguration languagecfg;
    public File languagefile;
    //------

    public void setup() {
        languagecfg = Files.setupYaml(plugin.getDataFolder(), "language.yml");
        languagefile = new File(plugin.getDataFolder(), "language.yml");

        File playerdata = new File(plugin.getDataFolder(), "/do not edit/playerdata.json");
        new File(plugin.getDataFolder(), "/do not edit/").mkdirs();
        try {
            playerdata.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not create playerdata.json");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        downloadItems();
    }

    private void downloadItems(){
        if(!new File(plugin.getDataFolder(), "/do not edit/drugs.json").exists()) {
            try {
                URL website = new URL("https://drive.google.com/uc?export=download&id=1xNsgp5-zC7MBM0R66oVeJC8d6Iey_lyI");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getPath() + "/do not edit/drugs.json");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Could not download drugs.json!");
                plugin.getLogger().warning("Please download it from https://drive.google.com/uc?export=download&id=1xNsgp5-zC7MBM0R66oVeJC8d6Iey_lyI and drag&drop it into /MyTrip/do not edit/");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }
        }
        if(!new File(plugin.getDataFolder(), "/do not edit/tools.json").exists()) {
            try {
                URL website = new URL("https://drive.google.com/uc?export=download&id=1ALn6_l2jWmtBnQceaZYQ60h-J_a2NkOx");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getPath() + "/do not edit/tools.json");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Could not download tools.json!");
                plugin.getLogger().warning("Please download it from https://drive.google.com/uc?export=download&id=1ALn6_l2jWmtBnQceaZYQ60h-J_a2NkOx and drag&drop it into /MyTrip/do not edit/");
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    public FileConfiguration getLanguage() {
        return languagecfg;
    }

    public void saveLanguage() {
        try {
            languagecfg.save(languagefile);
        }catch(IOException e) {
            plugin.getLogger().severe("Error 015: Could not save language.yml file");
        }
    }


    public void saveItems(){

        ArrayList<MyDrug> drugs = new ArrayList<>();
        for (CrucialItem cItem:CrucialItem.getCrucialItems()) {
            if(cItem.isRegistered() && cItem instanceof MyDrug){
                drugs.add((MyDrug) cItem);
            }
        }
        if(!drugs.isEmpty()){
            Json.saveFile(Json.toJson(drugs), plugin.getDataFolder().getPath() + "/do not edit/drugs.json");
        }
        if(Main.tools.length != 0){
            Json.saveFile(Json.toJson(Main.tools), plugin.getDataFolder().getPath() + "/do not edit/tools.json");
        }
    }
}