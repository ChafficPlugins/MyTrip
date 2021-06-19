package de.chaffic.MyTrip.Inventories.drugCreator;

import de.chaffic.MyTrip.Main;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.API.GUIs.*;
import de.chaffic.MyTrip.API.GUIs.content.*;
import io.github.chafficui.CrucialAPI.API.CItem;
import io.github.chafficui.CrucialAPI.API.Stack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;

public class Effects implements InventoryProvider {

    private final Main plugin = Main.getPlugin(Main.class);
    private MyDrug myDrug;

    public SmartInventory getEffectinv(MyDrug drug) {
        return SmartInventory.builder()
                .id("effectMenu")
                .provider(new Effects())
                .size(4, 9)
                .title(drug.getName() + " - " + plugin.getWord("InvCreation 2"))
                .closeable(false)
                .manager(plugin.GUIAPI)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        myDrug = MyDrug.getByName(contents.inventory().getTitle().split(" - ")[0]);

        contents.fill(ClickableItem.empty(Stack.setStack(Material.GRAY_STAINED_GLASS_PANE, "")));

        //navigation
        contents.set(3, 0, ClickableItem.of(Stack.setStack(Material.RED_STAINED_GLASS_PANE, "BACK"), e -> {
            if(e.isLeftClick()){
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                contents.inventory().close(player);
                //TODO open Menu 1
                //placeholder
                Inventory inv = Bukkit.createInventory(p, 27, myDrug.getName() + " - " + plugin.getWord("InvCreation 1"));
                p.openInventory(inv);
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
            }
        }));
        contents.set(3, 8, ClickableItem.of(Stack.setStack(Material.GREEN_STAINED_GLASS_PANE, "CONTINUE"), e -> {
            if(e.isLeftClick()){
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                contents.inventory().close(player);
                FXEffects fxEffects = new FXEffects();
                fxEffects.createFXEffects(myDrug).open(p);
            }
        }));
        contents.set(3, 4, ClickableItem.of(Stack.setStack(Material.RED_STAINED_GLASS_PANE, "RESET",
                Collections.singletonList("reset all effects")), event -> {
            myDrug.setEffects(new ArrayList<>());
            contents.inventory().close(player);
            getEffectinv(myDrug).open(player);
        }));

        {
            //effects
            addEffect(0, 0, Material.GOLDEN_APPLE, "ABSORPTION", contents);
            addEffect(0, 1, Material.GHAST_TEAR, "BLINDNESS", contents);
            addEffect(0, 2, Material.SPONGE, "CONFUSION", contents);
            addEffect(0, 3, Material.SHIELD, "DAMAGE_RESISTANCE", contents);
            addEffect(0, 4, Material.DIAMOND_SHOVEL, "FAST_DIGGING", contents);
            addEffect(0, 5, Material.LAVA_BUCKET, "FIRE_RESISTANCE", contents);
            addEffect(0, 6, Material.BELL, "GLOWING", contents);
            addEffect(0, 7, Material.ZOMBIE_HEAD, "HARM", contents);
            addEffect(0, 8, Material.APPLE, "HEAL", contents);
            addEffect(1, 0, Material.ENCHANTED_GOLDEN_APPLE, "HEALTH_BOOST", contents);
            addEffect(1, 1, Material.BREAD, "HUNGER", contents);
            addEffect(1, 2, Material.DIAMOND_SWORD, "INCREASE_DAMAGE", contents);
            addEffect(1, 3, Material.LEATHER_CHESTPLATE, "INVISIBILITY", contents);
            addEffect(1, 4, Material.DIAMOND_BOOTS, "JUMP", contents);
            addEffect(1, 5, Material.SHULKER_BOX, "LEVITATION", contents);
            addEffect(1, 6, Material.CHORUS_FLOWER, "LUCK", contents);
            addEffect(1, 7, Material.SKELETON_SKULL, "NIGHT_VISION", contents);
            addEffect(1, 8, Material.SPIDER_EYE, "POISON", contents);
            addEffect(2, 0, Material.CARROT, "REGENERATION", contents);
            addEffect(2, 1, Material.CARROT_ON_A_STICK, "SATURATION", contents);
            addEffect(2, 2, Material.LEATHER_BOOTS, "SLOW", contents);
            addEffect(2, 3, Material.WOODEN_SHOVEL, "SLOW_DIGGING", contents);
            addEffect(2, 4, Material.ELYTRA, "SLOW_FALLING", contents);
            addEffect(2, 5, Material.SHIELD, "DAMAGE_RESISTANCE", contents);
            addEffect(2, 6, Material.WITHER_ROSE, "UNLUCK", contents);
            addEffect(2, 7, Material.PUFFERFISH, "WATER_BREATHING", contents);
            addEffect(2, 8, Material.WHITE_BANNER, "WEAKNESS", contents);
            addEffect(3, 2, Material.WITHER_SKELETON_SKULL, "WITHER", contents);
            addEffect(3, 6, Material.POTION, "SPEED", contents);
        }
    }

    private void addEffect(int row, int column, Material material, String effect, InventoryContents contents){
        contents.set(row, column, ClickableItem.of(Stack.setStack(material, effect), e -> {
            if (e.isLeftClick()) {
                System.out.println(effect);
                Player player = (Player) e.getWhoClicked();
                e.getInventory().clear(e.getSlot());
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 29);
                myDrug.addEffect(new String[]{effect, "1"});
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
