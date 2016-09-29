package com.isnowfox.util.collect;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于数组的背包实现
 * <p>
 * 可以堆叠
 * <p>
 * <p>
 * <p>
 * 实现参考 CopyOnWriteArrayList，但是放弃了一些一致性
 * <p>
 * 保存的时候可能发生线程安全问题！
 * <p>
 * 关于线程安全，这个实现只合适多线程读，单线程写的情况！，请小心
 *
 * @author zuoge85
 * @see CopyOnWriteArrayList
 */
public class ConcurrentArrayBag<E extends CoreGoods> {

    transient final ReentrantLock lock = new ReentrantLock();

    private List<ConcurrentArrayBagListener<E>> observers = new ArrayList<>();

    /** The lock protecting all mutators */
    /**
     * The array, accessed only via getArray/setArray.
     */
    private volatile transient CoreGoods[] array;

    public ConcurrentArrayBag() {
        this(new CoreGoods[0]);
    }

    public ConcurrentArrayBag(int size) {
        this(new CoreGoods[size]);
    }

    @JsonCreator
    protected ConcurrentArrayBag(CoreGoods[] value) {
        setArray(value);
    }

    /**
     * Gets the array. Non-private so as to also be accessible from
     * CopyOnWriteArraySet class.
     */
    public final CoreGoods[] getArray() {
        return array;
    }

    public CoreGoods[] toCopyArray() {
        CoreGoods[] array = getArray();
        return Arrays.copyOf(array, array.length);
    }

    /**
     * Sets the array.
     */
    public final void setArray(CoreGoods[] a) {
        array = a;
    }


    @SuppressWarnings("unchecked")
    final E get(Object[] a, int index) {
        return (E) a[index];
    }

    @SuppressWarnings("unchecked")
    protected E create(int id) {
        return (E) new CoreGoods(id);
    }

    public E get(int index) {
        return get(getArray(), index);
    }

    public int size() {
        return getArray().length;
    }

    public boolean isCanSet(int index, CoreGoods e) {
        //检查此位置是否能放下这个物品,需要重载
        return true;
    }

    public boolean isCanSet(int index, int itemId) {
        //检查此位置是否能放下这个物品,需要重载
        return true;
    }

    public boolean isCanPile(CoreGoods e, int id, int level, int quality) {
        return e.getId() == id && e.getLevel() == level && e.getGrade() == quality;
    }

