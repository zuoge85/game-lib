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
package com.isnowfox.game.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.isnowfox.game.proxy.message.PxMsg;

public class PxMsgEncoder extends MessageToByteEncoder<PxMsg> {
	
    @Override
    protected void encode(ChannelHandlerContext ctx, PxMsg msg, ByteBuf out) throws Exception {
    	try{
    		int startIdx = out.writerIndex();
    		//MessageProtocol.LENGTH_BYTE_NUMS 修改后必须修改这个代码
        	out.writeInt(0);
        	out.writeByte(msg.getType());
        	
        	msg.encode(out);
        	
        	int endIdx = out.writerIndex();
        	int len = endIdx - startIdx - PxMsg.HEAD_LENGTH;
        	out.setInt(startIdx, len);
    	}catch(Throwable t){
			ctx.fireExceptionCaught(t);
		}
    }
}
