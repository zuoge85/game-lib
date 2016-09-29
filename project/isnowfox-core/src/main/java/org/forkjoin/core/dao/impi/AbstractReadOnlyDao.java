package org.forkjoin.core.dao.impi;

import com.isnowfox.core.PageResult;
import org.forkjoin.core.dao.*;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

/**
 * @author zuoge85 on 15/6/9.
 */
public abstract class AbstractReadOnlyDao<T extends EntityObject, K extends KeyObject>
        extends JdbcDaoSupport implements ReadOnlyDao<T, K> {


    @Override
    public PageResult<T> findPage(int page, int pageSize) {
        return findPage(null, null, page, pageSize);
    }

    @Override
    public PageResult<T> findPage(Order order, int page, int pageSize) {
        return findPage(null, order, page, pageSize);
    }

    @Override
    public PageResult<T> findPage(QueryParams params, int page, int pageSize) {
        return findPage(params, null, page, pageSize);
    }

    @Override
    public abstract PageResult<T> findPage(QueryParams params, Order order, int page, int pageSize);

    @Override
    public List<T> find(int max) {
        return find(max, (QueryParams) null, null);
    }

    @Override
    public List<T> find(int max, String key, Object value) {
        return find(max, QueryParams.single(key, value), null);
    }

    @Override
    public List<T> find(int max, String key, Object value, Order order) {
        return find(max, QueryParams.single(key, value), order);
    }

    @Override
    public List<T> find(int max, String key0, Object value0, String key1, Object value1) {
        return find(max, QueryParams.create().add(key0, value0).add(key1, value1));
    }

    @Override
    public List<T> find(int max, String key0, Object value0, String key1, Object value1, Order order) {
        return find(max, QueryParams.create().add(key0, value0).add(key1, value1), order);
    }

    @Override
    public List<T> find(int max, QueryParams params) {
        return find(max, params, null);
    }

    @Override
    public abstract List<T> find(int max, QueryParams params, Order order);


    @Override
    public T findObject() {
        return findObject((QueryParams) null);
    }

    @Override
    public T findObject(String key, Object value) {
        return findObject(QueryParams.single(key, value));
    }

    @Override
    public T findObject(String key0, Object value0, String key1, Object value1) {
        return findObject(QueryParams.create().add(key0, value0).add(key1, value1));
    }

    @Override
    public T findObject(String key0, Object value0, String key1, Object value1, String key2, Object value2) {
        return findObject(QueryParams.create().add(key0, value0).add(key1, value1).add(key2, value2));
    }

    @Override
    public T findObject(QueryParams params) {
        List<T> results = find(1, params);
        return DataAccessUtils.singleResult(results);
    }

    ;

    @Override
    public T findObject(Select select) {
        List<T> results = find(1, select);
        return DataAccessUtils.singleResult(results);
    }

    ;
}
