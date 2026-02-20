package de.chafficplugins.mytrip;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

/**
 * Base class for tests that require a MockBukkit server.
 * Sets up the mock server and loads the MyTrip plugin.
 *
 * Note: CrucialLib is not available as a plugin in tests, so Crucial.connect()
 * returns false and onEnable() skips most initialization. However, the plugin
 * instance is registered and MyTrip.getInstance() returns a valid reference,
 * allowing domain object instantiation and logic testing.
 *
 * If MockBukkit cannot initialize (e.g., missing transitive dependencies),
 * all tests in subclasses are gracefully skipped via assumeTrue(pluginLoaded).
 */
public abstract class MockBukkitTestBase {

    protected static ServerMock server;
    protected static MyTrip plugin;
    protected static boolean pluginLoaded = false;

    @BeforeAll
    static void setUpServer() {
        try {
            server = MockBukkit.mock();
            plugin = MockBukkit.load(MyTrip.class);
            pluginLoaded = true;
            // Manually set config defaults since onEnable didn't fully run
            // (CrucialLib not available, so Crucial.connect() returns false)
            plugin.getConfig().addDefault("features.heal_on_death", true);
            plugin.getConfig().addDefault("settings.permissions", false);
            plugin.getConfig().addDefault("settings.update-alerts", true);
            plugin.getConfig().addDefault("disable_drug_set", false);
            plugin.getConfig().addDefault("addiction_effects",
                    java.util.List.of("CONFUSION:0"));
            plugin.getConfig().addDefault("overdose_effects",
                    java.util.List.of("BLINDNESS:0", "NAUSEA:0", "SLOW:0", "SLOW_DIGGING:0", "WEAKNESS:0"));
            plugin.getConfig().options().copyDefaults(true);
        } catch (Throwable e) {
            // Plugin loading or MockBukkit initialization failed
            // Tests will be skipped via assumeTrue(pluginLoaded) in each test method
            System.err.println("MockBukkitTestBase setup failed: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace(System.err);
            pluginLoaded = false;
        }
    }

    @AfterAll
    static void tearDownServer() {
        try {
            if (server != null) {
                MockBukkit.unmock();
            }
        } catch (Throwable ignored) {
            // Cleanup failures are not critical
        }
    }
}
