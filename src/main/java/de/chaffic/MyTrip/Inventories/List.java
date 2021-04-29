package de.chaffic.MyTrip.Inventories;

import de.chaffic.MyTrip.Main;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.API.GUIs.*;
import de.chaffic.MyTrip.API.GUIs.content.*;
import io.github.chafficui.CrucialAPI.API.CItem;
import io.github.chafficui.CrucialAPI.API.Stack;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class List implements InventoryProvider {

    private static final Main plugin = Main.getPlugin(Main.class);

    public static final SmartInventory drugsInv = SmartInventory.builder()
            .id("drugList")
            .provider(new List())
            .size(6, 9)
            .title(plugin.getWord("drugs"))
            .closeable(true)
            .manager(plugin.GUIAPI)
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        int o = 0; //inv slot
        int q = 0;

        inventoryContents.fill(ClickableItem.empty(Stack.setStack(Material.GRAY_STAINED_GLASS_PANE, "")));

        for (CrucialItem cItem : CrucialItem.getRegisteredCrucialItems()) {
            if(cItem instanceof MyDrug){
                MyDrug drug = (MyDrug) cItem;

                inventoryContents.set(q, o, ClickableItem.of(drug.get(), e -> {
                    if(e.isLeftClick()){
                        //inv öffnen
                        Player p = (Player) e.getWhoClicked();
                        DrugShow drugShow = new DrugShow();
                        SmartInventory inventory = drugShow.createDrugShow(drug);
                        inventory.open(p);
                        p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                    }
                }));
                o++;
                if(o > 8){
                    q++;
                    o = 0;
                }
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
