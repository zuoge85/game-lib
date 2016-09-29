package com.isnowfox.web.annotation.result;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TmplViewGroup {
    TmplViewResult value();
}
