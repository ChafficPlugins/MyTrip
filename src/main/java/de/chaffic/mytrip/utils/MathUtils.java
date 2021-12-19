package de.chaffic.mytrip.utils;

public class MathUtils {
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
