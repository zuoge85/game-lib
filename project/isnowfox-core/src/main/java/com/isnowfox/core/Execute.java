package com.isnowfox.core;

/**
 * 类似闭包的执行接口
 *
 * @param <T>
 * @author zuoge85
 */
public interface Execute<T> {
    T exe();

    Object getResult();
}
