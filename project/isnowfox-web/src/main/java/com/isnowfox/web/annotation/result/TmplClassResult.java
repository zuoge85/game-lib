package com.isnowfox.web.annotation.result;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TmplClassResult{
	public static final String JSON ="json";
	public static final String HTTL ="httl";
	
	String type() default HTTL;
	Class<?> cls() ;
	String value() default "";
}
