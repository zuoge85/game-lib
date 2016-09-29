package com.isnowfox.el.exp;

import com.isnowfox.el.Expression;

public abstract class CharExpression implements Expression<Character> {
    public Character el(Object obj) {
        return charEl(obj);
    }

    public abstract char charEl(Object obj);
}
