package de.chafficplugins.mytrip.drugs.inventories.drugCreation;

import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class DrugCraft extends Page {
    private final MyDrug drug;

    public DrugCraft(MyDrug drug) {
        super(5, drug.getName() + " Creator", Material.WHITE_STAINED_GLASS_PANE);
        this.drug = drug;
        this.isMovable = true;
    }

    @Override
    public void populate() {
        addItem(new InventoryItem(10, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(11, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(12, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(19, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(20, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(21, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(28, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(29, new ItemStack(Material.AIR)));
        addItem(new InventoryItem(30, new ItemStack(Material.AIR)));

        addItem(new InventoryItem(24, new ItemStack(Material.AIR)));

        addItem(new InventoryItem(44, Material.GREEN_STAINED_GLASS_PANE, "Continue", null,
                click -> {
                    ItemStack[] stacks = new ItemStack[]{
                        click.getClickedInventory().getItem(10),
                        click.getClickedInventory().getItem(11),
                        click.getClickedInventory().getItem(12),
                        click.getClickedInventory().getItem(19),
                        click.getClickedInventory().getItem(20),
                        click.getClickedInventory().getItem(21),
                        click.getClickedInventory().getItem(28),
                        click.getClickedInventory().getItem(29),
                        click.getClickedInventory().getItem(30)
                    };
                    ItemStack result = click.getClickedInventory().getItem(24);
                    if((stacks[0] != null || stacks[1] != null || stacks[2] != null || stacks[3] != null ||
                            stacks[4] != null || stacks[5] != null || stacks[6] != null || stacks[7] != null ||
                            stacks[8] != null) && result != null) {
                        drug.setRecipe(stacks);
                        drug.setMaterial(result);
                        click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                        click.getPlayer().closeInventory();
                        new DrugEffects(drug, this).open(click.getPlayer());
                    } else {
                        click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 29);
                    }
                }
        ));
    }
}
