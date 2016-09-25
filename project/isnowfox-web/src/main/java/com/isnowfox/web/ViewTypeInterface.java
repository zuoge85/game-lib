package com.isnowfox.web;


public interface ViewTypeInterface {
	void init(Config config) throws Exception;
	/**
	 * 
	 * @param action 执行结果
	 * @param result action 执行返回结果
	 * @param value 视图映射值,一般用于模板匹配
	 * @param request 
	 * @param response
	 * @throws Exception
	 */
	void doView(String pattern,Object action, Object result,String value, Request request, Response response) throws Exception;
}
