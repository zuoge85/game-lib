package com.isnowfox.util.collect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.apache.commons.lang.ArrayUtils;

import java.beans.Transient;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个格子只能放一个物品的背包
 * <p>
 * <p>
 * 实现参考 CopyOnWriteArrayList，但是放弃了一些一致性
 *
 * @author zuoge85
 * @see CopyOnWriteArrayList
 */
@JacksonStdImpl
public class ConcurrentSingleArrayBag<E> implements List<E>, RandomAccess,
        Cloneable, java.io.Serializable {
    private List<ConcurrentSingleArrayBagListener> observers = new ArrayList<>();
    /**
     *
     */
    private static final long serialVersionUID = -5979295302580035335L;

    /**
     * The lock protecting all mutators
     */
    transient final ReentrantLock lock = new ReentrantLock();

    /**
     * The array, accessed only via getArray/setArray.
     */
    private volatile transient Object[] array;

    public ConcurrentSingleArrayBag() {
        setArray(new Object[0]);
    }

    public ConcurrentSingleArrayBag(int size) {
        setArray(new Object[size]);
    }

    @JsonCreator
    private ConcurrentSingleArrayBag(E[] value) {
        setArray(value);
    }


    /**
     * Gets the array. Non-private so as to also be accessible from
     * CopyOnWriteArraySet class.
     */
    final Object[] getArray() {
        return array;
    }

    /**
     * Sets the array.
     */
    final void setArray(Object[] a) {
        array = a;
    }

    @SuppressWarnings("unchecked")
    final E get(Object[] a, int index) {
        return (E) a[index];
    }

    @Override
    public E get(int index) {
        return get(getArray(), index);
    }

    @Override
    public E set(int index, E element) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            E oldValue = get(elements, index);
            elements[index] = element;
            fireChanged();
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        return getArray().length;
    }


    @Transient
    @JsonIgnore
    public boolean isEmpty() {
        return size() == 0;
    }


    @JsonValue
    private Object[] getValue() {
        return getArray();
    }

    /**
     * 检查是否能保存下 nums的物品
     * <p>
     * 线程不安全，获取的值在多线程插入删除的情况不可靠 我们的游戏框架一般是单线程写，多线程读，所以在写线程还是
     *
     * @param nums
     * @return
     */
    public boolean check(int nums) {
        Object[] elements = getArray();
        int space = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                if (++space >= nums) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 线程不安全，获取的值在多线程插入删除的情况不可靠 我们的游戏框架一般是单线程写，多线程读，所以在写线程还是可靠的
     *
     * @return
     */
    public boolean check() {
        Object[] elements = getArray();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扩容，只加大不缩小
     *
     * @param size
     */
    public void extend(int size) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            if (size > elements.length) {
                setArray(Arrays.copyOf(elements, size));
                fireChanged();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        Object[] elements = getArray();
        return ArrayUtils.indexOf(elements, o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new COWIterator<E>(getArray(), 0);
    }

    @Override
    public Object[] toArray() {
        Object[] elements = getArray();
        return Arrays.copyOf(elements, elements.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Object[] elements = getArray();
        int len = elements.length;
        if (a.length < len)
            return (T[]) Arrays.copyOf(elements, len, a.getClass());
        else {
            System.arraycopy(elements, 0, a, 0, len);
            if (a.length > len)
                a[len] = null;
            return a;
        }
    }

    /**
     * 查找一个可以使用的位置插入,并不会扩充大小
     *
     * @return
     */
    @Override
    public boolean add(E e) {
        return addElement(e) > -1;
    }

    /**
     * @param e
     * @return 返回-1表示插入失败，返回其他值表示背包位置
     */
    public int addElement(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            for (int i = 0; i < len; i++) {
                if (elements[i] == null) {
                    elements[i] = e;
                    fireChanged();
                    return i;
                }
            }
        } finally {
            lock.unlock();
        }
        return -1;
    }

    @Override
    public boolean remove(Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                for (int i = 0; i < len; i++) {
                    if (eq(o, elements[i])) {
                        elements[i] = null;
                        fireChanged();
                        return true;
                    }
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        return set(index, null);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] cs = c.toArray();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int nums = 0;
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] == null) {
                    nums++;
                }
            }
            if (nums >= cs.length) {
                for (int i = 0, j = 0; i < elements.length && j < cs.length; i++) {
                    if (elements[i] == null) {
                        elements[i] = cs[j++];
                    }
                }
                fireChanged();
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                for (int i = 0; i < len; i++) {
                    elements[i] = null;
                }
            }
            fireChanged();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        return ArrayUtils.indexOf(array, o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return ArrayUtils.lastIndexOf(array, o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new COWIterator<E>(getArray(), 0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        Object[] elements = getArray();
        int len = elements.length;
        if (index < 0 || index > len)
            throw new IndexOutOfBoundsException("Index: " + index);

        return new COWIterator<E>(elements, index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private static boolean eq(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    private static class COWIterator<E> implements ListIterator<E> {
        /**
         * Snapshot of the array
         */
        private final Object[] snapshot;
        /**
         * Index of element to be returned by subsequent call to next.
         */
        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            return (E) snapshot[--cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         *
         * @throws UnsupportedOperationException always; <tt>remove</tt> is not supported by this
         *                                       iterator.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         *
         * @throws UnsupportedOperationException always; <tt>set</tt> is not supported by this iterator.
         */
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         *
         * @throws UnsupportedOperationException always; <tt>add</tt> is not supported by this iterator.
         */
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(getArray());
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public void addObserver(ConcurrentSingleArrayBagListener obs) {
        observers.add(obs);
    }

    private void fireChanged() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).onChanged();
        }
    }
}
