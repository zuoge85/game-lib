package com.isnowfox.dbtool.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isnowfox.dbtool.mysql.Column.OBJECT_TYPE;

/**
 * 现在只支持无主键和单主键
 * @author zuoge85@gmail.com
 *
 */
public class Table {
	private static final Pattern XML_PATTERN = Pattern.compile("XML:([^;]+);{1}(.*)");
	private static final Pattern JSON_PATTERN = Pattern.compile("JSON:([^;]+);{1}(.*)");
	private static final Logger log = LoggerFactory.getLogger(Table.class);
	private String name;
	private String type;
	private String schema;
	private String remark;
	private List<Column> allColumns=Lists.newArrayList();
	private Map<String,Column> allMap=Maps.newHashMap();
	private List<Column> noIdcolumns=Lists.newArrayList();
	private List<Column> keyColumns=Lists.newArrayList();
	private Column keyColumn;
	private boolean oneKey;
	private boolean key;
	public Table(Connection con,DatabaseMetaData dm,String name, String type, String remark) throws SQLException {
		super();
		this.name = name;
		this.type = type;
		this.remark = remark;
		
		//
		Statement st=con.createStatement();
		ResultSet rs=null;
		ResultSet r=null;
		ResultSet indexRs=null;
		try{
			log.debug("获取主键信息");
			//主键信息
			r = dm.getPrimaryKeys(null, null,name);
			while(r.next()){
				Column c=new Column();
				c.setName(String.valueOf(r.getObject("COLUMN_NAME")));
				c.setSeq(Integer.valueOf(String.valueOf(r.getObject("KEY_SEQ"))));
				keyColumns.add(c);
				allMap.put(c.getName(), c);
//				TABLE_CAT:manage
//				TABLE_SCHEM:null
//				TABLE_NAME:player
//				COLUMN_NAME:uid
//				KEY_SEQ:1
//				PK_NAME:PRIMARY
			}
			r.close();
			log.debug("执行查询语句:select * from `{}` limit 1",name);
			rs=st.executeQuery("select * from `"+name+"` limit 1");
			ResultSetMetaData ms=rs.getMetaData();
			schema=ms.getSchemaName(1);
			if(schema==null||schema.length()<1){
				schema=ms.getCatalogName(1);
			}
			r=dm.getColumns(null, null, name, null);
			
			int i=1;
			while(r.next()){
				String cname=String.valueOf(r.getObject("COLUMN_NAME"));
				Column c=allMap.get(cname);
				if(c==null){
					c=new Column();
					c.setName(cname);
					allMap.put(cname, c);
				}
				allColumns.add(c);
				c.setAutoIncrement(ms.isAutoIncrement(i));
				c.setNullable("yes".equalsIgnoreCase(r.getString("IS_NULLABLE")));
				String cr = r.getString("REMARKS");
				
				c.setType(r.getInt("DATA_TYPE"));
				c.setSize(r.getInt("COLUMN_SIZE"));
				c.setTypeName(r.getString("TYPE_NAME"));
				
				//处理xml包装类型
				//XML:ClanJoinType;加入条件
				Matcher m = XML_PATTERN.matcher(cr);
				if(m.find()){
					c.setMapClassName(m.group(1));
					c.setRemark(m.group(2));
					c.setObjectType(OBJECT_TYPE.XML);
				}else{
					m = JSON_PATTERN.matcher(cr);
					if(m.find()){
						c.setMapClassName(m.group(1));
						c.setRemark(m.group(2));
						c.setObjectType(OBJECT_TYPE.JSON);
					}else{
						c.setRemark(cr);
						c.setObjectType(OBJECT_TYPE.NORMAL);
					}
				}
				
				if(!c.isAutoIncrement()){
					noIdcolumns.add(c);
				}
				i++;
				log.debug("字段信息:{}",c);
			}
			if(!keyColumns.isEmpty()){
				Collections.sort(keyColumns,new Comparator<Column>(){
					@Override
					public int compare(Column o1, Column o2) {
						return Integer.valueOf(o1.getSeq()).compareTo(o2.getSeq());
					}
				});
				key=true;
			}else{
				key=false;
			}
			if (keyColumns.size()==1){
				oneKey=true;
				keyColumn=keyColumns.get(0);
			}else if(keyColumns.size()>1){
				//throw new IllegalArgumentException("暂时不支持多主键:"+name);
				oneKey=false;
				keyColumn=null;
			}else{
				oneKey=false;
				keyColumn=null;
			}
//			int c=ms.getColumnCount();
//			for(int i=1;i<=c;i++){
//				Column col=new Column(ms,i);
//				columns.add(col);
//				System.out.println(col);
//			}
			//printRs(rs);
			//唯一键休息,唯一键应该只在插入出现!
			indexRs = dm.getIndexInfo(null, null, name, true, false);
			while(indexRs.next()){
				String cname=String.valueOf(indexRs.getObject("COLUMN_NAME"));
				Column c=allMap.get(cname);
				if(c!=null){
					c.setUnique(true);
				}
			}
		}finally{
			if(rs!=null)rs.close();
			if(r!=null)r.close();
			if(indexRs!=null)indexRs.close();
			st.close();
		}
	}
	
	public boolean isHasKeyConstructorArgs(){
		return !keyColumns.isEmpty();
	}
	
