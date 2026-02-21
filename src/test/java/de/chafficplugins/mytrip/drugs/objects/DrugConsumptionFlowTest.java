package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Integration tests for the full drug consumption lifecycle.
 * Tests dose tracking, overdose detection, and dose recovery
 * through the DrugPlayer.consume() / subDose() flow.
 *
 * Note: Tests that involve Addiction.consumed() (existing addiction + re-consume)
 * are excluded because consumed() calls CrucialItem.getById() which requires
 * CrucialLib to have registered the items.
 */
class DrugConsumptionFlowTest extends MockBukkitTestBase {

    private PlayerMock player;
    private DrugPlayer drugPlayer;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        player = server.addPlayer();
        drugPlayer = new DrugPlayer(player);
        DrugPlayer.playerData.clear();
        DrugPlayer.addPlayer(drugPlayer);
    }

    @AfterEach
    void tearDown() {
        DrugPlayer.playerData.clear();
    }

    // --- Single drug consumption to overdose ---

    @Test
    void singleDrug_consumeToOverdose_overdose3() {
        MyDrug drug = createDrug("TestDrug", Material.SUGAR, 3, 0);

        assertFalse(consumeNonAddictive(drug), "1st dose: should not overdose");
        assertFalse(consumeNonAddictive(drug), "2nd dose: should not overdose");
        assertTrue(consumeNonAddictive(drug), "3rd dose: should overdose (dose >= 1.0)");
    }

    @Test
    void singleDrug_consumeToOverdose_overdose5() {
        MyDrug drug = createDrug("Drug5", Material.SUGAR, 5, 0);

        for (int i = 1; i <= 4; i++) {
            assertFalse(consumeNonAddictive(drug), "Dose " + i + " should not overdose");
        }
        assertTrue(consumeNonAddictive(drug), "5th dose should trigger overdose");
    }

    @Test
    void singleDrug_overdose1_immediateOverdose() {
        MyDrug drug = createDrug("Instant", Material.GLOWSTONE_DUST, 1, 0);
        assertTrue(consumeNonAddictive(drug), "overdose=1 should overdose on first consume");
    }

    // --- Dose tracking precision ---

    @Test
    void doseValue_tracksCorrectly_overdose3() {
        MyDrug drug = createDrug("Precise", Material.SUGAR, 3, 0);

        consumeNonAddictive(drug);
        assertEquals(1.0 / 3.0, drugPlayer.getDose(), 1e-10, "After 1 consume");

        consumeNonAddictive(drug);
        assertEquals(2.0 / 3.0, drugPlayer.getDose(), 1e-10, "After 2 consumes");

        consumeNonAddictive(drug);
        assertEquals(3.0 / 3.0, drugPlayer.getDose(), 1e-10, "After 3 consumes");
    }

    @Test
    void doseValue_tracksCorrectly_overdose7() {
        MyDrug drug = createDrug("SevenDrug", Material.SUGAR, 7, 0);

        for (int i = 1; i <= 7; i++) {
            consumeNonAddictive(drug);
            assertEquals((double) i / 7.0, drugPlayer.getDose(), 1e-9,
                    "After " + i + " consumes of overdose=7 drug");
        }
        assertTrue(drugPlayer.getDose() >= 1.0 - 1e-9, "Dose should be at overdose threshold");
    }

    // --- Two different drugs accumulate dose ---

    @Test
    void twoDrugs_doseAccumulates() {
        MyDrug drugA = createDrug("DrugA", Material.SUGAR, 4, 0);
        MyDrug drugB = createDrug("DrugB", Material.BLAZE_POWDER, 4, 0);

        assertFalse(consumeNonAddictive(drugA)); // dose = 0.25
        assertFalse(consumeNonAddictive(drugB)); // dose = 0.50
        assertFalse(consumeNonAddictive(drugA)); // dose = 0.75
        assertEquals(0.75, drugPlayer.getDose(), 1e-10);
        // 4th dose would reach 1.0
        assertTrue(consumeNonAddictive(drugA), "4th total dose should trigger overdose");
    }

    @Test
    void twoDrugs_crossOverdose() {
        MyDrug drugA = createDrug("CrossA", Material.SUGAR, 2, 0);
        MyDrug drugB = createDrug("CrossB", Material.BLAZE_POWDER, 2, 0);

        consumeNonAddictive(drugA); // dose = 0.5
        assertTrue(consumeNonAddictive(drugB), "Cross-drug overdose: dose should reach 1.0");
    }

    // --- Consume then subDose cycle ---

    @Test
    void consumeThenSubDose_doseReturnsToZero() {
        MyDrug drug = createDrug("Cycle", Material.SUGAR, 3, 0);

        consumeNonAddictive(drug);
        consumeNonAddictive(drug);
        assertEquals(2.0 / 3.0, drugPlayer.getDose(), 1e-10);

        drugPlayer.subDose(drug);
        assertEquals(1.0 / 3.0, drugPlayer.getDose(), 1e-10, "After one subDose");

        drugPlayer.subDose(drug);
        assertEquals(0.0, drugPlayer.getDose(), 1e-10, "After two subDoses, back to zero");
    }

    @Test
    void overdoseThenSubDoseThenConsumeAgain() {
        MyDrug drug = createDrug("Again", Material.SUGAR, 2, 0);

        // Overdose
        consumeNonAddictive(drug);
        assertTrue(consumeNonAddictive(drug), "Should overdose");
        assertEquals(1.0, drugPlayer.getDose(), 1e-10);

        // Recovery
        drugPlayer.subDose(drug);
        drugPlayer.subDose(drug);
        assertEquals(0.0, drugPlayer.getDose(), 1e-10, "Back to zero after subDose");

        // Consume again
        assertFalse(consumeNonAddictive(drug), "First consume after recovery: no overdose");
        assertTrue(consumeNonAddictive(drug), "Second consume after recovery: overdose again");
    }

    @Test
    void manualDoseReset_clearsCompletely() {
        MyDrug drug = createDrug("Reset", Material.SUGAR, 3, 0);

        consumeNonAddictive(drug);
        consumeNonAddictive(drug);
        assertTrue(drugPlayer.getDose() > 0);

        drugPlayer.setDose(0);
        assertEquals(0.0, drugPlayer.getDose());

        // Can consume from scratch
        assertFalse(consumeNonAddictive(drug));
    }

    // --- Addiction creation via consume (probability only, no existing addiction) ---

    @Test
    void consume_addictionProbability100_alwaysCreatesAddiction() {
        MyDrug drug = createDrug("Addictive", Material.SUGAR, 5, 100);

        assertTrue(drugPlayer.getAddictions().isEmpty(), "No addictions before consuming");
        drugPlayer.consume(drug);
        assertEquals(1, drugPlayer.getAddictions().size(), "Addiction should be created with probability=100");
        assertEquals(drug.getId(), drugPlayer.getAddictions().get(0).getDrugId());
    }

    @Test
    void consume_addictionProbability0_neverCreatesAddiction() {
        MyDrug drug = createDrug("Safe", Material.SUGAR, 5, 0);

        for (int i = 0; i < 20; i++) {
            consumeNonAddictive(drug);
        }
        assertTrue(drugPlayer.getAddictions().isEmpty(),
                "No addiction should be created with probability=0 (cleared after each consume)");
    }

    @Test
    void consume_twoDrugs_separateAddictions() {
        MyDrug drugA = createDrug("AddictA", Material.SUGAR, 5, 100);
        MyDrug drugB = createDrug("AddictB", Material.BLAZE_POWDER, 5, 100);

        drugPlayer.consume(drugA);
        drugPlayer.consume(drugB);
        assertEquals(2, drugPlayer.getAddictions().size(),
                "Each drug should create its own addiction");
    }

    // --- Floating-point edge cases ---

    @Test
    void overdose7_floatingPoint_triggersCorrectly() {
        MyDrug drug = createDrug("Float7", Material.SUGAR, 7, 0);

        for (int i = 1; i < 7; i++) {
            assertFalse(consumeNonAddictive(drug), "Dose " + i + "/7 should not overdose");
        }
        assertTrue(consumeNonAddictive(drug),
                "7th dose of overdose=7 should trigger overdose despite floating-point");
    }

    @Test
    void overdose10_floatingPoint_triggersCorrectly() {
        MyDrug drug = createDrug("Float10", Material.SUGAR, 10, 0);

        for (int i = 1; i < 10; i++) {
            assertFalse(consumeNonAddictive(drug), "Dose " + i + "/10 should not overdose");
        }
        assertTrue(consumeNonAddictive(drug),
                "10th dose of overdose=10 should trigger overdose");
    }

    // --- Helpers ---

    private MyDrug createDrug(String name, Material material, int overdose, int addictionProbability) {
        MyDrug drug = new MyDrug(name, material);
        drug.setOverdose(overdose);
        if (addictionProbability >= 0) {
            drug.setAddictionProbability(addictionProbability);
        }
        return drug;
    }

    /**
     * Consume a drug for dose-tracking tests only.
     * Clears any accidentally created addictions after each consume so that
     * subsequent consumes don't hit the Addiction.consumed() path
     * (which requires CrucialItem.getById() that's unavailable in tests).
     */
    private boolean consumeNonAddictive(MyDrug drug) {
        boolean result = drugPlayer.consume(drug);
        drugPlayer.getAddictions().clear();
        return result;
    }
}
