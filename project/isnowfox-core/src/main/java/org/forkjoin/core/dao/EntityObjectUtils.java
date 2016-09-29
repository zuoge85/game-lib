package org.forkjoin.core.dao;

import com.google.common.collect.ImmutableMap;
import com.isnowfox.util.JsonUtils;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class EntityObjectUtils {
    public static <T extends EntityObject, K extends KeyObject> Map<String, String> encodeStringMap(EntityObject<T, K> o) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        Collection<EntityProperty> properties = o.getTableInfo().getEntityProperties();
        for (EntityProperty pro : properties) {
            String valueStr = JsonUtils.serialize(o.get(pro.getDbName()));
            builder.put(pro.getDbName(), valueStr);
        }
        return builder.build();
    }


    public static <T extends EntityObject, K extends KeyObject> T decodeStringMap(Map<String, String> map, T t) {
        @SuppressWarnings("unchecked")
        Collection<EntityProperty> properties = t.getTableInfo().getEntityProperties();
        for (EntityProperty pro : properties) {
            String valueStr = map.get(pro.getDbName());
            if (valueStr != null) {
                Object value = JsonUtils.deserialize(valueStr, pro.getValueTypeRef());
                t.set(pro.getDbName(), value);
            }
        }
        return t;
    }
}
