package org.forkjoin.core.dao.impi;

import com.google.common.collect.ImmutableMap;
import org.forkjoin.core.dao.BaseDao;
import org.forkjoin.core.dao.EntityObject;
import org.forkjoin.core.dao.KeyObject;
import org.forkjoin.core.dao.TableInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zuoge85 on 15/6/10.
 */
public abstract class AbstractBaseDao<T extends EntityObject, K extends KeyObject>
        extends AbstractReadOnlyDao<T, K>
        implements BaseDao<T, K> {

    protected final TableInfo<T, K> tableInfo;

    public AbstractBaseDao(TableInfo<T, K> tableInfo) {
        this.tableInfo = tableInfo;
    }


    @Override
    public int insert(List<T> list) {
        for (T t : list) {
            insert(t);
        }
        return list.size();
    }

    @Override
    public boolean incrementUpdatePartial(final String name0, final Object value0, final K key) {
        return incrementUpdatePartial(Collections.singletonMap(name0, value0), key);
    }

    @Override
    public boolean incrementUpdatePartial(
            final String name0, final Object value0,
            final String name1, final Object value1, final K key
    ) {
        ImmutableMap<String, Object> map = ImmutableMap.of(name0, value0, name1, value1);
        return incrementUpdatePartial(map, key);
    }

    @Override
    public abstract boolean incrementUpdatePartial(final Map<String, Object> m, final K key);

    @Override
    public boolean updatePartial(String name, Object value, K key) {
        return updatePartial(Collections.singletonMap(name, value), key);
    }

    @Override
    public boolean updatePartial(
            String name0, Object value0,
            String name1, Object value1,
            K key
    ) {
        return updatePartial(ImmutableMap.of(name0, value0, name1, value1), key);
    }

    @Override
    public boolean updatePartial(
            String name0, Object value0,
            String name1, Object value1,
            String name2, Object value2,
            K key
    ) {
        return updatePartial(ImmutableMap.of(name0, value0, name1, value1, name2, value2), key);
    }

    @Override
    public int updatePartial(final Map<String, Object> m, String key0, Object keyValue0) {
        return updatePartial(m, Collections.singletonMap(key0, keyValue0));
    }

    @Override
    public int updatePartial(final Map<String, Object> m, String key0, Object keyValue0, String key1, Object keyValue1) {
        return updatePartial(m, ImmutableMap.of(key0, keyValue0, key1, keyValue1));
    }

    @Override
    public abstract boolean updatePartial(Map<String, Object> m, K key);
}
