package de.chafficplugins.mytrip.drugs.events;

import de.chafficplugins.mytrip.MockBukkitTestBase;
import de.chafficplugins.mytrip.drugs.objects.DrugPlayer;
import de.chafficplugins.mytrip.drugs.objects.MyDrug;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Integration tests for FeatureEvents: player death and join event handling.
 * Calls event handler methods directly since the listener is not auto-registered
 * (CrucialLib connect fails in test, so onEnable skips event registration).
 */
class FeatureEventsTest extends MockBukkitTestBase {

    private FeatureEvents featureEvents;
    private MyDrug testDrug;

    @BeforeEach
    void setUp() {
        assumeTrue(pluginLoaded, "MockBukkit plugin loading failed — skipping");
        DrugPlayer.playerData.clear();

        featureEvents = new FeatureEvents();

        testDrug = new MyDrug("EventTestDrug", Material.SUGAR);
        testDrug.setOverdose(5);
        testDrug.setAddictionProbability(100);
    }

    @SuppressWarnings("deprecation")
    private PlayerDeathEvent createDeathEvent(PlayerMock player) {
        DamageSource source = DamageSource.builder(DamageType.GENERIC).build();
        List<ItemStack> drops = new ArrayList<>();
        return new PlayerDeathEvent(player, source, drops, 0, "died");
    }

    private PlayerJoinEvent createJoinEvent(PlayerMock player) {
        return new PlayerJoinEvent(player, "joined");
    }

    // --- onJoin tests ---

    @Test
    void onJoin_newPlayer_createsDrugPlayer() {
        PlayerMock player = server.addPlayer();
        featureEvents.onJoin(createJoinEvent(player));

        DrugPlayer dp = DrugPlayer.getPlayer(player.getUniqueId());
        assertNotNull(dp, "DrugPlayer should be created on join");
        assertEquals(0.0, dp.getDose(), "New player should have dose=0");
    }

    @Test
    void onJoin_returningPlayer_resetsDoseToZero() {
        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        dp.setDose(0.75);
        DrugPlayer.addPlayer(dp);

        featureEvents.onJoin(createJoinEvent(player));

        assertEquals(0.0, dp.getDose(), "Dose should be reset to 0 on join");
    }

    @Test
    void onJoin_returningPlayerWithAddictions_addictionsPreserved() {
        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        dp.setDose(0.5);
        DrugPlayer.addPlayer(dp);
        dp.addAddiction(testDrug);
        assertEquals(1, dp.getAddictions().size());

        featureEvents.onJoin(createJoinEvent(player));

        assertEquals(0.0, dp.getDose(), "Dose reset on join");
        assertEquals(1, dp.getAddictions().size(), "Addictions should persist across join");
    }

    @Test
    void onJoin_newPlayer_addedToPlayerData() {
        int sizeBefore = DrugPlayer.playerData.size();
        PlayerMock player = server.addPlayer();

        featureEvents.onJoin(createJoinEvent(player));

        assertEquals(sizeBefore + 1, DrugPlayer.playerData.size(),
                "New player should be added to playerData");
    }

    @Test
    void onJoin_existingPlayer_notDuplicated() {
        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
        int sizeBefore = DrugPlayer.playerData.size();

        featureEvents.onJoin(createJoinEvent(player));

        assertEquals(sizeBefore, DrugPlayer.playerData.size(),
                "Existing player should not be duplicated in playerData");
    }

    // --- onDeath tests ---

    @Test
    void onDeath_healOnDeathEnabled_clearsAddictions() {
        plugin.getConfig().set("features.heal_on_death", true);

        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
        dp.addAddiction(testDrug);
        assertEquals(1, dp.getAddictions().size());

        featureEvents.onDeath(createDeathEvent(player));

        assertTrue(dp.getAddictions().isEmpty(),
                "Addictions should be cleared on death when heal_on_death is enabled");
    }

    @Test
    void onDeath_healOnDeathDisabled_addictionsPreserved() {
        plugin.getConfig().set("features.heal_on_death", false);

        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
        dp.addAddiction(testDrug);
        assertEquals(1, dp.getAddictions().size());

        featureEvents.onDeath(createDeathEvent(player));

        assertEquals(1, dp.getAddictions().size(),
                "Addictions should be preserved when heal_on_death is disabled");
    }

    @Test
    void onDeath_healOnDeathEnabled_noAddictions_noCrash() {
        plugin.getConfig().set("features.heal_on_death", true);

        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
        assertTrue(dp.getAddictions().isEmpty());

        assertDoesNotThrow(() -> featureEvents.onDeath(createDeathEvent(player)));
    }

    @Test
    void onDeath_healOnDeathEnabled_multipleAddictions_allCleared() {
        plugin.getConfig().set("features.heal_on_death", true);

        MyDrug drugB = new MyDrug("EventDrugB", Material.BLAZE_POWDER);
        drugB.setOverdose(5);
        drugB.setAddictionProbability(100);

        PlayerMock player = server.addPlayer();
        DrugPlayer dp = new DrugPlayer(player);
        DrugPlayer.addPlayer(dp);
        dp.addAddiction(testDrug);
        dp.addAddiction(drugB);
        assertEquals(2, dp.getAddictions().size());

        featureEvents.onDeath(createDeathEvent(player));

        assertTrue(dp.getAddictions().isEmpty(), "All addictions should be cleared on death");
    }

    @Test
    void onDeath_unregisteredPlayer_noCrash() {
        plugin.getConfig().set("features.heal_on_death", true);

        PlayerMock player = server.addPlayer();
        // Don't add to playerData

        assertDoesNotThrow(() -> featureEvents.onDeath(createDeathEvent(player)),
                "Death event should not crash for unregistered player");
    }

    // --- Join then death sequence ---

    @Test
    void joinThenDeath_fullFlow() {
        plugin.getConfig().set("features.heal_on_death", true);

        PlayerMock player = server.addPlayer();

        // Join
        featureEvents.onJoin(createJoinEvent(player));
        DrugPlayer dp = DrugPlayer.getPlayer(player.getUniqueId());
        assertNotNull(dp);

        // Get addicted
        dp.addAddiction(testDrug);
        dp.setDose(0.5);
        assertEquals(1, dp.getAddictions().size());

        // Die
        featureEvents.onDeath(createDeathEvent(player));
        assertTrue(dp.getAddictions().isEmpty(), "Addictions cleared on death");

        // Rejoin
        featureEvents.onJoin(createJoinEvent(player));
        assertEquals(0.0, dp.getDose(), "Dose reset on rejoin");
        assertTrue(dp.getAddictions().isEmpty(), "No addictions after death+rejoin");
    }
}
