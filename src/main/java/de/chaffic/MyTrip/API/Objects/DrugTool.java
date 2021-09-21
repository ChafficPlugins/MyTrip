package de.chaffic.MyTrip.API.Objects;

import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DrugTool extends CrucialItem {
    private final String id;
    public DrugTool(String name, String head, String key){
        super(name, head, "DRUG_TOOL");
        this.id = key;
    }

    public DrugTool(String name, Material material, String key){
        super(name, material, "DRUG_TOOL");
        this.id = key;
    }

    @Override
    public String getId(){
        return this.id;
    }

    @Override
    public String getKey(){
        return this.id;
    }

    public static String getKey(ItemStack stack){
        String provKey = CrucialItem.getKey(stack);
        if(provKey.contains("DRUG_TOOL")){
            if(provKey.contains("drug_set")){
                return "drug_set.HEAD:dohyunpark.DRUG_TOOL";
            } else if(provKey.contains("drug_test")){
                return "drug_test.STICK.DRUG_TOOL";
            } else if(provKey.contains("anti_toxin")){
                return "anti_toxin.HONEY_BOTTLE.DRUG_TOOL";
            }
        }
        return "";
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
