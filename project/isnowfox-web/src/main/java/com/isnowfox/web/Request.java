package com.isnowfox.web;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class Request {
	
	
	public abstract boolean isPost();
	/**
	 * 上传文件过大就不能使用这个方法
	 * 52428800=50mb（请参考这个数值计算）
	 * @param key
	 * @return
	 */
	//public abstract HttpSession getSession();
	/**
	 * 可选
	 */
	public abstract byte[] getBytes(String key);
	
	public abstract boolean getBoolean(String key);
	/**
	 * 可选
	 */
	public abstract String getString(String key);
	public abstract List<String> getStringList(String key);
	public abstract Map<String,List<String>> getAllParams();
	public Map<String,String> getParamsMap(){
		Map<String, List<String>> allParams = getAllParams();
		Map<String, String> result = new HashMap<>();
		for(Map.Entry<String, List<String>> e:allParams.entrySet()) {
			List<String> list = e.getValue();
			if(!list.isEmpty()){
				result.put(e.getKey(), list.get(0));
			}
		}
		return result;
	}
	public abstract String getStringByCharset(String key,String charset);
	public abstract String getString(String key, String defaultValue);
	public abstract int getInt(String key);
//	private Map<String,Cookie> cookieMap;
//	public final Cookie getCookie(String name){
//		if (cookieMap==null) {
//			Cookie cookis[]=request.getCookies();
//			if(ArrayUtils.isNotEmpty(cookis)){
//				cookieMap=Maps.newHashMapWithExpectedSize(cookis.length);
//				for(Cookie c:cookis){
//					cookieMap.put(c.getName(), c);
//				}
//			}else{
//				return null;
//			}
//		}
//		return cookieMap.get(name);
//	}
//	public final int getCookieInt(String name,int defaultInt){
//		Cookie ck=getCookie(name);
//		return ck==null?defaultInt:NumberUtils.toInt(ck.getName(), defaultInt);
//	}
//	public final String getCookieString(String name,String defaultString){
//		Cookie ck=getCookie(name);
//		return ck==null?defaultString:ck.getName();
//	}
//	public final String getCookieString(String name){
//		Cookie ck=getCookie(name);
//		return ck==null?null:ck.getName();
//	}
	public abstract int getInt(String key,int defaultValue);
	public abstract int getInt(String key,int defaultValue,int max);
	public abstract int getIntMin(String key,int defaultValue,int min);
	public abstract int getInt(String key,int defaultValue,int min,int max);
	public abstract float getFloat(String key,float defaultValue);
	//public abstract void setWordFilter(WordFilter wordFilter);
//	public abstract getUser(){
//		return Context.getUserInfo(request);
//	}
//	public String getAbsoluteUrl(String uri){
//		String cp=request.getContextPath();
//		if(cp==null||cp.equals("")||"/".equals(uri)){
//			return uri;
//		}else{
//			return cp+uri;
//		}
//	}
//	/**
//	 * @deprecated 强烈不推荐使用
//	 * @return
//	 */
//	@Deprecated
//	public HttpServletRequest getRequest(){
//		return request;
//	}
//	public abstract Map<String,List<String>> getParams();
	public abstract Charset getCharset();
	public abstract String getRemoteInfo();

	public abstract int getLocalPort();
//	{
//		return request.getRemoteHost()+":"+request.getRemoteAddr()+":"+request.getRemotePort()+"@"+request.getRemoteUser();
//	}
//	public String getRemoteAddr() {
//		return request.getRemoteAddr();
//	}
	public abstract String getPath();
	
	public abstract String getHeader(String key);

	public abstract List<String> getHeaders(String name);

	public abstract List<Map.Entry<String, String>> getHeaders();

	public abstract boolean containsHeader(String name);

	public abstract Set<String> getHeaderNames();
	
	public abstract String getCookieString(String key);
	
//	public abstract Cookie getCookie(String key){
//		Cookie
//	}
}