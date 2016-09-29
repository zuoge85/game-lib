package com.isnowfox.core.net.message;

public abstract class ExpandMessage extends AbstractMessage {
    public final int getMessageType() {
        return MessageProtocol.EXPAND_MESSAGE_TYPE;
    }

    @Override
    public String toString() {
        return "ExpandMessage [messageId=" + getMessageId() + "]";
    }

}
