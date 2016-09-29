package org.forkjoin.core.dao;


import com.google.common.collect.Lists;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 默认OperatorType为 EQ
 * not默认值是false
 *
 * @author zuoge85@gmail.com
 */
public class QueryParams {

    private List<QueryParam> params = Lists.newArrayList();

    public static QueryParams create() {
        return new QueryParams();
    }

    public static QueryParams single(String key, Object value) {
        return single(key, value, QueryParam.OperatorType.EQ, false);
    }

    public static QueryParams single(String key, Object value, QueryParam.OperatorType opt) {
        return single(key, value, opt, false);
    }

    public static QueryParams single(final String key, final Object value, final QueryParam.OperatorType opt, final boolean not) {
        return new QueryParams() {
            @Override
            public void toSql(StringBuilder sb) {
                sb.append(" WHERE ");
                if (not) {
                    sb.append(" NOT (");
                }
                sb.append('`');
                sb.append(key);
                sb.append('`');
                //操作符
                toSqlWhereOpt(sb, value, opt);
                //end操作符
                if (not) {
                    sb.append(") ");
                }
            }

            @Override
            public int setPreparedStatement(PreparedStatement ps, int i) throws SQLException {
                if (opt != QueryParam.OperatorType.IS_NULL && opt != QueryParam.OperatorType.IS_NOT_NULL) {
                    ps.setObject(i++, value);
                }
                return i;
            }

            @Override
            public Object[] toParams() {
                return new Object[]{value};
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }

    private QueryParams() {
    }

    public QueryParams add(String key, Object value) {
        QueryParam p = new QueryParam(key, value);
        params.add(p);
        return this;
    }

    //	public QueryParam add(String key,Object value,OperatorType opt){
//		QueryParam p=new QueryParam(key,value,opt);
//		params.add(p);
//		return this;
//	}
    public QueryParams add(String key, Object value, QueryParam.OperatorType opt) {
        QueryParam p = new QueryParam(key, value, opt);
        params.add(p);
        return this;
    }

    public QueryParams add(String key, Object value, QueryParam.OperatorType opt, boolean not) {
        QueryParam p = new QueryParam(key, value, opt, true, not);
        params.add(p);
        return this;
    }

    public QueryParams or(String key, Object value) {
        QueryParam p = new QueryParam(key, value);
        params.add(p);
        p.and = false;
        return this;
    }

    //	public QueryParam or(String key,String value,OperatorType opt){
//		QueryParam p=new QueryParam(key,value,opt);
//		params.add(p);
//		p.and=false;
//		return this;
//	}
    public QueryParams or(String key, Object value, QueryParam.OperatorType opt) {
        QueryParam p = new QueryParam(key, value, opt, false);
        params.add(p);
        p.and = false;
        return this;
    }

    public QueryParams or(String key, Object value, QueryParam.OperatorType opt, boolean not) {
        QueryParam p = new QueryParam(key, value, opt, false, not);
        params.add(p);
        p.and = false;
        return this;
    }

    private static final void toSqlWhereOpt(StringBuilder sb, final Object value, final QueryParam.OperatorType opt) {

        switch (opt) {
            case EQ:
                sb.append(" = ? ");
                break;
            case LIKE:
                sb.append(" LIKE ? ");
            case LIKE_BOTH:
                sb.append(" LIKE '%").append(SqlUtils.fieldFilter(value)).append("%'");
                break;
            case LT:
                sb.append(" < ? ");
                break;
            case GT:
                sb.append(" > ? ");
                break;
            case LE:
                sb.append(" <= ? ");
                break;
            case NOT:
                sb.append(" != ? ");
                break;
            case GE:
                sb.append(" >= ? ");
                break;
            case IS_NULL:
                sb.append(" IS NULL ");
                break;
            case IS_NOT_NULL:
                sb.append(" IS NOT NULL ");
                break;
            default:
                break;
        }
    }

    public String toSql() {
        StringBuilder sb = new StringBuilder();
        toSql(sb);
        return sb.toString();
    }

    public void toSql(StringBuilder sb) {
        if (params.isEmpty()) {
            return;
        }
        sb.append(" WHERE ");
        boolean fist = true;
        for (QueryParam p : params) {
            String key = p.key;
            if (fist) {
                fist = false;
            } else {
                if (p.and) {
                    sb.append(" AND ");
                } else {
                    sb.append(" OR ");
                }
            }
            if (p.not) {
                sb.append(" NOT (");
            }
            sb.append('`');
            sb.append(key);
            sb.append('`');
            //操作符
            toSqlWhereOpt(sb, p.getValue(), p.getOpt());
            //end操作符
            if (p.not) {
                sb.append(") ");
            }
        }
    }

    public int setPreparedStatement(PreparedStatement ps, int i) throws SQLException {
        for (QueryParam p : params) {
            if (p.opt != QueryParam.OperatorType.IS_NULL && p.opt != QueryParam.OperatorType.IS_NOT_NULL && p.opt != QueryParam.OperatorType.LIKE_BOTH) {
                ps.setObject(i++, p.getValue());
            }
        }
        return i;
    }

    public Object[] toParams() {
        List<Object> list = Lists.newArrayListWithCapacity(params.size());
        for (QueryParam p : params) {
            if (p.opt != QueryParam.OperatorType.IS_NULL && p.opt != QueryParam.OperatorType.IS_NOT_NULL && p.opt != QueryParam.OperatorType.LIKE_BOTH) {
                list.add(p.value);
            }
        }
        return list.toArray();
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

}
