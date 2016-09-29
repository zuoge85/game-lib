package com.isnowfox.util.collect;

import org.apache.commons.collections.map.ListOrderedMap;

import java.util.Iterator;

/**
 * FIFO 先进先出队列,
 * 这个队列有点特殊,
 * 插入重复的项,会把重复插入项拉到队列前端
 *
 * @author zuoge85@gmail.com
 */
public class MaxMapQueue<K, V> implements Iterable<V> {
    private ListOrderedMap map;
    private int max;

    public MaxMapQueue(int max) {
        this.max = max;
        map = new ListOrderedMap();
    }

    public void put(K key, V v) {
        //queue.add(e);
        if (map.size() == max) {
            map.remove(map.size() - 1);
        }
        map.put(0, key, v);
    }

    public void remove(K e) {
        map.remove(e);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public V get(K k) {
        return (V) map.get(k);
    }

    public boolean containsValue(K key) {
        return map.containsValue(key);
    }

    /**
     * 遍历的是value
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<V> iterator() {
        return map.values().iterator();
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }
}
