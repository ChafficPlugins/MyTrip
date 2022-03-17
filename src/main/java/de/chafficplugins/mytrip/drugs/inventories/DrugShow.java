package de.chafficplugins.mytrip.drugs.inventories;

import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static de.chafficplugins.mytrip.utils.ConfigStrings.PREFIX;

public class DrugShow extends Page {
    private final MyDrug drug;

    private DrugShow(int size, MyDrug drug) {
        super(size, drug.getName(), Material.WHITE_STAINED_GLASS_PANE);
        this.drug = drug;
    }

    @Override
    public void populate() {
        addItem(new InventoryItem(45, Material.RED_STAINED_GLASS_PANE, "BACK", Collections.singletonList("Return to drug list"),
                (click -> DrugList.drugList.open(click.getPlayer()))
        ));
        addItem(new InventoryItem(53, Material.SKELETON_SKULL, "DELETE", Collections.singletonList(ChatColor.RED + "THIS CANNOT BE UNDONE!"),
                (click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                    MyDrug.deleteDrug(drug, p);
                    DrugList.drugList.open(p);
                    try {
                        MyDrug.saveAll();
                    } catch (IOException e) {
                        p.sendMessage(PREFIX + ChatColor.RED + "Failed to save drugs!");
                    }
                    p.closeInventory();
                    DrugList.drugList.open(click.getPlayer());
                })
        ));
        addItem(new InventoryItem(52, Material.WOODEN_AXE, "EDIT", Collections.singletonList("Edit your drugs."),
                (click -> {
                    if(click.getEvent().isLeftClick()) {
                        Player p = click.getPlayer();
                        p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                        new DrugEdit(drug, this).open(p);
                    }
                })
        ));

        //Drugcrafting
        String[] ingredients = drug.getRecipe();
        addItem(new InventoryItem(10, new ItemStack(Material.getMaterial(ingredients[0]))));
        addItem(new InventoryItem(11, new ItemStack(Material.getMaterial(ingredients[1]))));
        addItem(new InventoryItem(12, new ItemStack(Material.getMaterial(ingredients[2]))));
        addItem(new InventoryItem(19, new ItemStack(Material.getMaterial(ingredients[3]))));
        addItem(new InventoryItem(20, new ItemStack(Material.getMaterial(ingredients[4]))));
        addItem(new InventoryItem(21, new ItemStack(Material.getMaterial(ingredients[5]))));
        addItem(new InventoryItem(28, new ItemStack(Material.getMaterial(ingredients[6]))));
        addItem(new InventoryItem(29, new ItemStack(Material.getMaterial(ingredients[7]))));
        addItem(new InventoryItem(30, new ItemStack(Material.getMaterial(ingredients[8]))));

        addItem(new InventoryItem(24, drug.getItemStack()));

        //effects
        String[] effects = new String[drug.getEffects().size()];
        int i = 0;
        for (String[] effect : drug.getEffects()) {
            effects[i] = effect[0];
            i++;
        }

        addItem(new InventoryItem(48, Stack.getStack(Material.LINGERING_POTION, ChatColor.GRAY + "Effects", Arrays.asList(effects))));
        //FX-effects
        //addItem(new InventoryItem(40, Stack.getStack(Material.RED_DYE, ChatColor.GRAY + "FXEffects" + Arrays.asList(effects))));
        //numbers
        addItem(new InventoryItem(50, Stack.getStack(Material.CLOCK, ChatColor.GRAY + "Information", Arrays.asList("Overdose: " +
                drug.getOverdose(), "Addiction probability: " + drug.getAddictionProbability(), "Duration: " +
                drug.getDuration(), "Effect delay: " + drug.getEffectDelay()))));
    }

    public static DrugShow get(MyDrug drug) {
        for (Page page : pages) {
            if(page instanceof DrugShow && ((DrugShow) page).drug.equals(drug)) {
                return (DrugShow) page;
            }
        }
        return new DrugShow(6, drug);
    }
}
