package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.utils.ConfigStrings;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialHead;
import io.github.chafficui.CrucialAPI.Utils.customItems.CrucialItem;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;

public class DrugSet extends CrucialHead {
    public DrugSet(String type) {
        super(type);
    }

    public static boolean isDrugSet(BlockState state) {
        if (state instanceof Skull) {
            OfflinePlayer p = ((Skull) state).getOwningPlayer();
            if(p != null) {
                CrucialItem drugSet = getById(ConfigStrings.DRUG_SET_UUID);
                if(drugSet instanceof DrugSet)
                    return ((DrugSet) drugSet).headOwner.equals(p.getUniqueId());
            }
        }
        return false;
    }
}
