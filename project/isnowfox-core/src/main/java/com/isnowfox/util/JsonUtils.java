package com.isnowfox.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static final ObjectMapper mapper = new ObjectMapper();

    //private static final SimpleFilterProvider prov = new SimpleFilterProvider();
    static {
        mapper.registerModule(new GuavaModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //mapper.configure(SerializationFeature.INDENT_OUTPUT , true);
//		mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS , true);
//		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES , true);
//		mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH , true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);


        //mapper.constructType(t)
//		SerializerFactory.
//		mapper.setSerializerFactory(f);
//		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
//			@Override
//			public void serialize(Object value, JsonGenerator jgen,
//					SerializerProvider provider) throws IOException,
//					JsonProcessingException {
//				jgen.writeString("");
//				System.out.println(value);
//			}
//			
//		});
        //mapper.getSerializerProvider().
//		mapper.getSerializerProvider().findTypedValueSerializer(valueType, cache, property)(type)setDefaultKeySerializer(new JsonSerializer<Object>(){
//			@Override
//			public void serialize(Object value, JsonGenerator jgen,
//					SerializerProvider provider) throws IOException,
//					JsonProcessingException {
//				System.out.println("sb:"+value);
//			}
//		});
        //包装root元素
        //mapper.configure(SerializationFeature.WRAP_ROOT_VALUE , true);
//		mapper.enableDefaultTyping();
//		mapper.configure(DeserializationFeature. , false);
//		mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS , false);

    }

    public static final JsonFactory jsonFactory = new JsonFactory();

    /**
     * 不能处理复杂情况,和继承情况
     *
     * @param o
     * @return
     */
    public final static String serialize(Object o) {
        try {
            if (o instanceof List) {
                return mapper.writeValueAsString(((List<?>) o).toArray());
            }
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 不能处理复杂情况,和继承情况
     *
     * @param json
     * @param valueType
     * @return
     */
    public final static <T> T deserialize(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 不能处理复杂情况,和继承情况
     * 对付一般的List<MyClass> 方式狗了
     */
    public final static <T> T deserialize(String json, TypeReference<T> valueTypeRef) {
        try {
            return mapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
