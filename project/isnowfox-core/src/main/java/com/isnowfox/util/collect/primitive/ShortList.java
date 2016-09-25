package com.isnowfox.util.collect.primitive;

import java.util.Arrays;

/**
 * @author zuoge85 on 2014/8/10.
 */
public final class ShortList {
    private static final short[] EMPTY_ELEMENTDATA = {};
    private static final int DEFAULT_CAPACITY = 10;
    public static final ShortList EMPTY = new ShortList(EMPTY_ELEMENTDATA, 0);
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private short[] objects;
    private int size;


    public ShortList(short[] array, int length) {
        objects = array;
        size = length;
    }

    public ShortList() {
        this.objects = EMPTY_ELEMENTDATA;
        this.size = 0;
    }

    public ShortList(int initialCapacity) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        this.objects = new short[initialCapacity];
    }


    public boolean contains(short item) {
        for (int i = 0; i < size; i++) {
            if (objects[i] == item) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof com.sun.org.apache.xerces.internal.xs.ShortList)) {
            return false;
        }
        com.sun.org.apache.xerces.internal.xs.ShortList rhs = (com.sun.org.apache.xerces.internal.xs.ShortList) obj;

        if (size != rhs.getLength()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (objects[i] != rhs.item(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean add(short value) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        objects[size++] = value;
        return true;
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (objects == EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {

        // overflow-conscious code
        if (minCapacity - objects.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = objects.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        objects = Arrays.copyOf(objects, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            // overflow
            throw new OutOfMemoryError();
        }

        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public short get(int index) {
        if (index >= 0 && index < size) {
            return objects[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return size;
    }

    public boolean remove(short value) {
        for (int index = 0; index < size; index++) {
            if (value == objects[index]) {
                fastRemove(index);
                return true;
            }
        }
        return false;
    }

    public short removeByIndex(int index) {
        rangeCheck(index);

        short oldValue = objects[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(objects, index + 1, objects, index,
                    numMoved);
        }

//        objects[--size] = null; // clear to let GC do its work
        --size;
        return oldValue;
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(objects, index + 1, objects, index,
                    numMoved);
        }
        --size;//objects[--size] = 0; // short 不需要   clear to let GC do its work
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public short[] cloneArray() {
        short[] copy = new short[size];
        System.arraycopy(objects, 0, copy, 0, size);
        return copy;
    }

    public short[] cloneArray(short value) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (objects[i] == value) {
                index = i;
                short[] copy = new short[size - 1];
                if (index > 0) {
                    //开始部分
                    System.arraycopy(objects, 0, copy, 0, index);
                }
                if (index < copy.length) {
                    System.arraycopy(objects, index + 1, copy, index, copy.length - index);
                }
//                System.arraycopy(objects, 0, copy, 0, index);
                return copy;
            }
        }

        short[] copy = new short[size];
        System.arraycopy(objects, 0, copy, 0, size);
        return copy;
    }
}
