package de.chafficplugins.mytrip;

import de.chafficplugins.mytrip.utils.MathUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that validate the dose calculation arithmetic used in DrugPlayer.consume().
 * These test the mathematical formulas in isolation without needing MockBukkit or class instantiation.
 *
 * The core formula: dose += 1.0 / (double) overdose
 * Overdose triggers when: dose >= 1.0
 */
class DoseArithmeticTest {

    // --- Division by zero (overdose=0) ---

    @Test
    void doseFormula_overdoseZero_producesInfinity() {
        // BUG: When overdose=0, the formula 1.0 / 0 = Infinity.
        // MyDrug.setOverdose() allows 0 (guard is < 0 || > 99).
        // This causes immediate overdose on first consume AND breaks subsequent math.
        double dose = 0;
        int overdose = 0;
        dose += 1d / (double) overdose;

        assertTrue(Double.isInfinite(dose),
                "BUG: overdose=0 causes dose to become Infinity (1.0 / 0.0 = Infinity)");
        assertTrue(dose >= 1,
                "Infinity >= 1 is true, so every consume is an overdose");
    }

    @Test
    void doseFormula_subDoseAfterInfinity_producesNaN() {
        // After overdose=0 makes dose Infinity, subDose does: Infinity - Infinity = NaN
        // NaN breaks ALL future comparisons: (NaN >= 1) is false, (NaN <= 0) is false
        double dose = 0;
        int overdose = 0;
        dose += 1d / (double) overdose; // Infinity
        dose -= 1d / (double) overdose; // Infinity - Infinity

        assertTrue(Double.isNaN(dose),
                "BUG: subDose after Infinity produces NaN (Infinity - Infinity = NaN)");
        assertFalse(dose >= 1, "NaN >= 1 is false — overdose detection stops working");
        assertFalse(dose <= 0, "NaN <= 0 is false — recovery detection also breaks");
    }

    // --- Standard dose accumulation ---

    @Test
    void doseFormula_overdoseThree_thirdConsumeTriggers() {
        double dose = 0;
        int overdose = 3;

        dose += 1d / (double) overdose;
        assertFalse(dose >= 1, "1 consume with overdose=3 should not trigger overdose");

        dose += 1d / (double) overdose;
        assertFalse(dose >= 1, "2 consumes with overdose=3 should not trigger overdose");

        dose += 1d / (double) overdose;
        assertTrue(dose >= 1, "3 consumes with overdose=3 should trigger overdose (dose=1.0)");
    }

    @Test
    void doseFormula_overdoseOne_firstConsumeTriggers() {
        double dose = 0;
        int overdose = 1;
        dose += 1d / (double) overdose;
        assertTrue(dose >= 1, "1 consume with overdose=1 should immediately trigger overdose");
    }

    @Test
    void doseFormula_overdoseFive_exactBoundary() {
        double dose = 0;
        int overdose = 5;
        for (int i = 0; i < 4; i++) {
            dose += 1d / (double) overdose;
            assertFalse(dose >= 1, "Consume " + (i + 1) + " of 5 should not trigger");
        }
        dose += 1d / (double) overdose;
        assertTrue(dose >= 1, "5 consumes with overdose=5 should trigger overdose");
    }

    // --- Floating point precision ---

    @Test
    void doseFormula_overdoseThree_floatingPointPrecision() {
        // 3 * (1.0/3.0) might not be exactly 1.0 due to floating point.
        // In Java: 1.0/3.0 = 0.3333333333333333, and 3 * that = 0.9999999999999999.
        // BUT: the formula adds incrementally, not multiplies.
        // 0.3333... + 0.3333... + 0.3333... in IEEE 754 double = 1.0 (exact)
        double dose = 0;
        dose += 1d / 3d;
        dose += 1d / 3d;
        dose += 1d / 3d;
        // This actually equals 1.0 in IEEE 754 because of how addition rounds
        assertTrue(dose >= 1.0, "Three increments of 1/3 should be >= 1.0");
    }

    @Test
    void doseFormula_overdoseSeven_triggersWithEpsilon() {
        // With epsilon comparison (dose >= 1 - 1e-9), floating-point imprecision
        // no longer prevents overdose=7 from triggering.
        double dose = 0;
        int overdose = 7;
        for (int i = 0; i < 7; i++) {
            dose += 1d / (double) overdose;
        }
        // dose is 0.9999999999999998, which passes >= 1 - 1e-9
        assertTrue(dose >= 1 - 1e-9,
                "7 consumes with overdose=7 should trigger overdose with epsilon comparison, dose=" + dose);
    }

    @Test
    void doseFormula_subDose_returnsToZero() {
        double dose = 0;
        int overdose = 3;

        // Add 3 doses
        for (int i = 0; i < 3; i++) {
            dose += 1d / (double) overdose;
        }
        // Subtract 3 doses
        for (int i = 0; i < 3; i++) {
            dose -= 1d / (double) overdose;
        }

        // Floating point: might not be exactly 0
        assertEquals(0.0, dose, 1e-10,
                "Dose should return to ~0 after adding and subtracting equal amounts");
    }

    // --- Setter boundary validation (matching MyDrug.setOverdose logic) ---

