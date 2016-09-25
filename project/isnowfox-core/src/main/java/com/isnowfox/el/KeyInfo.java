package com.isnowfox.el;

class KeyInfo {
	enum Type {
		FIELD,PROPERTY,METHOD
	}
	private Type type;
	private Class<?> cls;
	private String methodName;
	
	public KeyInfo(Type type, Class<?> cls) {
		this.type = type;
		this.cls = cls;
	}
	
	
	/**
	 * @param type
	 * @param cls
	 * @param methodName
	 */
	public KeyInfo(Type type, Class<?> cls, String methodName) {
		this.type = type;
		this.cls = cls;
		this.methodName = methodName;
	}


	Class<?> getCls() {
		return cls;
	}
	void setCls(Class<?> cls) {
		this.cls = cls;
	}
	Type getType() {
		return type;
	}
	void setType(Type type) {
		this.type = type;
	}
	void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	String getMethodName() {
		return methodName;
	}
	@Override
	public String toString() {
		return "KeyInfo [type=" + type + ", cls=" + cls + ", methodName="
				+ methodName + "]";
	}
}
