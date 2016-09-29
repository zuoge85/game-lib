package org.forkjoin.core.dao;

import com.isnowfox.core.PageResult;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * @author zuoge85 on 15/6/9.
 */
public interface ReadOnlyDao<T extends EntityObject, K extends KeyObject> {
    T get(K k);

    long getCount();

    long getCount(QueryParams params);

    long getCount(Select select);


    PageResult<T> findPage(int page, int pageSize);

    PageResult<T> findPage(Order order, int page, int pageSize);

    PageResult<T> findPage(QueryParams params, int page, int pageSize);

    PageResult<T> findPage(QueryParams params, Order order, int page, int pageSize);

    PageResult<T> findPage(Select select, int page, int pageSize);

    <C> PageResult<C> findPageBySelect(Select select, final RowMapper<C> rowMapper, final int page, final int pageSize);


    List<T> find(int max);

    List<T> find(int max, String key, Object value);

    List<T> find(int max, String key, Object value, Order order);

    List<T> find(int max, String key0, Object value0, String key1, Object value1);

    List<T> find(int max, String key0, Object value0, String key1, Object value1, Order order);

    List<T> find(int max, QueryParams params);

    List<T> find(int max, QueryParams params, Order order);

    List<T> find(int max, Select select);

    <C> List<C> findBySelect(int max, Select select, final RowMapper<C> rowMapper);

    T findObject();

    T findObject(String key, Object value);

    T findObject(String key0, Object value0, String key1, Object value1);

    T findObject(String key0, Object value0, String key1, Object value1, String key2, Object value2);

    T findObject(QueryParams params);

    T findObject(Select select);
}
