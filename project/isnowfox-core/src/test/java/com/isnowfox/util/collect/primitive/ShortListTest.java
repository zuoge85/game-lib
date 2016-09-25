package com.isnowfox.util.collect.primitive;

import com.isnowfox.core.junit.BaseTest;

/**
 * @author zuoge85 on 2014/8/10.
 */
public class ShortListTest extends BaseTest {

    public static final int INT = 80;
    public static final int COUNT = 50000;
    public static final Object EMPTEY = new Object();

    public void test() {
        ShortList list = new ShortList();
        list.add((short) INT);
        assertEquals(INT, list.get(0));

        list.remove((short) INT);
        assertEquals(list.size(), 0);

        for (int i = 0; i < INT; i++) {
            list.add((short) i);
        }
        assertEquals(list.size(), INT);
        short[] array = list.cloneArray();
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], i);
        }
        list.remove((short) 60);
        assertEquals(list.get(60), 61);


        array = list.cloneArray((short) 32);
        assertEquals(array[32], 33);

        array = list.cloneArray((short) (INT - 1));

        assertEquals(array[array.length - 1], INT - 2);


        array = list.cloneArray((short) 0);
        assertEquals(array[0], 1);
    }
//
//
//    public void test1ShortListPef() {
//        ShortList list = new ShortList();
//        for (int i = 0; i < COUNT; i++) {
//            list.add((short) i);
//        }
//
//        for (int i = 0; i < COUNT; i++) {
//            list.remove((short) RandomUtils.randInt(COUNT));
//        }
//    }
//
//    public void test1TShortListPef() {
//        TShortArrayList list = new TShortArrayList();
//        for (int i = 0; i < COUNT; i++) {
//            list.add((short) i);
//        }
//
//        for (int i = 0; i < COUNT; i++) {
//            list.remove((short) RandomUtils.randInt(COUNT));
//        }
//    }
//
//    public void test1ArrayListPef() {
//        ArrayList<Short> list = new ArrayList<>();
//        for (int i = 0; i < COUNT; i++) {
//            list.add(Short.valueOf((short) i));
//        }
//
//        for (int i = 0; i < COUNT; i++) {
//            list.remove(Short.valueOf((short) RandomUtils.randInt(COUNT)));
//        }
//    }
//
//
//    public void test2HashMap() {
////        System.gc();
////        long time = System.currentTimeMillis();
//        HashMap<Short, Object> map = new HashMap<>();
//        for (int i = 0; i < COUNT; i++) {
//            map.put((short) i, EMPTEY);
//        }
//
//        for (int i = 0; i < COUNT; i++) {
//            map.remove(Short.valueOf((short) RandomUtils.randInt(COUNT)));
//        }
//
////        System.out.println("test2HashMap:" + (System.currentTimeMillis() - time));
//    }
//
//    public void test2IntMap() {
////        System.gc();
////        long time = System.currentTimeMillis();
//        TShortObjectHashMap<Object> map = new TShortObjectHashMap<>(COUNT);
//        for (int i = 0; i < COUNT; i++) {
//            map.put((short) i, EMPTEY);
//        }
//
//        for (int i = 0; i < COUNT; i++) {
//            map.remove((short) RandomUtils.randInt(COUNT));
//        }
//
////        System.out.println("test2IntMap:" + (System.currentTimeMillis() - time));
//    }
}
