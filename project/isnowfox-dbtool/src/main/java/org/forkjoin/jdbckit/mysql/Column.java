package org.forkjoin.jdbckit.mysql;

import org.apache.commons.lang3.StringUtils;

import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Column {
    public enum OBJECT_TYPE {
        NORMAL, XML, JSON
    }

    private String name;
    private String remark;
    private String typeName;
    private int type;
    private boolean autoIncrement;
    private boolean nullable;
    private int size;
    private boolean isUnique;
    private String mapClassName;
    private String mapClassNameGeneric;
    private OBJECT_TYPE objectType;
    /**
     * seq是主键特有的,表示顺序
     */
    private int seq;

    public String getSetName() {
        return "set" + NameUtils.toClassName(name);
    }

    public String getConstantName() {
        return name.toUpperCase();
    }

    public String getFieldName() {
        return NameUtils.toFieldName(name);
    }

    public String getGetName() {
        return "get" + NameUtils.toClassName(name);
    }

    public String getDbName() {
        return name;
    }

    public boolean isDateColumn() {
        if (StringUtils.isNotBlank(remark)) {
//			if (remark.startsWith("datetime")) {
//				return true;
//			}
        }
        return false;
    }

    public String getResultGetMethod() {
        switch (type) {
            case Types.TINYINT:
                return "getByte";
            case Types.SMALLINT:
                return "getShort";
            case Types.BIGINT:
                return "getLong";
            case Types.BIT:
                return "getBoolean";
            case Types.LONGVARBINARY:
            case Types.BLOB:
                return "getBlob";
            case Types.BOOLEAN:
                return "getBoolean";
            case Types.CHAR:
                return "getString";
            case Types.CLOB:
                return "getClob";
            case Types.DATE:
                return "getDate";
            case Types.DECIMAL:
                return "getBigDecimal";
            case Types.NUMERIC:
                return "getBigDecimal";
            case Types.DOUBLE:
                return "getDouble";
            case Types.FLOAT:
                return "getFloat";
            case Types.INTEGER:
                return "getInt";
            case Types.TIME:
                return "getTime";
            case Types.TIMESTAMP:
                return "getTimestamp";
            case Types.VARCHAR:
                return "getString";
            case Types.LONGNVARCHAR:
                return "getString";
            case Types.LONGVARCHAR:
                return "getString";
            case Types.REAL:
                if (typeName.equals("FLOAT")) {
                    return "getFloat";
                } else {
                    throw new RuntimeException("未实现");
                }
            default:
                return "getObject";
        }
    }

    public void setMapClassNameGeneric(String mapClassNameGeneric) {
        this.mapClassNameGeneric = mapClassNameGeneric;
    }

    /**
     * 返回 类型的初始值字符串
     *
     * @return
     */
    public String getInitString() {
        if (nullable) {
            return "null";
        } else {
            switch (type) {
                case Types.BIGINT:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                    return "0";
                case Types.BOOLEAN:
                    return "false";
                case Types.REAL:
                    if (typeName.equals("FLOAT")) {
                        return "0";
                    } else {
                        throw new RuntimeException("未实现");
                    }
                default:
                    return "null";
            }
        }

    }

    public String getWrapClassName() {
        if (mapClassName != null) {
            return mapClassName;
        }
        switch (type) {
            case Types.TINYINT:
                return "Byte";
            case Types.SMALLINT:
                return "Short";
            case Types.BIGINT:
                return "Long";
            case Types.BIT:
                return "Boolean";
            case Types.LONGVARBINARY:
            case Types.BLOB:
                return "java.sql.Blob";
            case Types.BOOLEAN:
                return "Boolean";
            case Types.CHAR:
                return "String";
            case Types.CLOB:
                return "java.sql.Clob";
            case Types.DATE:
                return "java.sql.Date";
            case Types.DECIMAL:
                return "java.math.BigDecimal";
            case Types.NUMERIC:
                return "java.math.BigDecimal";
            case Types.DOUBLE:
                return "Double";
            case Types.FLOAT:
                return "Float";
            case Types.INTEGER:
                return "Integer";
            case Types.TIME:
                return "java.sql.Time";
            case Types.TIMESTAMP:
                return "java.util.Date";
            case Types.VARCHAR:
                return "String";
            case Types.LONGNVARCHAR:
                return "String";
            case Types.LONGVARCHAR:
                return "String";
            case Types.REAL:
                if (typeName.equals("FLOAT")) {
                    return "Float";
                } else {
                    throw new RuntimeException("未实现");
                }
            default:
                return "Object";
        }
    }

    public String getClassNameNoGeneric() {
        if (mapClassNameGeneric != null) {
            return mapClassNameGeneric;
        }
        return getClassName();
    }

    public String getClassName() {
        //处理下映射
        if (mapClassName != null) {
            return mapClassName;
        }
        switch (type) {
            case Types.TINYINT:
                return nullable ? "Byte" : "byte";
            case Types.SMALLINT:
                return nullable ? "Short" : "short";
            case Types.BIGINT:
                return nullable ? "Long" : "long";
            case Types.BIT:
                return nullable ? "Boolean" : "boolean";
            case Types.BLOB:
            case Types.LONGVARBINARY:
                return "java.sql.Blob";
            case Types.BOOLEAN:
                return nullable ? "Boolean" : "boolean";
            case Types.CHAR:
                return "String";
            case Types.CLOB:
                return "java.sql.Clob";
            case Types.DATE:
                return "java.sql.Date";
            case Types.DECIMAL:
                return "java.math.BigDecimal";
            case Types.NUMERIC:
                return "java.math.BigDecimal";
            case Types.DOUBLE:
                return nullable ? "Double" : "double";
            case Types.FLOAT:
                return nullable ? "Float" : "float";
            case Types.INTEGER:
                return nullable ? "Integer" : "int";
            case Types.TIME:
                return "java.sql.Time";
            case Types.TIMESTAMP:
                return "java.util.Date";
            case Types.VARCHAR:
                return "String";
            case Types.LONGNVARCHAR:
                return "String";
            case Types.LONGVARCHAR:
                return "String";
            case Types.REAL:
                if (typeName.equals("FLOAT")) {
                    return nullable ? "Float" : "float";
                } else {
                    throw new RuntimeException("未实现");
                }
            default:
                return "Object";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public String getMapClassName() {
        return mapClassName;
    }

    private static final Pattern GENERIC_PATTERN = Pattern.compile("([^<]+)<[^>]+>.*");

    public void setMapClassName(String mapClassName) {
        this.mapClassName = mapClassName;

        //java.util.ArrayList<String>
        Matcher m = GENERIC_PATTERN.matcher(mapClassName);
        if (m.find()) {
            setMapClassNameGeneric(m.group(1));
        }
    }

    public int getObjectType() {
        return objectType.ordinal();
    }

    public void setObjectType(OBJECT_TYPE objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "Column [name=" + name + ", remark=" + remark + ", typeName="
                + typeName + ", type=" + type + ", autoIncrement="
                + autoIncrement + ", nullable=" + nullable + ", size=" + size
                + ", isUnique=" + isUnique + ", mapClassName=" + mapClassName
                + ", objectType=" + objectType + ", seq=" + seq + "]";
    }
}
