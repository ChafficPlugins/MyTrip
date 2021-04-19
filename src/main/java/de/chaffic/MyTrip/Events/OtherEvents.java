package de.chaffic.MyTrip.Events;

import java.util.UUID;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.API.Objects.DrugPlayer;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.CItem;
import io.github.chafficui.CrucialAPI.API.Effects;
import io.github.chafficui.CrucialAPI.API.Interface;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import de.chaffic.MyTrip.Main;
import de.chaffic.MyTrip.API.UpdateCheckerAPI;
import org.bukkit.inventory.meta.SkullMeta;

public class OtherEvents implements Listener{

    public static String prefix = ChatColor.WHITE + "[" + ChatColor.WHITE + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;
    public static Main plugin = Main.getPlugin(Main.class);
    private final boolean permissionsOn = plugin.getConfig().getBoolean("settings.permissions");
    private final String master = "mytrip.*";
    private final String noPermissions = ChatColor.RED + plugin.getWord("no permissions");

    //Anti toxin and Drug set crafting
    @EventHandler
    public void onItemCraft(CraftItemEvent e) {
        Player p = (Player)e.getWhoClicked();
        ItemStack stack = e.getCurrentItem();

        //anti toxin and perms check
        if(stack.getType() != Material.AIR && ((stack.getItemMeta().getDisplayName().equals(plugin.getWord("anti toxin")) && !p.hasPermission("mytrip.craft.antitoxin")
                && !p.hasPermission(master) && permissionsOn) || (stack.getItemMeta().getDisplayName().equals(plugin.getWord("drug set")) && !p.hasPermission("mytrip.craft.drugset")
                && !p.hasPermission(master) && permissionsOn))) {
            p.sendMessage(prefix + noPermissions);
            e.setCancelled(true);
        }
    }

    //drug test false
    @EventHandler
    public void onDrugTest(PlayerInteractEvent e) {
        ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(stack.getType() != Material.AIR && stack.getItemMeta().getDisplayName().equals(plugin.getWord("drug test"))) {
                e.setCancelled(true);
            }
        }
    }

    //remove addiction
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(plugin.getConfig().getBoolean("features.heal_on_death")){
            Player p = e.getEntity();
            DrugAPI.getPlayerData(p.getUniqueId()).clear();
        }
    }

    //drug set usage
    @EventHandler
    public void onDrugSet(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.PLAYER_HEAD ||
                    e.getClickedBlock().getType() == Material.PLAYER_WALL_HEAD)){
                Skull meta = (Skull) e.getClickedBlock().getState();
                if(meta.getOwner() != null && meta.getOwner().equals(CItem.getCrucialItemByName(plugin.getWord("drug set")).getMaterial())){
                    if(p.hasPermission("mytrip.use.drugset") || p.hasPermission(master) || !permissionsOn) {
                        p.openWorkbench(null, true);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(prefix + noPermissions);
                    }
                }
            }
        }
    }

    //drug set break
    @EventHandler
    public void onDrugSetPickup(EntityPickupItemEvent e){
        ItemStack stack = e.getItem().getItemStack();

        if((stack.getType() == Material.PLAYER_HEAD || stack.getType() == Material.PLAYER_WALL_HEAD)){
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            if(meta.getOwner() != null && meta.getOwner().equals((CItem.getCrucialItemByName(plugin.getWord("drug set")).getMaterial()))) {
                MyDrug myDrug = (MyDrug) CItem.getCrucialItemByName(plugin.getWord("drug set"));
                meta = (SkullMeta) myDrug.get().getItemMeta();
                stack.setItemMeta(meta);
            }
        }
    }

    //update alert
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Effects.removeBlood(p);
        UUID UUID = p.getUniqueId();
        try {
            if(DrugAPI.getPlayerData(UUID) == null){
                DrugAPI.playerDatas.add(new DrugPlayer(p));
            }
            DrugAPI.getPlayerData(UUID).joined();
        } catch(Exception e1){
            plugin.getLogger().severe("Error 014: Failed to save playerdata.");
        }
        if(p.isOp() && plugin.getConfig().getBoolean("settings.alerts")) {
            new UpdateCheckerAPI(plugin, 76816).getVersion(version -> {
                String v = String.valueOf(version);
                String v2 = plugin.getDescription().getVersion();
                char[] onver = new char[3];
                char[] ofver = new char[3];

                onver[0] = v.charAt(0);
                ofver[0] = v2.charAt(0);
                if(ofver[0] < onver[0]) {
                    try {
                        onver[1] = v.charAt(2);
                    } catch(Exception ex) {
                        onver[1] = 0;
                    }
                    try {
                        ofver[1] = v2.charAt(2);
                    } catch(Exception ex) {
                        ofver[1] = 0;
                    }
                    if(ofver[1] < onver[1]) {
                        try {
                            onver[2] = v.charAt(4);
                        } catch(Exception ex) {
                            onver[2] = 0;
                        }
                        try {
                            ofver[2] = v2.charAt(4);
                        } catch(Exception ex) {
                            ofver[2] = 0;
                        }
                        if(ofver[2] < onver[2]) {
                            Interface.showText(p, "new MyTrip Update", "Download it from Spigot or GitHub, or activate AutoUpdater in Conifg.yml.");
                        }
                    }
                }
            });
        }
    }
}
