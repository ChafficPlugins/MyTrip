package de.chafficplugins.mytrip.drugs.objects;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mytrip.MyTrip;
import de.chafficplugins.mytrip.utils.ConfigStrings;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DrugTool extends CrucialItem {
    public static ArrayList<DrugTool> tools = new ArrayList<>();
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    public DrugTool(String name, String head) {
        super("drug_tool");
        this.isHead = true;
        this.name = name;
        this.material = head;
    }

    public DrugTool(String name, UUID headOwner) {
        super("drug_tool");
        this.isHead = true;
        this.name = name;
        this.headOwner = headOwner;
    }

    public DrugTool(String name, Material material) {
        super("drug_tool");
        this.isHead = false;
        this.name = name;
        this.material = material.name();
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = super.getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        plugin.log(isHead + " " + (itemMeta instanceof SkullMeta) + " " + (headOwner != null));
        if(isHead && itemMeta instanceof SkullMeta && headOwner != null) {
            SkullMeta meta = (SkullMeta) itemMeta;
            meta.setOwningPlayer(plugin.getServer().getOfflinePlayer(headOwner));
            item.setItemMeta(meta);
        }
        return item;
    }

    public UUID getHeadOwner() {
        return headOwner;
    }

    //static
    public static DrugTool getById(UUID id) {
        for (DrugTool item : tools) {
            plugin.log(item.id + " " + id.toString() + " " + item.isRegistered); //TODO: remove later
            if (item.isRegistered() && item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static boolean isDrugSet(BlockState state) {
        if (state instanceof Skull) {
            OfflinePlayer p = ((Skull) state).getOwningPlayer();
            plugin.log(p.getUniqueId().toString()); //TODO: remove later
            if(p != null) {
                DrugTool drugSet = getById(ConfigStrings.DRUG_SET_UUID);
                return (p.getUniqueId().equals(drugSet.headOwner));
            }
        }
        return false;
    }

    public static void saveAll() throws IOException {
        plugin.fileManager.saveToJson("tools.json", tools);
    }

    public static void loadAll() throws IOException, CrucialException {
        tools = plugin.fileManager.loadFromJson("tools.json", new TypeToken<ArrayList<DrugTool>>() {
        }.getType());
        for (DrugTool item : tools) {
            item.unregister();
            item.register();
        }
        if (tools == null) {
            throw new IOException("There were no drug tools recognized!"); //TODO: remove later
        }
    }
}
