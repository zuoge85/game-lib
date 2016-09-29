package com.isnowfox.el;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * el引擎
 *
 * @author zuoge85
 */
public class ElEngine {
    public static final boolean DEBUG = true;

    public static final ElEngine getInstance() {
        return Inner.instance;
    }

    private Map<Class<?>, Map<String, Expression<?>>> cacheMap = new HashMap<>();

    private AtomicInteger itemSeed = new AtomicInteger();

    /**
     * 不正确的返回类型会抛出  ClassCastException 运行时异常
     *
     * @param obj
     * @param el
     * @return
     * @throws UnknownKeyException
     * @throws ClassCastException
     */
    public <T> T el(Object obj, String el) throws UnknownKeyException {
        return this.<T, Expression<T>>compile(obj.getClass(), el).el(obj);
    }

    /**
     * 不正确的返回类型会抛出  ClassCastException 运行时异常
     *
     * @param cls
     * @param el
     * @return
     * @throws UnknownKeyException
     * @throws ClassCastException
     */
    @SuppressWarnings("unchecked")
    public final <T, C extends Expression<T>> C compile(Class<?> cls, String el) throws UnknownKeyException {
        if (cls == null) {
            throw new NullPointerException("null");
        }
        try {
            //	String cacheKey = cls.getName() + "-" + el;
            C p = (C) get(cls, el);
            if (p == null) {
                synchronized (this) {
                    p = (C) get(cls, el);
                    if (p == null) {
                        ClassPool pool = ClassPool.getDefault();
                        ClassBuilder builder = new ClassBuilder(this, pool, cls, el, itemSeed.incrementAndGet());
                        p = (C) builder.build();
                        put(cls, el, p);
                    }
                }
            }
            return p;
        } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final Expression<?> get(Class<?> cls, String el) {
        Map<String, Expression<?>> m = cacheMap.get(cls);
        if (m == null) {
            return null;
        }
        return m.get(el);
    }

    private final void put(Class<?> cls, String el, Expression<?> o) {
        Map<String, Expression<?>> m = cacheMap.get(cls);
        if (m == null) {
            m = new HashMap<>();
            cacheMap.put(cls, m);
        }
        m.put(el, o);
    }

//	private final String makeMethod(Class<?> cls, Object obj, String el) throws UnknownKeyException{
//		int varSeed = 0;
//		StringBuilder sb = new StringBuilder("{\n");
//		String[] keys = el.split("\\.");
//		sb.append(cls.getCanonicalName() + " var_" + (++varSeed) + " = (" +cls.getCanonicalName() + ")$1;\n" );
//		for(String key:keys){
//			KeyInfo info =analyse(cls,key);
//			int nextVar = varSeed++ ;
//			switch (info.getType()) {
//			case FIELD:
//				sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + key + ";\n");
//				break;
//			case PROPERTY:
//				sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + info.getMethodName() + "();\n");
//				break;
//			case METHOD:
//				sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + key + ";\n");
//				break;
//			default:
//				break;
//			}
//			cls = info.getCls();
//		}
//		if(cls.isPrimitive()){
//			cls = wrap(cls);
//			sb.append("return "+cls.getCanonicalName()+".valueOf(var_" + varSeed + ");}");
//		}else{
//			sb.append("return var_" + varSeed + ";\n}");
//		}
//		return sb.toString();
//	}


    private static class Inner {
        private static final ElEngine instance = new ElEngine();
    }
}
