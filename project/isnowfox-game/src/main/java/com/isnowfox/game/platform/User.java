package com.isnowfox.game.platform;

import io.netty.channel.Channel;

public class User {
    private short id;
    private Channel channel;
    private boolean isLogin;
    private boolean checkConnect = false;

    public User() {

    }

    public User(short id, Channel channel) {
        super();
        this.id = id;
        this.channel = channel;
    }

    public final short getId() {
        return id;
    }

    public final void setId(short id) {
        this.id = id;
    }

    public void writeAndFlush(Object obj) {
        channel.writeAndFlush(obj);
    }

    public final Channel getChannel() {
        return channel;
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isCheckConnect() {
        return checkConnect;
    }

    public void setCheckConnect(boolean checkConnect) {
        this.checkConnect = checkConnect;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", channel=" + channel +
                ", isLogin=" + isLogin +
                ", checkConnect=" + checkConnect +
                '}';
    }
}
