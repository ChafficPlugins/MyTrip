package de.chaffic.mytrip.utils;

import de.chaffic.mytrip.MyTrip;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static de.chaffic.mytrip.utils.ConfigStrings.*;

public class PlayerUtils {
    private static final MyTrip plugin = MyTrip.getPlugin(MyTrip.class);

    public static boolean hasPermissions(Entity entity, String... permissions) {
        if(entity instanceof Player && plugin.getConfigBoolean(SETTING_PERMISSIONS)) {
            if(entity.hasPermission(PERM_ADMIN)) {
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
