package com.isnowfox.game.proxy.message;

import com.isnowfox.core.net.Session;
import io.netty.buffer.ByteBuf;

/**
 * ProxyMessage
 * <p/>
 * 消息格式 头[_ _ _ _]类型[_]buf[_________________]扩展内容[____________]
 * 服务器之间转发的消息
 *
 * @author zuoge85
 */
public abstract class AbstractSessionPxMsg implements PxMsg {

    @SuppressWarnings("rawtypes")
    protected Session session;
    private int id;

    public AbstractSessionPxMsg(int id) {
        this.id = id;
    }


    protected void writeString(ByteBuf out, String value) throws Exception {
        if(value == null){
            out.writeInt(-1);
        }else{
            byte[] bytes = value.getBytes("utf8");
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
    }

    public String readString(ByteBuf in) throws Exception {
        int len = in.readInt();
        if (len == -1) {
            return null;
        } else {
            byte[] bytes = new byte[len];
            in.readBytes(bytes);
            return new String(bytes, "utf8");
        }
    }

    @SuppressWarnings("unchecked")
    public final <T> Session<T> getSession() {
        return session;
    }

    public final <T> void setSession(Session<T> session) {
        this.session = session;
    }


    @Override
    public int getType() {
        return id;
    }
}
