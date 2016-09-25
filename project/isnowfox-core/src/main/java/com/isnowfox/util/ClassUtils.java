package com.isnowfox.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class ClassUtils {
    public static Class<?> wrap(Class<?> cls) {
        return PrimitivesWrapInner.map.get(cls);
    }

    public static Class<?> unwrap(Class<?> cls) {
        return WrapPrimitivesWrapInner.map.get(cls);
    }

    public static boolean isWrap(Class<?> cls) {
        return WrapPrimitivesWrapInner.map.get(cls) != null;
    }

    /**
     * 是否是void和Void包装类型
     *
     * @param cls
     * @return
     */
    public static boolean isVoid(Class<?> cls) {
        return cls == void.class || cls == Void.class;
    }

    private static class PrimitivesWrapInner {
        private static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(16);

        static {
            map.put(boolean.class, Boolean.class);
            map.put(byte.class, Byte.class);
            map.put(char.class, Character.class);
            map.put(double.class, Double.class);
            map.put(float.class, Float.class);
            map.put(int.class, Integer.class);
            map.put(long.class, Long.class);
            map.put(short.class, Short.class);
            map.put(void.class, Void.class);
        }
    }

    private static class WrapPrimitivesWrapInner {
        private static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(16);

        static {
            map.put(Boolean.class, boolean.class);
            map.put(Byte.class, byte.class);
            map.put(Character.class, char.class);
            map.put(Double.class, double.class);
            map.put(Float.class, float.class);
            map.put(Integer.class, int.class);
            map.put(Long.class, long.class);
            map.put(Short.class, short.class);
            map.put(Void.class, void.class);
        }
    }


    /**
     * 返回class的标准名称！
     * @param parameterType
     * @return
     */
    public static String getClassName(Class<?> parameterType) {
        if (parameterType.isArray()) {
            return getClassName(parameterType.getComponentType()) + "[]";
        }
        return parameterType.getCanonicalName();
    }
}
