package com.isnowfox.core.net.message;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.core.Constants;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;

import com.isnowfox.core.net.message.ExpandMessage;
import com.isnowfox.core.net.message.Message;
import com.isnowfox.core.net.message.MessageException;
import com.isnowfox.core.net.message.MessageProtocol;

/**
 * 这个实现没实现对象池，使用一个不等长二维数组
 * 使用cglib实例化类！
 * @author zuoge85
 *
 */
public abstract class AbstractMessageFactory implements MessageFactory{

	private final FastConstructor[][] array;

	public AbstractMessageFactory() {
		array = init();
	}

	protected abstract FastConstructor[][] init();

	public final Message getMessage(int type, int id)
			throws InvocationTargetException {
		if (type == MessageProtocol.EXPAND_MESSAGE_TYPE) {
			ExpandMessage em = (ExpandMessage) array[type][id].newInstance();
			return em;
		} else {
			return (Message) array[type][id].newInstance();
		}
	}

	public final ExpandBuild expand() {
		return new ExpandBuild(this);
	}

	public static final class ExpandBuild {
		private final AbstractMessageFactory factory;

		private ExpandBuild(AbstractMessageFactory factory) {
			this.factory = factory;
		}

		private final Map<Integer, Class<? extends ExpandMessage>> map = new HashMap<>();
		private int maxId = 0;

		public void add(int id, Class<? extends ExpandMessage> cls)
				throws MessageException {
			if (id > MessageProtocol.TYPE_OR_ID_MAX) {
				throw MessageException.newIdValueRangeException(id);
			}
			Class<? extends ExpandMessage> cls1 = map.put(id, cls);
			if (cls1 != null) {
				throw MessageException.newIdDuplicateRangeException(id, cls,
						cls1);
			}

			if (maxId < id) {
				maxId = id;
			}
		}

		public void end() throws MessageException {
			if (factory.array[MessageProtocol.EXPAND_MESSAGE_TYPE] != null) {
				throw MessageException.newFactoryDuplicateExpandException();
			}
			FastConstructor[] array = new FastConstructor[maxId + 1];
			for (Map.Entry<Integer, Class<? extends ExpandMessage>> e : map
					.entrySet()) {
				array[e.getKey()] = FastClass.create(e.getValue())
						.getConstructor(Constants.EMPTY_CLASS_ARRAY);
			}
			
			factory.array[MessageProtocol.EXPAND_MESSAGE_TYPE] = array;
		}
	}
}