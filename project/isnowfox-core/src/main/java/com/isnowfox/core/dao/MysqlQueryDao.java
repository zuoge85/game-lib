package com.isnowfox.core.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.support.JdbcUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isnowfox.core.PageResult;
import com.isnowfox.core.dao.grid.Column;
import com.isnowfox.core.dao.grid.Columns;
import com.isnowfox.core.dao.grid.GridPageResult;


public class MysqlQueryDao extends MysqlJdbcDaoSupport implements SqlQueryDao{
	
	@Override
	public PageResult<Map<String,Object>> queryData(final Columns columns,final String sql,final int page,final int pageSize){
		return getJdbcTemplate().execute(new ConnectionCallback<PageResult<Map<String,Object>>>() {
			@Override
			public PageResult<Map<String,Object>> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				Statement ps=null;
				ResultSet rs=null;
				try{
					ps=con.createStatement();
					rs=ps.executeQuery(sql);
					//获取当前页需要的数据
					//GridPageResult<Map> result=GridPageResult<T>.createGridPage(count, page, pageSize, columns)
					int start=(page-1)*pageSize+1;
					List<Map<String,Object>> list=Lists.newArrayList();
					if(rs.absolute(start)){
						int i=0;
						do{
							Map<String,Object> map=Maps.newHashMap();
							for (Column c:columns) {
								map.put(c.getCode(), rs.getObject(c.getCode()));
							}
							list.add(map);
							i++;
						}while(rs.next()&&i<pageSize);
					}
					if(!rs.isLast()){
						rs.last();
					}
					int row=rs.getRow();
					PageResult<Map<String,Object>> result=PageResult.createPage(row, page, pageSize,list);
					return result;
					//结束当前页数据
				}finally{
					JdbcUtils.closeResultSet(rs);
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
	
	@Override
	public GridPageResult<Map<String,Object>> query(final String sql,final int page,final int pageSize){
		return getJdbcTemplate().execute(new ConnectionCallback<GridPageResult<Map<String,Object>>>() {
			@Override
			public GridPageResult<Map<String,Object>> doInConnection(Connection con) throws SQLException,
					DataAccessException {
				Statement ps=null;
				ResultSet rs=null;
				try{
					ps=con.createStatement();
					rs=ps.executeQuery(sql);
					//获取反射信息
					ResultSetMetaData metaData=rs.getMetaData();
					int count=metaData.getColumnCount();
					Columns columns=new Columns();
					for(int i=1;i<=count;i++){
						//
//						System.out.println(String.format("ColumnClassName:%s,ColumnDisplaySize:%d," +
//								"ColumnLabel:%s,ColumnName:%s,ColumnType:%d,ColumnTypeName:%s," +
//								"Precision:%d,Scale:%d,SchemaName:%s,TableName:%s",
//								metaData.getColumnClassName(i),metaData.getColumnDisplaySize(i),
//								metaData.getColumnLabel(i),metaData.getColumnName(i),
//								metaData.getColumnType(i),metaData.getColumnTypeName(i),
//								metaData.getPrecision(i),metaData.getScale(i),
//								metaData.getSchemaName(i),metaData.getTableName(i)));
						columns.add(new Column(metaData.getColumnLabel(i), metaData.getColumnLabel(i), metaData.getColumnType(i), metaData.getColumnClassName(i),metaData.getColumnDisplaySize(i)));
					}
					//获取当前页需要的数据
					//GridPageResult<Map> result=GridPageResult<T>.createGridPage(count, page, pageSize, columns)
					int start=(page-1)*pageSize+1;
					List<Map<String,Object>> list=Lists.newArrayList();
					if(rs.absolute(start)){
						int i=0;
						do{
							Map<String,Object> map=Maps.newHashMap();
							for (Column c:columns) {
								map.put(c.getCode(), rs.getObject(c.getCode()));
							}
							list.add(map);
							i++;
						}while(rs.next()&&i<pageSize);
					}
					if(!rs.isLast()){
						rs.last();
					}
					int row=rs.getRow();
					GridPageResult<Map<String,Object>> result=GridPageResult.createGridPage(row, page, pageSize, columns,list);
					return result;
					//结束当前页数据
				} catch (ClassNotFoundException e) {
					throw new SQLException("驱动返回错误的Class,通常不可能产生这样的错误.",e);
				}finally{
					JdbcUtils.closeResultSet(rs);
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
}
