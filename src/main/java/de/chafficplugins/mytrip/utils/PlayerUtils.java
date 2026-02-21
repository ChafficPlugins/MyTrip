package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static boolean hasOnePermissions(Entity entity, String... permissions) {
        if(entity instanceof Player && MyTrip.getInstance().getConfigBoolean(ConfigStrings.SETTING_PERMISSIONS)) {
            if(entity.hasPermission(ConfigStrings.PERM_ADMIN) || entity.isOp()) {
                return true;
            }
            for (String permission : permissions) {
                if (entity.hasPermission(permission)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
