package org.forkjoin.core.dao;

/**
 * @author zuoge85 on 15/11/17.
 */

//SELECT
//        SUM(a.service_out_count) ssum, SUM(a.advice_count) asum,SUM(a.rent_fee) rsum,SUM(a.foregift_fee) fsum,SUM(a.couplant_fee) csum
//        FROM
//        statistics a
//
//        where
//        a.create_time > '2015-11-11 11:17:22'
//        and
//        a.create_time < '2015-12-11 11:17:22'
//        and
//        a.hospital_id in(1,2,3)
//        GROUP BY
//        a.hospital_id
//
//        ORDER BY
//        asum DESC

import org.apache.commons.lang3.ArrayUtils;

/**
 * 处理相对复杂的查询
 * 依然是简单查询封装
 */
public class Select {
    private final Field[] fields;
    private String tableName;
    private String tableAliasName;
    private QueryParams queryParams;
    private Order order;
    private Field[] groupFields;

    public Select() {
        this(Field.ALL_FIELDS);
    }

    public Select(Field... fields) {
        this.fields = fields;
    }

    public Select(String... fields) {
        this.fields = Field.forms(fields);
    }

    public Select from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Select from(String tableName, String tableAliasName) {
        this.tableName = tableName;
        this.tableAliasName = tableAliasName;
        return this;
    }

    public Select where(QueryParams params) {
        this.queryParams = params;
        return this;
    }

    public Select where(String key, Object value) {
        return where(QueryParams.single(
                key, value, QueryParam.OperatorType.EQ, false
        ));
    }

    public Select where(String key, Object value, QueryParam.OperatorType opt) {
        return where(QueryParams.single(
                key, value, opt, false
        ));
    }

    public Select where(final String key, final Object value, final QueryParam.OperatorType opt, final boolean not) {
        return where(QueryParams.single(
                key, value, opt, not
        ));
    }

    public Select orderBy(Order order) {
        this.order = order;
        return this;
    }

    public Select orderBy(String name) {
        this.order = Order.createSingleton(name, true);
        return this;
    }

    public Select orderBy(String name, boolean isDesc) {
        this.order = Order.createSingleton(name, isDesc);
        return this;
    }

    public Select orderByDesc(String name) {
        this.order = Order.desc(name);
        return this;
    }

    public Select orderByAsc(String name) {
        this.order = Order.asc(name);
        return this;
    }


    public Select groupBy(Field... fields) {
        this.groupFields = fields;
        return this;
    }

    public Select groupByNames(String... fields) {
        this.groupFields = Field.forms(fields);
        return this;
    }

    public Select groupBy(String name) {
        this.groupFields = Field.forms(name);
        return this;
    }

    public Select groupBy(String tableName, String name) {
        return groupBy(Field.forms(tableName, name));
    }

    public Select groupByAliasName(String name, String filedAliasName) {
        return groupBy(Field.formAliasName(name, filedAliasName));
    }

    public Select groupBy(String tableName, String name, String filedAliasName) {
        return groupBy(Field.forms(tableName, name, filedAliasName));
    }

    //    SELECT
//
//            FROM
//
//    WHERE
//
//    GROUP BY
//
//    ORDER BY `id`
    public String toSql() {
        return toSql(new StringBuilder()).toString();
    }

    public StringBuilder toSql(StringBuilder sb) {
        return toSql(sb, false);
    }

    public String toCountSql() {
        return toCountSql(new StringBuilder()).toString();
    }

    public StringBuilder toCountSql(StringBuilder sb) {
        return toSql(sb, true);
    }

    public StringBuilder toSql(StringBuilder sb, boolean isCount) {
        boolean isGroup = ArrayUtils.isNotEmpty(groupFields);

        sb.append("SELECT ");
        if (isCount) {
            if (isGroup) {
                sb.append("count(distinct ");
                for (int i = 0; i < groupFields.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(groupFields[i].getValue());
                }
                sb.append(")");
            } else {
                sb.append(SqlUtils.STRING_COUNT);
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(fields[i].getValue());
            }
        }
        sb.append(" FROM ");

        sb.append("`").append(SqlUtils.nameFilter(tableName))
                .append("` ");

        if (tableAliasName != null) {
            sb.append("`")
                    .append(SqlUtils.nameFilter(tableAliasName))
                    .append("` ");
        }
        if (queryParams != null) {
            queryParams.toSql(sb);
        }


        if (isCount) {
            return sb;
        }

        if (isGroup) {
            sb.append(" GROUP BY ");
            for (int i = 0; i < groupFields.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(groupFields[i].getValue());
            }
        }

        if (order != null) {
            order.toSql(sb);
        }
        return sb;
    }

    public Object[] toParams() {
        return queryParams == null ? ArrayUtils.EMPTY_OBJECT_ARRAY : queryParams.toParams();
    }


}
