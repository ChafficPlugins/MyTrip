package de.chaffic.MyTrip.Events;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import io.github.chafficui.CrucialAPI.API.Effects;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.chaffic.MyTrip.Main;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DrugEvents implements Listener {

    private static final String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_RED + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;
    private static final Main plugin = Main.getPlugin(Main.class);
    private static final String master = "mytrip.*";
    private final boolean permissionsOn = plugin.getConfig().getBoolean("settings.permissions");
    private static final String noPermissions = ChatColor.RED + plugin.getWord("no permissions");

    //drug consume
    @EventHandler
    public void onDrugConsume(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack stack = p.getInventory().getItemInMainHand();
        Action action = e.getAction();

        if((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) && stack.getType() != Material.AIR)){
            CrucialItem cItem = CrucialItem.getByKey(stack);
            if(cItem instanceof MyDrug && cItem.isRegistered()){
                e.setCancelled(true);
                if(p.hasPermission("mytrip." + cItem.getName()) || p.hasPermission(master) || !permissionsOn){
                    if(((MyDrug) cItem).isBloody()){
                        Effects.setBlood(p, 10);
                    }

                    //all addiction events
                    if(plugin.getConfig().getBoolean("features.addiction")){
                        if(DrugAPI.getPlayerData(p.getUniqueId()).consume((MyDrug) cItem)){
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100*20, 0));
                        }
                    }

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            DrugAPI.doDrug(p, (MyDrug) cItem, stack);
                        }
                    }.runTaskAsynchronously(plugin);
                } else {
                    p.sendMessage(prefix + noPermissions);
                    e.setCancelled(true);
                }
            }
        }
    }

    //drug craft
    @EventHandler
    public void onDrugCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack stack = e.getCurrentItem();
        if(stack != null && stack.getItemMeta() != null) {
            if (CrucialItem.getByKey(stack) instanceof MyDrug) {
                MyDrug myDrug = (MyDrug) CrucialItem.getByKey(stack);
                if (!(!permissionsOn || p.hasPermission(master) || p.hasPermission("mytrip.craft.*") || p.hasPermission("mytrip.craft." + myDrug.getName()))) {
                    p.sendMessage(prefix + noPermissions);
                    e.setCancelled(true);
                }
            }
        }
    }

    //crafting menu //fine //TODO add not drug set warning
    @EventHandler
    public void onDrugset(PrepareItemCraftEvent e) {
        String drugset = plugin.getWord("drug set");
        for(HumanEntity he:e.getViewers()) {
            if(e.getRecipe() != null){
                ItemStack result = e.getRecipe().getResult();
                /*
                if(he.getTargetBlock(null, 5).getType() == Material.PLAYER_WALL_HEAD || he.getTargetBlock(null, 5).getType() == Material.PLAYER_HEAD) {
                    Skull meta = (Skull) he.getTargetBlock(null, 5).getState();
                    if(meta.hasOwner() && meta.getOwner().equals(CItem.getCrucialItemByName(plugin.getWord("drug set")).getMaterial())){
                        if(result.getItemMeta() == null || CItem.getCrucialItemByName(result.getItemMeta().getDisplayName()) instanceof MyDrug){
                            e.getInventory().setItem(0, new ItemStack(Material.AIR));
                        }
                    }
                } else if(e.getRecipe().getResult().getItemMeta() != null && CItem.getCrucialItemByName(e.getRecipe().getResult().getItemMeta().getDisplayName()) instanceof MyDrug){
                    e.getInventory().setItem(0, new ItemStack(Material.AIR));
                }
                */
            }
        }
    }
}
