package com.isnowfox.util;

public final class MathUtils {
    public static final int sum(int[] ints) {
        int number = 0;
        for (int i : ints) {
            number += i;
        }
        return number;
    }
}
