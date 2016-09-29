package com.isnowfox.game.proxy.message;

import com.isnowfox.core.io.Input;
import com.isnowfox.core.io.MarkCompressInput;
import com.isnowfox.core.net.message.Message;
import com.isnowfox.core.net.message.MessageFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public class SinglePxMsg extends AbstractPxMsg {
    public static final int ID = 0;

    public SinglePxMsg() {

    }

    public SinglePxMsg(short sessionId, ByteBuf buf) {
        this.sessionId = sessionId;
        this.buf = buf;
        this.bufOffset = 0;
        this.bufLen = buf.capacity();
    }

    public final Message toMessage(MessageFactory messageFactory) throws Exception {
        buf.readerIndex(bufOffset);

        ByteBufInputStream bin = new ByteBufInputStream(buf, bufLen);
        Input in = MarkCompressInput.create(bin);
        int type = in.readInt();
        int id = in.readInt();

        Message rawMsg = messageFactory.getMessage(type, id);
        rawMsg.decode(in);
        return rawMsg;
    }

    private short sessionId;

    @Override
    protected void encodeData(ByteBuf out) throws Exception {
        out.writeShort(sessionId);
    }

    @Override
    protected void decodeData(ByteBuf in) throws Exception {
        sessionId = in.readShort();
    }

    @Override
    public int getType() {
        return ID;
    }

    public short getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "SinglePxMsg [sessionId=" + sessionId + "]" + super.toString();
    }

    public static class EncodeWrapper extends AbstractEncodeWrapper {
        private short sessionId;

        public EncodeWrapper(short sessionId, Message msg) {
            this.sessionId = sessionId;
            this.msg = msg;
        }

        @Override
        public int getType() {
            return ID;
        }

        @Override
        protected void encodeData(ByteBuf out) throws Exception {
            out.writeShort(sessionId);
        }

        @Override
        public String toString() {
            return "SinglePxMsg.Wrapper [sessionId=" + sessionId + "]" + super.toString();
        }
    }
}
