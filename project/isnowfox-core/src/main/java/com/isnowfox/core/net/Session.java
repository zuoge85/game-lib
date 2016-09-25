package com.isnowfox.core.net;

import io.netty.channel.Channel;

public class Session<T> {
	public final Channel channel;
	private T value;
    private Object info;

    public Session(Channel channel){
		this.channel = channel;
	}
	
	public T get(){
		return value;
	}
	
	public void set(T value){
		this.value = value;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Session{" +
				"channel=" + channel +
				", value=" + value +
				", info=" + info +
				'}';
	}
}
