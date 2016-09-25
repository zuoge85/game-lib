package com.isnowfox.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author zuoge85 on 2014/9/21.
 */
public final class ObjectUtils {

    public static final int checkInt(Object obj, int defaultValue) {
        if (obj == null || !(obj instanceof Integer)) {
            return defaultValue;
        }
        return (Integer) obj;
    }

    public static final boolean check(Object obj, Class<?>... clses) {
        if (obj == null) {
            return false;
        }
        if (clses.length == 1) {
            return clses[0].isInstance(obj);
        }
        if (obj instanceof Object[]) {
            Object[] objs = (Object[]) obj;
            if (objs.length == clses.length) {
                for (int i = 0; i < clses.length; i++) {
                    if (!clses[i].isInstance(objs[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static int toInt(Object ret) {
        if(ret == null){
            return -1;
        }
        if(ret instanceof Number) {
            return ((Number)ret).intValue();
        } else {
            return NumberUtils.toInt(String.valueOf(ret));
        }
    }
}
