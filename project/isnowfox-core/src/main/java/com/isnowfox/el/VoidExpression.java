package com.isnowfox.el;

public abstract class VoidExpression implements Expression<Object> {
    public Object el(Object obj) {
        voidEl(obj);
        return null;
    }

    public abstract void voidEl(Object obj);
}
