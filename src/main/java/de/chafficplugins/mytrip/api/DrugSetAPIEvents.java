package de.chafficplugins.mytrip.api;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class DrugSetAPIEvents extends MyTripEvent{
    public boolean onDrugSetOpen(Player p, BlockState state) {
        return false;
    }
}
