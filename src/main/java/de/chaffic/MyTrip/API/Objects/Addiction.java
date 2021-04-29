package de.chaffic.MyTrip.API.Objects;

import de.chaffic.MyTrip.API.DrugAPI;
import de.chaffic.MyTrip.Events.OtherEvents;
import io.github.chafficui.CrucialAPI.API.Interface;
import io.github.chafficui.CrucialAPI.Interfaces.CrucialItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Addiction {
    String drug;
    int intensity;
    UUID uuid;

    public Addiction(MyDrug drug, int intensity, UUID uuid) {
        this.drug = drug.getKey();
        this.intensity = intensity;
        this.uuid = uuid;
        loop();
    }

    public String getId(){
        return  drug;
    }

    public void alterIntensity(int alter){
        if(intensity+alter < 9){
            intensity += alter;
        }
    }

    public BukkitTask loop(){
        return new BukkitRunnable() {
            final int rem = intensity;
            @Override
            public void run() {
                if(rem == intensity) {
                    if (Bukkit.getPlayer(uuid) != null && DrugAPI.getPlayerData(uuid).isAddicted(drug) != null) {
                        Player player = Bukkit.getPlayer(uuid);
                        for (PotionEffect effect : getEffects()) {
                            player.addPotionEffect(effect);
                        }
                        Interface.showText(player, OtherEvents.plugin.getWord("addiction"), OtherEvents.plugin.getWord("consume") + CrucialItem.getByKey(drug).getName());
                    } else
                        this.cancel();
                } else {
                    loop();
                    this.cancel();
                }
            }
        }.runTaskTimer(OtherEvents.plugin, 16000/intensity, 16000/intensity);
    }

    public PotionEffect[] getEffects(){
        PotionEffect hunger1 = new PotionEffect(PotionEffectType.HUNGER,  100*20, 1);
        PotionEffect hunger2 = new PotionEffect(PotionEffectType.HUNGER,  100*20, 2);
        PotionEffect slow = new PotionEffect(PotionEffectType.SLOW,  100*20, 1);
        PotionEffect poison1 = new PotionEffect(PotionEffectType.POISON,  100*20, 1);
        PotionEffect poison2 = new PotionEffect(PotionEffectType.POISON,  100*20, 2);
        PotionEffect confusion1 = new PotionEffect(PotionEffectType.CONFUSION,  10*20, 1);
        PotionEffect confusion2 = new PotionEffect(PotionEffectType.CONFUSION,  30*20, 1);
        PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS,  100*20, 1);
        switch(intensity){
            case 1:
                return new PotionEffect[]{hunger1};
            case 2:
                return new PotionEffect[]{hunger1, slow};
            case 3:
                return new PotionEffect[]{hunger2, slow};
            case 4:
                return new PotionEffect[]{hunger2, slow, confusion1};
            case 5:
                return new PotionEffect[]{hunger2, slow, confusion1, poison1};
            case 6:
                return new PotionEffect[]{hunger2, slow, confusion1, poison2};
            case 7:
                return new PotionEffect[]{hunger2, slow, confusion1, poison2, blindness};
            case 8:
                return new PotionEffect[]{hunger2, slow, confusion2, poison2, blindness};
            default:
                return new PotionEffect[]{};
        }
    }

    public void consumed(){
        if((int) (Math.random()*(100-1) + 1) <= ((MyDrug)CrucialItem.getByKey(drug)).getAddict()){
            alterIntensity(1);
        }
    }
}
