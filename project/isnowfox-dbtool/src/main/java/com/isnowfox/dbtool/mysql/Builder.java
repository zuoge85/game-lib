package com.isnowfox.dbtool.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Builder {
	private static final Logger log = LoggerFactory.getLogger(Builder.class);
	private Config config;
	//private Connection conn;
	private List<Table> list;
	public Builder(Config config,Connection conn) throws SQLException{
		this.config=config;
		//this.conn=conn;
		//初始化
		log.info("初始化数据库信息");
		DatabaseMetaData dm=conn.getMetaData();
		log.info("数据库系统名词:"+dm.getDatabaseProductName());
		log.info("数据库系统版本:"+dm.getDatabaseProductVersion());
		ResultSet rs=dm.getTables(null, null, null, null);
		list=Lists.newArrayList();
		while(rs.next()){
			String type=String.valueOf(rs.getObject("TABLE_TYPE"));
			String name=String.valueOf(rs.getObject("TABLE_NAME"));
			String remark=String.valueOf(rs.getObject("REMARKS"));
			if(type.equals("TABLE")){
				Table t=new Table(conn,dm,name,type,remark);
				list.add(t);
			}
		}
		rs.close();
	}
	public void objectCreate() throws ResourceNotFoundException, ParseErrorException, Exception{
		ObjectCreate.create(list,config,config.getObjectPack());
	}
	public void daoCreate() throws ResourceNotFoundException, ParseErrorException, Exception{
		DaoCreate.create(list,config,config.getObjectPack(),config.getDaoPack());
	}
	public void daoImplCreate() throws ResourceNotFoundException, ParseErrorException, Exception{
		DaoImplCreate.create(list,config,config.getObjectPack(),config.getDaoPack(),null);
	}
	public void springXmlCreate() throws ResourceNotFoundException, ParseErrorException, Exception{
		SpringXmlCreate.create(list,config,config.getObjectPack(),config.getDaoPack(),"impl");
	}
}
