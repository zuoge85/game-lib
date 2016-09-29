package com.isnowfox.core.net.message;

public class MessageException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1591880161791275378L;

    public MessageException() {
        super();
    }

    public MessageException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public static final MessageException newLengthException(int length) {
        return new MessageException("to length :" + length + ",max:" + MessageProtocol.MESSAGE_MAX);
    }

    public static final MessageException newTypeValueRangeException(int type) {
        return new MessageException("type value :" + type + ",max:" + MessageProtocol.TYPE_OR_ID_MAX);
    }

    public static final MessageException newIdValueRangeException(int id) {
        return new MessageException("id value :" + id + ",max:" + MessageProtocol.TYPE_OR_ID_MAX);
    }

    public static final MessageException newIdDuplicateRangeException(int id, Class<?> c0, Class<?> c1) {
        return new MessageException("id value :" + id + ",duplicate:" + id + ",cls:" + c0 + ",cls:" + c1);
    }

    public static final MessageException newFactoryDuplicateExpandException() {
        return new MessageException("duplicate expand");
    }
}
