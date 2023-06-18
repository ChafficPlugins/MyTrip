package de.chafficplugins.mytrip.drugs.events;

import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.DrugTool;
import de.chafficplugins.mytrip.utils.PlayerUtils;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.Utils.player.effects.VisualEffects;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static de.chafficplugins.mytrip.utils.ConfigStrings.*;
import static de.chafficplugins.mytrip.utils.CustomMessages.getLocalized;

public class DrugToolEvents implements Listener {

    @EventHandler
    public void onTest(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        CrucialItem tool = DrugTool.getByStack(p.getInventory().getItemInMainHand());

        if (tool instanceof DrugTool) {
            event.setCancelled(true);
            Entity entity = event.getRightClicked();
            if(tool.getId().equals(DRUG_TEST_UUID)) {
                if (PlayerUtils.hasOnePermissions(entity, PERM_USE_ANY, PERM_USE_DRUG_TEST)) {
                    DrugPlayer player = DrugPlayer.getPlayer(entity.getUniqueId());
                    if (player != null && player.getDose() > 0) {
                        p.sendMessage(PREFIX + getLocalized(IS_HIGH, ((Player) entity).getDisplayName()));
                    } else {
                        p.sendMessage(PREFIX + getLocalized(IS_NOT_HIGH, ((Player) entity).getDisplayName()));
                    }
                } else {
                    p.sendMessage(PREFIX + getLocalized(NO_PERMS_TO_DO_THIS));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void OnDrugSet(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        Player p = event.getPlayer();
        CrucialItem tool = DrugTool.getByStack(p.getInventory().getItemInMainHand());

        if(tool != null && tool.getId().equals(DRUG_SET_UUID)) {
            event.setCancelled(true);
            if (PlayerUtils.hasOnePermissions(p, PERM_USE_ANY, PERM_USE_DRUG_SET)) {
                p.openWorkbench(null, true);
            } else {
                p.sendMessage(PREFIX + getLocalized(NO_PERMS_TO_DO_THIS));
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
            if (PlayerUtils.hasOnePermissions(p, PERM_USE_ANY, PERM_USE_ANTITOXIN)) {
                for (PotionEffect effect : p.getActivePotionEffects()) {
                    p.removePotionEffect(effect.getType());
                }
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
                p.sendMessage(PREFIX + getLocalized(NO_PERMS_TO_DO_THIS));
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
}