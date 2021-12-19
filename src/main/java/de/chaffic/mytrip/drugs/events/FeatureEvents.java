package de.chaffic.mytrip.drugs.events;

import de.chaffic.mytrip.MyTrip;
import de.chaffic.mytrip.drugs.objects.DrugPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.chaffic.mytrip.utils.ConfigStrings.FEATURE_HEAL_ON_DEATH;

public class FeatureEvents implements Listener {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(plugin.getConfigBoolean(FEATURE_HEAL_ON_DEATH)) {
            DrugPlayer p = DrugPlayer.getPlayer(event.getEntity().getUniqueId());
            if(p != null) {
                p.clear();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DrugPlayer p = DrugPlayer.getPlayer(event.getPlayer().getUniqueId());
        if(p == null) {
            p = new DrugPlayer(event.getPlayer());
            DrugPlayer.addPlayer(p);
        }
        p.setDose(0);
        p.joined();
    }
}
