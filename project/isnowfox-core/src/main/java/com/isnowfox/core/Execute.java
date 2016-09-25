package com.isnowfox.core;

/**
 * 类似闭包的执行接口
 * @author zuoge85
 *
 * @param <T>
 */
public interface Execute<T> {
	T exe();
	Object getResult();
}
