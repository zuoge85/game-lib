package com.isnowfox.el.exp;

import com.isnowfox.el.Expression;

public abstract class ByteExpression implements Expression<Byte> {
	public Byte el(Object obj){
		return byteEl(obj);
	}
	public abstract byte byteEl(Object obj);
}
