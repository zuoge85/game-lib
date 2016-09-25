package com.isnowfox.core.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends EntityObject>  extends MysqlJdbcDaoSupport {
	protected static final Logger log = LoggerFactory.getLogger(BaseDao.class);
	public BaseDao() {
		
	}
//	static ExecutorService executor = Executors.newFixedThreadPool(10);
//	protected static ExecutorService executor() {
//		return executor;
//	}
//	public static void waitCloseAsync() throws InterruptedException{
//		executor.shutdown();
//		log.info("等待executor关闭最迟10分钟");
//		executor.awaitTermination(10*60, TimeUnit.SECONDS);
//		log.info("executor关闭成功");
//		executor = null;
//	}
	
//	/**
//	 * 替换和update是一个级别的
//	 * @author zuoge85
//	 *
//	 */
//	public enum AsyncType{
//		DEL,UPDATE,INSERT
//	}
//	private  abstract class AsyncRunnable implements Runnable{
//		private T t;
//		private AsyncType type;
//		public AsyncRunnable(final T t,AsyncType type) {
//			this.t = t;
//			this.type = type;
//		}
//		@Override
//		public void run() {
//			int version = t.getDaoVersion();
//			if (t.isDaoDelete()) {
//				del(t.getKey());
//			}else{
//				try{
//					execute();
//				}finally{
//					if (t.isDaoDelete()) {
//						t.daoSaveCompare(version);
//						del(t.getKey());
//					}else{
//						if (t.daoSaveCompare(version)) {
//							//继续执行保存
//							if (type == AsyncType.INSERT  || type == AsyncType.UPDATE) {
//								log.debug("需要继续保存:" + t);
//								asyncUpdate(t);
//							}else{
//								//删除之后不用做任何保存处理
//							}
//						}
//					}
//				}
//			}
//		}
//		abstract void execute();
//	}
//
//	public void asyncCall(AsyncRunnable r) {
//		try {
//			executor().execute(r);
//		} catch (Exception e) {
//			log.error("异步调用错误",e);
//		}
//	}
//
//	public void asyncInsert(final T t) {
//		log.debug("asyncInsert{}",t);
//		if (t.daoTrySave()) {
//			this.asyncCall(new AsyncRunnable(t,AsyncType.INSERT) {
//				@Override
//				public void execute() {
//					insert(t);
//				}
//			});
//		}else{
//			log.error("错误的保存过程,在插入数据之前执行了update:{}",t);
//			throw new RuntimeException("错误的保存过程,在插入数据之前执行了update:{}");
//		}
//	}
//
//	public void asyncUpdate(final T t) {
//		log.debug("asyncUpdate{}",t);
//		if (t.daoTrySave()) {
//			this.asyncCall(new AsyncRunnable(t,AsyncType.UPDATE) {
//				@Override
//				public void execute() {
//					update(t);
//				}
//			});
//		}
//	}
//
//	public void asyncDel(final T t) {
//		log.debug("asyncDel{}",t);
//		t.daoDelete();
//		if (t.daoTrySave()) {
//			this.asyncCall(new AsyncRunnable(t,AsyncType.DEL) {
//				@Override
//				public void execute() {
//					del(t.getKey());
//				}
//			});
//		}
//	}
	
	public long insert(final T t) {
		return getJdbcTemplate().execute(new ConnectionCallback<Long>() {
			@Override
			public Long doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				ResultSet rs=null;
				try{
					TableInfo<EntityObject, KeyObject> tableInfo = t.getTableInfo();
					if(log.isDebugEnabled()){
						log.debug("insert: {}[object:{}]" ,tableInfo.getInsertSql(),t);
					}
					ps=con.prepareStatement(tableInfo.getInsertSql(),PreparedStatement.RETURN_GENERATED_KEYS);
					
					tableInfo.setPreparedStatement(t, ps, 1, true);
					ps.execute();
					long key=0;
					rs=ps.getGeneratedKeys();
					if (rs.next()) {
						key=rs.getLong(1);
					}
					return key;
				}catch(Exception e){
					log.error("sql错误{}",t,e);
					throw e;
				}finally{
					JdbcUtils.closeResultSet(rs);
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
	
	public boolean update(final T t) {
		return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					TableInfo<EntityObject, KeyObject> tableInfo = t.getTableInfo();
					if(log.isDebugEnabled()){
						log.debug("update: {}[nums:{}]", tableInfo.getUpdateSql(), t.toString());
					}
					ps=con.prepareStatement(tableInfo.getUpdateSql());
					
					int i = 1;
					i = tableInfo.setPreparedStatement(t, ps, i, false);
					//设置 WHERE
					tableInfo.setPreparedStatementKeys(t, ps, i);
					return ps.executeUpdate()>0;
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
	
	public boolean del(final KeyObject key) {
		return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					TableInfo<EntityObject, KeyObject> tableInfo = key.getTableInfo();
					if(log.isDebugEnabled()){
						log.debug("del: {}[map:{}]",tableInfo.getKeyDeleteSql(), key);
					}
					ps=con.prepareStatement(tableInfo.getKeyDeleteSql());
					tableInfo.setKeyPreparedStatement(key ,ps, 1);
					return ps.executeUpdate() > 0;
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
	

//	private int insert(final List<T> list) {
//		return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
//			@Override
//			public Integer doInConnection(Connection con) throws SQLException,
//					DataAccessException {
//				PreparedStatement ps=null;
//				try{
//					if(!list.isEmpty()){
//						T f = list.get(0);
//						if(log.isDebugEnabled()){
//							log.debug("insert batch: {}[nums:{}]" ,f.getInsertSql(),list.size());
//						}
//						ps=con.prepareStatement(f.getInsertSql(),PreparedStatement.RETURN_GENERATED_KEYS);
//						
//						for(T t:list){
//							t.setPreparedStatement(ps, 1, true);
//							ps.addBatch();
//						}
//						int[] ints=ps.executeBatch();
//						return MathUtils.sum(ints);
//					}
//					return 0;
//				}catch(Exception e){
//					log.error("sql错误",e);
//					throw e;
//				}finally{
//					JdbcUtils.closeStatement(ps);
//				}
//			}
//		});
//	}
//	
//	public int fastInsert(final List<T> list) {
//		return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
//			@Override
//			public Integer doInConnection(Connection con) throws SQLException,
//					DataAccessException {
//				PreparedStatement ps=null;
//				try{
//					if(!list.isEmpty()){
//						T f = list.get(0);
//						StringBuilder sb=new StringBuilder(f.getFastInsertPrefixSql());
//						
//						for(int i=0;i<list.size();i++){
//							if(i!=0){
//								sb.append(",");
//							}
//							sb.append(f.getFastInsertValueItemSql());
//						}
//						if(log.isDebugEnabled()){
//							log.debug("fast insert: {}[nums:{}]" ,sb.toString(),list.size());
//						}
//						ps=con.prepareStatement(sb.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
//						
//						int i=1;
//						for(T t:list){
//							i = t.setPreparedStatement(ps, i, true);
//						}
//						return ps.executeUpdate();
//					}
//					return 0;
//				}catch(Exception e){
//					log.error("sql错误",e);
//					throw e;
//				}finally{
//					JdbcUtils.closeStatement(ps);
//				}
//			}
//		});
//	}
//	public void asyncUpdate(final String name,final  Object value, final KeyObject key) {
//		this.asyncCall(new Runnable() {
//			@Override
//			public void run() {
//				updatePartial(name,value,key);
//			}
//		});
//	}
//	
	public boolean updatePartial(final String name,final  Object value, final KeyObject key) {
		return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					StringBuilder sb=new StringBuilder();
                    TableInfo<EntityObject, KeyObject> tableInfo = key.getTableInfo();

                    sb.append(tableInfo.getKeyUpdatePartialPrefixSql());
					sb.append('`');
					sb.append(name);
					sb.append("` =? ");
					sb.append(key.getTableInfo().getKeyWhereByKeySql());
					if(log.isDebugEnabled()){
						log.debug("updatePartial: {}[value:{},key:{}]",sb.toString(), name, value.toString());
					}

					ps=con.prepareStatement(sb.toString());

					ps.setObject(1,value);
					key.getTableInfo().setKeyPreparedStatement(key, ps, 2);
					return ps.executeUpdate()>0;
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}

	public boolean updatePartial(final Map<String, Object> m, final KeyObject key) {
		return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					StringBuilder sb=new StringBuilder();
                    TableInfo<EntityObject, KeyObject> tableInfo = key.getTableInfo();

					sb.append(tableInfo.getKeyUpdatePartialPrefixSql());
					toSqlSet(sb,m);
					sb.append(tableInfo.getKeyWhereByKeySql());

					if(log.isDebugEnabled()){
						log.debug("updatePartial: {}[map:{}]",sb.toString(), m.toString());
					}
					ps=con.prepareStatement(sb.toString());

					int i=1;
					for (Map.Entry<String, Object> e:m.entrySet()) {
						ps.setObject(i,e.getValue());
						i++;
					}
                    tableInfo.setKeyPreparedStatement(key, ps, i);
					return ps.executeUpdate()>0;
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}

	protected int updatePartial(final Map<String, Object> m, final QueryParams wh,final String sql) {
		return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {
			@Override
			public Integer doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					StringBuilder sb=new StringBuilder();
					sb.append(sql);
					toSqlSet(sb,m);
					wh.toSql(sb);

					if(log.isDebugEnabled()){
						log.debug("updatePartial: {}[map:{}]",sb.toString(), m.toString());
					}
					ps=con.prepareStatement(sb.toString());

					int i=1;
					for (Map.Entry<String, Object> e:m.entrySet()) {
						ps.setObject(i, e.getValue());
						i++;
					}
					wh.setPreparedStatement(ps, i);
					return ps.executeUpdate();
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}

	public Void execute(final String sql, final List<Object> m) {
		return getJdbcTemplate().execute(new ConnectionCallback<Void>() {
			@Override
			public Void doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement ps=null;
				try{
					if(log.isDebugEnabled()){
						log.debug("execute: {}[map:{}]",sql, m);
					}
					ps = con.prepareStatement(sql);

					for (int j = 1; j <= m.size(); j++) {
						ps.setObject(j, m.get(j));
					}

					ps.execute();
					return null;
				}catch(Exception e){
					log.error("sql错误",e);
					throw e;
				}finally{
					JdbcUtils.closeStatement(ps);
				}
			}
		});
	}
}