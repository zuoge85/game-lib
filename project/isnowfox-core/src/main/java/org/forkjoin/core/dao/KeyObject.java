package org.forkjoin.core.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;

public interface KeyObject<T extends EntityObject, K extends KeyObject> {
    @JsonIgnore
    @Transient
    TableInfo<T, K> getTableInfo();

    @JsonIgnore
    @Transient
    Object[] getQueryParams();

    /**
     * 转换成字符串表达的key, 用于kv 数据库或者换成之内
     */
    String toStringKey();
}