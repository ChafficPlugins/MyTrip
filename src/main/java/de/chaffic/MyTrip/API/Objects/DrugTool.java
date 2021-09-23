package de.chaffic.MyTrip.API.Objects;

import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import org.bukkit.Material;

import java.util.UUID;

public class DrugTool extends CrucialItem {
    public DrugTool(String name, String head) {
        super("drug_tool");
        this.isHead = true;
        this.name = name;
        this.material = head;
    }

    public DrugTool(String name, Material material) {
        super("drug_tool");
        this.isHead = false;
        this.name = name;
        this.material = material.name();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    public static DrugTool getByKey(String key){
        for (DrugTool item: Main.tools){
            if(item.isRegistered() && item.getKey().equals(key)){
                return item;
            }
        }
        return null;
    }
}
