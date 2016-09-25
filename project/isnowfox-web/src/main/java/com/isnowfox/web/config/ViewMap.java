package com.isnowfox.web.config;

public class ViewMap {
	private Object c;
	private String value;
	
	public ViewMap() {
		
	}
	
	public ViewMap(Object c, String value) {
		super();
		this.c = c;
		this.value = value;
	}
	public Object getC() {
		return c;
	}
	public void setC(Object c) {
		this.c = c;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
