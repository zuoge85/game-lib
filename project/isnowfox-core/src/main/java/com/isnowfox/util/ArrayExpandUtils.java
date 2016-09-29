package com.isnowfox.util;

import gnu.trove.list.array.TIntArrayList;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.List;

public final class ArrayExpandUtils extends org.apache.commons.lang.ArrayUtils {
    public static int[] toPrimitive(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static int[] toPrimitive(Collection<Integer> collect) {
        if (collect == null) {
            return null;
        }
        int[] array = new int[collect.size()];
        int i = 0;
        for (int value : collect) {
            array[i++] = value;
        }
        return array;
    }

    public static boolean check(Object[] objs, Class<?>... clses) {
        if (objs.length == clses.length) {
            for (int i = 0; i < clses.length; i++) {
                if (!clses[i].isInstance(objs[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static <T> T get(T[] objs, int i) {
        if (objs != null && i < objs.length && i > -1) {
            return objs[i];
        }
        return null;
    }

    public static String join(String[] array, char sep, int startIndex) {
        return join(array, sep, startIndex, array.length);
    }

    /**
     * 连接 startIndex 到 endIndex-1的字符串
     *
     * @param array
     * @param sep        间隔字符
     * @param startIndex
     * @param endIndex   不包含
     * @return
     */
    public static String join(String[] array, char sep, int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex > endIndex || endIndex > array.length) {
            throw new IndexOutOfBoundsException(
                    "startIndex " + startIndex + ", endIndex " + endIndex + ", array.length"
                            + array.length);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < array.length && i < endIndex; i++) {
            if (i > startIndex) {
                sb.append(sep);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public static final String toString(byte[] array, int len) {
        StringBuilder sb = new StringBuilder(len * 5);
        sb.append('[');
        for (int i = 0; i < array.length && i < len; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static final String toString(byte[] array, int offset, int len) {
        StringBuilder sb = new StringBuilder(len * 5);
        sb.append('[');
        for (int i = offset; i < array.length && i < len; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static int[] toIntArray(String str) {
        String[] array = str.split(",");
        int[] intArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            intArray[i] = NumberUtils.toInt(array[i]);
        }
        return intArray;
    }

    public static String toSizeString(TIntArrayList[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i].size() + ",");
        }
        return sb.toString();
    }
}
