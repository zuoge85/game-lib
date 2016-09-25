package com.isnowfox.game.platform;

import java.util.Map;

/**
 * @author zuoge85 on 2015/3/11.
 */
public interface ApiCallback {
    void callback(boolean result, Map<String, Object> map);
}
