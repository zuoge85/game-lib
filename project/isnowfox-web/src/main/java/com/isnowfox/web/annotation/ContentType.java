package com.isnowfox.web.annotation;

public @interface ContentType {
    public static final String TEXT_HTML = "text/html";

    String value() default TEXT_HTML;
}
