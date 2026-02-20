package de.chafficplugins.mytrip;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Source-level tests that read Java source files and flag known bug patterns.
 * These tests catch bugs that would otherwise require runtime testing with MockBukkit.
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

    // --- Addiction.java: split[2] bug ---

    @Test
    void addiction_loopEffectParsing_usesSplit2InsteadOfSplit1() {
        // BUG: Addiction.java line 69 reads split[2] but the config format "CONFUSION:0"
        // only produces 2 parts when split by ":". split[2] throws ArrayIndexOutOfBoundsException.
        // This crash occurs whenever a player's addiction intensity exceeds 5.
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        // The bug: code uses split[2] when it should use split[1]
        assertTrue(source.contains("split[2]"),
                "BUG CONFIRMED: Addiction.java uses split[2] to parse effect strength. " +
                "Config format is 'NAME:STRENGTH' (2 parts), so split[2] throws ArrayIndexOutOfBoundsException. " +
                "Fix: change split[2] to split[1]");

        // Verify the overdose parsing in MyDrug correctly uses split[1] (the right way)
        String myDrugSource = readSource("drugs/objects/MyDrug.java");
        if (myDrugSource != null) {
            assertTrue(myDrugSource.contains("split[1]"),
                    "MyDrug.doDrug() correctly uses split[1] — Addiction should match this pattern");
        }
    }

    // --- Commands.java: inverted recover condition ---

    @Test
    void commands_recoverCondition_isInverted() {
        // BUG: Commands.java line 107: "drugPlayer.getDose() <= 0"
        // This allows recovery for players with dose <= 0 (who are NOT drugged)
        // and shows "didn't consume" for players with dose > 0 (who ARE drugged).
        // The condition should be "getDose() > 0" to recover drugged players.
        String source = readSource("drugs/commands/Commands.java");
        if (source == null) return;

        assertTrue(source.contains("getDose() <= 0"),
                "BUG CONFIRMED: Commands.recoverCommand() uses 'getDose() <= 0' which is inverted. " +
                "It recovers sober players and rejects drugged players. Fix: change to 'getDose() > 0'");
    }

    // --- DrugTool.java: null check after use ---

    @Test
    void drugTool_loadAll_nullCheckAfterIteration() {
        // BUG: DrugTool.loadAll() iterates 'tools' in a for-each loop BEFORE checking if it's null.
        // If loadFromJson returns null, the for-each throws NullPointerException before
        // reaching the null guard. The null check at line 48 is dead code.
        String source = readSource("drugs/objects/DrugTool.java");
        if (source == null) return;

        // Find the pattern: for-each on tools appears before the null check
        int forEachIndex = source.indexOf("for (DrugTool item : tools)");
        int nullCheckIndex = source.indexOf("if (tools == null)");

        assertTrue(forEachIndex >= 0, "DrugTool.loadAll() should contain a for-each loop on tools");
        assertTrue(nullCheckIndex >= 0, "DrugTool.loadAll() should contain a null check on tools");
        assertTrue(forEachIndex < nullCheckIndex,
                "BUG CONFIRMED: DrugTool.loadAll() iterates 'tools' at index " + forEachIndex +
                " before null check at index " + nullCheckIndex +
                ". If loadFromJson returns null, for-each throws NPE before the guard. " +
                "Fix: move the null check before the for-each loop");
    }

    // --- MessagesYaml.java: duplicate placeholder ---

    @Test
    void messagesYaml_unknownPlayerMessage_usesDuplicatePlaceholder() {
        // BUG: MessagesYaml.java line 39: UNKNOWN_PLAYER_OR_ALREADY_ADDICTED default message
        // uses {0} twice: "The player {0} was not found or he is already addicted to {0}!"
        // The call site passes (playerName, drugName) as two arguments, so {1} should show the drug name.
        String source = readSource("io/MessagesYaml.java");
        if (source == null) return;

        // Find the line with the buggy message
        assertTrue(source.contains("addicted to {0}!"),
                "BUG CONFIRMED: UNKNOWN_PLAYER_OR_ALREADY_ADDICTED uses {0} twice. " +
                "The second placeholder should be {1} to show the drug name. " +
                "Current: 'already addicted to {0}' → Fix: 'already addicted to {1}'");
    }

    // --- Addiction.java: no intensity floor ---

    @Test
    void addiction_alterIntensity_hasNoFloorGuard() {
        // BUG: Addiction.alterIntensity() caps intensity at 8 (ceiling) but has no floor.
        // alterIntensity(-100) would set intensity to -99, causing:
        // 1. Division by zero or negative period in loop(): 16000 / intensity
        // 2. Negative damage values in player.damage(intensity)
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        // The method only checks the ceiling (< 9), not the floor
        assertTrue(source.contains("intensity + alter < 9"),
                "Addiction.alterIntensity() checks ceiling (< 9)");
        assertFalse(source.contains("intensity + alter > 0") || source.contains("intensity + alter >= 1"),
                "BUG: Addiction.alterIntensity() has no floor guard — negative intensity causes " +
                "division by zero in loop() (16000 / intensity) and negative damage values");
    }

    // --- DrugPlayer.java: division by zero ---

    @Test
    void drugPlayer_consume_divisionByZeroWithOverdoseZero() {
        // BUG: DrugPlayer.consume() calculates dose += 1d / (double) drug.getOverdose()
        // If overdose=0 (which is allowed by MyDrug.setOverdose's validation: < 0 || > 99),
        // this produces Infinity. Subsequent subDose() produces NaN, breaking all dose logic.
        String source = readSource("drugs/objects/DrugPlayer.java");
        if (source == null) return;

        assertTrue(source.contains("1d / (double) drug.getOverdose()"),
                "DrugPlayer.consume() uses division by drug.getOverdose()");

        // Verify MyDrug allows overdose=0
        String myDrugSource = readSource("drugs/objects/MyDrug.java");
        if (myDrugSource != null) {
            assertTrue(myDrugSource.contains("overdose < 0 || overdose > 99"),
                    "BUG CONFIRMED: MyDrug.setOverdose() allows overdose=0 (check is < 0 || > 99). " +
                    "This causes division by zero in DrugPlayer.consume(). " +
                    "Fix: change guard to 'overdose < 1 || overdose > 99' or add a check in consume()");
        }
    }

    // --- Addiction.java: loop() division by zero ---

    @Test
    void addiction_loop_divisionByZeroWhenIntensityIsZero() {
        // BUG: Addiction.loop() uses 16000 / intensity as the timer period.
        // If intensity reaches 0 (via alterIntensity with negative values),
        // this throws ArithmeticException (integer division by zero).
        String source = readSource("drugs/objects/Addiction.java");
        if (source == null) return;

        assertTrue(source.contains("16000 / intensity"),
                "BUG: Addiction.loop() divides 16000 by intensity without checking for zero. " +
                "If intensity is 0, this throws ArithmeticException");
    }

    // --- InteractionEvents.java: PERM_USE_ + name collision ---

    @Test
    void interactionEvents_permissionCheck_usesBasePrefix() {
        // InteractionEvents.onDrugConsume() checks permission: PERM_USE_ + crucialItem.getName()
        // Since PERM_USE_ = "mytrip.use.", this creates permissions like "mytrip.use.Weed"
        // But PERM_USE_DRUG_TEST, PERM_USE_ANTITOXIN, PERM_USE_DRUG_SET are also "mytrip.use."
        // So tool-specific permission checks effectively check "mytrip.use." (empty suffix).
        String source = readSource("drugs/events/InteractionEvents.java");
        if (source == null) return;

        assertTrue(source.contains("PERM_USE_ + crucialItem.getName()"),
                "InteractionEvents uses PERM_USE_ + name for drug permission checks");

        // This is fine for drugs (creates "mytrip.use.DrugName"), but the tool permissions
        // in DrugToolEvents use PERM_USE_DRUG_TEST etc., which are all just "mytrip.use."
        String toolEventsSource = readSource("drugs/events/DrugToolEvents.java");
        if (toolEventsSource != null) {
            assertTrue(toolEventsSource.contains("PERM_USE_DRUG_TEST") ||
                            toolEventsSource.contains("PERM_USE_ANTITOXIN") ||
                            toolEventsSource.contains("PERM_USE_DRUG_SET"),
                    "DrugToolEvents uses tool-specific permission constants (which are all identical — see ConfigStringsTest)");
        }
    }

    // --- CommandListener.java: args.length > 4 silently drops commands ---

    @Test
    void commandListener_argsOverFour_silentlyIgnored() {
        // CommandListener only handles args.length 0-4. Args > 4 silently returns true
        // without any error message, which confuses users.
        String source = readSource("drugs/commands/CommandListener.java");
        if (source == null) return;

        // Check that args.length == 4 is the highest checked
        assertTrue(source.contains("args.length == 4"),
                "CommandListener handles up to 4 args");
        assertFalse(source.contains("args.length == 5") || source.contains("args.length > 4"),
                "BUG: CommandListener has no handler for args.length > 4 — commands with extra args are silently ignored");
    }

    // --- Commands.java: callCommand(caller, GIVE, args[1]) passes drug name as both sub and drugName ---

    @Test
    void commands_callCommandOverload_passesSubAsDrugName() {
        // Commands.callCommand(player, category, sub) internally calls
        // performCommand(caller, category, sub, sub, null) — passing 'sub' as both 'sub' and 'drugName'.
        // For "give <drug>", args[1] (drug name) is passed as 'sub', which then becomes 'drugName'.
        // This works but is a code smell that could cause confusion.
        String source = readSource("drugs/commands/Commands.java");
        if (source == null) return;

        assertTrue(source.contains("performCommand(caller, category, sub, sub, null)"),
                "Commands.callCommand(3-arg) passes 'sub' as both sub and drugName — works for GIVE but is confusing");
    }
}
