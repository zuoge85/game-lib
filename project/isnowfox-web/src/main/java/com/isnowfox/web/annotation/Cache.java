package com.isnowfox.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.isnowfox.web.CacheType;

/**
 * http缓存机制
 * @author zuoge85
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
	CacheType type() default CacheType.NO_CACHE;
	String value() default "";
}
