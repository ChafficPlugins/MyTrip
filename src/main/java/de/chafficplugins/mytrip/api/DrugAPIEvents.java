package de.chafficplugins.mytrip.api;

import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import org.bukkit.entity.Player;

public class DrugAPIEvents extends MyTripEvent {
    public boolean onDrugStart(Player p, MyDrug drug) {
        return false;
    }

    public void onDrugEnd(Player p, MyDrug drug) {
    }
}
