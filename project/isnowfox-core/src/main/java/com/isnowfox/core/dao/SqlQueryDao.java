package com.isnowfox.core.dao;


import java.util.Map;

import com.isnowfox.core.PageResult;
import com.isnowfox.core.dao.grid.Columns;
import com.isnowfox.core.dao.grid.GridPageResult;


public interface SqlQueryDao {

	GridPageResult<Map<String, Object>> query(String sql, int page, int pageSize);

	PageResult<Map<String, Object>> queryData(Columns columns,String sql, int page, int pageSize);

}
