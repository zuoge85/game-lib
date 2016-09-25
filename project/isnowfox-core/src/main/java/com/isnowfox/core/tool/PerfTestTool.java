package com.isnowfox.core.tool;


import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.isnowfox.core.Execute;
import com.isnowfox.util.TimeSpan;

public class PerfTestTool {
	protected static final Logger log = LoggerFactory.getLogger(PerfTestTool.class);
	
	private static final PerfTestTool instance = new PerfTestTool();
	public static final PerfTestTool getInstance(){
		return instance;
	}
	private Map<Class<?>,FastClass> map = Maps.newHashMap();
	private PerfTestTool(){};
	
	public void init(Class<?> cls){
		FastClass fc = FastClass.create(cls);
		map.put(cls, fc);
	}
	
	public void test(Object obj, String func, Object ...args) throws Exception{
		FastClass fc = map.get(obj.getClass());
		FastMethod fm = fc.getMethod(func, getClassArray(args));
		System.gc();
		TimeSpan ts = new TimeSpan();
		ts.start();
		Object o = fm.invoke(obj, args);
		String second = ts.end();
		log.info(String.format("Test %-30s\t:%s\t result:%s", func,second,o));
	}
	
	public void test(final Execute<?> execute,int threadNums,String name) throws Exception{
		Thread[] ts = new Thread[threadNums];
		final CyclicBarrier cb = new CyclicBarrier(threadNums+1);
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					try {
						cb.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						log.error(e.getMessage(),e);
					}
					execute.exe();
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		TimeSpan time = new TimeSpan();
		cb.await();
		time.start();
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		String second = time.end();
		log.info(String.format("Test %-30s\t:%s\t result:%s,Thread:%s", name, second, execute.getResult(), threadNums));
	}
	
	private Class<?>[] getClassArray(Object ...args){
		Class<?>[] clss = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			clss[i] = args[i].getClass();
		}
		return clss;
	}
}
