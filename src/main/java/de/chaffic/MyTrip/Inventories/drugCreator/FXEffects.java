package de.chaffic.MyTrip.Inventories.drugCreator;

import de.chaffic.MyTrip.API.GUIs.ClickableItem;
import de.chaffic.MyTrip.API.GUIs.SmartInventory;
import de.chaffic.MyTrip.API.GUIs.content.InventoryContents;
import de.chaffic.MyTrip.API.GUIs.content.InventoryProvider;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class FXEffects implements InventoryProvider {
    private static final String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_RED + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;

    private final Main plugin = Main.getPlugin(Main.class);
    private MyDrug myDrug;

    public SmartInventory createFXEffects(MyDrug drug){
        return SmartInventory.builder()
                .id("fxMenu")
                .provider(new FXEffects())
                .size(2, 9)
                .title(plugin.getWord("InvCreation 3") + " - " + drug.getKey())
                .closeable(false)
                .manager(plugin.GUIAPI)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        myDrug = MyDrug.getUnregisteredDrugByKey(inventoryContents.inventory().getTitle().split(" - ")[1]);

        inventoryContents.fill(ClickableItem.empty(Stack.getStack(Material.GRAY_STAINED_GLASS_PANE, "")));
        inventoryContents.set(0, 3, ClickableItem.empty(Stack.getStack(Material.RED_DYE, "BloodFX")));
        inventoryContents.set(1, 3, ClickableItem.empty(Stack.getStack(Material.GRAY_DYE, "ParticleFX")));

        //blood options
        inventoryContents.set(0, 5, ClickableItem.of(Stack.getStack(Material.GREEN_STAINED_GLASS_PANE, "ENABLE"), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.setBloody(true);
            }
        }));
        inventoryContents.set(0, 6, ClickableItem.of(Stack.getStack(Material.RED_STAINED_GLASS_PANE, "DISABLE"), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.setBloody(false);
            }
        }));

        //particle options
        inventoryContents.set(1, 5, ClickableItem.of(Stack.getStack(Material.GREEN_STAINED_GLASS_PANE, "ENABLE"), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.setParticle("ASH");
            }
        }));
        inventoryContents.set(1, 6, ClickableItem.of(Stack.getStack(Material.RED_STAINED_GLASS_PANE, "DISABLE"), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.setParticle(null);
            }
        }));

        //navigation
        inventoryContents.set(1, 8, ClickableItem.of(Stack.getStack(Material.PURPLE_STAINED_GLASS_PANE,
                "CREATE", Collections.singletonList("Create drug")), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                inventoryContents.inventory().close(p);
                myDrug.setEffectDelay(2);
                myDrug.setAddict(5);
                myDrug.setDuration(100);
                myDrug.setOverdose(5);
                try {
                    myDrug.register();
                    p.sendMessage(prefix + ChatColor.GREEN + "Drug was successfully created!");
                } catch (CrucialException ex) {
                    ex.printStackTrace();
                    p.sendMessage(prefix + ChatColor.RED + "Failed to create Drug!");
                }
                MyDrug.clearUnregisteredDrugs();
                plugin.fc.saveItems();
            }
        }));
        inventoryContents.set(1, 0, ClickableItem.of(Stack.getStack(Material.RED_STAINED_GLASS_PANE,
                "BACK", Collections.singletonList("Return to previous page")), e -> {
            if (e.isLeftClick()) {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.setEffects(new ArrayList<>());
                inventoryContents.inventory().close(p);
                Effects effects = new Effects();
                effects.getEffectinv(myDrug).open(p);
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
