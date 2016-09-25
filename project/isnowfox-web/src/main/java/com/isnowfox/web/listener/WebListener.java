package com.isnowfox.web.listener;

public interface WebListener {
	public void contextInitialized(WebContextEvent evt);
	public void contextDestroyed(WebContextEvent evt);
	public void response(WebResponseEvent evt);
	/**
	 * 
	 * 数据全部收到之后,马上开始调用action处理请求 之前,而且只在action有效的情况调用
	 * @param evt
	 */
	public void requestHandlerStart(WebRequestEvent evt);
	/**
	 * action处理结束
	 * @param evt
	 */
	public void requestHandlerEnd(WebRequestEvent evt);
}
