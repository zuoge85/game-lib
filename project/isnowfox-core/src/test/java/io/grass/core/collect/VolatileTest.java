package io.grass.core.collect;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.Unsafe;

/**
 * jvm参数 -server -verbose:gc -Xms1024m -Xmx1536m
 * 
 * @author zuoge85
 * 
 */
public class VolatileTest {
	private static final int THREAD_NUMS = 4;
	private static final long NUMS = 10000000000000000l;

	public static void main(String... args) {
		try {
			test();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static long value = 0;
	private static AtomicInteger atomicValue = new AtomicInteger();
	private static final long[] values = {0x3333333333333333l,0x4444444444444444l,0x5555555555555555l,0x6666666666666666l};
	private static void test() throws InterruptedException {
//		Thread[] ts = new Thread[THREAD_NUMS];
//		for (int i = 0; i < ts.length; i++) {
//			final int n = i;
//			ts[i] = new Thread() {
//				public void run() {
//					for (long j = 0; j < NUMS; j++) {
//						value = values[n];
//					}
//				}
//			};
//		}
//		Thread readThread = new Thread(){
//			public void run() {
//				while(true){
//					long v = value;
//					if(Arrays.binarySearch(values, value) >=0){
//
//					}else{
//						System.out.println("error:"+Long.toHexString(v));
//					}
//				}
//			}
//		};
//		for (int i = 0; i < ts.length; i++) {
//			ts[i].start();
//		}
//		readThread.start();
//		for (int i = 0; i < ts.length; i++) {
//			ts[i].join();
//		}
//		readThread.stop();
//		readThread.join();
//		System.out.println(value);
//		System.out.println(atomicValue);
	}
}
