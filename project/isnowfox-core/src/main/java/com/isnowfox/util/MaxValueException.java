package com.isnowfox.util;

public class MaxValueException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6461873646027129299L;

    public MaxValueException() {
        super();
    }

    public MaxValueException(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MaxValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxValueException(String message) {
        super(message);
    }

    public MaxValueException(Throwable cause) {
        super(cause);
    }


}
