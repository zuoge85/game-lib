package com.isnowfox.util;

import java.text.NumberFormat;

/**
 * 测试时间
 * 
 * @author zuoge85
 * 
 */
public final class TimeSpan {
	public TimeSpan(){
		nf.setParseIntegerOnly(false);
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		start();
		//avgTime = System.nanoTime();
	}
	private long time;
	//private long avgTime;
	public TimeSpan start() {
		time = System.nanoTime();
		return this;
	}
	private NumberFormat nf = NumberFormat.getNumberInstance();
	public String end() {
		return nf.format((System.nanoTime() - time) / 1000000000d);
	}
	public double getSecond() {
		return (System.nanoTime() - time) / 1000000000d;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nf.format((System.nanoTime() - time) / 1000000000d);
	}
//	/**
//	 * 平均时间
//	 * @param i
//	 * @return
//	 */
//	public String avg(int i) {
//		return nf.format((System.nanoTime() - avgTime)/i / 1000000000d);
//	}
}