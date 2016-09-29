package com.isnowfox.core.id;

import com.isnowfox.util.MaxValueException;

import java.util.BitSet;

/**
 * 线程不安全
 *
 * @author zuoge85 on 14-5-5.
 */
public class IntIdMaker {
    private final int size;
    private int max = 0;
    private final BitSet bits;

    @SuppressWarnings("unchecked")
    public IntIdMaker(int size) {
        this.size = size;
        bits = new BitSet();
    }

    public int newId() {
        for (int i = 0, id = max + 1; i < size; i++, id++) {
            if (id >= size) {
                id = 0;
            }
            if (!bits.get(id)) {
                max = id;
                bits.set(id);
                return id;
            }
        }
        throw new MaxValueException("IntKeyMap id too big:" + max + ", max:" + size);
    }

    public boolean freeId(int id) {
        boolean result = bits.get(id);
        bits.clear(id);
        return result;
    }
}
