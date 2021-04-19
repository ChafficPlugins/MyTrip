package de.chaffic.MyTrip.Events;

import de.chaffic.MyTrip.Inventories.drugCreator.Effects;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.CItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.chaffic.MyTrip.Main;

import java.util.ArrayList;


public class MenuEvents implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);

    //create /mt create inv
    @EventHandler
    public void onCreateMenu(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        String name = e.getView().getTitle();
        ItemStack stack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta;
        //configure material
        if(name.contains(" - ") && name.split(" - ")[1].equals(plugin.getWord("InvCreation 1"))) {
            stack.setType(Material.GRAY_STAINED_GLASS_PANE);
            inv.setItem(0, stack);
            inv.setItem(4, stack);
            inv.setItem(5, stack);
            inv.setItem(6, stack);
            inv.setItem(7, stack);
            inv.setItem(8, stack);
            inv.setItem(9, stack);
            inv.setItem(13, stack);
            inv.setItem(14, stack);
            inv.setItem(16, stack);
            inv.setItem(17, stack);
            inv.setItem(18, stack);
            inv.setItem(22, stack);
            inv.setItem(23, stack);
            inv.setItem(24, stack);
            inv.setItem(25, stack);
            meta = stack.getItemMeta();
            meta.setDisplayName("CONTINUE");
            stack.setItemMeta(meta);
            stack.setType(Material.GREEN_STAINED_GLASS_PANE);
            inv.setItem(26, stack);
        }
    }

    //InvCreationClick
    @EventHandler
    public void onDrugListClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        String name = e.getView().getTitle();

        if(name.contains(" - ") && name.split(" - ")[1].equals(plugin.getWord("InvCreation 1"))) {
            ItemStack stack = e.getCurrentItem();
            if(stack != null && stack.getType() == Material.GREEN_STAINED_GLASS_PANE) {
                Inventory inventory = p.getOpenInventory().getTopInventory();
                if(inventory.getItem(15) != null && inventory.getItem(15).getType() != Material.AIR){
                    e.setCancelled(true);
                    MyDrug drug = (MyDrug) CItem.getCrucialItemByNameIgnoreRegistration(name.split(" - ")[0]);
                    p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10.0F, 29.0F);
                    ArrayList<String> crafting = new ArrayList<>();
                    crafting.add(getItem(inventory.getItem(1)));
                    crafting.add(getItem(inventory.getItem(2)));
                    crafting.add(getItem(inventory.getItem(3)));
                    crafting.add(getItem(inventory.getItem(10)));
                    crafting.add(getItem(inventory.getItem(11)));
                    crafting.add(getItem(inventory.getItem(12)));
                    crafting.add(getItem(inventory.getItem(19)));
                    crafting.add(getItem(inventory.getItem(20)));
                    crafting.add(getItem(inventory.getItem(21)));
                    drug.setCrafting(crafting.toArray(new String[0]));
                    drug.setMaterial(getItem(inventory.getItem(15)));
                    Effects effects = new Effects();
                    p.closeInventory();
                    effects.getEffectinv(drug).open(p);
                } else {
                    e.setCancelled(true);
                    p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 10.0F, 29.0F);
                }
            }
        }
    }

    private String getItem(ItemStack stack){
        if(stack != null){
            return stack.getType().toString();
        } else {
            return "AIR";
        }
    }
}
