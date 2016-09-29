package io.grass.util;

import com.isnowfox.core.junit.BaseTest;
import com.isnowfox.util.BitUtils;


public class BitTest extends BaseTest {

    public void test() {
        int i = 0;
        i = BitUtils.set(i, 2);
        i = BitUtils.set(i, 3);
        System.out.println(Integer.toBinaryString(2));
    }

}
