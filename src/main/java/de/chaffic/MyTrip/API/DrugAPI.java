package de.chaffic.MyTrip.API;

import de.chaffic.MyTrip.API.Objects.DrugPlayer;
import de.chaffic.MyTrip.API.Objects.MyDrug;
import de.chaffic.MyTrip.Main;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import io.github.chafficui.CrucialAPI.Utils.player.effects.VisualEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class DrugAPI {

    private static final Main plugin = Main.getPlugin(Main.class);
    public static String prefix = ChatColor.WHITE + "[" + ChatColor.WHITE + "MyTrip" + ChatColor.WHITE + "] " + ChatColor.RESET;
    public static ArrayList<DrugPlayer> playerDatas = new ArrayList<>();

    public static DrugPlayer getPlayerData(UUID uuid){
        for (DrugPlayer playerData:playerDatas){
            if(playerData.getUUID().equals(uuid)){
                return playerData;
            }
        }
        return null;
    }

    public static void deleteDrug(MyDrug drug, Player p){
        if (CrucialItem.getByStack(drug.getItemStack()) != null) {
            drug.delete();
            p.sendMessage(prefix + drug.getName() + " was deleted.");
            return;
        }
        p.sendMessage(prefix + drug.getName() + " is no existing drug!");
    }

    public static ArrayList<String[]> getEffects(MyDrug drug){
        return drug.getEffects();
    }

    public static void deleteEffects(MyDrug drug){
        drug.setEffects(new ArrayList<>());
    }

    public static void doDrug(Player p, MyDrug myDrug, ItemStack stack){
        final int DURATION = myDrug.getDuration()*20;
        final int EFFECT_DELAY = myDrug.getEffectDelay()*20;

        //instant visuals
        int amount = stack.getAmount();
        if (amount > 1){
            stack.setAmount(amount - 1);
            p.getInventory().setItemInMainHand(stack);
        }
        if (amount == 1) {
            p.getInventory().getItemInMainHand().setAmount(0);
        }
        p.playSound(p.getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK, 10, 29);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 10, 29);
            VisualEffects.removeBlood(p);

            doEffects(p, myDrug, DURATION);

            //bloodfx
            if(myDrug.isBloody()) {
                VisualEffects.setBlood(p, 100, DURATION);
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> VisualEffects.removeBlood(p), DURATION);
            getPlayerData(p.getUniqueId()).subDose(myDrug);
        },EFFECT_DELAY);
    }

    public static int doEffects(Player p, MyDrug myDrug, int add){
        int duration = 0;

        //effects
        for (String[] effect : getEffects(myDrug)){
            try {
                PotionEffectType type = PotionEffectType.getByName(effect[0]);
                int strength = Integer.parseInt(effect[1]);

                if(p.hasPotionEffect(type)) {
                    duration = p.getPotionEffect(type).getDuration();
                } else {
                    duration = 0;
                }
                p.removePotionEffect(type);
                p.addPotionEffect(new PotionEffect(type,duration + add, strength-1));
                if(p.hasPotionEffect(type)) {
                    duration = p.getPotionEffect(type).getDuration();
                } else {
                    duration = 1;
                }
            }catch(Exception ex) {
                plugin.getLogger().severe("Error 012: Tryied to run drug " + myDrug.getName() +
                        " but failed. Is PotionEffect " + effect[0] + " legal?");
            }
        }
        return duration + add;
    }
}
