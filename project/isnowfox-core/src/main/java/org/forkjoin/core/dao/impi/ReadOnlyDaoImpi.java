package org.forkjoin.core.dao.impi;

import com.google.common.collect.Lists;
import com.isnowfox.core.PageResult;
import org.forkjoin.core.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class ReadOnlyDaoImpi<T extends EntityObject, K extends KeyObject>
        extends
        AbstractReadOnlyDao<T, K> {

    protected static final Logger log = LoggerFactory
            .getLogger(ReadOnlyDaoImpi.class);

    protected final TableInfo<T, K> tableInfo;

    public ReadOnlyDaoImpi(TableInfo<T, K> tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override
    public T get(K k) {
        return querySingle(tableInfo.getSelectByKeySql(),
                tableInfo.getRowMapper(), k.getQueryParams());
    }

    @Override
    public long getCount() {
        if (log.isDebugEnabled()) {
            log.debug("getCount: {};table:{}", tableInfo.getSelectCountSql(),
                    tableInfo.getDbTableName());
        }
        return getJdbcTemplate().queryForObject(tableInfo.getSelectCountSql(),
                Long.class);
    }

    @Override
    public long getCount(QueryParams params) {
        StringBuilder sb = new StringBuilder(tableInfo.getSelectCountSql());
        params.toSql(sb);
        Object[] args = params.toParams();
        String sql = sb.toString();
        return getCount(sql, args);
    }

    @Override
    public long getCount(Select select) {
        return getCount(select.toSql(), select.toParams());
    }

    private long getCount(String sql, Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("getCount: {}", sql);
        }
        return getJdbcTemplate().queryForObject(sql, Long.class, args);
    }

    @Override
    public PageResult<T> findPage(QueryParams params, Order order, int page,
                                  int pageSize) {
        StringBuilder sb = new StringBuilder(
                tableInfo.getFormatSelectPrefixSql());
        if (params != null) {
            sb.append(params.toSql().replace("%", "%%"));
        }
        if (order == null) {
            sb.append(tableInfo.getOrderByIdDescSql());
        } else {
            order.toSql(sb);
        }

        if (params != null) {
            return fastQueryPage(sb.toString(), tableInfo.getRowMapper(), page, pageSize, params.toParams());
        } else {
            return fastQueryPage(sb.toString(), tableInfo.getRowMapper(), page, pageSize);
        }
    }

    @Override
    public List<T> find(int max, QueryParams params, Order order) {
        StringBuilder sb = new StringBuilder(tableInfo.getSelectPrefixSql());
        if (params != null) {
            params.toSql(sb);
        }
        if (order == null) {
            sb.append(tableInfo.getOrderByIdDescSql());
        } else {
            order.toSql(sb);
        }

        if (params != null) {
            return find(max, sb.toString(), params.toParams(),
                    tableInfo.getRowMapper());
        } else {
            return find(max, sb.toString(), null, tableInfo.getRowMapper());
        }
    }

    @Override
    public List<T> find(int max, Select select) {
        return findBySelect(max, select, tableInfo.getRowMapper());
    }

    @Override
    public <C> List<C> findBySelect(int max, Select select,
                                    final RowMapper<C> rowMapper) {
        return find(max, select.toSql(), select.toParams(), rowMapper);
    }

    private <C> List<C> find(int max, String sql, Object[] args,
                             RowMapper<C> rowMapper) {
        sql = sql + " LIMIT " + max;
        if (args != null) {
            return query(sql, rowMapper, args);
        } else {
            return query(sql, rowMapper);
        }
    }

    private <C> List<C> query(String sql, RowMapper<C> rowMapper)
            throws DataAccessException {
        if (log.isDebugEnabled()) {
            log.debug("query: {},table:{}", sql, tableInfo.getDbTableName());
        }
        return getJdbcTemplate().query(sql,
                new RowMapperResultSetExtractor<>(rowMapper));
    }

    private <C> List<C> query(String sql, RowMapper<C> rowMapper,
                              Object... args) throws DataAccessException {
        if (log.isDebugEnabled()) {
            log.debug("query: {}; args:{}; table:{}", sql,
                    Arrays.toString(args), tableInfo.getDbTableName());
        }
        return getJdbcTemplate().query(sql,
                new RowMapperResultSetExtractor<>(rowMapper), args);
    }

    private <C> C querySingle(Select select, RowMapper<C> rowMapper,
                              Object... args) {
        return querySingleBySql(select.toSql(), rowMapper, select.toParams());
    }

    private <C extends T> C querySingle(String sql, RowMapper<C> rowMapper,
                                        Object... args) {
        return querySingleBySql(sql, rowMapper, args);
    }

    private <C> C querySingleBySql(String sql, RowMapper<C> rowMapper,
                                   Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("querySingle: {}; args:{}; table:{}", sql,
                    Arrays.toString(args), tableInfo.getDbTableName());
        }
        List<C> results = getJdbcTemplate().query(sql, args,
                new RowMapperResultSetExtractor<>(rowMapper, 1));
        return DataAccessUtils.singleResult(results);
    }

    @Override
    public PageResult<T> findPage(Select select, int page, int pageSize) {
        return findPageBySelect(select, tableInfo.getRowMapper(), page,
                pageSize);
    }

    @Override
    public <C> PageResult<C> findPageBySelect(Select select,
                                              final RowMapper<C> rowMapper, final int page, final int pageSize) {
        StringBuilder sb = new StringBuilder();
        String sql = select.toSql(sb).toString();
        sb.setLength(0);
        String countSql = select.toCountSql(sb).toString();
        return fastQueryPage(countSql, sql, rowMapper, page, pageSize,
                select.toParams());
    }

    /**
     * TODO 应该修改可见性,变成private的 sql语句类似这种 SELECT %s FROM `activity` WHERE id=?
     * 这个查询为了简单,但是比较耦合
     */
    public <C extends T> PageResult<C> fastQueryPage(final String sql,
                                                     final RowMapper<C> rowMapper, final int page, final int pageSize,
                                                     final Object... args) {
        return fastQueryPage(String.format(sql, SqlUtils.STRING_COUNT), String.format(sql, " * "), rowMapper, page, pageSize, args);
    }

    private <C> PageResult<C> fastQueryPage(final String countSql,
                                            final String sql, final RowMapper<C> rowMapper, int curPage,
                                            int curPageSize, final Object[] args) {
        int page = Math.max(curPage, 1);
        int pageSize = Math.min(curPageSize, 200);
        return getJdbcTemplate()
                .execute(
                        (ConnectionCallback<PageResult<C>>) con -> {
                            PreparedStatement ps = null;
                            ResultSet rs = null;
                            try {

                                if (log.isDebugEnabled()) {
                                    log.debug(
                                            "fastQueryPage countSql: {}; params:{}; table:{}",
                                            countSql, args,
                                            tableInfo.getDbTableName());
                                }
                                ps = con.prepareStatement(countSql);
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

                                if (log.isDebugEnabled()) {
                                    log.debug(
                                            "fastQueryPage: {}; params:{}; table:{}",
                                            pageSql, args,
                                            tableInfo.getDbTableName());
                                }

                                ps = con.prepareStatement(pageSql);
                                i = 1;
                                for (Object o : args) {
                                    ps.setObject(i++, o);
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