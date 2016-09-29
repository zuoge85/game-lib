package com.isnowfox.util.collect;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * FIFO 先进先出队列
 *
 * @author zuoge85@gmail.com
 */
public class MaxQueue<T> implements Iterable<T> {
    private Deque<T> queue;
    private int max;

    public MaxQueue(int max) {
        this.max = max;
        queue = new ArrayDeque<>(max);
    }

    public void add(Collection<T> collect) {
        int i = 0;
        for (T t : collect) {
            if (i < max) {
                queue.addLast(t);
            } else {
                break;
            }
            i++;
        }
    }

    public void add(T e) {
        if (queue.size() == max) {
            queue.removeLast();
        }
        queue.addFirst(e);
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }

    public T poll() {
        return queue.poll();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
