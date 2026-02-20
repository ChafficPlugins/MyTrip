package de.chafficplugins.mytrip.drugs.objects;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for MyDrug setter validation and boundary conditions.
 * Requires MockBukkit because MyDrug has a static MyTrip.getPlugin() initializer.
 */
class MyDrugTest extends MockBukkitTestBase {

    private MyDrug drug;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        drug = new MyDrug("TestDrug", Material.SUGAR);
    }

    // --- setOverdose boundary tests ---

    @Test
    void setOverdose_zero_isAccepted() {
        // BUG: overdose=0 is accepted but causes division by zero in DrugPlayer.consume()
        drug.setOverdose(0);
        assertEquals(0, drug.getOverdose(),
                "BUG: overdose=0 passes validation but causes division by zero in consume()");
    }

    @Test
    void setOverdose_one_isAccepted() {
        drug.setOverdose(1);
        assertEquals(1, drug.getOverdose());
    }

    @Test
    void setOverdose_99_isAccepted() {
        drug.setOverdose(99);
        assertEquals(99, drug.getOverdose());
    }

    @Test
    void setOverdose_100_isRejected() {
        drug.setOverdose(5); // set a known value first
        drug.setOverdose(100); // should be rejected
        assertEquals(5, drug.getOverdose(), "overdose=100 should be rejected (> 99)");
    }

    @Test
    void setOverdose_negative_isRejected() {
        drug.setOverdose(5);
        drug.setOverdose(-1);
        assertEquals(5, drug.getOverdose(), "overdose=-1 should be rejected (< 0)");
    }

    // --- setAddictionProbability boundary tests ---

    @Test
    void setAddictionProbability_zero_isAccepted() {
        drug.setAddictionProbability(0);
        assertEquals(0, drug.getAddictionProbability());
    }

    @Test
    void setAddictionProbability_100_isAccepted() {
        drug.setAddictionProbability(100);
        assertEquals(100, drug.getAddictionProbability());
    }

    @Test
    void setAddictionProbability_101_isRejected() {
        drug.setAddictionProbability(50);
        drug.setAddictionProbability(101);
        assertEquals(50, drug.getAddictionProbability(), "probability=101 should be rejected (> 100)");
    }

    @Test
    void setAddictionProbability_negative_isRejected() {
        drug.setAddictionProbability(50);
        drug.setAddictionProbability(-1);
        assertEquals(50, drug.getAddictionProbability(), "probability=-1 should be rejected (< 0)");
    }

    @Test
    void setOverdoseAndAddiction_asymmetricBounds() {
        // overdose allows 0-99, addiction allows 0-100 — this asymmetry is worth documenting
        drug.setOverdose(99);
        drug.setAddictionProbability(100);
        assertEquals(99, drug.getOverdose());
        assertEquals(100, drug.getAddictionProbability());

        // 100 is out of range for overdose but valid for addiction
        drug.setOverdose(100);
        assertEquals(99, drug.getOverdose(), "overdose max is 99");
        drug.setAddictionProbability(100);
        assertEquals(100, drug.getAddictionProbability(), "addiction max is 100");
    }

    // --- setDuration boundary tests ---

    @Test
    void setDuration_positive_isAccepted() {
        drug.setDuration(60);
        assertEquals(60, drug.getDuration());
    }

    @Test
    void setDuration_zero_isAccepted() {
        drug.setDuration(0);
        assertEquals(0, drug.getDuration());
    }

    @Test
    void setDuration_negative_isRejected() {
        drug.setDuration(30);
        drug.setDuration(-1);
        assertEquals(30, drug.getDuration(), "Negative duration should be rejected");
    }

    // --- setEffectDelay boundary tests ---

    @Test
    void setEffectDelay_positive_isAccepted() {
        drug.setEffectDelay(5);
        assertEquals(5, drug.getEffectDelay());
    }

    @Test
    void setEffectDelay_zero_isAccepted() {
        drug.setEffectDelay(0);
        assertEquals(0, drug.getEffectDelay());
    }

    @Test
    void setEffectDelay_negative_isRejected() {
        drug.setEffectDelay(10);
        drug.setEffectDelay(-1);
        assertEquals(10, drug.getEffectDelay(), "Negative effect delay should be rejected");
    }

    // --- setMaterial tests ---

    @Test
    void setMaterial_null_setsAir() {
        // Cast to ItemStack to disambiguate from CrucialItem.setMaterial(String)
        assertDoesNotThrow(() -> drug.setMaterial((org.bukkit.inventory.ItemStack) null));
    }

    // --- getByName tests ---

    @Test
    void getByName_nonExistent_returnsNull() {
        assertNull(MyDrug.getByName("NonExistentDrug_12345"));
    }

    // --- Effect management ---

    @Test
    void addEffect_addsToList() {
        drug.addEffect(new String[]{"SPEED", "1"});
        assertEquals(1, drug.getEffects().size());
        assertEquals("SPEED", drug.getEffects().get(0)[0]);
        assertEquals("1", drug.getEffects().get(0)[1]);
    }

    @Test
    void addEffect_multipleEffects() {
        drug.addEffect(new String[]{"SPEED", "1"});
        drug.addEffect(new String[]{"JUMP", "2"});
        assertEquals(2, drug.getEffects().size());
    }

    // --- Command management ---

    @Test
    void addCommand_addsToList() {
        drug.addCommand("say hello");
        assertEquals(1, drug.getCommands().size());
        assertEquals("say hello", drug.getCommands().get(0));
    }
}
