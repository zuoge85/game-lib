package com.isnowfox.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * page 是1开始，第一页表示 1
 *
 * @param <T>
 */
public abstract class PageResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2686009887672236028L;

    public static final <T> PageResult<T> createPage(final int count, final int page, final int pageSize) {
        return createPage(count, page, pageSize, null);
    }

    public static final <T> PageResult<T> createPageByAllList(final int page, final int pageSize, final List<T> list) {
        int count = list.size();
        PageResult<T> result = createPage(count, page, pageSize, null);
        int start = result.getStart();
        if (start < count) {
            result.setValue(new ArrayList<T>(list.subList(result.getStart(), result.getStart() + Math.min(count - start, pageSize))));
        }
        return result;
    }

    public static final <T> ArrayList<T> createListByFromTo(final int from, final int to, final List<T> list) {
        int count = list.size();
        ArrayList<T> result = new ArrayList<T>();
        int start = (from - 1) > 0 ? (from - 1) : 0;
        int end = (to > count) ? (count - 1) : (to - 1);
        if (end >= start) {
            for (int i = start; i <= end; i++) {
                result.add(list.get(i));
            }
        }
        return result;
    }

    public static final <T> PageResult<T> createPage(final int count, final int page, final int pageSize, final List<T> list) {

        int m = count % pageSize;
        int s = count / pageSize;
        final int pageCount = m > 0 ? ++s : s;
        return new PageResult<T>() {
            /**
             *
             */
            private static final long serialVersionUID = 6863895606890210253L;
            private List<T> value = list;

            @Override
            public int getPageCount() {
                return pageCount;
            }

            @Override
            public void setValue(List<T> value) {
                this.value = value;
            }

            @Override
            public int getStart() {
                return (page - 1) * pageSize;
            }

            @Override
            public List<T> getValue() {
                return value;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public int getPage() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public boolean isHasPrev() {
                return page > 1;
            }

            @Override
            public boolean isHasNext() {
                return page < pageCount;
            }

            @Override
            public String toString() {
                return "PageResult [count=" + count + ", page=" + page + ", pageSize="
                        + pageSize + ", value=" + value + "]";
            }

        };
    }

    /**
     * 返回一共多少页
     *
     * @return
     */
    public abstract int getPageCount();

    /**
     * 返回开始位置
     *
     * @return
     */
    public abstract int getStart();

    public abstract List<T> getValue();

    public abstract void setValue(List<T> value);

    /**
     * 返记录集的总数
     *
     * @return
     */
    public abstract int getCount();


    public abstract int getPage();


    public abstract int getPageSize();


    public abstract boolean isHasPrev();

    public abstract boolean isHasNext();

    public abstract String toString();

}