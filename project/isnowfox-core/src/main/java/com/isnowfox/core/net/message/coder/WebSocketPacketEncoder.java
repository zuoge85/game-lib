/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.isnowfox.core.net.message.coder;

import com.isnowfox.core.net.message.MessageException;
import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.core.net.message.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

public class WebSocketPacketEncoder extends MessageToMessageEncoder<Packet> {
    public WebSocketPacketEncoder() {

    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        int len = msg.getLength();
        if (len > MessageProtocol.MESSAGE_MAX) {
            throw MessageException.newLengthException(len);
        }
        if (msg.getBufOffset() > 0) {
            ByteBuf buffer = ctx.alloc().buffer(len);
            buffer.writeBytes(msg.getBuf(), msg.getBufOffset(), len);
            out.add(new BinaryWebSocketFrame(buffer));
        } else {
            msg.getBuf().retain();
            out.add(new BinaryWebSocketFrame(msg.getBuf()));
        }
    }
}
