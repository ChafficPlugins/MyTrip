package de.chaffic.MyTrip.Events;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.API.Objects.DrugTool;
import io.github.chafficui.CrucialAPI.API.Effects;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.chaffic.MyTrip.Main;

public class HighEvents implements Listener {
    public static String prefix = ChatColor.WHITE + "[" + ChatColor.WHITE + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;
    private static final Main plugin = Main.getPlugin(Main.class);
    private final boolean permissionsOn = plugin.getConfig().getBoolean("settings.permissions");
    private final String noPermissions = ChatColor.RED + plugin.getWord("no permissions");

    //drug test
    @EventHandler
    public void onTest(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        p.getInventory().getItemInMainHand();
        if(p.getInventory().getItemInMainHand().getItemMeta() != null) {
            p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            ItemStack stack = p.getInventory().getItemInMainHand();
            if (DrugTool.getByKey(stack) != null && DrugTool.getKey(stack).equals("drug_test.STICK.DRUG_TOOL")) {
                stack.setAmount(stack.getAmount());
                p.getInventory().setItemInMainHand(stack);
                if ((e.getRightClicked().getType().equals(EntityType.PLAYER))) {
                    Player clicked = (Player) e.getRightClicked();
                    if (DrugAPI.getPlayerData(p.getUniqueId()).dose > 0) {
                        p.sendMessage(prefix + clicked.getDisplayName() + " " + plugin.getWord("is high"));
                    } else {
                        p.sendMessage(prefix + clicked.getDisplayName() + " " + plugin.getWord("not high"));
                    }
                }
            }
        }
    }

    //anti toxin consumption & disable milk bukkit
    @EventHandler
    public void onAntiToxin(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if(DrugTool.getByKey(item) != null && DrugTool.getKey(item).equals("anti_toxin.HONEY_BOTTLE.DRUG_TOOL")) {
            String master = "mytrip.*";
            if(p.hasPermission("mytrip.use.antitoxin") || p.hasPermission(master) || !permissionsOn) {
                for(PotionEffect effect : p.getActivePotionEffects()) {
                    p.removePotionEffect(effect.getType());
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 120, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 120, 1));
                Effects.removeBlood(p);
                DrugAPI.getPlayerData(p.getUniqueId()).clear();
                DrugAPI.getPlayerData(p.getUniqueId()).dose = 0;
            } else {
                e.setCancelled(true);
                p.sendMessage(prefix + noPermissions);
            }
        } else {
            item.getType();
            if(item.getType().equals(Material.MILK_BUCKET)) {
                if(DrugAPI.getPlayerData(p.getUniqueId()).dose > 0) {
                    e.setCancelled(true);
                }
            }
        }
    }
}