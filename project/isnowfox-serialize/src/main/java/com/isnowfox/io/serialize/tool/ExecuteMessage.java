package com.isnowfox.io.serialize.tool;

import com.isnowfox.io.serialize.tool.model.Message;

/**
 * @author zuoge85 on 2014/8/27.
 */
public interface ExecuteMessage {
    void exe(Message msg) throws Exception;
}
