package org.forkjoin.core.dao;


import com.isnowfox.core.PageResult;
import org.forkjoin.core.dao.grid.Columns;
import org.forkjoin.core.dao.grid.GridPageResult;

import java.util.Map;


public interface SqlQueryDao {

    GridPageResult<Map<String, Object>> query(String sql, int page, int pageSize);

    PageResult<Map<String, Object>> queryData(Columns columns, String sql, int page, int pageSize);

}
