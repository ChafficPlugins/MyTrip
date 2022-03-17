package de.chafficplugins.mytrip.drugs.inventories.drugCreation;

import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import io.github.chafficui.CrucialAPI.exceptions.CrucialException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static de.chafficplugins.mytrip.utils.ConfigStrings.PREFIX;

public class DrugEffects extends Page {
    private final MyDrug drug;
    private final DrugCraft predecessor;

    public DrugEffects(MyDrug drug, DrugCraft predecessor) {
        super(6, drug.getName() + " Creator", Material.WHITE_STAINED_GLASS_PANE);
        this.drug = drug;
        this.predecessor = predecessor;
    }

    @Override
    public void populate() {
        //effects
        addEffect(0, Material.GOLDEN_APPLE, "ABSORPTION");
        addEffect(1, Material.GHAST_TEAR, "BLINDNESS");
        addEffect(2, Material.SPONGE, "CONFUSION");
        addEffect(3, Material.SHIELD, "DAMAGE_RESISTANCE");
        addEffect(4, Material.DIAMOND_SHOVEL, "FAST_DIGGING");
        addEffect(5, Material.LAVA_BUCKET, "FIRE_RESISTANCE");
        addEffect(6, Material.BELL, "GLOWING");
        addEffect(7, Material.ZOMBIE_HEAD, "HARM");
        addEffect(8, Material.APPLE, "HEAL");
        addEffect(9, Material.ENCHANTED_GOLDEN_APPLE, "HEALTH_BOOST");
        addEffect(10, Material.BREAD, "HUNGER");
        addEffect(11, Material.DIAMOND_SWORD, "INCREASE_DAMAGE");
        addEffect(12, Material.LEATHER_CHESTPLATE, "INVISIBILITY");
        addEffect(13, Material.DIAMOND_BOOTS, "JUMP");
        addEffect(14, Material.SHULKER_BOX, "LEVITATION");
        addEffect(15, Material.CHORUS_FLOWER, "LUCK");
        addEffect(16, Material.SKELETON_SKULL, "NIGHT_VISION");
        addEffect(17, Material.SPIDER_EYE, "POISON");
        addEffect(18, Material.CARROT, "REGENERATION");
        addEffect(19, Material.CARROT_ON_A_STICK, "SATURATION");
        addEffect(20, Material.LEATHER_BOOTS, "SLOW");
        addEffect(21, Material.WOODEN_SHOVEL, "SLOW_DIGGING");
        addEffect(22, Material.ELYTRA, "SLOW_FALLING");
        addEffect(23, Material.SHIELD, "DAMAGE_RESISTANCE");
        addEffect(24, Material.WITHER_ROSE, "UNLUCK");
        addEffect(25, Material.PUFFERFISH, "WATER_BREATHING");
        addEffect(26, Material.WHITE_BANNER, "WEAKNESS");
        addEffect(30, Material.WITHER_SKELETON_SKULL, "WITHER");
        addEffect(32, Material.POTION, "SPEED");

        //navigation
        addItem(new InventoryItem(45, Material.RED_STAINED_GLASS_PANE, "BACK", null, click -> {
            Player player = click.getPlayer();
            drug.setEffects(new ArrayList<>());
            predecessor.open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        addItem(new InventoryItem(40, Material.YELLOW_STAINED_GLASS_PANE, "RESET", null, click -> {
            Player player = click.getPlayer();
            drug.setEffects(new ArrayList<>());
            reloadInventory();
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
        addItem(new InventoryItem(53, Material.GREEN_STAINED_GLASS_PANE, "CREATE", null, click -> {
            Player player = click.getPlayer();
            drug.setEffectDelay(2);
            drug.setAddictionProbability(5);
            drug.setDuration(100);
            drug.setOverdose(5);
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
            click.getPlayer().closeInventory();
            try {
                drug.register();
                player.sendMessage(PREFIX + ChatColor.GREEN + "Created " + drug.getName() + ".");
                click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 29);
            } catch (CrucialException e) {
                player.sendMessage(PREFIX + ChatColor.RED + "Failed to create drug.");
                drug.delete();
                click.getPlayer().playSound(click.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 29);
            }
        }));
    }

    private void addEffect(int slot, Material material, String effect) {
        addItem(new InventoryItem(slot, material, effect, null, click -> {
            Player player = click.getPlayer();
            click.getClickedInventory().clear(click.getSlot());
            drug.addEffect(new String[]{effect, "1"});
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
        }));
    }
}
