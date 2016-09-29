package com.isnowfox.web.annotation;

import com.isnowfox.web.HttpMethodType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
    HttpMethodType method() default HttpMethodType.ALL;

    String value();
}
