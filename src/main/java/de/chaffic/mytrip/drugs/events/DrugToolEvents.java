package de.chaffic.mytrip.drugs.events;

import de.chaffic.mytrip.drugs.objects.DrugPlayer;
import de.chaffic.mytrip.drugs.objects.DrugTool;
import de.chaffic.mytrip.utils.PlayerUtils;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.Utils.player.effects.VisualEffects;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static de.chaffic.mytrip.utils.ConfigStrings.*;

public class DrugToolEvents implements Listener {

    @EventHandler
    public void onTest(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        CrucialItem tool = DrugTool.getByStack(p.getInventory().getItemInMainHand());

        if (tool instanceof DrugTool && tool.getId().equals(DRUG_TEST_UUID)) {
            event.setCancelled(true);
            Entity entity = event.getRightClicked();
            if (PlayerUtils.hasPermissions(entity, PERM_USE_ANY, PERM_USE_DRUG_TEST)) {
                DrugPlayer player = DrugPlayer.getPlayer(entity.getUniqueId());
                if (player != null && player.getDose() > 0) {
                    p.sendMessage(PREFIX + ((Player) entity).getDisplayName() + " is high!"); //TODO: Localization
                } else {
                    p.sendMessage(PREFIX + ((Player) entity).getDisplayName() + " is not high!"); //TODO: Localization
                }
            } else {
                p.sendMessage(PREFIX + "§cYou do not have the permission to do this!"); //TODO: Localization
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAntiToxin(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        CrucialItem tool = DrugTool.getByStack(item);

        if (tool instanceof DrugTool && tool.getId().equals(ANTITOXIN_UUID)) {
            if (PlayerUtils.hasPermissions(p, PERM_USE_ANY, PERM_USE_ANTITOXIN)) {
                for (PotionEffect effect : p.getActivePotionEffects()) {
                    p.removePotionEffect(effect.getType());
                }
                //TODO: make this configurable
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 120, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 120, 1));
                VisualEffects.removeBlood(p);
                DrugPlayer player = DrugPlayer.getPlayer(p.getUniqueId());
                if(player != null) {
                    player.clear();
                    player.setDose(0);
                }
            } else {
                p.sendMessage(PREFIX + "§cYou do not have the permission to do this!"); //TODO: Localization
                event.setCancelled(true);
            }
        } else {
            if(item.getType().equals(Material.MILK_BUCKET)) {
                DrugPlayer player = DrugPlayer.getPlayer(p.getUniqueId());
                if(player != null && player.getDose() > 0) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onOpenDrugSet(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock() != null) {
            BlockState state = event.getClickedBlock().getState();
            if(DrugTool.isDrugSet(state)) {
                if(PlayerUtils.hasPermissions(p, PERM_USE_ANY, PERM_USE_DRUG_SET)) {
                    p.openWorkbench(event.getClickedBlock().getLocation(), true);
                } else {
                    p.sendMessage(PREFIX + "§cYou do not have the permission to do this!"); //TODO: Localization
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrugSetPickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if((item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD)) {
            SkullMeta meta  = (SkullMeta) item.getItemMeta();
            if(meta != null && meta.getOwningPlayer() != null) {
                UUID uuid = meta.getOwningPlayer().getUniqueId();
                DrugTool drugSet = DrugTool.getById(DRUG_SET_UUID);
                if(drugSet != null && uuid.equals(drugSet.getHeadOwner())) {
                    item.setItemMeta(drugSet.getItemStack().getItemMeta());
                }
            }
        }
    }
}