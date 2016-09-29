package io.grass.util;

import com.isnowfox.core.junit.BaseTest;
import com.isnowfox.util.ArrayExpandUtils;

public class ArrayUtilsTest extends BaseTest {
    public void test() {
        assertTrue(ArrayExpandUtils.check(new Object[]{1}, Number.class));
    }
}
