package com.isnowfox.core.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 实现一系列函数,
 * dao通过这些函数让异步存储逻辑变成存储队列,这样可以避免脏数据保存到内存!
 * @author zuoge85
 *
 */
public interface TableInfo<T extends EntityObject, K extends KeyObject>{
	String getInsertSql();
	String getFastInsertPrefixSql();
	String getFastInsertValueItemSql();
	String getUpdateSql();
	int setPreparedStatement(T t,PreparedStatement ps, int i, boolean isSetUnique) throws SQLException;
	int setPreparedStatementKeys(T t,PreparedStatement ps, int i) throws SQLException;
	

	String getKeyUpdatePartialPrefixSql();
	String getKeyWhereByKeySql();
	String getKeyDeleteSql();
	int setKeyPreparedStatement(K k,PreparedStatement ps, int i) throws SQLException;
	String getSelectByKeySql();
	String getSelectCountSql();
	String getFormatSelectSql();
	String getFormatSelectPrefixSql();
	String getSelectPrefixSql();
	String getOrderByIdDescSql();
	RowMapper<T> getRowMapper();
	<C extends T> RowMapper<C> getRowMapper(Class<C> cls);
}