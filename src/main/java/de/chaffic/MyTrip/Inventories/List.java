package de.chaffic.MyTrip.Inventories;

import de.chaffic.MyTrip.API.GUIs.ClickableItem;
import de.chaffic.MyTrip.API.GUIs.SmartInventory;
import de.chaffic.MyTrip.API.GUIs.content.InventoryContents;
import de.chaffic.MyTrip.API.GUIs.content.InventoryProvider;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
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

        inventoryContents.fill(ClickableItem.empty(Stack.getStack(Material.GRAY_STAINED_GLASS_PANE, "")));

        for (CrucialItem cItem : CrucialItem.CRUCIAL_ITEMS) {
            if (cItem.isRegistered() && cItem instanceof MyDrug) {
                MyDrug drug = (MyDrug) cItem;

                inventoryContents.set(q, o, ClickableItem.of(drug.getItemStack(), e -> {
                    if (e.isLeftClick()) {
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
