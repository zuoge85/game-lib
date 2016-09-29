package com.isnowfox.el;

public abstract class ShortExpression implements Expression<Short> {
    public Short el(Object obj) {
        return shortEl(obj);
    }

    public abstract short shortEl(Object obj);
}
