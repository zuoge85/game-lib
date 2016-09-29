package com.isnowfox.web;

import com.google.common.collect.Maps;
import com.isnowfox.core.bean.Attributes;

import java.util.Map;
import java.util.Set;

/**
 * maxInactiveInterval 最大有效时间,默认20分钟,单位为秒
 *
 * @author zuoge85@gmail.com
 */
public class HttpSession implements Attributes {
    private Map<String, Object> attributesMap = Maps.newConcurrentMap();
    private int maxInactiveInterval = 20 * 60;
    //private long createTime;

    private HttpSession() {

    }

    @Override
    public void removeAttribute(Object name) {
        attributesMap.remove(name);
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributesMap.put(name, object);
    }

    @Override
    public Object getAttribute(String name) {
        return attributesMap.get(name);
    }

    @Override
    public Map<String, Object> getAttributesMap() {
        return attributesMap;
    }

    @Override
    public Set<String> getAttributeNames() {
        return attributesMap.keySet();
    }

    /**
     * maxInactiveInterval 最大有效时间,默认20分钟,单位为秒
     *
     * @param maxInactiveInterval
     */
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /**
     * maxInactiveInterval 最大有效时间,默认20分钟,单位为秒
     *
     * @return
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }
}
