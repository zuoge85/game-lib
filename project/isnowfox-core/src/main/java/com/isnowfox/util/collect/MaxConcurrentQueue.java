package com.isnowfox.util.collect;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全的带容量的队列，默认容量为20
 * 使用SizeConcurrentQueue实现
 * 超过容量的插入将会把头部上的元素移除
 * 使用ConcurrentLinkedQueue实现
 *
 * @author zuoge85@gmail.com
 */
public class MaxConcurrentQueue<T> {
    private AtomicInteger size = new AtomicInteger();
    private int max;
    private ConcurrentLinkedQueue<T> q = new ConcurrentLinkedQueue<T>();

    public MaxConcurrentQueue() {
        max = 20;
    }

    public MaxConcurrentQueue(int max) {
        this.max = max;
    }

    public void add(T t) {
        q.add(t);
        int s = size.incrementAndGet();
        if (s > max) {
            q.poll();
        }
    }
}
