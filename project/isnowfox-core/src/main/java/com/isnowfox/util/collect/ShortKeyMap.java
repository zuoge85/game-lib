package com.isnowfox.util.collect;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程不安全！
 *
 * @param <E>
 * @author zuoge85
 */
public class ShortKeyMap<E> {
    private final Map<Short, E> map = new HashMap<>();
    private short max = 0;

    public ShortKeyMap() {
    }

    public E get(int i) {
        return map.get(i);
    }

    public short add(E e) {
        short id = max++;
        while (map.get(id) != null) {
            id = max++;
        }
        map.put(id, e);
        return id;
    }

    public void remove(short id) {
        map.remove(id);
    }

    @Override
    public String toString() {
        return "IntKeyMap{" +
                "map=" + map +
                ", max=" + max +
                '}';
    }
}
