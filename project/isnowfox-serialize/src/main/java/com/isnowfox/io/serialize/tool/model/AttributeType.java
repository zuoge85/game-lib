package com.isnowfox.io.serialize.tool.model;

public enum AttributeType {
	INT, LONG, FLOAT, DOUBLE, BOOLEAN, STRING,BYTES, BYTE_BUF, OTHER;
	
	public static AttributeType to(String name){
		switch (name) {
		case "int":
			return INT;
		case "long":
			return LONG;
		case "float":
			return FLOAT;
		case "double":
			return DOUBLE;
		case "boolean":
			return BOOLEAN;
		case "String":
			return STRING;
		case "byte":
			return BYTES;
		case "ByteBuf":
			return BYTE_BUF;
		default:
			return OTHER;
		}
	}
}
