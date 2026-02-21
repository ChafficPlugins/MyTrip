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
 * Integration tests for the full addiction lifecycle.
 * Tests addiction creation, intensity management, deduplication,
 * removal, clearing, and loop scheduling through DrugPlayer.
 *
 * Note: Tests that call Addiction.consumed() or consume() on a player
 * with an existing addiction are excluded because consumed() calls
 * CrucialItem.getById() which requires CrucialLib item registration.
 */
class AddictionLifecycleTest extends MockBukkitTestBase {

    private PlayerMock player;
    private DrugPlayer drugPlayer;
    private MyDrug drugA;
    private MyDrug drugB;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        player = server.addPlayer();
        drugPlayer = new DrugPlayer(player);
        DrugPlayer.playerData.clear();
        DrugPlayer.addPlayer(drugPlayer);

        drugA = new MyDrug("LifecycleDrugA", Material.SUGAR);
        drugA.setOverdose(5);
        drugA.setAddictionProbability(100);

        drugB = new MyDrug("LifecycleDrugB", Material.BLAZE_POWDER);
        drugB.setOverdose(5);
        drugB.setAddictionProbability(100);
    }

    @AfterEach
    void tearDown() {
        DrugPlayer.playerData.clear();
    }

    // --- addAddiction basic lifecycle ---

    @Test
    void addAddiction_createsAddictionWithIntensity1() {
        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size());
        assertEquals(1, drugPlayer.getAddictions().get(0).getIntensity());
        assertEquals(drugA.getId(), drugPlayer.getAddictions().get(0).getDrugId());
    }

    @Test
    void addAddiction_sameDrugTwice_deduplicates() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size(),
                "Same drug should not create duplicate addiction");
    }

    @Test
    void addAddiction_twoDifferentDrugs_addsBoth() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);
        assertEquals(2, drugPlayer.getAddictions().size());
    }

    @Test
    void getAddicted_returnsCorrectAddiction() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);

        Addiction addictionA = drugPlayer.getAddicted(drugA.getId());
        Addiction addictionB = drugPlayer.getAddicted(drugB.getId());

        assertNotNull(addictionA);
        assertNotNull(addictionB);
        assertEquals(drugA.getId(), addictionA.getDrugId());
        assertEquals(drugB.getId(), addictionB.getDrugId());
    }

    @Test
    void getAddicted_nonExistent_returnsNull() {
        drugPlayer.addAddiction(drugA);
        MyDrug drugC = new MyDrug("NonExistent", Material.DIAMOND);
        drugC.setOverdose(5);
        assertNull(drugPlayer.getAddicted(drugC.getId()));
    }

    // --- Removal ---

    @Test
    void remove_specificDrug_keepsOthers() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);
        assertEquals(2, drugPlayer.getAddictions().size());

        drugPlayer.remove(drugA);
        assertEquals(1, drugPlayer.getAddictions().size());
        assertNull(drugPlayer.getAddicted(drugA.getId()), "DrugA should be removed");
        assertNotNull(drugPlayer.getAddicted(drugB.getId()), "DrugB should remain");
    }

    @Test
    void remove_nonExistent_doesNotThrow() {
        drugPlayer.addAddiction(drugA);
        MyDrug drugC = new MyDrug("NonExistent2", Material.DIAMOND);
        drugC.setOverdose(5);
        assertDoesNotThrow(() -> drugPlayer.remove(drugC));
        assertEquals(1, drugPlayer.getAddictions().size());
    }

    @Test
    void clear_removesAllAddictions() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);
        assertEquals(2, drugPlayer.getAddictions().size());

        drugPlayer.clear();
        assertTrue(drugPlayer.getAddictions().isEmpty());
    }

    @Test
    void clear_thenAddAgain_works() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.clear();
        assertTrue(drugPlayer.getAddictions().isEmpty());

        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size(),
                "Should be able to add addiction again after clear");
    }

    // --- alterIntensity direct tests ---

    @Test
    void alterIntensity_increment_works() {
        drugPlayer.addAddiction(drugA);
        Addiction addiction = drugPlayer.getAddictions().get(0);
        assertEquals(1, addiction.getIntensity());

        addiction.alterIntensity(1);
        assertEquals(2, addiction.getIntensity());

        addiction.alterIntensity(3);
        assertEquals(5, addiction.getIntensity());
    }

    @Test
    void alterIntensity_ceilingAt8() {
        drugPlayer.addAddiction(drugA);
        Addiction addiction = drugPlayer.getAddictions().get(0);

        addiction.alterIntensity(7); // 1 + 7 = 8
        assertEquals(8, addiction.getIntensity());

        addiction.alterIntensity(1); // 8 + 1 = 9, blocked
        assertEquals(8, addiction.getIntensity());
    }

    @Test
    void alterIntensity_floorAt1() {
        drugPlayer.addAddiction(drugA);
        Addiction addiction = drugPlayer.getAddictions().get(0);
        assertEquals(1, addiction.getIntensity());

        addiction.alterIntensity(-1); // 1 - 1 = 0, blocked
        assertEquals(1, addiction.getIntensity());

        addiction.alterIntensity(-10); // 1 - 10 = -9, blocked
        assertEquals(1, addiction.getIntensity());
    }

    @Test
    void alterIntensity_decrementFromHigher() {
        drugPlayer.addAddiction(drugA);
        Addiction addiction = drugPlayer.getAddictions().get(0);

        addiction.alterIntensity(4); // 1 → 5
        assertEquals(5, addiction.getIntensity());

        addiction.alterIntensity(-2); // 5 → 3
        assertEquals(3, addiction.getIntensity());

        addiction.alterIntensity(-1); // 3 → 2
        assertEquals(2, addiction.getIntensity());

        addiction.alterIntensity(-1); // 2 → 1
        assertEquals(1, addiction.getIntensity());

        addiction.alterIntensity(-1); // 1 → 0, blocked
        assertEquals(1, addiction.getIntensity());
    }

    // --- Loop scheduling ---

    @Test
    void addAddiction_schedulesLoop_noCrash() {
        assertDoesNotThrow(() -> drugPlayer.addAddiction(drugA));
    }

    @Test
    void joined_restartsLoops_noCrash() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);
        assertDoesNotThrow(() -> drugPlayer.joined());
    }

    @Test
    void clearThenJoined_noCrash() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.clear();
        assertDoesNotThrow(() -> drugPlayer.joined());
    }

    // --- Full lifecycle: add → alter intensity → remove → re-add ---

    @Test
    void fullLifecycle_addAlterRemoveReadd() {
        // Phase 1: Add addiction
        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size());
        Addiction addiction = drugPlayer.getAddictions().get(0);
        assertEquals(1, addiction.getIntensity());

        // Phase 2: Increase intensity
        addiction.alterIntensity(4); // → 5
        assertEquals(5, addiction.getIntensity());

        // Phase 3: Remove
        drugPlayer.remove(drugA);
        assertTrue(drugPlayer.getAddictions().isEmpty());

        // Phase 4: Re-add
        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size());
        assertEquals(1, drugPlayer.getAddictions().get(0).getIntensity(),
                "New addiction should start at intensity 1");
    }

    @Test
    void fullLifecycle_clearAndRestart() {
        drugPlayer.addAddiction(drugA);
        drugPlayer.addAddiction(drugB);
        drugPlayer.setDose(0.5);

        drugPlayer.clear();
        drugPlayer.setDose(0);

        assertTrue(drugPlayer.getAddictions().isEmpty());
        assertEquals(0.0, drugPlayer.getDose());

        drugPlayer.addAddiction(drugA);
        assertEquals(1, drugPlayer.getAddictions().size());
    }
}
