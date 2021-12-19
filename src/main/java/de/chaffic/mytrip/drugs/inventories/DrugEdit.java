package de.chaffic.mytrip.drugs.inventories;

import de.chaffic.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DrugEdit extends Page {
    private final MyDrug drug;
    private final DrugShow predecessor;

    /**
     * Every child needs to do super() in its constructor.
     */
    public DrugEdit(MyDrug drug, DrugShow predecessor) {
        super(6, drug.getName() + " Editor", Material.WHITE_STAINED_GLASS_PANE);
        this.drug = drug;
        this.predecessor = predecessor;
    }

    @Override
    public void populate() {
        addItem(new InventoryItem(53, Material.GREEN_STAINED_GLASS_PANE, "APPLY", Collections.singletonList("Apply your changes."),
                click -> {
                    Player p = click.getPlayer();
                    predecessor.open(click.getPlayer());
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    reloadInventory();
                }
        ));

        addItem(new InventoryItem(3, Material.RED_WOOL, "-1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setDuration(drug.getDuration()-1);
                    reloadInventory();
                }
        ));
        addItem(new InventoryItem(4, Material.CLOCK, "Alter Duration:", Collections.singletonList(String.valueOf(drug.getDuration())),
                click -> {}
        ));
        addItem(new InventoryItem(5, Material.GREEN_WOOL, "+1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setDuration(drug.getDuration()+1);
                    reloadInventory();
                }
        ));

        addItem(new InventoryItem(12, Material.RED_WOOL, "-1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setEffectDelay(drug.getEffectDelay() - 1);
                    reloadInventory();
                }
        ));
        addItem(new InventoryItem(13, Material.COMPARATOR, "Alter Effect Delay:",  Collections.singletonList(String.valueOf(drug.getEffectDelay())),
                click -> {}
        ));
        addItem(new InventoryItem(14, Material.GREEN_WOOL, "+1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setEffectDelay(drug.getEffectDelay()+1);
                    reloadInventory();
                }
        ));

        addItem(new InventoryItem(21, Material.RED_WOOL, "-1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setOverdose(drug.getOverdose()-1);
                    reloadInventory();
                }
        ));
        addItem(new InventoryItem(22, Material.WITHER_SKELETON_SKULL, "Alter Overdose:",  Collections.singletonList(String.valueOf(drug.getOverdose())),
                click -> {}
        ));
        addItem(new InventoryItem(23, Material.GREEN_WOOL, "+1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setOverdose(drug.getOverdose()+1);
                    reloadInventory();
                }
        ));

        addItem(new InventoryItem(30, Material.RED_WOOL, "-1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setAddictionProbability(drug.getAddictionProbability()-1);
                    reloadInventory();
                }
        ));
        addItem(new InventoryItem(31, Material.SPIDER_EYE, "Alter Addiction Probability:",  Collections.singletonList(String.valueOf(drug.getAddictionProbability())),
                click -> {}
        ));
        addItem(new InventoryItem(32, Material.GREEN_WOOL, "+1", null,
                click -> {
                    Player p = click.getPlayer();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 29);
                    drug.setAddictionProbability(drug.getAddictionProbability()+1);
                    reloadInventory();
                }
        ));
    }
}
