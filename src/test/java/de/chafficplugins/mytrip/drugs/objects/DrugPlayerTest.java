package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for DrugPlayer dose tracking, overdose detection, and addiction management.
 * Requires MockBukkit for Player mocking and BukkitRunnable scheduling.
 */
class DrugPlayerTest extends MockBukkitTestBase {

    private DrugPlayer drugPlayer;
    private PlayerMock player;
    private MyDrug testDrug;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        player = server.addPlayer();
        drugPlayer = new DrugPlayer(player);
        DrugPlayer.playerData.clear();

        testDrug = new MyDrug("TestDrug", Material.SUGAR);
        testDrug.setOverdose(3);
        testDrug.setAddictionProbability(0); // Minimize random addiction creation
    }

    // --- UUID tracking ---

    @Test
    void constructor_storesPlayerUuid() {
        assertEquals(player.getUniqueId(), drugPlayer.getUuid());
    }

    @Test
    void constructor_startsWithZeroDose() {
        assertEquals(0.0, drugPlayer.getDose());
    }

    @Test
    void constructor_startsWithNoAddictions() {
        assertTrue(drugPlayer.getAddictions().isEmpty());
    }

    // --- Dose management ---

    @Test
    void setDose_updatesValue() {
        drugPlayer.setDose(0.5);
        assertEquals(0.5, drugPlayer.getDose());
    }

    @Test
    void setDose_zero_resetsDose() {
        drugPlayer.setDose(0.75);
        drugPlayer.setDose(0);
        assertEquals(0.0, drugPlayer.getDose());
    }

    // --- consume() overdose detection ---

    @Test
    void consume_firstDose_noOverdose() {
        boolean overdosed = drugPlayer.consume(testDrug);
        assertFalse(overdosed, "First consume with overdose=3 should not trigger overdose");
        assertEquals(1.0 / 3.0, drugPlayer.getDose(), 1e-10);
    }

    @Test
    void consume_secondDose_noOverdose() {
        drugPlayer.consume(testDrug);
        boolean overdosed = drugPlayer.consume(testDrug);
        assertFalse(overdosed, "Second consume with overdose=3 should not trigger overdose");
    }

    @Test
    void consume_thirdDose_triggersOverdose() {
        drugPlayer.consume(testDrug);
        drugPlayer.consume(testDrug);
        boolean overdosed = drugPlayer.consume(testDrug);
        assertTrue(overdosed, "Third consume with overdose=3 should trigger overdose (dose >= 1.0)");
    }

    @Test
    void consume_overdoseOne_immediateOverdose() {
        MyDrug instantDrug = new MyDrug("Instant", Material.GLOWSTONE_DUST);
        instantDrug.setOverdose(1);
        instantDrug.setAddictionProbability(0);

        boolean overdosed = drugPlayer.consume(instantDrug);
        assertTrue(overdosed, "overdose=1 should trigger on first consume");
    }

    @Test
    void consume_overdoseZero_divisionByZero() {
        // BUG: overdose=0 causes dose to become Infinity
        MyDrug zeroDrug = new MyDrug("ZeroDose", Material.REDSTONE);
        zeroDrug.setOverdose(0);
        zeroDrug.setAddictionProbability(0);

        boolean overdosed = drugPlayer.consume(zeroDrug);
        assertTrue(overdosed, "BUG: overdose=0 produces Infinity, which >= 1 → immediate overdose");
        assertTrue(Double.isInfinite(drugPlayer.getDose()),
                "BUG: dose is Infinity after consuming with overdose=0");
    }

    // --- subDose ---

    @Test
    void subDose_reducesDose() {
        drugPlayer.consume(testDrug);
        double doseAfterConsume = drugPlayer.getDose();
        drugPlayer.subDose(testDrug);
        assertEquals(doseAfterConsume - (1.0 / 3.0), drugPlayer.getDose(), 1e-10);
    }

    @Test
    void subDose_afterOverdoseZero_producesNaN() {
        // BUG: Infinity - Infinity = NaN
        MyDrug zeroDrug = new MyDrug("ZeroDose2", Material.REDSTONE);
        zeroDrug.setOverdose(0);
        zeroDrug.setAddictionProbability(0);

        drugPlayer.consume(zeroDrug);
        drugPlayer.subDose(zeroDrug);
        assertTrue(Double.isNaN(drugPlayer.getDose()),
                "BUG: subDose after overdose=0 produces NaN (Infinity - Infinity)");
    }

    // --- Static player data management ---

    @Test
    void addPlayer_addsToList() {
        DrugPlayer.addPlayer(drugPlayer);
        assertEquals(1, DrugPlayer.playerData.size());
    }

    @Test
    void getPlayer_findsRegistered() {
        DrugPlayer.addPlayer(drugPlayer);
        DrugPlayer found = DrugPlayer.getPlayer(player.getUniqueId());
        assertNotNull(found);
        assertEquals(player.getUniqueId(), found.getUuid());
    }

    @Test
    void getPlayer_returnsNullForUnknown() {
        assertNull(DrugPlayer.getPlayer(UUID.randomUUID()));
    }

    @Test
    void addPlayer_duplicateUuid_addsBoth() {
        // BUG: No deduplication — calling addPlayer twice for the same player adds duplicates
        DrugPlayer.addPlayer(drugPlayer);
        DrugPlayer.addPlayer(drugPlayer);
        assertEquals(2, DrugPlayer.playerData.size(),
                "addPlayer has no deduplication — same player added twice");
    }

    @Test
    void getPlayer_withDuplicates_returnsFirst() {
        DrugPlayer dp1 = new DrugPlayer(player);
        DrugPlayer dp2 = new DrugPlayer(player);
        dp1.setDose(0.5);
        dp2.setDose(0.9);
        DrugPlayer.addPlayer(dp1);
        DrugPlayer.addPlayer(dp2);

        DrugPlayer found = DrugPlayer.getPlayer(player.getUniqueId());
        assertEquals(0.5, found.getDose(),
                "getPlayer returns the first match when duplicates exist");
    }

    // --- Addiction management ---

    @Test
    void getAddicted_noAddictions_returnsNull() {
        assertNull(drugPlayer.getAddicted(UUID.randomUUID()));
    }

    @Test
    void remove_nonExistentDrug_doesNotThrow() {
        MyDrug otherDrug = new MyDrug("Other", Material.DIAMOND);
        assertDoesNotThrow(() -> drugPlayer.remove(otherDrug));
    }

    @Test
    void clear_removesAllAddictions() {
        drugPlayer.addAddiction(testDrug);
        assertFalse(drugPlayer.getAddictions().isEmpty());
        drugPlayer.clear();
        assertTrue(drugPlayer.getAddictions().isEmpty());
    }

    @Test
    void addAddiction_preventsDoubleAdd() {
        drugPlayer.addAddiction(testDrug);
        int sizeAfterFirst = drugPlayer.getAddictions().size();
        drugPlayer.addAddiction(testDrug); // same drug again
        assertEquals(sizeAfterFirst, drugPlayer.getAddictions().size(),
                "addAddiction checks for existing addiction and doesn't add duplicates");
    }

    @Test
    void addAddiction_differentDrugs_addsBoth() {
        MyDrug drug2 = new MyDrug("Drug2", Material.BLAZE_POWDER);
        drug2.setOverdose(5);
        drug2.setAddictionProbability(0);

        drugPlayer.addAddiction(testDrug);
        drugPlayer.addAddiction(drug2);
        assertEquals(2, drugPlayer.getAddictions().size());
    }

    @Test
    void remove_specificDrug_keepsOthers() {
        MyDrug drug2 = new MyDrug("Drug3", Material.BLAZE_POWDER);
        drug2.setOverdose(5);
        drug2.setAddictionProbability(0);

        drugPlayer.addAddiction(testDrug);
        drugPlayer.addAddiction(drug2);
        assertEquals(2, drugPlayer.getAddictions().size());

        drugPlayer.remove(testDrug);
        assertEquals(1, drugPlayer.getAddictions().size());
        assertNull(drugPlayer.getAddicted(testDrug.getId()));
        assertNotNull(drugPlayer.getAddicted(drug2.getId()));
    }
}
