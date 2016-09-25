package com.isnowfox.cms.action;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;
import com.isnowfox.util.RandomUtils;
import com.isnowfox.web.ParameterType;
import com.isnowfox.web.annotation.Action;
import com.isnowfox.web.annotation.NoCache;
import com.isnowfox.web.annotation.Param;
import com.isnowfox.web.annotation.result.JsonResult;
import com.isnowfox.web.annotation.result.TmplClassGroup;
import com.isnowfox.web.annotation.result.TmplClassResult;

public class IndexAction {
	@Action("index")
	@JsonResult
	@NoCache
	@TmplClassGroup(
				{
					@TmplClassResult(cls = Date.class, type = TmplClassResult.JSON),
					@TmplClassResult(cls = Map.class, value = "index")
				}
			)
	public Object  index(
				@Param(value=ParameterType.HEADER) String userAgent
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("test", new Date());
		map.put("userAgent", userAgent);
		if(RandomUtils.randInt(1, 2) ==1){
			return new Date();
		}
		return map;
	}
}
