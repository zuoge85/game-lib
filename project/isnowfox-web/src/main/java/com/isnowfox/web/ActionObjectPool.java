package com.isnowfox.web;

import com.isnowfox.core.IocFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * action 对象池
 * 用于单例对象池
 *
 * @author zuoge85
 */
public class ActionObjectPool {
    private Map<Class<?>, Object> map;
    private IocFactory iocFactory;

    public ActionObjectPool(IocFactory iocFactory) {
        map = new HashMap<>();
        this.iocFactory = iocFactory;
    }

    public Object get(Class<?> cls) throws InstantiationException, IllegalAccessException {
        Object obj = map.get(cls);
        if (obj == null) {
            synchronized (this) {
                obj = map.get(cls);
                if (obj == null) {
                    obj = cls.newInstance();
                    if (iocFactory != null) {
                        iocFactory.initBean(obj);
                    }
                    map.put(cls, obj);
                }
            }
        }
        return obj;
    }
}
