package io.grass.core.collect;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.isnowfox.core.Execute;
import com.isnowfox.core.tool.PerfTestTool;
import com.isnowfox.util.StringExpandUtils;

import net.sf.cglib.reflect.FastClass;

public class LockTest {

	private static final int THREAD_NUMS = 1;
	private static final long NUMS = 1000000000l;
	
	
	public static void main(String[] args) throws Exception {
		PerfTestTool tool = PerfTestTool.getInstance();
		tool.init(LockTest.class);
		
		LockTest lt= new LockTest();
		for (int i = 0; i < 2; i++) {
			System.out.println(StringExpandUtils.center("test", 80, '*'));
			tool.test(lt, "testSynchronized");
			tool.test(lt, "testAtomicLong");
			tool.test(lt, "testAtomicBooleanLockLong");
			tool.test(lt, "testLockLong");
			tool.test(lt, "testWarpLong");
		}
		tool.test(new Execute<Object>() {
			@Override
			public Object exe() {
				
				return null;
			}

			@Override
			public Object getResult() {
				return null;
			}
			@Override
			public String toString() {
				return "SyncLong";
			}
		}, THREAD_NUMS, "SyncLong");
	}
	
	public static void test(FastClass cls,Object o,String name) {
		
	}

	public long testSynchronized() throws InterruptedException{
		final SyncLong l = new SyncLong();
		Thread[] ts = new Thread[THREAD_NUMS];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					for (long j = 0; j < NUMS; j++) {
						l.incrementAndGet();
					}
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		return l.get();
	}

	public long testAtomicLong() throws InterruptedException{
		Thread[] ts = new Thread[THREAD_NUMS];
		final AtomicInteger l = new AtomicInteger();
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					for (long j = 0; j < NUMS; j++) {
						l.incrementAndGet();
					}
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		return l.get();
	}
	
	public long testAtomicBooleanLockLong() throws InterruptedException{
		final AtomicBooleanLockLong l = new AtomicBooleanLockLong();
		Thread[] ts = new Thread[THREAD_NUMS];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					for (long j = 0; j < NUMS; j++) {
						l.incrementAndGet();
					}
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		return l.get();
	}
	
	
	public long testLockLong() throws InterruptedException{
		final LockLong l = new LockLong();
		Thread[] ts = new Thread[THREAD_NUMS];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					for (long j = 0; j < NUMS; j++) {
						l.incrementAndGet();
					}
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		return l.get();
	}
	public long testWarpLong() throws InterruptedException{
		final WarpLong l = new WarpLong();
		Thread[] ts = new Thread[THREAD_NUMS];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new Thread() {
				public void run() {
					for (long j = 0; j < NUMS; j++) {
						l.incrementAndGet();
					}
				}
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		return l.get();
	}
	
	
	
	
	public static class SyncLong{
		private  long value;
		public final synchronized void  incrementAndGet(){
				value++;
		}
		public long get(){
			return value;
		}
	}
	
	public static class AtomicBooleanLockLong{
		private long value;
		private AtomicBoolean lock = new AtomicBoolean(false);
		public final void incrementAndGet(){
			while(true){
				if(lock.compareAndSet(false, true)){
					value++;
					lock.set(false);
					break;
				}else{
					Thread.yield();
				}
			}
		}
		public long get(){
			return value;
		}
	}
	
	public static class LockLong{
		private final ReentrantLock lock = new ReentrantLock();
		private long value;
		public final void incrementAndGet(){
			try{
				lock.lock();
				value++;
			}finally{
				lock.unlock();
			}
		}
		public long get(){
			return value;
		}
	}
	public static class WarpLong{
		private long value;
		public final void incrementAndGet(){
			value++;
		}
		public long get(){
			return value;
		}
	}
	
}
