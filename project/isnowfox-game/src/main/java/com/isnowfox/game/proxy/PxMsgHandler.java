package com.isnowfox.game.proxy;

import com.isnowfox.core.net.NetHandler;
import com.isnowfox.game.proxy.message.PxMsg;

public interface PxMsgHandler<T> extends NetHandler<T>{
	void onMessage(PxMsg msg) throws Exception;
}
