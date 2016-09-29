package org.forkjoin.core.dao;

/**
 * @author qiang on 2015/10/16.
 *         jinliqiang@ihbaby.com
 */
public class SqlUtils {
    public static final String STRING_COUNT = " count(1) ";

    public static String fieldFilter(String sql) {
        return sql.trim().replace("'", "''");
    }

    public static String fieldFilter(Object obj) {
        return (obj == null) ? "" : fieldFilter(obj.toString());
    }


    public static String nameFilter(String sql) {
        return sql.trim().replace("`", "``");
    }
}
