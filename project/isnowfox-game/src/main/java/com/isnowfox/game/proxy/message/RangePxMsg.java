package com.isnowfox.game.proxy.message;

import com.isnowfox.util.collect.primitive.ShortList;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isnowfox.core.net.message.Message;

public class RangePxMsg extends AbstractPxMsg{
	private static final Logger log = LoggerFactory.getLogger(RangePxMsg.class);
	
	private static final int INIT_SIZE = 64;
	public static final int ID = 1;

	private final ShortList userList;
	
	public RangePxMsg() {
		this(INIT_SIZE);
	}
	
	public RangePxMsg(int initialCapacity) {
		userList = new ShortList(initialCapacity);
	}

	@Override
	protected void encodeData(ByteBuf out) throws Exception {
		out.writeShort(userList.size());
        for (int i = 0; i < userList.size(); i++) {
            out.writeShort(userList.get(i));
        }
    }

	@Override
	protected void decodeData(ByteBuf in) throws Exception {
		int len = in.readShort();
		try{
			for (int i = 0; i < len; i++) {
				userList.add(in.readShort());
			}
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	
	public final ShortList getUserList() {
		return userList;
	}

	public final void add(short userId) {
		userList.add(userId);
	}
	
	public final void add(short ...array) {
		for (int i = 0; i < array.length; i++) {
			userList.add(array[i]);
		}
	}
	
	@Override
	public int getType() {
		return ID;
	}
	
	@Override
	public String toString() {
		return "RangePxMsg [userList=" + userList + "]" + super.toString();
	}
	
	public static class EncodeWrapper extends AbstractEncodeWrapper{
		
		private final short[] userList;
		
//		public EncodeWrapper(Message msg) {
//			this.msg = msg;
////			userList = new ArrayList<>(INIT_SIZE);
//		}
		
		public EncodeWrapper(Message msg,  short[] userList) {
			this.msg = msg;
			this.userList = userList;
		}

		@Override
		public int getType() {
			return ID;
		}

		@Override
		protected void encodeData(ByteBuf out) throws Exception {
//            if(userList.size()>1){
//                log.error("debug");
//            }

			out.writeShort(userList.length);
			for (Short userId : userList) {
				out.writeShort(userId);
			}
		}
		
//		public final void add(short userId) {
//            if(userList.contains(userId)){
//                log.error("debug");
//            }
//			userList.add(userId);
//		}
//
//		public final void add(short ...array) {
//			for (int i = 0; i < array.length; i++) {
//				userList.add(array[i]);
//			}
//		}
		@Override
		public String toString() {
			return "RangePxMsg.Wrapper [userList=" + userList + "]" + super.toString();
		}
	}
}
