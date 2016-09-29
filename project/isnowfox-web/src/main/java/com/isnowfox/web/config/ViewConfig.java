package com.isnowfox.web.config;

import com.isnowfox.web.ViewType;
import com.isnowfox.web.ViewTypeInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 支持下面几种 视图映射
 * <ul>
 * <ol>ONEY,这种无条件隐射</ol>
 * <ol>class 映射</ol>
 * <ol>{@linkcom.isnowfox.web.View} 对象 映射</ol>
 * <ol>el求值表达式 映射,$开头的表达式表示Action对象本身,其他表示返回对象</ol>
 * </ul>
 *
 * @author zuoge85
 * @see com.isnowfox.web.View
 */
public class ViewConfig {
    /**
     * 视图映射类型
     *
     * @author zuoge85
     */
    public enum MapType {
        /**
         * class映射类型
         */
        CLASS,
        /**
         * view映射类型
         */
        VIEW,
        ONLY,
        EL,
        NULL
    }

    private MapType mapType = MapType.NULL;
    private List<Item> list = new ArrayList<>();
    /**
     * 单个视图的时候使用
     */
    private ViewTypeInterface viewType;
    private String el;
    private String value;

    ViewConfig addClassMap(Class<?> cls, String value, ViewTypeInterface viewType) throws IllegalConfigException {
        check(MapType.CLASS);
        add(new Item(viewType, cls, null, value));
        return this;
    }

    ViewConfig addViewMap(String name, String value, ViewTypeInterface viewType) throws IllegalConfigException {
        if (mapType == MapType.VIEW) {
            check(MapType.VIEW);
            add(new Item(viewType, null, name, value));
        } else {
            check(MapType.EL);
            add(new Item(viewType, null, name, value));
        }
        return this;
    }

    ViewConfig el(String el) throws IllegalConfigException {
        check(MapType.EL);
        this.el = el;
        return this;
    }

    void Json() throws IllegalConfigException {
        viewType(ViewType.JSON, null);
    }

    void stream() throws IllegalConfigException {
        viewType(ViewType.STREAM, null);
    }

    void viewType(ViewTypeInterface viewType, String value) throws IllegalConfigException {
        check(MapType.ONLY);
        if (list.isEmpty() && this.viewType == null) {
            this.viewType = viewType;
            this.value = value;
        } else {
            throw new IllegalConfigException("单类型映射!不能设置多个");
        }
    }

    void add(Item i) {
        if (i.value.length() > 0) {
            if (i.value.charAt(0) != '/') {
                i.value = '/' + i.value;
            } else {
                i.value = i.value;
            }
        }
        list.add(i);
    }

    private void check(MapType type) throws IllegalConfigException {
        if (mapType == MapType.NULL || type == mapType) {
            mapType = type;
        } else {
            throw new IllegalConfigException("只能有一个类型的映射");
        }
    }

    public MapType getMapType() {
        return mapType;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(list);
    }

    public String getEl() {
        return el;
    }

    public ViewTypeInterface getViewType() {
        return viewType;
    }

    public String getValue() {
        return value;
    }

    private static class Item {
        public Item(ViewTypeInterface viewType, Class<?> cls, String key, String value) {
            this.viewType = viewType;
            this.cls = cls;
            this.key = key;
            this.value = value;
        }

        /**
         * 视图type
         */
        private ViewTypeInterface viewType;
        private Class<?> cls;
        private String key;
        private String value;
    }
}
