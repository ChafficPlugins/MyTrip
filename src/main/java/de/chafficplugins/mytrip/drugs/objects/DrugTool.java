package de.chafficplugins.mytrip.drugs.objects;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.utils.ConfigStrings;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DrugTool extends CrucialItem {
    public static ArrayList<DrugTool> tools = new ArrayList<>();
    private static final MyTrip plugin = MyTrip.getInstance();

    public DrugTool(String type) {
        super(type);
    }

    //static
    public static DrugTool getById(UUID id) {
        for (DrugTool item : tools) {
            if (item.isRegistered() && item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static boolean isDrugSet(DrugTool tool) {
        return tool.getId().equals(ConfigStrings.DRUG_SET_UUID);
    }

    public static void saveAll() throws IOException {
        plugin.fileManager.saveToJson("tools.json", tools);
    }

    public static void loadAll() throws IOException, CrucialException {
        tools = plugin.fileManager.loadFromJson("tools.json", new TypeToken<ArrayList<DrugTool>>() {
        }.getType());
        for (DrugTool item : tools) {
            item.unregister();
            if(plugin.getConfigBoolean(ConfigStrings.DISABLE_DRUG_SET) && item.getId().equals(ConfigStrings.DRUG_SET_UUID))
                continue;
            item.register();
        }
        if (tools == null) {
            throw new IOException("There were no drug tools recognized!");
        }
    }
}
