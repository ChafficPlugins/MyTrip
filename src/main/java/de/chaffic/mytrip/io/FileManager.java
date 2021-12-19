package de.chaffic.mytrip.io;

import de.chaffic.mytrip.MyTrip;
import de.chaffic.mytrip.drugs.objects.DrugPlayer;
import de.chaffic.mytrip.drugs.objects.DrugTool;
import de.chaffic.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import io.github.chafficui.CrucialAPI.io.Json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import static de.chaffic.mytrip.utils.ConfigStrings.*;

public class FileManager {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    //TODO: LanguageYML

    public FileManager() throws IOException {
        setup();
    }

    public boolean reload() {
        if(saveFiles()) {
            try {
                setup();
                if(loadFiles()) {
                    return true;
                }
            } catch (IOException ignored) {}
        }
        return false;
    }

    private void setup() throws IOException {
        File playerData = new File(plugin.getDataFolder(), "/do not edit/playerdata.json");
        File dir = new File(plugin.getDataFolder(), "/do not edit/");
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("Could not create directory");
        if (!playerData.exists()) {
            if (!playerData.createNewFile())
                throw new IOException("Could not create directory");
        }
        if (!new File(plugin.getDataFolder(), "/do not edit/drugs.json").exists()) {
            try {
                URL website = new URL(DOWNLOAD_URL + DRUGS_JSON);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getPath() + "/do not edit/drugs.json");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (IOException e) {
                throw new IOException("Could not download drugs.json!");
            }
        }
        if (!new File(plugin.getDataFolder(), "/do not edit/tools.json").exists()) {
            try {
                URL website = new URL(DOWNLOAD_URL + TOOLS_JSON);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getPath() + "/do not edit/tools.json");
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (IOException e) {
                throw new IOException("Could not download tools.json!");
            }
        }
    }

    public boolean saveFiles() {
        try {
            plugin.log("Saving files...");
            DrugTool.saveAll();
            plugin.log("Saved tools.");
            MyDrug.saveAll();
            plugin.log("Saved drugs.");
            DrugPlayer.saveAll();
            plugin.log("Saved player data.");
        } catch (IOException e) {
            e.printStackTrace(); //TODO: remove later
            return false;
        }
        return true;
    }

    public boolean loadFiles() {
        try {
            MyDrug.loadAll();
            DrugTool.loadAll();
            DrugPlayer.loadAll();
        } catch (IOException | CrucialException e) {
            e.printStackTrace(); //TODO: remove later
            return false;
        }
        return true;
    }

    public <T> void saveToJson(String fileName, ArrayList<T> data) throws IOException {
        Json.saveFile(Json.toJson(data), plugin.getDataFolder().getPath() + "/do not edit/" + fileName);
    }

    public <T> ArrayList<T> loadFromJson(String fileName, Type type) throws IOException {
        return Json.fromJson(plugin.getDataFolder().getPath() + "/do not edit/" + fileName, type);
    }
}
