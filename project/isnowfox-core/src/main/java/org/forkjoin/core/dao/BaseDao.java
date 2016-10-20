package org.forkjoin.core.dao;

import java.util.List;
import java.util.Map;

/**
 * @author zuoge85 on 15/6/9.
 */
public interface BaseDao<T extends EntityObject, K extends KeyObject> extends ReadOnlyDao<T, K> {
    long insert(T t);

    long replace(T t);

    boolean update(T t);

    int insert(List<T> list);

    boolean del(K key);

    boolean del(String key0, Object value0);

//    boolean del(String key0, Object value0);

//    boolean del(String key0, Object value0, Order order, int n);


    boolean incrementUpdatePartial(String name0, Object value0, K key);

    boolean incrementUpdatePartial(
            String name0, Object value0,
            String name1, Object value1, K key
    );

    boolean incrementUpdatePartial(Map<String, Object> m, K key);


    boolean updatePartial(String name, Object value, K key);

    boolean updatePartial(
            String name0, Object value0,
            String name1, Object value1,
            K key
    );

    boolean updatePartial(
            String name0, Object value0,
            String name1, Object value1,
            String name2, Object value2,
            K key
    );

    boolean updatePartial(Map<String, Object> m, K key);

    int updatePartial(final Map<String, Object> m, String key0, Object keyValue0);
    int updatePartial(final Map<String, Object> m, String key0, Object keyValue0, String key1, Object keyValue1);
    int updatePartial(final Map<String, Object> m, final Map<String, Object> key);

//    boolean updatePartial(String sql, ArrayList<Object> args);
}
