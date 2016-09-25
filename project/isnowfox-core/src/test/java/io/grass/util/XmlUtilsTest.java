package io.grass.util;


import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
import com.isnowfox.core.junit.BaseTest;
import com.isnowfox.util.JsonUtils;
import com.isnowfox.util.XmlUtils;

public class XmlUtilsTest extends BaseTest{
	public void test() throws IOException{
		List<MyClass> list = Lists.newCopyOnWriteArrayList();
		MyClass c = new MyClass();
		c.name = "测试";
		list.add(c);
		list.add(c);
		MyWrap w = new MyWrap();
		w.name = "MyWrap";
		w.list1 = list;
		//
		String xml = XmlUtils.serialize(list);
		System.out.println(xml);
		List<MyClass> list1 = XmlUtils.deserialize(xml);
		
		System.out.println(list1);
		//CopyOnWriteArrayList 
		c.ssss.add(new Date());
		//c.abc = new Abc();
		MyClass[] mc = new MyClass[]{c,c,c};
		System.out.println(Arrays.toString(mc));
		String json =JsonUtils.serialize(mc);
		System.out.println(json);
		MyClass[] mc1 = JsonUtils.deserialize(json, MyClass[].class);
		System.out.println(Arrays.toString(mc1));
		
		
	}
	public static class MyWrap{
		private List<MyClass> list1;
		public String name;
		public List<MyClass> getList1() {
			return list1;
		}

		public void setList1(List<MyClass> list1) {
			this.list1 = list1;
		}

		@Override
		public String toString() {
			return "MyWrap [list1=" + list1 + ", name=" + name + "]";
		}
		
	}
	public static class MyClass{
		public Class<MyClass> cls = MyClass.class;
		private int iii;
		public long lll = Long.MAX_VALUE;
		private String name;
		public boolean mmm;
		public Object abc;
		public Set<Date> ssss = new TreeSet<Date>();
		public int getIii() {
			return iii;
		}
		public void setIii(int iii) {
			this.iii = iii;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		@Override
		public String toString() {
			return "MyClass [iii=" + iii + ", name=" + name + "]";
		}
		
	}
	public static class Abc{
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
