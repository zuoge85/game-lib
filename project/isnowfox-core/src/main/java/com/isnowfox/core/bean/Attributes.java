package com.isnowfox.core.bean;

import java.util.Map;
import java.util.Set;

public interface Attributes {

	public abstract void removeAttribute(Object name);

	public abstract void setAttribute(String name, Object object);

	public abstract Object getAttribute(String name);

	public abstract Map<String, Object> getAttributesMap();

	public abstract Set<String> getAttributeNames();

}