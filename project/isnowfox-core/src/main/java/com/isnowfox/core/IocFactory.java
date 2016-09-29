package com.isnowfox.core;

import java.util.Map;

/**
 * ioc装配器
 *
 * @author zuoge85
 */
public interface IocFactory {
    //void init(ServletContext sc);

    /**
     * 依赖注入
     *
     * @param o
     */
    void initBean(Object o);

    <T> T getBean(Class<T> cls);

    <T> T getBean(String name);

    <T> T getBean(String name, Class<T> cls);

    <T> Map<String, T> getBeansOfType(Class<T> cls);

    void destroy();
}
