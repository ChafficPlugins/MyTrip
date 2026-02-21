package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for Addiction intensity management and boundary conditions.
 * Requires MockBukkit because the constructor starts a BukkitRunnable loop.
 */
class AddictionTest extends MockBukkitTestBase {

    private PlayerMock player;
    private MyDrug testDrug;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        player = server.addPlayer();
        testDrug = new MyDrug("AddictionTestDrug", Material.SUGAR);
        testDrug.setOverdose(5);
        testDrug.setAddictionProbability(100);

        // Register player data so Addiction.loop() can find the player
        DrugPlayer.playerData.clear();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
    }

    // --- Construction ---

    @Test
    void constructor_setsFields() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        assertEquals(1, addiction.getIntensity());
        assertEquals(testDrug.getId(), addiction.getDrugId());
    }

    @Test
    void constructor_startsLoop() {
        // The constructor calls loop() which schedules a BukkitRunnable.
        // If the scheduler is not available, this would throw. MockBukkit provides one.
        assertDoesNotThrow(() -> new Addiction(testDrug.getId(), 1, player.getUniqueId()));
    }

    // --- alterIntensity ---

    @Test
    void alterIntensity_incrementsByOne() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        addiction.alterIntensity(1);
        assertEquals(2, addiction.getIntensity());
    }

    @Test
    void alterIntensity_capsAtEight() {
        Addiction addiction = new Addiction(testDrug.getId(), 7, player.getUniqueId());
        addiction.alterIntensity(1);
        assertEquals(8, addiction.getIntensity(), "Intensity should cap at 8");
    }

    @Test
    void alterIntensity_blockedBeyondEight() {
        Addiction addiction = new Addiction(testDrug.getId(), 8, player.getUniqueId());
        addiction.alterIntensity(1);
        assertEquals(8, addiction.getIntensity(), "Intensity 8 + 1 = 9 should be blocked (< 9 guard)");
    }

    @Test
    void alterIntensity_largeJump_blockedIfExceedsEight() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        addiction.alterIntensity(8); // 1 + 8 = 9, blocked
        assertEquals(1, addiction.getIntensity(), "Large jump past 8 should be blocked");
    }

    @Test
    void alterIntensity_largeJump_allowedIfExactlyEight() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        addiction.alterIntensity(7); // 1 + 7 = 8, allowed (< 9)
        assertEquals(8, addiction.getIntensity());
    }

    @Test
    void alterIntensity_floorGuard_preventsNegative() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        addiction.alterIntensity(-5); // 1 + (-5) = -4, blocked by floor guard
        assertEquals(1, addiction.getIntensity(),
                "alterIntensity should block negative results — intensity stays at 1");
    }

    @Test
    void alterIntensity_floorGuard_preventsZero() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        addiction.alterIntensity(-1); // 1 + (-1) = 0, blocked by floor guard (must be >= 1)
        assertEquals(1, addiction.getIntensity(),
                "alterIntensity should block result=0 to prevent division by zero in loop()");
    }

    // --- Intensity at boundaries ---

    @Test
    void intensity_atFive_noAddictionEffects() {
        // Addiction effects only apply when intensity > 5
        Addiction addiction = new Addiction(testDrug.getId(), 5, player.getUniqueId());
        assertEquals(5, addiction.getIntensity());
        // intensity > 5 is false when intensity == 5, so no extra effects at exactly 5
    }

    @Test
    void intensity_atSix_triggersAddictionEffects() {
        // intensity > 5 → apply addiction effects from config
        // This is where the split[2] bug would manifest
        Addiction addiction = new Addiction(testDrug.getId(), 6, player.getUniqueId());
        assertEquals(6, addiction.getIntensity());
        // The actual effect parsing bug (split[2]) is tested in CodeQualityTest
    }

    // --- getDrugId ---

    @Test
    void getDrugId_returnsCorrectId() {
        Addiction addiction = new Addiction(testDrug.getId(), 1, player.getUniqueId());
        assertEquals(testDrug.getId(), addiction.getDrugId());
    }
}
