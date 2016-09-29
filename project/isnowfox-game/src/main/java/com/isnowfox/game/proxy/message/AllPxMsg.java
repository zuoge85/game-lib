package com.isnowfox.game.proxy.message;

import com.isnowfox.core.net.message.Message;
import io.netty.buffer.ByteBuf;

/**
 * 发给全部玩家的
 *
 * @author zuoge85
 */
public class AllPxMsg extends AbstractPxMsg {
    public static final int ID = 2;

    @Override
    protected void encodeData(ByteBuf out) throws Exception {

    }

    @Override
    protected void decodeData(ByteBuf in) throws Exception {

    }

    @Override
    public int getType() {
        return ID;
    }

    @Override
    public String toString() {
        return "AllPxMsg []" + super.toString();
    }

    public static class EncodeWrapper extends AbstractEncodeWrapper {
        public EncodeWrapper(Message msg) {
            super.msg = msg;
        }

        @Override
        public int getType() {
            return ID;
        }

        @Override
        protected void encodeData(ByteBuf out) throws Exception {

        }

        @Override
        public String toString() {
            return "AllPxMsg.Wrapper []" + super.toString();
        }
    }
}
