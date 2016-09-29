package com.isnowfox.web.annotation;


import com.isnowfox.web.ParameterType;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    ParameterType value() default ParameterType.REQUEST;

    String name() default "";
}
