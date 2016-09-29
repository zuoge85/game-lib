package org.forkjoin.core.dao;

import com.google.common.base.Function;

import java.util.Arrays;

/**
 * 唯一键信息
 */
public class UniqueInfo {
    private String name;
    private String[] fields;
    private Function getFunction;

    public UniqueInfo() {
    }

    public UniqueInfo(String name, String... fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "UniqueInfo{" +
                "name='" + name + '\'' +
                ", fields=" + Arrays.toString(fields) +
                '}';
    }
}
