package com.isnowfox.core.io;

import com.isnowfox.core.junit.BaseTest;

public class Bast extends BaseTest {
    public void test() {
        System.out.println(Double.doubleToLongBits(-1));
        System.out.println(Long.toHexString(0b11111111111111111111111111111111111111111111111111111l >> 32));
        System.out.println(Long.toBinaryString(0b11111111111111111111111111111111111111111111111111111l).length());

        System.out.println(0b11111111111111111111111111111111111111111111111111111l);
        System.out.println((int) 0b11111111111111111111111111111111111111111111111111111l);
        System.out.println(Long.toBinaryString(Long.MAX_VALUE).length());
//		System.out.println(Long.MAX_VALUE);
//		System.out.println(-Long.MAX_VALUE);
    }
}
