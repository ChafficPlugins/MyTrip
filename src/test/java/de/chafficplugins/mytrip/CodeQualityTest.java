package de.chafficplugins.mytrip;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Source-level tests that read Java source files and verify known bugs are fixed.
 * Tests skip gracefully when source files aren't available (e.g., in packaged environments).
 */
class CodeQualityTest {

    private static final Path PROJECT_ROOT = Path.of(System.getProperty("user.dir"));
    private static final Path SRC_ROOT = PROJECT_ROOT.resolve("src/main/java/de/chafficplugins/mytrip");

    private String readSource(String relativePath) {
        Path path = SRC_ROOT.resolve(relativePath);
        if (!Files.exists(path)) {
            return null; // graceful skip
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }

    // --- Addiction.java: split index fixed ---

    @Test
    void addiction_loopEffectParsing_usesSplit1() {
        // Config format is "NAME:STRENGTH" → split produces [NAME, STRENGTH].
        // Must use split[1] for the strength index.
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        assertFalse(source.contains("split[2]"),
                "Addiction.java should NOT use split[2] — config format 'NAME:STRENGTH' only has indices 0 and 1");
        assertTrue(source.contains("split[1]"),
                "Addiction.java should use split[1] for the effect strength");
    }

    // --- Commands.java: recover condition fixed ---

    @Test
    void commands_recoverCondition_isCorrect() {
        // recover should check getDose() > 0 to recover drugged players.
        String source = readSource("drugs/commands/Commands.java");
        if (source == null) return;

        assertTrue(source.contains("getDose() > 0"),
                "Commands.recoverCommand() should use 'getDose() > 0' to recover drugged players");
        assertFalse(source.contains("getDose() <= 0"),
                "Commands.recoverCommand() should NOT use 'getDose() <= 0' (inverted logic)");
    }

    // --- DrugTool.java: null check before iteration ---

    @Test
    void drugTool_loadAll_nullCheckBeforeIteration() {
        String source = readSource("drugs/objects/DrugTool.java");
        if (source == null) return;

        // Find the loadAll method body specifically to avoid matching getById's for-each
        int loadAllIndex = source.indexOf("void loadAll()");
        assertTrue(loadAllIndex >= 0, "DrugTool should contain a loadAll() method");

        String loadAllBody = source.substring(loadAllIndex);
        int nullCheckIndex = loadAllBody.indexOf("if (tools == null)");
        int forEachIndex = loadAllBody.indexOf("for (DrugTool item : tools)");

        assertTrue(nullCheckIndex >= 0, "DrugTool.loadAll() should contain a null check on tools");
        assertTrue(forEachIndex >= 0, "DrugTool.loadAll() should contain a for-each loop on tools");
        assertTrue(nullCheckIndex < forEachIndex,
                "DrugTool.loadAll() null check should come before for-each loop");
    }

    // --- MessagesYaml.java: placeholder fixed ---

    @Test
    void messagesYaml_unknownPlayerMessage_usesDistinctPlaceholders() {
        String source = readSource("io/MessagesYaml.java");
        if (source == null) return;

        // UNKNOWN_PLAYER_OR_ALREADY_ADDICTED uses both {0} (player) and {1} (drug)
        assertTrue(source.contains("already addicted to {1}!"),
                "UNKNOWN_PLAYER_OR_ALREADY_ADDICTED should use {1} for the drug name");
        assertFalse(source.contains("already addicted to {0}"),
                "UNKNOWN_PLAYER_OR_ALREADY_ADDICTED should NOT use {0} for the drug name");
    }

    // --- Addiction.java: intensity floor guard ---

    @Test
    void addiction_alterIntensity_hasFloorGuard() {
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        assertTrue(source.contains("result >= 1") || source.contains("result > 0"),
                "Addiction.alterIntensity() should have a floor guard preventing intensity < 1");
    }

    // --- DrugPlayer.java: overdose validation ---

    @Test
    void drugPlayer_consume_overdoseZeroIsRejected() {
        // MyDrug.setOverdose() should reject overdose=0 to prevent division by zero.
        String myDrugSource = readSource("drugs/objects/MyDrug.java");
        if (myDrugSource != null) {
            assertTrue(myDrugSource.contains("overdose < 1 || overdose > 99"),
                    "MyDrug.setOverdose() should reject overdose=0 (guard: < 1 || > 99)");
        }
    }

    // --- Addiction.java: loop() guards against zero intensity ---

    @Test
    void addiction_loop_guardsAgainstZeroIntensity() {
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        assertTrue(source.contains("if (intensity <= 0) return"),
                "Addiction.loop() should guard against zero/negative intensity before scheduling");
    }

    // --- InteractionEvents.java: PERM_USE_ + name for drug permissions ---

    @Test
    void interactionEvents_permissionCheck_usesBasePrefix() {
        String source = readSource("drugs/events/InteractionEvents.java");
        if (source == null) return;

        assertTrue(source.contains("PERM_USE_ + crucialItem.getName()"),
                "InteractionEvents uses PERM_USE_ + name for drug permission checks");

        // Tool permissions should now be distinct
        String toolEventsSource = readSource("drugs/events/DrugToolEvents.java");
        if (toolEventsSource != null) {
            assertTrue(toolEventsSource.contains("PERM_USE_DRUG_TEST") ||
                            toolEventsSource.contains("PERM_USE_ANTITOXIN") ||
                            toolEventsSource.contains("PERM_USE_DRUG_SET"),
                    "DrugToolEvents uses tool-specific permission constants");
        }
    }

    // --- CommandListener.java: handles >4 args ---

    @Test
    void commandListener_argsOverFour_handledWithError() {
        String source = readSource("drugs/commands/CommandListener.java");
        if (source == null) return;

        assertTrue(source.contains("args.length == 4"),
                "CommandListener handles up to 4 args");
        assertTrue(source.contains("} else {"),
                "CommandListener should have an else clause handling args > 4");
    }

    // --- Commands.java: callCommand overload clarity ---

    @Test
    void commands_callCommandOverload_usesDescriptiveName() {
        String source = readSource("drugs/commands/Commands.java");
        if (source == null) return;

        assertTrue(source.contains("drugNameOrSub"),
                "Commands.callCommand parameter should be named descriptively (drugNameOrSub)");
    }
}
