package com.isnowfox.core.dao;

import java.util.List;

import com.google.common.collect.Lists;

public class Order {
	private List<Item> items = Lists.newArrayList();
	
	private Order(){
		
	}
	
	public  static Order create(){
		return new Order();
	}
	public  static Order create(String name, boolean isDesc){
		Order p= new Order();
		p.add(name, isDesc);
		return p;
	}
	public  static Order createSingleton(final String name,final boolean isDesc){
		return new Order(){
			@Override
			public void toSql(StringBuilder sb) {
				sb.append(" ORDER BY ");
				sb.append('`');
				sb.append(name);
				sb.append('`');
				if (isDesc) {
					sb.append(" DESC");
				}else{
					sb.append(" ASC");
				}
			}
			@Override
			public void add(String name, boolean isDesc) {
				throw new  IllegalStateException("单例Order不能添加");
			}
		};
	}
	public void add(String name, boolean isDesc) {
		items.add(new Item(name, isDesc));
	}
	public String toSql() {
		StringBuilder sb=new StringBuilder();
		toSql(sb);
		return sb.toString();
	}
	public void toSql(StringBuilder sb) {
		if(items.isEmpty()){
			return ;
		}
		sb.append(" ORDER BY ");
		boolean fist=true;
		for(Item item:items){
			if(fist){
				fist=false;
			}else{
				sb.append(',');
			}
			sb.append('`');
			sb.append(item.name);
			sb.append('`');
			if (item.desc) {
				sb.append(" DESC");
			}else{
				sb.append(" ASC");
			}
		}
	}
	static class Item {
		Item(String name, boolean desc) {
			this.name = name;
			this.desc = desc;
		}

		String name;
		boolean desc;
	}
}
