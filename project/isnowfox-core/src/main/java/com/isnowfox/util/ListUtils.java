package com.isnowfox.util;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class ListUtils {
    @SuppressWarnings("unchecked")
    public static final LinkedList EMPTY_LINKED_LIST = new LinkedList(){
        @Override
        public void addFirst(Object o) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public void addLast(Object o) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public boolean addAll(Collection c) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public boolean addAll(int index, Collection c) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public void add(int index, Object element) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public void push(Object o) {
            throw new java.lang.RuntimeException("不能修改 EMPTY_LINKED_LIST");
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final boolean isEmpty() {
            return true;
        }
    };

    @SuppressWarnings("unchecked")
    public static final <T> LinkedList<T> emptyLinkedList() {
        return (LinkedList<T>) EMPTY_LINKED_LIST;
    }

    public static <T>  T get(List<T> l,int index){
		return l.size()>index?l.get(index):null;
	}
	public static <T>  T get(T[] l,int index){
		return l.length>index?l[index]:null;
	}
}
