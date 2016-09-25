package com.isnowfox.game.platform;

import java.util.Map;

/**
 * @author zuoge85 on 2015/3/11.
 */
public interface GamePayResult {
    void pay(Map<String, String> allParams, int rmb, int gold, String openId, ApiCallback callback);

    void paySuccess(String openId, String billno);

    void error(Map<String, String> allParams, String remoteInfo, String header);


    void login(Map<String, String> allParams, String remoteInfo, String header);
}
