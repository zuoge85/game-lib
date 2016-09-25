package com.isnowfox.util.collect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.collections.set.ListOrderedSet;

/**
 * FIFO 先进先出队列,
 * 这个队列有点特殊,
 * 插入重复的项,会把重复插入项拉到队列前端
 * @author zuoge85@gmail.com
 *
 */
public class MaxSetQueue<T>  implements Iterable<T>{
	private ListOrderedSet set ;
	private int max;
	public MaxSetQueue(int max){
		this.max = max;
		set = ListOrderedSet.decorate(new HashSet<>(max),new ArrayList<>(max));
	}
	
	public void add(T e){
		//queue.add(e);
		if(set.size()==max){
			set.remove(set.size()-1);
		}
		if(set.contains(e)){
			set.remove(e);
		}
		set.add(0, e);
	}
	public void remove(T e){
		set.remove(e);
	}
	public boolean contains(T e){
		return set.contains(e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return set.iterator();
	}
	
	public int size(){
		return set.size();
	}
}
