package com.isnowfox.web.annotation;

import java.lang.annotation.*;

/**
 * http缓存机制
 *
 * @author zuoge85
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoCache {
}