	public String getKeyConstructorArgs(){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < keyColumns.size(); i++) {
            Column c = keyColumns.get(i);
            if(sb.length() > 0){
                sb.append(',');
            }
            sb.append(c.getClassName());
            sb.append(' ');
            sb.append(c.getFieldName());
        }
        return sb.toString();
	}
	
	public boolean isHasConstructorArgs(){
		return isHasConstructorArgs(allColumns);
	}
	
	public String getConstructorArgs(){
		return getConstructorArgsString(allColumns);
	}
	
	public boolean isHasConstructorArgs(List<Column> list){
		for (int i = 0; i < list.size(); i++) {
			Column c = list.get(i);
			if(!c.isAutoIncrement()){
				return true;
			}
		}
		return false;
	}
	
	private String getConstructorArgsString(List<Column> list){
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Column c = list.get(i);
			if(!c.isAutoIncrement()){
				if(sb.length() > 0){
					sb.append(',');
				}
				sb.append(c.getClassName());
				sb.append(' ');
				sb.append(c.getFieldName());
			}
		}
		return sb.toString();
	}
	
	public String getToString(){
		return getToStringString(allColumns);
	}
	
	public String getKeyToString(){
		return getToStringString(keyColumns);
	}
	
	private String getToStringString(List<Column> list){
		StringBuilder sb=new StringBuilder("\"");
		sb.append(getClassName());
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Column c = list.get(i);
			//return "${t.className} [#foreach(${c} in ${t.keyColumns})#if($velocityCount != 1)+",#end${c.fieldName}:"+ ${c.fieldName} #end+ "]";
			if(i != 0){
				sb.append("+\",");
			}
			sb.append(c.getFieldName());
			sb.append(":\"+ ");
			sb.append(c.getFieldName());
		}
		sb.append("+ \"]\"");
		return sb.toString();
	}
	
	public String getColumnNameString(){
		StringBuilder sb=new StringBuilder();
		for(Column c:noIdcolumns){
			sb.append('`');
			sb.append(c.getName());
			sb.append('`');
			sb.append(',');
		}
		if(sb.length()>1)
			sb.setLength(sb.length()-1);
		return sb.toString();
	}
	public String getColumnDbNameString(){
		StringBuilder sb=new StringBuilder();
		for(Column c:noIdcolumns){
			sb.append('#');
			sb.append(c.getDbName());
			sb.append('#');
			sb.append(',');
		}
		if(sb.length()>1)
			sb.setLength(sb.length()-1);
		
		return sb.toString();
	}
	public List<Column> getKeyColumns() {
		return keyColumns;
	}
	public boolean isHasDateColumns(){
		for(Column c:allColumns){
			if (c.isDateColumn()) {
				return true;
			}
		}
		return false;
	}
	public String getColumnValuesDbNameString(){
		StringBuilder sb=new StringBuilder();
		for(Column c:noIdcolumns){
			sb.append("#values[].");
			sb.append(c.getDbName());
			sb.append("#");
			sb.append(',');
		}
		if(sb.length()>1)
			sb.setLength(sb.length()-1);
		return sb.toString();
	}
	public String getColumnUpdateString(){
		StringBuilder sb=new StringBuilder();
		for(Column c:noIdcolumns){
			sb.append('`');
			sb.append(c.getName());
			sb.append('`');
			sb.append("=#");
			sb.append(c.getDbName());
			sb.append('#'); 
			sb.append(',');
		}
		if(sb.length()>1)
			sb.setLength(sb.length()-1);
		return sb.toString();
	}
	public String getClassName(){
		return NameUtils.toClassName(name);
	}
	public String getFieldName(){
		return NameUtils.toFieldName(name);
	}
	
	/**
	 * 返回主键的参数列表
	 * @return
	 */
	public String getKeysParameters(){
		if(keyColumns.isEmpty()){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for(Column c:keyColumns){
			if(sb.length()>0){
				sb.append(',');
			}
			sb.append(c.getClassName());
			sb.append(' ');
			sb.append(c.getFieldName());
		}
		return sb.toString();
	}
	/**
	 * 返回主键的 java method 调用参数列表
	 * @return
	 */
	public String getMethodParameters(){
		if(keyColumns.isEmpty()){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for(Column c:keyColumns){
			if(sb.length()>0){
				sb.append(',');
			}
			sb.append(c.getFieldName());
		}
		return sb.toString();
	}
	/**
	 * 返回final主键的参数列表
	 * @return
	 */
	public String getFinalKeysParameters(){
		if(keyColumns.isEmpty()){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for(Column c:keyColumns){
			if(sb.length()>0){
				sb.append(',');
			}
			sb.append("final ");
			sb.append(c.getClassName());
			sb.append(' ');
			sb.append(c.getFieldName());
		}
		return sb.toString();
	}
	public String getDbName(){
		return name;
	}
	private static Random ran=new Random(System.nanoTime());

	public String getSerialVersionUID() {
		return String.valueOf(ran.nextLong());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setAllColumns(List<Column> allColumns) {
		this.allColumns = allColumns;
	}
	public List<Column> getAllColumns() {
		return allColumns;
	}
	public List<Column> getNoIdColumns() {
		return noIdcolumns;
	}
	
	public Column getKeyColumn() {
		return keyColumn;
	}
	public void setKeyColumn(Column keyColumn) {
		this.keyColumn = keyColumn;
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public boolean isOneKey() {
		return oneKey;
	}
	public void setOneKey(boolean oneKey) {
		this.oneKey = oneKey;
	}
}
