package de.chafficplugins.mytrip.io;

import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.DrugTool;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import io.github.chafficui.CrucialLib.io.Json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;

public class FileManager {
    private static final MyTrip plugin = MyTrip.getInstance();

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
        File dir = new File(plugin.getDataFolder(), "/do not edit/");
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("Could not create directory");

        File playerData = new File(dir, "playerdata.json");
        if (!playerData.exists()) {
            if (!playerData.createNewFile())
                throw new IOException("Could not create playerdata.json");
        }

        copyDefaultIfMissing("drugs.json");
        copyDefaultIfMissing("tools.json");
    }

    /**
     * Copies a default JSON file from the bundled resources to the data folder
     * if it does not already exist.
     */
    private void copyDefaultIfMissing(String fileName) throws IOException {
        File target = new File(plugin.getDataFolder(), "/do not edit/" + fileName);
        if (!target.exists()) {
            try (InputStream in = plugin.getResource("defaults/" + fileName)) {
                if (in == null) {
                    throw new IOException("Default resource not found: defaults/" + fileName);
                }
                Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            plugin.log("Created default " + fileName);
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
