package com.isnowfox.core;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObject implements JsonTransform{
	
	/**
	 * 可以单例,线程安全
	 */
	protected  static ObjectMapper mapper = new ObjectMapper();

	
	public static <T> T readObject(String json,Class<T> cls){
		try {
			return mapper.readValue(json,cls);
		} catch (IOException e) {
			throw new RuntimeException("json read exception",e);
		}
	}

	@Override
	public void toJson(Appendable appendable) {
		try {
			appendable.append(mapper.writeValueAsString(this)) ;
		} catch (Exception e) {
			throw new RuntimeException("json toJson exception",e);
		}
	}
}
