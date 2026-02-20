package de.chafficplugins.mytrip.utils;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for the permission checking system in PlayerUtils.
 * The permission model has a critical default: settings.permissions=false means
 * everyone can use everything (OP-only for commands). Getting this wrong would
 * either lock everyone out or give everyone access.
 */
class PlayerUtilsTest extends MockBukkitTestBase {

    private PlayerMock normalPlayer;
    private PlayerMock opPlayer;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        normalPlayer = server.addPlayer();
        normalPlayer.setOp(false);

        opPlayer = server.addPlayer();
        opPlayer.setOp(true);
    }

    // --- Permissions disabled (default: settings.permissions=false) ---

    @Test
    void permissionsDisabled_normalPlayer_alwaysAllowed() {
        // When settings.permissions=false (default), everyone has access
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, false);
        assertTrue(PlayerUtils.hasOnePermissions(normalPlayer, "mytrip.recover"),
                "With permissions disabled, normal players should have access");
    }

    @Test
    void permissionsDisabled_opPlayer_alwaysAllowed() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, false);
        assertTrue(PlayerUtils.hasOnePermissions(opPlayer, "mytrip.recover"));
    }

    // --- Permissions enabled ---

    @Test
    void permissionsEnabled_opPlayer_alwaysAllowed() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        assertTrue(PlayerUtils.hasOnePermissions(opPlayer, "mytrip.recover"),
                "OP players should always have access regardless of specific permissions");
    }

    @Test
    void permissionsEnabled_normalPlayer_deniedWithoutPermission() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        assertFalse(PlayerUtils.hasOnePermissions(normalPlayer, "mytrip.recover"),
                "Normal players without the permission should be denied");
    }

    @Test
    void permissionsEnabled_normalPlayerWithPermission_allowed() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        normalPlayer.addAttachment(plugin, "mytrip.recover", true);
        assertTrue(PlayerUtils.hasOnePermissions(normalPlayer, "mytrip.recover"),
                "Normal players with the specific permission should be allowed");
    }

    @Test
    void permissionsEnabled_adminWildcard_grantsAll() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        normalPlayer.addAttachment(plugin, ConfigStrings.PERM_ADMIN, true);
        assertTrue(PlayerUtils.hasOnePermissions(normalPlayer, "mytrip.recover"),
                "Players with mytrip.* should have access to everything");
    }

    // --- Multiple permission check (any one passes) ---

    @Test
    void permissionsEnabled_oneOfMultiplePermissions_allowed() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        normalPlayer.addAttachment(plugin, "mytrip.recover.self", true);
        assertTrue(PlayerUtils.hasOnePermissions(normalPlayer,
                        "mytrip.recover", "mytrip.recover.self"),
                "Having any one of the checked permissions should grant access");
    }

    @Test
    void permissionsEnabled_noneOfMultiplePermissions_denied() {
        plugin.getConfig().set(ConfigStrings.SETTING_PERMISSIONS, true);
        assertFalse(PlayerUtils.hasOnePermissions(normalPlayer,
                        "mytrip.recover", "mytrip.recover.self"),
                "Having none of the checked permissions should deny access");
    }
}
