package de.chafficplugins.mytrip.drugs.inventories;

import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialLib.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DrugList extends Page {
    public static DrugList drugList;
    /**
     * Every child needs to do super() in its constructor.
     */
    public DrugList() {
        super(6, "Drugs", Material.WHITE_STAINED_GLASS_PANE);
        drugList = this;
    }

    @Override
    public void populate() {
        int slot = 0;
        for (CrucialItem crucialItem : CrucialItem.CRUCIAL_ITEMS) {
            if(crucialItem instanceof MyDrug && crucialItem.isRegistered()) {
                MyDrug myDrug = (MyDrug) crucialItem;
                addItem(new InventoryItem(slot++, myDrug.getItemStack(), click -> {
                    if (click.getEvent().isLeftClick()) {
                        Player p = click.getPlayer();
                        DrugShow drugShow = DrugShow.get(myDrug);
                        drugShow.open(p);
                        p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                    }
                }));
            }
        }
    }
}