    @JsonValue
    private Object[] getValue() {
        return getArray();
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
            CoreGoods[] elements = getArray();
            if (size > elements.length) {
                setArray(Arrays.copyOf(elements, size));
                fireChanged();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查是否有足够的物品够使用
     *
     * @param id
     * @param nums
     * @return
     */
    public boolean checkDeduct(int id, int nums) {
        final CoreGoods[] elements = getArray();
        int len = elements.length;
        int hasNums = 0;
        for (int i = 0; i < len && hasNums < nums; i++) {
            CoreGoods ele = elements[i];
            if (ele != null && ele.getId() == id) {
                hasNums += ele.getNums();
            }
        }
        if (hasNums >= nums) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否有住够的空间
     *
     * @param id
     * @param nums
     * @param pileNumsMax
     * @return
     */
    public boolean checkSpace(int id, int nums, int pileNumsMax) {
        final CoreGoods[] elements = getArray();
        int len = elements.length;
        int freeNums = 0;
        for (int i = 0; i < len && freeNums < nums; i++) {
            CoreGoods ele = elements[i];
            if (ele == null) {
                freeNums += pileNumsMax;
            } else if (ele.getId() == id) {
                int fn = pileNumsMax - ele.getNums();
                if (fn > 0) {
                    freeNums += fn;
                }
            }
        }
        if (freeNums >= nums) {
            return true;
        }
        return false;
    }


    /**
     * @param id
     * @return 返回空余空间
     */
    public int getNums(int id) {
        final CoreGoods[] elements = getArray();
        int len = elements.length;
        int hasNums = 0;
        for (int i = 0; i < len; i++) {
            CoreGoods ele = elements[i];
            if (ele != null && ele.getId() == id) {
                hasNums += ele.getNums();
            }
        }
        return hasNums;
    }


    /**
     * 交换背包2个位置
     *
     * @param index0
     * @param index1
     * @return
     */
    public boolean swap(int index0, int index1) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final CoreGoods[] elements = getArray();
            if (index0 < 0 || index0 >= elements.length || index1 < 0 || index1 >= elements.length) {
                return false;
            }
            CoreGoods e = elements[index0];
            elements[index0] = elements[index1];
            elements[index1] = e;
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 移除制定位置的物品，
     *
     * @param index0
     * @return 返回移除的物品，如果位置没有物品或者index0超出范围，那么返回null
     */
    public CoreGoods remove(int index0) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final CoreGoods[] elements = getArray();
            if (index0 < 0 || index0 >= elements.length) {
                return null;
            }
            CoreGoods e = elements[index0];
            elements[index0] = null;
            return e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置制定的位置
     *
     * @param index0
     * @param goods
     * @return 如果制定位置已经有物品了，那么返回这个物品
     */
    public CoreGoods set(int index0, CoreGoods goods) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final CoreGoods[] elements = getArray();
            if (index0 < 0 || index0 >= elements.length) {
                return null;
            }
            CoreGoods e = elements[index0];
            elements[index0] = goods;
            return e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param id
     * @return 返回是否成功
     */
    protected TIntArrayList checkAndAppend(int id, int nums, int level, int quality, int pileNumsMax, int type) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final CoreGoods[] elements = getArray();
            int len = elements.length;
            if (!checkSpace(id, nums, pileNumsMax)) {
                return null;
            }
            TIntArrayList destPos = new TIntArrayList();
            for (int i = 0; i < len && nums > 0; i++) {
                @SuppressWarnings("unchecked")
                E ele = (E) elements[i];
                if (!isCanSet(i, id)) {
                    continue;
                }
                if (ele == null) {
                    ele = create(id);
                    ele.setNums(Math.min(nums, pileNumsMax));
                    ele.setGrade(quality);
                    ele.setLevel(level);
                    nums -= ele.getNums();
                    elements[i] = ele;
                    fireChangedItem(ele, i);
                    destPos.add(i);
                } else if (isCanPile(ele, id, level, quality)) {
                    int freeNum = pileNumsMax - ele.getNums();
                    if (freeNum > 0) {
                        int n = Math.min(Math.min(freeNum, nums), pileNumsMax);
                        ele.setNums(ele.getNums() + n);
                        nums -= n;
                        fireChangedItem(ele, i);
                        destPos.add(i);
                    }
                }
            }
            return destPos;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param id
     * @return 返回是否成功
     */
    public boolean checkAndDeduct(int id, int nums) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final CoreGoods[] elements = getArray();
            int len = elements.length;
            if (!checkDeduct(id, nums)) {
                return false;
            }
            for (int i = 0; i < len && nums > 0; i++) {
                @SuppressWarnings("unchecked")
                E ele = (E) elements[i];
                if (ele != null && ele.getId() == id) {
                    int n = Math.min(ele.getNums(), nums);
                    nums -= n;
                    ele.setNums(ele.getNums() - n);
                    if (ele.getNums() <= 0) {
                        elements[i] = null;
                        ele = null;
                    }
                    fireChangedItem(ele, i);
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void addListener(ConcurrentArrayBagListener<E> obs) {
        observers.add(obs);
    }

    public void fireChanged() {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).onChanged();
        }
    }

    public void fireChangedItem(E e, int index) {
        for (int i = 0; i < observers.size(); i++) {
            ConcurrentArrayBagListener<E> listener = observers.get(i);
            listener.onChangedItem(e, index);
//			listener.onChanged();
        }
    }

    @Override
    public String toString() {
        return "ConcurrentArrayBag [array=" + Arrays.toString(array) + "]";
    }
}
