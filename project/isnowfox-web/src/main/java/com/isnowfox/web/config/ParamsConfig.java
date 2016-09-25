package com.isnowfox.web.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.isnowfox.web.ParameterType;

public class ParamsConfig {
	private List<Item> list = new ArrayList<>();
	
	void add(Class<?> cls, ParameterType type ,String name){
		list.add(new Item(type, name, cls));
	}

//	public void fillArgs(Appendable sb) {
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				Item item = list.get(i);
//				sb.append(item.cls.getCanonicalName());
//			}
//		}catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
	public List<Item> getList() {
		return Collections.unmodifiableList(list);
	}
	
	public static class Item{
		/**
		 * @param type
		 * @param name
		 * @param cls
		 */
		public Item(ParameterType type, String name, Class<?> cls) {
			this.type = type;
			this.name = name;
			this.cls = cls;
		}
		
		private ParameterType type;
		private String name;
		private Class<?> cls;
		public ParameterType getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public Class<?> getCls() {
			return cls;
		}
		
	}
}
