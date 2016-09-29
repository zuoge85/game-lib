package org.forkjoin.core.dao.impi;

import com.isnowfox.core.PageResult;
import org.forkjoin.core.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseDaoImpi<T extends EntityObject, K extends KeyObject>
        extends AbstractBaseDao<T, K> {

    protected static final Logger log = LoggerFactory.getLogger(BaseDaoImpi.class);

    private ReadOnlyDaoImpi<T, K> readOnlyDaoImpi;

    public BaseDaoImpi(TableInfo<T, K> tableInfo) {
        super(tableInfo);
        readOnlyDaoImpi = new ReadOnlyDaoImpi<>(tableInfo);
    }

    @Override
    protected void initTemplateConfig() {
        super.initTemplateConfig();
        readOnlyDaoImpi.setJdbcTemplate(getJdbcTemplate());
    }

    @Override
    public long replace(final T t) {
        return getJdbcTemplate().execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    String sql = tableInfo.getReplaceSql();
                    if (log.isDebugEnabled()) {
                        log.debug("replace: {}[object:{}]", sql, t);
                    }
                    ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    tableInfo.setAllPreparedStatement(t, ps, 1);
                    ps.execute();

                    long key = 0;
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getLong(1);
                    }
                    return key;
                } catch (Exception e) {
                    log.error("sql错误{}", t, e);
                    throw e;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public long insert(final T t) {
        return getJdbcTemplate().execute(new ConnectionCallback<Long>() {
            @Override
            public Long doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    String sql = tableInfo.getInsertSql();
                    if (log.isDebugEnabled()) {
                        log.debug("insert: {}[object:{}]", sql, t);
                    }
                    ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    tableInfo.setPreparedStatement(t, ps, 1, true);
                    ps.execute();
                    long key = 0;
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getLong(1);
                    }
                    return key;
                } catch (Exception e) {
                    log.error("sql错误{}", t, e);
                    throw e;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean update(final T t) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("update: {}[nums:{}]", tableInfo.getUpdateSql(), t.toString());
                    }
                    ps = con.prepareStatement(tableInfo.getUpdateSql());

                    int i = 1;
                    i = tableInfo.setPreparedStatement(t, ps, i, true);
                    //设置 WHERE
                    tableInfo.setPreparedStatementKeys(t, ps, i);
                    return ps.executeUpdate() > 0;
                } catch (Exception e) {
                    log.error("sql错误", e);
                    throw e;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean del(final K key) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("del: {}[map:{}]", tableInfo.getKeyDeleteSql(), key);
                    }
                    ps = con.prepareStatement(tableInfo.getKeyDeleteSql());
                    tableInfo.setKeyPreparedStatement(key, ps, 1);
                    return ps.executeUpdate() > 0;
                } catch (Exception e) {
                    log.error("sql错误", e);
                    throw e;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean del(final String key0, final Object value0) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    String tableName = tableInfo.getDbTableName();
                    String sql = "DELETE FROM `" + tableName + "` WHERE `" + SqlUtils.nameFilter(key0) + "` = ?";

                    if (log.isDebugEnabled()) {
                        log.debug("del by {}: {}[map:{}]", tableName, sql, value0);
                    }
                    ps = con.prepareStatement(sql);
                    ps.setObject(1, value0);

                    return ps.executeUpdate() > 0;
                } catch (Exception e) {
                    log.error("sql错误", e);
                    throw e;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }
//
//    /**
//     * 更具排序删除
//     * delete xxx from xxx order by b limit n;
//     */
//    @Override
//    public boolean del(final String key0, final Object value0, final Order order, final int n) {
//        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
//            @Override
//            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
//                PreparedStatement ps = null;
//                try {
//                    String tableName = tableInfo.getDbTableName();
//                    String sql = "DELETE FROM `" + tableName + "` WHERE `" + key0 + "` = ? " + order.toSql() + " limit " + n;
//
//                    if (log.isDebugEnabled()) {
//                        log.debug("del by {}: {}[map:{}]", tableName, sql, value0);
//                    }
//                    ps = con.prepareStatement(sql);
//                    ps.setObject(1, value0);
//
//                    return ps.executeUpdate() > 0;
//                } catch (Exception e) {
//                    log.error("sql错误", e);
//                    throw e;
//                } finally {
//                    JdbcUtils.closeStatement(ps);
//                }
//            }
//        });
//    }