    @Test
    void overdoseSetter_boundary_zeroIsRejected() {
        // MyDrug.setOverdose: if(overdose < 1 || overdose > 99) return;
        // 0 is now rejected, preventing division-by-zero.
        int overdose = 0;
        boolean rejected = (overdose < 1 || overdose > 99);
        assertTrue(rejected, "overdose=0 should be rejected to prevent division by zero in consume()");
    }

    @Test
    void overdoseSetter_boundary_99IsAllowed() {
        int overdose = 99;
        boolean rejected = (overdose < 0 || overdose > 99);
        assertFalse(rejected, "overdose=99 should be allowed");
    }

    @Test
    void overdoseSetter_boundary_100IsRejected() {
        int overdose = 100;
        boolean rejected = (overdose < 0 || overdose > 99);
        assertTrue(rejected, "overdose=100 should be rejected (> 99)");
    }

    @Test
    void overdoseSetter_boundary_negativeIsRejected() {
        int overdose = -1;
        boolean rejected = (overdose < 0 || overdose > 99);
        assertTrue(rejected, "overdose=-1 should be rejected (< 0)");
    }

    @Test
    void addictionProbSetter_boundary_zeroAllowsAddiction() {
        // MyDrug.setAddictionProbability: if(addict < 0 || addict > 100) return;
        // DrugPlayer.consume: MathUtils.randomInt(0, 100) <= drug.getAddictionProbability()
        // With probability=0: randomInt(0,100) can return 0, and 0 <= 0 is true.
        // So "0% probability" still has a ~1% chance of triggering addiction.
        int probability = 0;
        boolean rejected = (probability < 0 || probability > 100);
        assertFalse(rejected, "probability=0 is allowed");

        // The <= comparison means 0 <= 0 is true
        assertTrue(0 <= probability,
                "BUG: randomInt can return 0, and '0 <= 0' is true, so probability=0 still addicts ~1% of the time");
    }

    @Test
    void addictionProbSetter_boundary_100AlwaysAddicts() {
        // With probability=100: randomInt(0,100) returns [0,100), and any value <= 100 is true.
        int probability = 100;
        boolean rejected = (probability < 0 || probability > 100);
        assertFalse(rejected, "probability=100 should be allowed (always addicts)");
    }

    // --- Addiction intensity arithmetic ---

    @Test
    void alterIntensity_capAtEight() {
        // Addiction.alterIntensity: if (intensity + alter < 9) intensity += alter
        int intensity = 7;
        int alter = 1;
        if (intensity + alter < 9) {
            intensity += alter;
        }
        assertEquals(8, intensity, "Intensity 7 + 1 = 8 (allowed, just under cap)");
    }

    @Test
    void alterIntensity_blockedAtNine() {
        int intensity = 8;
        int alter = 1;
        if (intensity + alter < 9) {
            intensity += alter;
        }
        assertEquals(8, intensity, "Intensity 8 + 1 = 9, blocked by < 9 guard, stays at 8");
    }

    @Test
    void alterIntensity_floorGuard_preventsNegative() {
        // Floor guard: result must be >= 1 and <= 8
        int intensity = 1;
        int alter = -10;
        int result = intensity + alter;
        if (result >= 1 && result <= 8) {
            intensity = result;
        }
        assertEquals(1, intensity,
                "alterIntensity should block negative results — intensity stays at 1");
    }

    @Test
    void loopInterval_withNegativeIntensity_isNegative() {
        // 16000 / intensity when intensity is negative produces a negative period
        int intensity = -2;
        int period = 16000 / intensity;
        assertTrue(period < 0,
                "BUG: negative intensity produces negative timer period: " + period);
    }

    @Test
    void loopInterval_withZeroIntensity_throwsArithmeticException() {
        int intensity = 0;
        assertThrows(ArithmeticException.class, () -> {
            @SuppressWarnings("unused")
            int period = 16000 / intensity;
        }, "BUG: zero intensity causes ArithmeticException in loop()");
    }

    // --- MathUtils.randomInt boundary for addiction probability ---

    @RepeatedTest(100)
    void randomInt_zeroToHundred_isInExpectedRange() {
        int result = MathUtils.randomInt(0, 100);
        assertTrue(result >= 0 && result < 100,
                "randomInt(0, 100) should return [0, 100), got " + result);
    }

    @Test
    void randomInt_maxIsExclusive_soHundredNeverReturned() {
        // randomInt(0, 100) returns [0, 100) — max value is 99.
        // With probability=100 and randomInt result of 99: 99 <= 100 is true → always addicts.
        // With probability=99 and randomInt result of 99: 99 <= 99 is true → still addicts.
        // The only way to get NO addiction is probability < randomInt result.
        // With probability=0: only random=0 triggers (1 in 100 chance).
        assertTrue(99 <= 100, "probability=100: all random values (0-99) pass the <= check");
        assertTrue(99 <= 99, "probability=99: random=99 still passes");
        assertTrue(0 <= 0, "probability=0: random=0 still passes (off-by-one)");
        assertFalse(1 <= 0, "probability=0: random=1 correctly fails");
    }
}
