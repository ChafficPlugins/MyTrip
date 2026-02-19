package de.chafficplugins.mytrip.io;

import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.DrugTool;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import io.github.chafficui.CrucialLib.io.Json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;

public class FileManager {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

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
        downloadFile("drugs.json", DRUGS_JSON);
        downloadFile("tools.json", TOOLS_JSON);
    }

    public void downloadFile(String fileName, String downloadString) throws IOException {
        if (!new File(plugin.getDataFolder(), "/do not edit/" + fileName).exists()) {
            try {
                URL website = new URL(DOWNLOAD_URL + downloadString);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getPath() + "/do not edit/" + fileName);
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (IOException e) {
                throw new IOException("Could not download " + fileName + "!");
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
            e.printStackTrace();
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
            e.printStackTrace();
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
