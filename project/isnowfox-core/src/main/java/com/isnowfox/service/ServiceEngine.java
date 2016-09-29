package com.isnowfox.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author zuoge85 on 2014/12/14.
 */
public class ServiceEngine {
    public static final boolean DEBUG = true;

    private HashMap<Class, HashMap<Class, BaseService>> cacheMap = new HashMap<>();
    //    private HashMap<Object, > proxyClassMap = new HashMap<>();
    private ArrayList<BaseService> instanceList = new ArrayList<>();

    public <T> T get(Class<T> interfaceCls, Class impiCls) throws ServiceException {
        T t = get(interfaceCls, impiCls);
        if (t == null) {
            t = create(interfaceCls, impiCls);
        }
        return t;
    }

    private <T> T create(Class<T> interfaceCls, Class impiCls) throws ServiceException {
        ClassBuilder builder = new ClassBuilder(this, interfaceCls, impiCls);
        return null;
    }


    @SuppressWarnings("unchecked")
    private <T> T getInstance(Class<T> interfaceCls, Class impiCls) {
        HashMap<Class, BaseService> map = cacheMap.get(interfaceCls);
        if (map != null) {
            return (T) map.get(impiCls);
        }
        return null;
    }
}
