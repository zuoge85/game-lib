package com.isnowfox.game.proxy.message;

import com.isnowfox.core.net.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface PxMsg {
    public static final int HEAD_LENGTH = 5;

    abstract void encode(ByteBuf out) throws Exception;

    abstract void decode(ByteBuf in, ChannelHandlerContext ctx) throws Exception;

    abstract int getType();

    <T> Session<T> getSession();

    <T> void setSession(Session<T> session);
}