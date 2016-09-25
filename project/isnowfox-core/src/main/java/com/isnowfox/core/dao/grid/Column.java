package com.isnowfox.core.dao.grid;

/**
 * 
 * @author zuoge85@gmail.com
 * @see java.sql.Types
 */
public class Column {
	public String text;
	public String code;
	public int sqlType;
	public Class<?> javaType;
	private int columnDisplaySize;
	
	public Column(String text, String code, int sqlType,Class<?> javaType) {
		this.text = text;
		this.code = code;
		this.sqlType = sqlType;
		this.javaType = javaType;
	}
	public Column(String text, String code, int sqlType,String javaType,int columnDisplaySize) throws ClassNotFoundException {
		this.text = text;
		this.code = code;
		this.sqlType = sqlType;
		this.javaType = ClassLoader.getSystemClassLoader().loadClass(javaType);
		this.columnDisplaySize = columnDisplaySize;
	}
	
	
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @see java.sql.Types
	 * @return
	 */
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public Class<?> getJavaType() {
		return javaType;
	}
	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}
	@Override
	public String toString() {
		return "Column [text=" + text + ", code=" + code + ", sqlType="
				+ sqlType + ", javaType=" + javaType + ", columnDisplaySize="
				+ columnDisplaySize + "]";
	}
}
