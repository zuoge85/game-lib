package com.isnowfox.util;

import com.isnowfox.core.CloneInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin on 14-1-15.
 */
public class CloneUtils {
    @SuppressWarnings("unchecked")
    public static final <T extends CloneInterface> T[] clone(T[] array) throws CloneNotSupportedException {
        if (array == null) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        for (int i = 0; i < array.length; i++) {
            T v = array[i];
            if (v != null) {
                result[i] = (T) v.clone();
            }
        }
        return result;
    }

    /**
     * 不clone key
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return 返回HashMap
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    public static final <K, V extends CloneInterface> Map<K, V> clone(Map<K, V> map)
            throws CloneNotSupportedException {
        if (map == null) {
            return null;
        }
        HashMap<K, V> result = new HashMap<>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V value = entry.getValue();
            K key = entry.getKey();
            if (value != null) {
                value = (V) value.clone();
            }
            result.put(key, value);
        }
        return result;
    }

    public static int[][] clone(int[][] array) {
        int[][] result = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            int[] v = array[i];
            if (v != null) {
                result[i] = (int[]) v.clone();
            }
        }
        return result;
    }
}
