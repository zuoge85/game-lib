package com.isnowfox.game.proxy;

import com.isnowfox.game.proxy.message.*;
import net.sf.cglib.core.Constants;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;


public class PxMsgFactory {
//	private static final PxMsgFactory instance = new PxMsgFactory();

    private FastConstructor[] array;
    private TreeMap<Integer, Class<? extends PxMsg>> tempMap = new TreeMap<>();

    public PxMsgFactory() {
        init();
        fixed();
    }

    protected void init() {
        add(SinglePxMsg.ID, SinglePxMsg.class);
        add(RangePxMsg.ID, RangePxMsg.class);
        add(AllPxMsg.ID, AllPxMsg.class);
        add(LogoutPxMsg.ID, LogoutPxMsg.class);
    }

    protected void add(int id, Class<? extends PxMsg> cls) {
        tempMap.put(id, cls);
    }

    protected void fixed() {
        array = new FastConstructor[tempMap.lastKey() + 1];
        for (Map.Entry<Integer, Class<? extends PxMsg>> e : tempMap.entrySet()) {
            array[e.getKey()] = FastClass.create(e.getValue()).getConstructor(Constants.EMPTY_CLASS_ARRAY);
        }
        tempMap = null;
    }

    public PxMsg get(int id) throws InvocationTargetException {
        return (PxMsg) array[id].newInstance();
    }
}
