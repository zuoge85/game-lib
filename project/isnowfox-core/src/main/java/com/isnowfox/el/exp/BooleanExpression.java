package com.isnowfox.el.exp;

import com.isnowfox.el.Expression;

public abstract class BooleanExpression implements Expression<Boolean> {
    public Boolean el(Object obj) {
        return booleanEl(obj);
    }

    public abstract boolean booleanEl(Object obj);
}
