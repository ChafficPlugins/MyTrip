package de.chafficplugins.mytrip.api;

import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public abstract class DrugToolAPIEvents extends MyTripEvent {
    public boolean onDrugTest(Player tester, DrugPlayer tested, boolean isPositive) {
        return true;
    }

    public boolean onAntiToxin(Player p, Collection<PotionEffect> activePotionEffects) {
        return true;
    }
}
