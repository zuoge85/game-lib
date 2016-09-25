package com.isnowfox.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.thoughtworks.xstream.XStream;

public final class XmlUtils {
	private final static JacksonXmlModule module = new JacksonXmlModule();
	static {
		module.setDefaultUseWrapper(false);
	}
	private final static ObjectMapper mapper = new XmlMapper(module);
	static {
		mapper.registerModule(new GuavaModule());
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
	}
	private final static XStream xstream = new XStream();
	static {
		xstream.aliasPackage("igu", "io.grass.util");
		xstream.aliasPackage("kgb", "koc.game.build");
		xstream.aliasPackage("kg", "koc.game");
		xstream.aliasPackage("kgt", "koc.game.task");
		xstream.aliasPackage("kgc", "koc.net.c2s");
		xstream.aliasPackage("kgs", "koc.net.s2c");
		xstream.aliasPackage("kgi", "koc.net.info");
	}

	public final static String serialize(Object o) {
		// ByteArrayOutputStream aout = new ByteArrayOutputStream();
		// XMLEncoder e = new XMLEncoder(aout,"UTF-8", true, 0);
		// e.writeObject(o);
		// e.close();
		// return new String(aout.toByteArray(),"UTF-8");
		
		return xstream.toXML(o);
	}

	@SuppressWarnings("unchecked")
	public final static <T> T deserialize(String xml)  {
		return (T) xstream.fromXML(xml);
	}

	/**
	 * 不能处理复杂情况,和继承情况
	 * 
	 * @param o
	 * @return
	 */
	public final static String baseSerialize(Object o) {
		try {

			return mapper.writeValueAsString(o);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 不能处理复杂情况,和继承情况
	 * 
	 * @param xml
	 * @param valueType
	 * @return
	 */
	public final static <T> T baseDeserialize(String xml, Class<T> valueType) {
		try {
			return mapper.readValue(xml, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
//	/**
//	 * TODO 必须实现这个类,不然...CopyOnWriteArrayList的线程安全是个大问题
//	 * @author zuoge85
//	 *
//	 */
//	public static class ListConverter extends AbstractCollectionConverter{
//
//		public ListConverter(Mapper mapper) {
//			super(mapper);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public boolean canConvert(Class type) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public void marshal(Object source, HierarchicalStreamWriter writer,
//				MarshallingContext context) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public Object unmarshal(HierarchicalStreamReader reader,
//				UnmarshallingContext context) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}
}
