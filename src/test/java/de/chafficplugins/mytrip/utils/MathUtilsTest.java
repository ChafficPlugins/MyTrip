package de.chafficplugins.mytrip.utils;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @RepeatedTest(50)
    void randomInt_returnsValueWithinRange() {
        int result = MathUtils.randomInt(0, 100);
        assertTrue(result >= 0 && result < 100,
                "Expected value in [0, 100), got " + result);
    }

    @RepeatedTest(20)
    void randomInt_negativeRange() {
        int result = MathUtils.randomInt(-50, 50);
        assertTrue(result >= -50 && result < 50,
                "Expected value in [-50, 50), got " + result);
    }

    @Test
    void randomInt_sameMinMax_returnsMin() {
        int result = MathUtils.randomInt(5, 5);
        assertEquals(5, result);
    }

    @RepeatedTest(20)
    void randomInt_smallRange() {
        int result = MathUtils.randomInt(10, 12);
        assertTrue(result >= 10 && result < 12,
                "Expected value in [10, 12), got " + result);
    }
}
