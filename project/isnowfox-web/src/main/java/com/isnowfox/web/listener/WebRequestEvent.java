package com.isnowfox.web.listener;

import com.isnowfox.web.Request;


public class WebRequestEvent extends java.util.EventObject {

    /**
     *
     */
    private static final long serialVersionUID = -6314087908548104461L;

    public WebRequestEvent(Object source) {
        super(source);
    }

    public Request getRequest() {
        return (Request) super.getSource();
    }

}