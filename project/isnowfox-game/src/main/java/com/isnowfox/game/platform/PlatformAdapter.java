package com.isnowfox.game.platform;

import io.netty.buffer.ByteBuf;

import java.util.Map;

/**
 * @author zuoge85 on 2015/2/4.
 */
public class PlatformAdapter implements Platform {
    public boolean onIn(ByteBuf in, User user) throws Exception {
        return true;
    }

    public UserInfo login(String info) {
        return null;
    }

    public void pay(int rmb, String openId, ApiCallback callback) {

    }

    public void logout(String openId) {

    }

    public void payResult(Map<String, String> allParams) {

    }

    public void close() throws InterruptedException {

    }
}
