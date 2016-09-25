package com.isnowfox.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 线程不安全！
 *
 * @param <E>
 * @author zuoge85
 */
public class IntKeyMap<E> {
    private final E[] values;
    //	private final TreeSet<Integer> idPool = new TreeSet<>();
    private final int size;
    private int max = 0;

    @SuppressWarnings("unchecked")
    public IntKeyMap(int size, Class<E> elementClass) {
        this.size = size;
        values = (E[]) Array.newInstance(elementClass, size);
    }

    public E getInt(int i) {
        return values[i];
    }

    public int add(E e) {
        if (max >= size) {
            throw new MaxValueException("IntKeyMap id too big:" + max + ", max:" + size);
        }
        int id = max++;
        while (values[id] != null) {
            id = max++;
        }
        values[id] = e;
        return id;
    }

    private int getFreeId() {
        for (int i = 0; i < size; i++) {
            int id = max + 1;
            if (id >= size) {
                id = 0;
            }
            if (values[id] == null) {
                max = id;
                return id;
            }
        }
        throw new MaxValueException("IntKeyMap id too big:" + max + ", max:" + size);
    }

    public void remove(int id) {
//		if(id == max-1){
//			max--;
//		}else{
//			idPool.add(id);
//		}
        values[id] = null;
    }

    @Override
    public String toString() {
        return "IntKeyMap [values=" + Arrays.toString(values) + ", size=" + size + ", max=" + max + "]";
    }
}
