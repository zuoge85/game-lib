package com.isnowfox.util;

import java.io.IOException;

public final class StackTraceUtils {

    /**
     * 这个函数可以尝试获取指定深度的堆栈休息
     *
     * @param i 深度0表示调用函数的位置
     * @return
     * @throws IOException
     */
    public static String getBaseStackInfo(int i) {
        Throwable t = new Throwable();
        StackTraceElement[] array = t.getStackTrace();
        i += 1;
        if (i < array.length) {
            //oc.game.CodeLineTest.test(CodeLineTest.java:7)
            return String.valueOf(array[i]);
        } else {
            return "no StackInfo";
        }
    }

    /**
     * @param i    深度0表示调用函数的位置
     * @param nums 堆栈条数
     * @return
     */
    public static CharSequence getBaseStackInfo(int i, int nums) {
        Throwable t = new Throwable();
        StackTraceElement[] array = t.getStackTrace();
        i += 1;
        if (i < array.length) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0, index = i; j < nums && index < array.length; j++, index++) {
                if (j > 0) {
                    sb.append("\n");
                }
                sb.append(array[index]);
            }
            return sb;
        } else {
            return "no StackInfo";
        }
    }
}
