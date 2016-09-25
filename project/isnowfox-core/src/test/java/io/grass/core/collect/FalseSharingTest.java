package io.grass.core.collect;

import com.isnowfox.util.StringExpandUtils;
import com.isnowfox.util.TimeSpan;



/**
 * jvm参数 !-server -verbose:gc -Xms1024m -Xmx1536m
 * 注意gc对垃圾回收的影响
 * @author zuoge85
 *
 */
public class FalseSharingTest {
	private static final int TEST_NUMS = 100000000;
	private static final int THREAD_NUMS = 4;

	public static void main(String[] args) throws InterruptedException {
		//测试伪装共享
		test1();
		System.out.println(StringExpandUtils.center("sb", 90, '*'));
		test2();
	}
	static LongWarp[] longs = new LongWarp[THREAD_NUMS];
	static LongWarpPadded[] longPaddeds = new LongWarpPadded[THREAD_NUMS];
	static{
		for (int j = 0; j < THREAD_NUMS; j++) {
			longs[j] = new LongWarp();
		}
		for (int j = 0; j < THREAD_NUMS; j++) {
			longPaddeds[j] = new LongWarpPadded();
		}
	}
	public static void test1() throws InterruptedException{
		Thread[] ts=new Thread[THREAD_NUMS];
		
		for (int i = 0; i < ts.length; i++) {
			final int n = i;
			ts[i] = new Thread(){
				 public void run() {
					 TimeSpan ts = new TimeSpan();
					for (int j = 0; j < TEST_NUMS; j++) {
						 longs[n].setI(longs[n].getI()+1);
					}
					String end = ts.end();
					System.out.println("Thread"+n+":\t"+end+";end value:"+ longs[n]);
		         }
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
	}
	
	public static void test2() throws InterruptedException{
		Thread[] ts=new Thread[THREAD_NUMS];
		//final int[] ints = new int[THREAD_NUMS*1000];
		for (int i = 0; i < ts.length; i++) {
			final int n = i;
			ts[i] = new Thread(){
				 public void run() {
					TimeSpan ts = new TimeSpan();
					for (int j = 0; j < TEST_NUMS; j++) {
						longPaddeds[n].setI(longPaddeds[n].getI()+1);
					}
					String end = ts.end();
					System.out.println("Thread"+n+":\t"+end+";end value:"+ longPaddeds[n]);
		         }
			};
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
	}
	public static class LongWarp{
		private volatile long i;

		public long getI() {
			return i;
		}

		public void setI(long i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return "LongWarp [i=" + i + "]";
		}
	}
	 public static long sumPaddingToPreventOptimisation(final int index)
	 {
		 LongWarpPadded v = longPaddeds[index];
	      return v.p1 + v.p2 + v.p3 + v.p4 + v.p5 + v.p6;
	}

	public static class LongWarpPadded{
		private volatile long i;
		public  long p1, p2, p3, p4, p5, p6 = 7L;
		public long getI() {
			return i;
		}

		public void setI(long i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return "LongWarpPadded [i=" + i + ", p1=" + p1 + ", p2=" + p2
					+ ", p3=" + p3 + ", p4=" + p4 + ", p5=" + p5 + ", p6=" + p6
					+ "]";
		}
		
	}
}
