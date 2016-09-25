package com.isnowfox.el.exp;

import com.isnowfox.el.Expression;

public abstract class DoubleExpression implements Expression<Double> {
	public Double el(Object obj){
		return doubleEl(obj);
	}
	public abstract double doubleEl(Object obj);
}
