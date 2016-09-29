package org.forkjoin.jdbckit.mysql;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Builder {
    private static final Logger log = LoggerFactory.getLogger(Builder.class);
    private Config config;
    //private Connection conn;
    private List<Table> list;

    public Builder(Config config, Connection conn) throws SQLException {
        this.config = config;
        //this.conn=conn;
        //初始化
        log.info("初始化数据库信息");
        DatabaseMetaData dm = conn.getMetaData();
        log.info("数据库系统名词:" + dm.getDatabaseProductName());
        log.info("数据库系统版本:" + dm.getDatabaseProductVersion());
        ResultSet rs = dm.getTables(null, null, null, null);
        list = Lists.newArrayList();
        while (rs.next()) {
            String type = String.valueOf(rs.getObject("TABLE_TYPE"));
            String name = String.valueOf(rs.getObject("TABLE_NAME"));
            String remark = String.valueOf(rs.getObject("REMARKS"));
            if (type.equals("TABLE")) {
                Table t = new Table(config.getTablePrefix(), conn, dm, name, type, remark);
                list.add(t);
            }
        }
        rs.close();
    }

    public void objectCreate() throws Exception {
        ObjectCreate.create(list, config, config.getObjectPack());
    }

    public void daoImplCreate() throws Exception {
        DaoImplCreate.create(list, config, config.getObjectPack(), config.getDaoPack(), null);
    }

    public void springXmlCreate() throws Exception {
        SpringXmlCreate.create(SpringXmlCreate.Type.BASE, list, config, config.getObjectPack(), config.getDaoPack(), "impl");
    }

    public void readOnlyDaoImplCreate() throws Exception {
        ReadOnlyDaoImplCreate.create(list, config, config.getObjectPack(), config.getDaoPack() + ".readonly", null);
    }

    public void readOnlySpringXmlCreate() throws Exception {
        SpringXmlCreate.create(SpringXmlCreate.Type.READ_ONLY, list, config, config.getObjectPack(), config.getDaoPack(), "impl");
    }

    public void cacheDaoImplCreate() throws Exception {
        CacheDaoImplCreate.create(list, config, config.getObjectPack(), config.getDaoPack() + ".cache", null);
    }

    public void cacheSpringXmlCreate() throws Exception {
        SpringXmlCreate.create(SpringXmlCreate.Type.CACHE, list, config, config.getObjectPack(), config.getDaoPack(), "impl");
    }
}
