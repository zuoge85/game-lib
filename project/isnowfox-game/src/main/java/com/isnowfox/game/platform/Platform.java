package com.isnowfox.game.platform;

import com.isnowfox.core.net.Session;
import io.netty.buffer.ByteBuf;

import java.util.Map;

/**
 * @author zuoge85 on 2015/2/4.
 */
public interface Platform {
    /**
     * 返回true 表示
     */
    boolean onIn(ByteBuf in, User user) throws Exception;

    UserInfo login(String info);

    void pay(int rmb, String openId,ApiCallback callback);

    void logout(String openId);

    void payResult(Map<String, String> allParams);

    void close() throws InterruptedException;
}
