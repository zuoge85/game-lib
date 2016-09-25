package com.isnowfox.web.config;

import com.isnowfox.web.CacheType;
import com.isnowfox.web.annotation.Cache;

public class CacheConfig {
	private CacheType type;
	private String value;
	
	public CacheConfig(Cache cache) {
		this.type = cache.type();
		this.value =cache.value();
	}
	
	public CacheConfig(CacheType type, String value) {
		this.type = type;
		this.value = value;
	}
	public CacheType getType() {
		return type;
	}
	public void setType(CacheType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CacheConfig [type=" + type + ", value=" + value + "]";
	}
	
}
