package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MyTrip;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerUtils {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    public static boolean hasOnePermissions(Entity entity, String... permissions) {
        if(entity instanceof Player && plugin.getConfigBoolean(ConfigStrings.SETTING_PERMISSIONS)) {
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
