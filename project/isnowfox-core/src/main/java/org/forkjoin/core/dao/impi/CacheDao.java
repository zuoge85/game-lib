package org.forkjoin.core.dao.impi;

import com.google.common.collect.Lists;
import com.isnowfox.core.PageResult;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.forkjoin.core.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * 这个版本还未实现缓存
 *
 * @author zuoge85 on 15/6/9.
 */
public class CacheDao<T extends EntityObject, K extends KeyObject>
        extends
        AbstractBaseDao<T, K> {

    protected static final Logger log = LoggerFactory.getLogger(CacheDao.class);
    private LocalCache<T, K> localCache;
    private AbstractBaseDao<T, K> dbDao;

    @SuppressWarnings("unchecked")
    public CacheDao(Class<T> cls) throws Exception {
        this((TableInfo) FieldUtils.readStaticField(cls, "TABLE_INFO"));
        localCache = new LocalCache<>();
        dbDao = new BaseDaoImpi<>(tableInfo);
    }

    public CacheDao(TableInfo<T, K> tableInfo) {
        super(tableInfo);
        localCache = new LocalCache<>();
        dbDao = new BaseDaoImpi<>(tableInfo);
    }

    @Override
    protected void initTemplateConfig() {
        super.initTemplateConfig();
        dbDao.setJdbcTemplate(getJdbcTemplate());
    }

    @Override
    public long insert(T t) {
        return dbDao.insert(t);
    }

    @Override
    public long replace(T t) {
        return dbDao.replace(t);
    }

    @Override
    public boolean update(T t) {
        return dbDao.update(t);
    }

    @Override
    public boolean del(K key) {
        return dbDao.del(key);
    }

    @Override
    public boolean del(String key0, Object value0) {
        return dbDao.del(key0, value0);
    }

    @Override
    public boolean incrementUpdatePartial(Map<String, Object> m, K key) {
        return dbDao.incrementUpdatePartial(m, key);
    }

    @Override
    public boolean updatePartial(Map<String, Object> m, K key) {
        return dbDao.updatePartial(m, key);
    }

    @Override
    public int updatePartial(Map<String, Object> m, Map<String, Object> key) {
        return dbDao.updatePartial(m, key);
    }

    @Override
    public T get(K k) {
        return dbDao.get(k);
    }

    @Override
    public long getCount() {
        return dbDao.getCount();
    }

    @Override
    public long getCount(QueryParams params) {
        return dbDao.getCount(params);
    }

    @Override
    public long getCount(Select select) {
        return dbDao.getCount(select);
    }

    @Override
    public PageResult<T> findPage(QueryParams params, Order order, int page,
                                  int pageSize) {
        return dbDao.findPage(params, order, page, pageSize);
    }

    @Override
    public PageResult<T> findPage(Select select, int page, int pageSize) {
        return dbDao.findPage(select, page, pageSize);
    }

    @Override
    public <C> PageResult<C> findPageBySelect(Select select,
                                              RowMapper<C> rowMapper, int page, int pageSize) {
        return dbDao.findPageBySelect(select, rowMapper, page, pageSize);
    }

    @Override
    public List<T> find(int max, QueryParams params, Order order) {
        return dbDao.find(max, params, order);
    }

    @Override
    public List<T> find(int max, Select select) {
        return dbDao.find(max, select);
    }

    @Override
    public <C> List<C> findBySelect(int max, Select select,
                                    RowMapper<C> rowMapper) {
        return dbDao.findBySelect(max, select, rowMapper);
    }

    @Override
    public T findObject(QueryParams params) {
        return dbDao.findObject(params);
    }

    /**
     * TODO 应该修改可见性,变成private的 sql语句类似这种 SELECT %s FROM `activity` WHERE id=?
     * 这个查询为了简单,但是比较耦合
     */
    public <C> PageResult<C> fastQueryPage(final String sql,
                                           final RowMapper<C> rowMapper, final int page, final int pageSize,
                                           final Object... args) {
        return fastQueryPage(String.format(sql, SqlUtils.STRING_COUNT),
                String.format(sql, " * "), rowMapper, page, pageSize, args);
    }

    public <C> PageResult<C> fastQueryPage(final String countSql,
                                           final String sql, final RowMapper<C> rowMapper, final int page,
                                           final int pageSize, final Object[] args) {
        return getJdbcTemplate()
                .execute(
                        (ConnectionCallback<PageResult<C>>) con -> {
                            PreparedStatement ps = null;
                            ResultSet rs = null;
                            try {
                                ps = con.prepareStatement(String.format(
                                        countSql, SqlUtils.STRING_COUNT));
                                int i = 1;
                                for (Object o : args) {
                                    ps.setObject(i++, o);
                                }
                                rs = ps.executeQuery();
                                int count;
                                if (rs.next()) {
                                    count = Integer.valueOf(rs.getObject(1)
                                            .toString());
                                } else {
                                    throw new ResultPageDataAccessException(
                                            "查询count失败!");
                                }
                                JdbcUtils.closeResultSet(rs);
                                JdbcUtils.closeStatement(ps);

                                PageResult<C> pageResult = PageResult
                                        .createPage(count, page, pageSize, null);

                                List<C> list = Lists
                                        .newArrayListWithCapacity(pageResult
                                                .getPageCount());
                                int start = pageResult.getStart();

                                String pageSql = sql + " LIMIT " + start + ","
                                        + pageSize;
                                ps = con.prepareStatement(pageSql);
                                i = 1;
                                for (Object o : args) {
                                    ps.setObject(i++, o);
                                }

                                if (log.isDebugEnabled()) {
                                    log.debug(
                                            "fastQueryPage: {}; params:{}; countSql:{}; table:{}",
                                            pageSql, args, countSql,
                                            tableInfo.getDbTableName());
                                }
                                rs = ps.executeQuery();
                                int rowNum = 0;
                                while (rs.next()) {
                                    list.add(rowMapper.mapRow(rs, rowNum++));
                                }
                                pageResult.setValue(list);
                                return pageResult;
                            } finally {
                                JdbcUtils.closeResultSet(rs);
                                JdbcUtils.closeStatement(ps);
                            }
                        });
    }

}
