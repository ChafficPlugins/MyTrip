package de.chafficplugins.mytrip.api;

import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public abstract class DrugToolAPIEvents extends MyTripEvent {
    public boolean onDrugTest(Player tester, DrugPlayer tested, boolean isPositive) {
        return false;
    }

    public boolean onAntiToxin(Player player, Collection<PotionEffect> activePotionEffects) {
        return false;
    }

    public boolean onDrugCraftPrepare(LivingEntity entity, BlockState drugSetBlock, CrucialItem result, CraftingInventory drugSetInventory) {
        return false;
    }
}
