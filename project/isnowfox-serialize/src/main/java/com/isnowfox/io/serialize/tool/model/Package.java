package com.isnowfox.io.serialize.tool.model;

import java.util.*;

/**
 * 包信息
 *
 * @author zuoge85
 */
public class Package {
    private Set<Message> sets = new TreeSet<>(new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            return o1.getName().compareTo(o2.getName());
        }
    });
    private Map<String, Message> map = new HashMap<>();

    private String name;
    private int type;


    public void add(Message msg) {
        msg.setPack(this);
        sets.add(msg);
        map.put(msg.getName(), msg);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstantName(int left) {
        String str = name.toUpperCase();
        return String.format("%-" + left + "s", str.replace('.', '_'));
    }

    public Set<Message> getValues() {
        return sets;
    }

    public Message get(String name) {
        return map.get(name);
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }
}
