package com.isnowfox.core.net;

import com.isnowfox.core.net.message.Message;

public interface NetMessageHandler<T> extends NetHandler<T> {
    void onMessage(Message msg) throws Exception;
}
