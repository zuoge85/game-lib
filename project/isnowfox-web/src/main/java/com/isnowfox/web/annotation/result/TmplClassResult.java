package com.isnowfox.web.annotation.result;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TmplClassResult {
    public static final String JSON = "json";
    public static final String HTTL = "httl";

    String type() default HTTL;

    Class<?> cls();

    String value() default "";
}
