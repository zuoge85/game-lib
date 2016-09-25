package com.isnowfox.game.proxy;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import net.sf.cglib.core.Constants;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;

import com.isnowfox.game.proxy.message.AllPxMsg;
import com.isnowfox.game.proxy.message.LogoutPxMsg;
import com.isnowfox.game.proxy.message.PxMsg;
import com.isnowfox.game.proxy.message.RangePxMsg;
import com.isnowfox.game.proxy.message.SinglePxMsg;

public class PxMsgFactory {
	private static final PxMsgFactory instance = new PxMsgFactory();
	
	private final FastConstructor[] array;
	
	public static final PxMsgFactory getInstance(){
		return instance;
	}
	
	public PxMsgFactory(){
		TreeMap<Integer, Class<? extends PxMsg>> map = new TreeMap<>();

		map.put(SinglePxMsg.ID, SinglePxMsg.class);
		map.put(RangePxMsg.ID, RangePxMsg.class);
		map.put(AllPxMsg.ID, AllPxMsg.class);
		map.put(LogoutPxMsg.ID, LogoutPxMsg.class);
		
		array = new  FastConstructor[map.lastKey()+1];
		for (Map.Entry<Integer, Class<? extends PxMsg>> e : map.entrySet()) {
			array[e.getKey()] = FastClass.create(e.getValue()).getConstructor(Constants.EMPTY_CLASS_ARRAY);
		}
	}
	
	public PxMsg get(int id) throws InvocationTargetException{
		return (PxMsg) array[id].newInstance();
	}
}
