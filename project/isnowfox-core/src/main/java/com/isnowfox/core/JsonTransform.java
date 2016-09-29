package com.isnowfox.core;

/**
 * 实现这个接口的对象表示支持转换成json
 *
 * @author zuoge85@gmail.com
 */
public interface JsonTransform {
    void toJson(Appendable appendable);
}
