package com.isnowfox.web;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.isnowfox.core.bean.Attributes;

/**
 * 通过reg函数注册的action的view使用第一个作为默认view
 * @author zuoge85@gmail.com
 */
public final class Context implements Attributes{
	//private static final Logger log = LoggerFactory.getLogger(Context.class);
	
	private static final Context instance=new Context();
//	private Map<String,ActionConfig> actionMap=Maps.newHashMap();
	private Map<String,Object> attributesMap=Maps.newConcurrentMap();
//	private Map<String,ViewHandler> viewHandlerMap=Maps.newHashMap();
//	private ViewHandler defaultViewHandler;
//	private ThreadLocal<Request> requestThreadLocal=new ThreadLocal<Request>();
//	private IocFactory iocFactory;
//	private Invoke invoke;
//	private Config config;
//	private List<WebListener> listeners=Lists.newArrayList();

	private ThreadLocal<Request> requestThreadLocal=new ThreadLocal<Request>();
	
	private Context(){
		
	}
	
	public static Context getInstance(){
		return instance;
	}

	public void setRequest(Request re){
		requestThreadLocal.set(re);
		if(re==null){
			requestThreadLocal.remove();
		}
	}
	public Request getRequest(){
		return requestThreadLocal.get();
	}
	public void clearRequest(){
		requestThreadLocal.remove();
	}
	
	public void removeAttribute(Object name){
		attributesMap.remove(name);
	}
	public void setAttribute(String name,Object object){
		attributesMap.put(name, object);
	}
	public Object getAttribute(String name){
		return attributesMap.get(name);
	}
	public Map<String,Object> getAttributesMap() {
		return attributesMap;
	}
	public Set<String> getAttributeNames() {
		return attributesMap.keySet();
	}
}
