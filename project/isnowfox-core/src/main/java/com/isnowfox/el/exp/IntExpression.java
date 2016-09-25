package com.isnowfox.el.exp;

import com.isnowfox.el.Expression;

public abstract class IntExpression implements Expression<Integer> {
	public Integer el(Object obj){
		return intEl(obj);
	}
	public abstract int intEl(Object obj);
}
