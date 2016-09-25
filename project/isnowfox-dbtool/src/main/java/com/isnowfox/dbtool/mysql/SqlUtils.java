package com.isnowfox.dbtool.mysql;

public class SqlUtils {
	/**
	 * 生产类似下面的语句
	 * "INSERT INTO `activity` (`gid`,`sid`,`content`,`type`,`create_time`) VALUES (?,?,?,?,?)"
	 * @param t
	 * @return
	 */
	public String toInsert(Table t){
		StringBuilder sb=new StringBuilder("INSERT INTO `");
		sb.append(t.getDbName());
		sb.append("` (");
		toSqlColumn(t,sb);
		sb.append(") VALUES (");
		toSqlColumnPlaceholder(t,sb);
		sb.append(')');
		return sb.toString();
	}
	
	/**
	 * 
	 * "INSERT INTO `activity` (`gid`,`sid`,`content`,`type`,`create_time`) VALUES "
	 */
	public String toInsertPrefix(Table t){
		StringBuilder sb=new StringBuilder("INSERT INTO `");
		sb.append(t.getDbName());
		sb.append("` (");
		toSqlColumn(t,sb);
		sb.append(") VALUES ");
		return sb.toString();
	}
	/**
	 * " (?,?,?,?,?) ";
	 */
	public String toInsertValueItem(Table t){
		StringBuilder sb=new StringBuilder(" (");
		toSqlColumnPlaceholder(t,sb);
		sb.append(") ");
		return sb.toString();
	}
	/**
	 * "UPDATE `activity` SET `gid`=?,`sid`=?,`content`=?,`type`= ?,`create_time` = ? WHERE `id` = ?"
	 */
	public String toUpdateTable(Table t){
		StringBuilder sb=new StringBuilder("UPDATE `");
		sb.append(t.getDbName());
		sb.append("` SET ");
		toSqlSetlColumn(t,sb);
		sb.append(" WHERE ");
		toSqlKeyWhere(t,sb);
		return sb.toString();
	}
	
	/**
	 * "UPDATE `activity` SET ";
	 */
	public String toUpdatePartialPrefix(Table t){
		StringBuilder sb=new StringBuilder("UPDATE `");
		sb.append(t.getDbName());
		sb.append("` SET ");
		return sb.toString();
	}
	
	/**
	 * " WHERE `id` = ?"
	 */
	public String toWhereByKey(Table t){
		StringBuilder sb=new StringBuilder(" WHERE ");
		toSqlKeyWhere(t,sb);
		return sb.toString();
	}
	
	/**
	 * DELETE FROM `activity` WHERE `id`=?
	 */
	public String toDelete(Table t){
		StringBuilder sb=new StringBuilder("DELETE FROM `");
		sb.append(t.getDbName());
		sb.append("` WHERE ");
		toSqlKeyWhere(t,sb);
		return sb.toString();
	}
	
	/**
	 * "SELECT * FROM `activity` WHERE `id`=? ORDER BY `id` DESC";
	 */
	public String toSelectByKey(Table t){
		StringBuilder sb=new StringBuilder("SELECT * FROM `");
		sb.append(t.getDbName());
		sb.append("` WHERE ");
		toSqlKeyWhere(t,sb);
		if (t.isKey()) {
			sb.append(" ORDER BY ");
		}
		toSqlOrderByKey(t,sb);
		return sb.toString();
	}
	
	/**
	 * "SELECT count(*) FROM `activity`";
	 */
	public String toSelectCount(Table t){
		StringBuilder sb=new StringBuilder("SELECT count(*) FROM `");
		sb.append(t.getDbName());
		sb.append('`');
		return sb.toString();
	}
	
	/**
	 * "SELECT %s FROM `activity` ORDER BY `id` DESC";
	 */
	public String toFormatSelectAll(Table t){
		StringBuilder sb=new StringBuilder("SELECT %s FROM `");
		sb.append(t.getDbName());
		sb.append('`');
		if (t.isKey()) {
			sb.append(" ORDER BY ");
		}
		toSqlOrderByKey(t,sb);
		return sb.toString();
	}
	
	/**
	 * "SELECT %s FROM `activity` ";
	 */
	public String toFormatSelectPrefix(Table t){
		StringBuilder sb=new StringBuilder("SELECT %s FROM `");
		sb.append(t.getDbName());
		sb.append("` ");
		return sb.toString();
	}
	
	/**
	 * "SELECT * FROM `activity` ";
	 */
	public String toSelectPrefix(Table t){
		StringBuilder sb=new StringBuilder("SELECT * FROM `");
		sb.append(t.getDbName());
		sb.append("` ");
		return sb.toString();
	}
	/**
	 * " ORDER BY `id` DESC";
	 */
	public String toOrderBykey(Table t){
		StringBuilder sb=new StringBuilder(" ORDER BY ");
		toSqlOrderByKey(t,sb);
		return sb.toString();
	}

	public void toSqlOrderByKey(Table t,StringBuilder sb){
		boolean first=true;
		for (Column c:t.getKeyColumns()){
			if(first){
				first=false;
			}else{
				sb.append(",");
			}
			sb.append('`');
			sb.append(c.getDbName());
			sb.append("` DESC");
		}
	}
	public void toSqlKeyWhere(Table t,StringBuilder sb){
		boolean first=true;
		for (Column c:t.getKeyColumns()){
			if(first){
				first=false;
			}else{
				sb.append(" AND ");
			}
			sb.append('`');
			sb.append(c.getDbName());
			sb.append("`=?");
		}
	}
	public void toSqlSetlColumn(Table t,StringBuilder sb){
		if(t.getAllColumns().isEmpty()){
			return;
		}
		boolean del=false;
		for (Column c:t.getNoIdColumns()) {
			if(!c.isUnique()){
				sb.append('`');
				sb.append(c.getDbName());
				sb.append("`=?,");
				del=true;
			}
		}
		if(del){
			sb.setLength(sb.length()-1);
		}
	}
	public void toSqlColumn(Table t,StringBuilder sb){
		if(t.getAllColumns().isEmpty()){
			return;
		}
		boolean del=false;
		for (Column c:t.getAllColumns()) {
			if(!c.isAutoIncrement()){
				sb.append('`');
				sb.append(c.getDbName());
				sb.append("`,");
				del=true;
			}
		}
		if(del){
			sb.setLength(sb.length()-1);
		}
	}
	//占位符
	public void toSqlColumnPlaceholder(Table t,StringBuilder sb){
		if(t.getAllColumns().isEmpty()){
			return;
		}
		boolean del=false;
		for (Column c:t.getAllColumns()) {
			if(!c.isAutoIncrement()){
				sb.append("?,");
				del=true;
			}
		}
		if(del){
			sb.setLength(sb.length()-1);
		}
	}
}
