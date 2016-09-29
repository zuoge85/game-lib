package com.isnowfox.web;


public class View {
    public static final String VIEW_DEFAULT_NAME = "default";

    private View() {
    }

    private View(Object value) {
        this.value = value;
    }

    private View(String name) {
        this.name = name;
    }

    private View(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static final View view() {
        return new View();
    }

    public static final View view(String name, Object value) {
        return new View(name, value);
    }

    public static final View view(String name) {
        return new View(name);
    }

    public static final View view(Object value) {
        return new View(value);
    }

    private String name = VIEW_DEFAULT_NAME;
    private Object value;

    public String getName() {
        return name;
    }

    public View setName(String name) {
        this.name = name;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public View setValue(Object value) {
        this.value = value;
        return this;
    }

}
