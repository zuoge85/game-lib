package com.isnowfox.web.config;

import com.isnowfox.web.annotation.Header;
import com.isnowfox.web.annotation.HeaderGroup;

import java.util.ArrayList;
import java.util.List;

public class HeaderConfig {
    private List<Item> list = new ArrayList<>();

    public void add(String name, String value) {
        list.add(new Item(name, value));
    }

    public void add(Header header) {
        list.add(new Item(header.name(), header.value()));
    }

    public void add(HeaderGroup headerGroup) {
        for (Header header : headerGroup.value()) {
            add(header);
        }
    }

    public List<Item> getList() {
        return list;
    }

    public static class Item {
        public Item(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public final String name;
        public final String value;

        @Override
        public String toString() {
            return "Item [name=" + name + ", value=" + value + "]";
        }
    }

    @Override
    public String toString() {
        return "HeaderConfig [list=" + list + "]";
    }
}
