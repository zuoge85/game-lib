package io.grass.core;

import com.isnowfox.util.RandomUtils;

import junit.framework.TestCase;

public class RandomTest extends TestCase{
	public void test(){
		for (int i = 0; i < 1000000; i++) {
			int r = RandomUtils.randInt(5, 15);
			assertTrue(r<=15&&r>=5);
		}
		for (int i = 0; i < 1000000; i++) {
			int r = RandomUtils.randInt(10);
			assertTrue(r<=10&&r>=0);
		}
	}
}
