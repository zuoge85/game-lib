package com.isnowfox.util.collect;

import com.isnowfox.core.junit.BaseTest;

import java.util.PriorityQueue;

/**
 * @author zuoge85 on 2014/9/9.
 */
public class PriorityQueueTest extends BaseTest {
    public void test() {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(3);
        priorityQueue.add(2);
        System.out.println(priorityQueue);
        priorityQueue.add(1);

        Integer i;
        while ( (i = priorityQueue.poll()) != null){
            System.out.println(i);
        }
        System.out.println(priorityQueue);
    }
}
