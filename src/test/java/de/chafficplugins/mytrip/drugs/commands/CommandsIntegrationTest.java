package de.chafficplugins.mytrip.drugs.commands;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static de.chafficplugins.mytrip.drugs.commands.Commands.CommandCategory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Integration tests for Commands implementations.
 * Tests state changes caused by commands (dose, addictions, potion effects).
 *
 * Note: Commands use getLocalized() from CrucialLib for messages, which throws
 * NPE in test. We verify state changes by catching the NPE after the state change
 * has already occurred. Permissions are disabled by default (settings.permissions=false).
 */
class CommandsIntegrationTest extends MockBukkitTestBase {

    private PlayerMock caller;
    private DrugPlayer callerDrugPlayer;
    private MyDrug testDrug;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        DrugPlayer.playerData.clear();
        // Ensure permissions are disabled for these tests
        plugin.getConfig().set("settings.permissions", false);

        caller = server.addPlayer();
        callerDrugPlayer = new DrugPlayer(caller);
        DrugPlayer.addPlayer(callerDrugPlayer);

        testDrug = new MyDrug("CmdTestDrug", Material.SUGAR);
        testDrug.setOverdose(5);
        testDrug.setAddictionProbability(100);
    }

    @AfterEach
    void tearDown() {
        DrugPlayer.playerData.clear();
        plugin.getConfig().set("settings.permissions", false);
    }

    // --- Recover command ---

    @Test
    void recover_self_setsDoseToZero() {
        callerDrugPlayer.setDose(0.5);
        assertTrue(callerDrugPlayer.getDose() > 0);

        safeCallCommand(caller, RECOVER);

        assertEquals(0.0, callerDrugPlayer.getDose(),
                "recover should set dose to 0");
    }

    @Test
    void recover_self_removesPotionEffects() {
        callerDrugPlayer.setDose(0.5);
        caller.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, 1));
        caller.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1000, 1));
        assertFalse(caller.getActivePotionEffects().isEmpty());

        safeCallCommand(caller, RECOVER);

        assertTrue(caller.getActivePotionEffects().isEmpty(),
                "recover should remove all potion effects");
    }

    @Test
    void recover_otherPlayer_setsDoseToZero() {
        PlayerMock other = server.addPlayer();
        DrugPlayer otherDp = new DrugPlayer(other);
        otherDp.setDose(0.75);
        DrugPlayer.addPlayer(otherDp);

        safeCallCommand(caller, RECOVER, null, other.getName());

        assertEquals(0.0, otherDp.getDose(),
                "recover other player should set their dose to 0");
    }

    @Test
    void recover_playerWithZeroDose_noStateChange() {
        assertEquals(0.0, callerDrugPlayer.getDose());
        safeCallCommand(caller, RECOVER);
        assertEquals(0.0, callerDrugPlayer.getDose());
    }

    // --- Addictions clear command ---

    @Test
    void addictionsClear_self_clearsAll() {
        callerDrugPlayer.addAddiction(testDrug);
        assertEquals(1, callerDrugPlayer.getAddictions().size());

        safeCallCommand(caller, ADDICTIONS, "clear");

        assertTrue(callerDrugPlayer.getAddictions().isEmpty(),
                "addictions clear should remove all addictions");
    }

    @Test
    void addictionsClear_otherPlayer_clearsAll() {
        PlayerMock other = server.addPlayer();
        DrugPlayer otherDp = new DrugPlayer(other);
        DrugPlayer.addPlayer(otherDp);
        otherDp.addAddiction(testDrug);
        assertEquals(1, otherDp.getAddictions().size());

        safeCallCommand(caller, ADDICTIONS, "clear", other.getName());

        assertTrue(otherDp.getAddictions().isEmpty(),
                "addictions clear should remove other player's addictions");
    }

    @Test
    void addictionsClear_noAddictions_noCrash() {
        assertTrue(callerDrugPlayer.getAddictions().isEmpty());
        safeCallCommand(caller, ADDICTIONS, "clear");
    }

    // --- Info command ---

    @Test
    void info_doesNotThrow() {
        safeCallCommand(caller, INFO);
    }

    // --- Unknown subcommand ---

    @Test
    void addictions_unknownSubcommand_noCrash() {
        safeCallCommand(caller, ADDICTIONS, "nonexistent");
    }

    // --- Helper: safely call command, catching getLocalized() failures ---

    private void safeCallCommand(PlayerMock player, Commands.CommandCategory category, String... args) {
        try {
            switch (args.length) {
                case 0 -> Commands.callCommand(player, category);
                case 1 -> Commands.callCommand(player, category, args[0]);
                case 2 -> Commands.callCommand(player, category, args[0], args[1]);
                case 3 -> Commands.callCommand(player, category, args[0], args[1], args[2]);
            }
        } catch (NullPointerException e) {
            // getLocalized() throws NPE when CrucialLib Localizer isn't initialized.
            // State changes we care about happen BEFORE the message is sent.
            if (!isLocalizationError(e)) {
                throw e;
            }
        }
    }

    private boolean isLocalizationError(NullPointerException e) {
        for (StackTraceElement frame : e.getStackTrace()) {
            if (frame.getClassName().contains("Localizer") ||
                    frame.getClassName().contains("CustomMessages") ||
                    frame.getMethodName().equals("getLocalized") ||
                    frame.getMethodName().equals("sendMessage")) {
                return true;
            }
        }
        return false;
    }
}
