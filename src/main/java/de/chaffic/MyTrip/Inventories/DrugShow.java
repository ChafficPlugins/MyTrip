package de.chaffic.MyTrip.Inventories;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.Main;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.API.GUIs.*;
import de.chaffic.MyTrip.API.GUIs.content.*;
import io.github.chafficui.CrucialAPI.API.CItem;
import io.github.chafficui.CrucialAPI.API.Stack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class DrugShow implements InventoryProvider {

    private final Main plugin = Main.getPlugin(Main.class);


    public SmartInventory createDrugShow(MyDrug drug){
        return SmartInventory.builder()
                .id("drugShow" + drug.getName())
                .provider(new DrugShow())
                .size(6, 9)
                .title(drug.getName())
                .closeable(true)
                .manager(plugin.GUIAPI)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        MyDrug myDrug = MyDrug.getByName(contents.inventory().getTitle());

        contents.fill(ClickableItem.empty(Stack.setStack(Material.GRAY_STAINED_GLASS_PANE, "")));

        //navigation
        contents.set(5, 4, ClickableItem.of(Stack.setStack(Material.RED_STAINED_GLASS_PANE, "BACK",
                Collections.singletonList("Return to drug list")), e -> {
            if(e.isLeftClick()){
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                player.getOpenInventory().close();
                List.drugsInv.open(p);
            }
        }));
        contents.set(5, 0, ClickableItem.of(Stack.setStack(Material.SKELETON_SKULL, "DELETE",
                Collections.singletonList("This cannot be undone!")), e -> {
            if(e.isLeftClick()){
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                contents.inventory().close(p);
                DrugAPI.deleteDrug(myDrug, p);
                plugin.fc.saveItems();
            }
        }));
        contents.set(5, 8, ClickableItem.of(Stack.setStack(Material.WOODEN_AXE, "EDIT",
                Collections.singletonList("Editing is currently in an early alpha version.")), e -> {
            if(e.isLeftClick()){
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                contents.inventory().close(p);
                DrugEditor drugEditor = new DrugEditor();
                drugEditor.getDrugEditor(myDrug).open(p);
            }
        }));

        //durgcrafting
        String[] ingredients = myDrug.getCrafting();

        contents.set(0, 2, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[0]))));
        contents.set(0, 3, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[1]))));
        contents.set(0, 4, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[2]))));
        contents.set(1, 2, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[3]))));
        contents.set(1, 3, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[4]))));
        contents.set(1, 4, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[5]))));
        contents.set(2, 2, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[6]))));
        contents.set(2, 3, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[7]))));
        contents.set(2, 4, ClickableItem.empty(Stack.setStack(Material.getMaterial(ingredients[8]))));

        contents.set(1, 6, ClickableItem.empty(myDrug.get()));

        //druginformation
        {
            //effects

            String[] effects = new String[myDrug.getEffects().size()];
            int i = 0;
            for (String[] effect:myDrug.getEffects()) {
                effects[i] = effect[0];
                i++;
            }

            contents.set(4, 3, ClickableItem.empty(
                    Stack.setStack(Material.LINGERING_POTION, ChatColor.GRAY + "Effects", Arrays.asList(effects))));

           //fx effects
            contents.set(4, 5, ClickableItem.empty(
                    Stack.setStack(Material.RED_DYE, ChatColor.GRAY + "FXEffects", Collections.singletonList("Bloody: " +
                            myDrug.isBloody()))));

            //numbers
            contents.set(4, 4, ClickableItem.empty(
                    Stack.setStack(Material.CLOCK, ChatColor.GRAY + "Information", Arrays.asList("Overdose: " +
                    myDrug.getOverdose(), "Addiction probability: " +
                    myDrug.getAddict(), "Duration: " +
                    myDrug.getDuration(), "Effect delay: " +
                    myDrug.getEffectDelay()))));
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