//    @Override
//    public int insert(final List<T> list) {
//        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
//            @Override
//            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
//                PreparedStatement ps = null;
//                try {
//                    if (!list.isEmpty()) {
//                        T f = list.get(0);
//                        String sql = tableInfo.getInsertSql();
//                        if (log.isDebugEnabled()) {
//                            log.debug("inserts: {}; nums:{}", sql, list.size());
//                        }
//                        ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//
//                        for (T t : list) {
//                            tableInfo.setPreparedStatement(t, ps, 1, true);
//                            ps.addBatch();
//                        }
//                        int[] ints = ps.executeBatch();
//                        return MathUtils.sum(ints);
//                    }
//                    return 0;
//                } catch (Exception e) {
//                    log.error("sql错误", e);
//                    throw e;
//                } finally {
//                    JdbcUtils.closeStatement(ps);
//                }
//            }
//        });
//    }

    @Override
    public int insert(final List<T> list) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    if (!list.isEmpty()) {
//                        T f = list.get(0);
                        StringBuilder sb = new StringBuilder(tableInfo.getFastInsertPrefixSql());

                        for (int i = 0; i < list.size(); i++) {
                            if (i != 0) {
                                sb.append(",");
                            }
                            sb.append(tableInfo.getFastInsertValueItemsSql());
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("fastInsert: {}; nums:{}", sb.toString(), list.size());
                        }
                        ps = con.prepareStatement(sb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                        int i = 1;
                        for (T t : list) {
                            i = tableInfo.setPreparedStatement(t, ps, i, true);
                        }
                        return ps.executeUpdate();
                    }
                    return 0;
                } catch (Exception e) {
                    log.error("sql错误", e);
                    throw e;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean incrementUpdatePartial(final Map<String, Object> m, final K key) {
        StringBuilder sb = new StringBuilder();

        sb.append(tableInfo.getKeyUpdatePartialPrefixSql());
        ArrayList<Object> args = BaseDaoImpi.this.toIncrementSqlSet(sb, m);
        sb.append(tableInfo.getKeyWhereByKeySql());
        final String sql = sb.toString();


        Collections.addAll(args, key.getQueryParams());
        return updatePartial(sql, args);
    }

    @Override
    public boolean updatePartial(final Map<String, Object> m, final K key) {
        StringBuilder sb = new StringBuilder();

        sb.append(tableInfo.getKeyUpdatePartialPrefixSql());
        ArrayList<Object> args = BaseDaoImpi.this.toSqlSet(sb, m);
        sb.append(tableInfo.getKeyWhereByKeySql());
        final String sql = sb.toString();


        Collections.addAll(args, key.getQueryParams());
        return updatePartial(sql, args);
    }

    private boolean updatePartial(final String sql, final ArrayList<Object> args) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("updatePartial: {}; args:{}", sql, args);
                    }
                    ps = con.prepareStatement(sql);

                    int i = 1;

                    for (Object arg : args) {
                        ps.setObject(i, arg);
                        i++;
                    }
                    return ps.executeUpdate() > 0;
                } catch (Exception e) {
                    log.error("sql错误", e);
                    throw e;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    /**
     * 转换map成为UPDATE 一句里面的SET短
     * 例如:`content`='11',`type`=1
     */
    private ArrayList<Object> toIncrementSqlSet(StringBuilder sb, Map<String, Object> m) {
        boolean isAdd = false;
        ArrayList<Object> list = new ArrayList<>();
        for (Map.Entry<String, Object> e : m.entrySet()) {
            String name = SqlUtils.nameFilter(e.getKey());
            list.add(e.getValue());

            if (isAdd) {
                sb.append(',');
            } else {
                isAdd = true;
            }
            sb.append('`');
            sb.append(name);
            sb.append("` = `");
            sb.append(name);
            sb.append("` + ?");

        }
        return list;
    }


    private ArrayList<Object> toSqlSet(StringBuilder sb, Map<String, Object> m) {
        boolean isAdd = false;
        ArrayList<Object> list = new ArrayList<>();
        for (Map.Entry<String, Object> e : m.entrySet()) {
            String name = SqlUtils.nameFilter(e.getKey());
            list.add(e.getValue());

            if (isAdd) {
                sb.append(',');
            } else {
                isAdd = true;
            }
            sb.append('`');
            sb.append(name);
            sb.append("` = ?");
        }
        return list;
    }

    @Override
    public T get(K k) {
        return readOnlyDaoImpi.get(k);
    }

    @Override
    public long getCount() {
        return readOnlyDaoImpi.getCount();
    }

    @Override
    public long getCount(QueryParams params) {
        return readOnlyDaoImpi.getCount(params);
    }

    @Override
    public long getCount(Select select) {
        return readOnlyDaoImpi.getCount(select);
    }

    @Override
    public PageResult<T> findPage(QueryParams params, Order order, int page, int pageSize) {
        return readOnlyDaoImpi.findPage(params, order, page, pageSize);
    }

    @Override
    public PageResult<T> findPage(Select select, int page, int pageSize) {
        return readOnlyDaoImpi.findPage(select, page, pageSize);
    }

    @Override
    public <C> PageResult<C> findPageBySelect(Select select, RowMapper<C> rowMapper, int page, int pageSize) {
        return readOnlyDaoImpi.findPageBySelect(select, rowMapper, page, pageSize);
    }

    @Override
    public List<T> find(int max, QueryParams params, Order order) {
        return readOnlyDaoImpi.find(max, params, order);
    }

    @Override
    public List<T> find(int max, Select select) {
        return readOnlyDaoImpi.find(max, select);
    }

    @Override
    public <C> List<C> findBySelect(int max, Select select, RowMapper<C> rowMapper) {
        return readOnlyDaoImpi.findBySelect(max, select, rowMapper);
    }

    @Override
    public T findObject(QueryParams params) {
        return readOnlyDaoImpi.findObject(params);
    }


//    protected int updatePartial(final Map<String, Object> m, final QueryParams wh, final String sql) {
//        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
//            @Override
//            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
//                PreparedStatement ps = null;
//                try {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(sql);
//                    BaseDaoImpi.this.toSqlSet(sb, m);
//                    wh.toSql(sb);
//
//                    if (log.isDebugEnabled()) {
//                        log.debug("updatePartial: {}[map:{}]", sb.toString(), m.toString());
//                    }
//                    ps = con.prepareStatement(sb.toString());
//
//                    int i = 1;
//                    for (Map.Entry<String, Object> e : m.entrySet()) {
//                        ps.setObject(i, e.getValue());
//                        i++;
//                    }
//                    wh.setPreparedStatement(ps, i);
//                    return ps.executeUpdate();
//                } catch (Exception e) {
//                    log.error("sql错误", e);
//                    throw e;
//                } finally {
//                    JdbcUtils.closeStatement(ps);
//                }
//            }
//        });
//    }

//    public Void execute(final String sql, final List<Object> m) {
//        return getJdbcTemplate().execute(new ConnectionCallback<Void>() {
//            @Override
//            public Void doInConnection(Connection con) throws SQLException, DataAccessException {
//                PreparedStatement ps = null;
//                try {
//                    if (log.isDebugEnabled()) {
//                        log.debug("execute: {};map:{}", sql, m);
//                    }
//                    ps = con.prepareStatement(sql);
//
//                    for (int j = 1; j <= m.size(); j++) {
//                        ps.setObject(j, m.get(j));
//                    }
//
//                    ps.execute();
//                    return null;
//                } catch (Exception e) {
//                    log.error("sql错误", e);
//                    throw e;
//                } finally {
//                    JdbcUtils.closeStatement(ps);
//                }
//            }
//        });
//    }
}