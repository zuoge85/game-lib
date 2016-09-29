package com.isnowfox.web.listener;

import com.isnowfox.web.Response;


public class WebResponseEvent extends java.util.EventObject {

    /**
     *
     */
    private static final long serialVersionUID = -616889065752627441L;

    public WebResponseEvent(Object source) {
        super(source);
    }

    public Response getResponse() {
        return (Response) super.getSource();
    }
}
