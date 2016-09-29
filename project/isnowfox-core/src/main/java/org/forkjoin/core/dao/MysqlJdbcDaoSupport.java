package org.forkjoin.core.dao;


import com.google.common.collect.Lists;
import com.isnowfox.core.PageResult;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlJdbcDaoSupport extends org.springframework.jdbc.core.support.JdbcDaoSupport {
    public static final int SQL_MAX = 1000;
    public static final String STRING_COUNT = " count(1) ";
//	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
//		List<T> results = query(sql, args, new RowMapperResultSetExtractor<T>(rowMapper, 1));
//		return DataAccessUtils.requiredSingleResult(results);
//	}

    /**
     * 转换map成为UPDATE 一句里面的SET短
     * 例如:`content`='11',`type`=1
     */
    public void toSqlSet(StringBuilder sb, Map<String, Object> m) {
        boolean isAdd = false;
        for (Map.Entry<String, Object> e : m.entrySet()) {
            String name = e.getKey();
            if (isAdd) {
                sb.append(',');
            } else {
                isAdd = true;
            }
            sb.append('`');
            sb.append(name);
            sb.append("` = ?");
        }
    }

    public <T> T querySingle(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> results = getJdbcTemplate().query(sql, args, new RowMapperResultSetExtractor<T>(rowMapper, 1));
        return DataAccessUtils.singleResult(results);
    }

//	public <T> List<T> find(final String sql,final RowMapper<T> rowMapper,final Object ...objs){
//		return getJdbcTemplate().query(sql, objs, rowMapper);
//	}

    /**
     * sql语句类似这种
     * SELECT %s FROM `activity` WHERE id=?
     * 这个查询为了简单,但是比较耦合
     */
    protected <T> PageResult<T> fastQueryPage(final String sql, final RowMapper<T> rowMapper, final int page, final int pageSize, final Object... objs) {
        return getJdbcTemplate().execute(new ConnectionCallback<PageResult<T>>() {
            @Override
            public PageResult<T> doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = con.prepareStatement(String.format(sql, STRING_COUNT));
                    int i = 1;
                    for (Object o : objs) {
                        ps.setObject(i++, o);
                    }
                    rs = ps.executeQuery();
                    int count;
                    if (rs.next()) {
                        count = Integer.valueOf(rs.getObject(1).toString());
                    } else {
                        throw new ResultPageDataAccessException("查询count失败!");
                    }
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);

                    PageResult<T> pageResult = PageResult.createPage(count, page, pageSize, null);

                    List<T> list = Lists.newArrayListWithCapacity(pageResult.getPageCount());
                    int start = pageResult.getStart();
                    ps = con.prepareStatement(String.format(sql, " * ") + " LIMIT " + start + "," + pageSize);
                    i = 1;
                    for (Object o : objs) {
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
//
//
//				//SELECT count(1) FROM (SELECT * FROM `manage`.`activity`) AS TB LIMIT 0,1
//				//PreparedStatement ps=con.prepareStatement(sql);
//				//先获取count
//				PreparedStatement ps=con.prepareStatement(sql);
//				int i=1;
//				for(Object o:objs){
//					ps.setObject(i++,o);
//				}
//				//m.put("end",pr.getStart()+pageSize);
//				//m.put("start", pr.getStart());
////				ps.setObject(i++,o);
////				ps.setObject(i++,o);
//				return null;
            }
        });
//		return execute(n, new PreparedStatementCallback<T>() {
//			public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
//				ResultSet rs = null;
//				try {
//					if (pss != null) {
//						pss.setValues(ps);
//					}
//					rs = ps.executeQuery();
//					ResultSet rsToUse = rs;
//					if (nativeJdbcExtractor != null) {
//						rsToUse = nativeJdbcExtractor.getNativeResultSet(rs);
//					}
//					return rse.extractData(rsToUse);
//				}
//				finally {
//					JdbcUtils.closeResultSet(rs);
//					if (pss instanceof ParameterDisposer) {
//						((ParameterDisposer) pss).cleanupParameters();
//					}
//				}
//			}
//		});
    }
}
