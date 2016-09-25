package com.isnowfox.web.config;

import com.isnowfox.web.CacheType;
import com.isnowfox.web.Config;
import com.isnowfox.web.ParameterType;
import com.isnowfox.web.ViewType;
import com.isnowfox.web.ViewTypeInterface;

/***
 * action 配置,不管是那种方式的配置最后都演变成这个类
 * @author zuoge85
 *
 */
public class ActionConfig {
	/**
	 * 生存周期类型,不引入session这种周期是避免复杂性
	 * @author zuoge85
	 *
	 */
	public enum LiefCycleType{
		SINGLETON,REQUEST
	}
	
	private LiefCycleType liefCycle;
	private String pattern;
	private String method;
	private Class<?> actionClass;
	
	private CacheConfig cache;
	private HeaderConfig headerConfig = new HeaderConfig();
	private ParamsConfig paramsConfig = new ParamsConfig();
	private ViewConfig viewConfig = new ViewConfig();
	private Config config;
	public ActionConfig(Class<?> actionClass,String method,String pattern, Config config,LiefCycleType liefCycle){
		this.actionClass = actionClass;
		if(pattern.length()>0){
			if(pattern.charAt(0) != '/'){
				this.pattern = '/' + pattern;
			}else{
				this.pattern = pattern;
			}
		}else{
			this.pattern = pattern;
		}
		
		this.method = method;
		this.config =  config;
		this.liefCycle = liefCycle;
	}
	
	public ActionConfig param(Class<?> cls,ParameterType type, String name) {
		paramsConfig.add(cls, type, name);
		return this;
	}
	
	public ActionConfig param(Class<?> cls, String name) {
		paramsConfig.add(cls, ParameterType.REQUEST, name);
		return this;
	}

	public ActionConfig header(String name, String value) {
		headerConfig.add(name, value);
		return this;
	}

	public HeaderConfig getHeaderConfig() {
		return headerConfig;
	}

	public ActionConfig cache(CacheType type, String value) throws IllegalConfigException{
		if(cache != null){
			throw new IllegalConfigException("错误的参数!");
		}
		cache = new CacheConfig(type, value);
		return this;
	}
	
	public ActionConfig noCache() throws IllegalConfigException{
		cache(CacheType.NO_CACHE, null);
		return this;
	}

	public ActionConfig view(Class<?> cls, String value) throws IllegalConfigException {
		viewConfig.addClassMap(cls, value, config.getDefaultViewType());
		return this;
	}
	
	public ActionConfig view(Class<?> cls, String value,ViewTypeInterface viewType) throws IllegalConfigException {
		viewConfig.addClassMap(cls, value, viewType);
		return this;
	}
	
	public ActionConfig viewJson(Class<?> cls) throws IllegalConfigException {
		viewConfig.addClassMap(cls, null, ViewType.JSON);
		return this;
	}
	
	public ActionConfig view(Class<?> cls,ViewTypeInterface viewType) throws IllegalConfigException {
		viewConfig.addClassMap(cls, null, viewType);
		return this;
	}
	
	public ActionConfig view(String name, String value) throws IllegalConfigException {
		viewConfig.addViewMap(name, value, config.getDefaultViewType());
		return this;
	}
	
	public ActionConfig view(String name, String value,ViewTypeInterface viewType) throws IllegalConfigException {
		viewConfig.addViewMap(name, value, viewType);
		return this;
	}
	
	public ActionConfig viewJson(String name) throws IllegalConfigException {
		viewConfig.addViewMap(name, null, ViewType.JSON);
		return this;
	}
	
	public ActionConfig view(String name,ViewTypeInterface viewType) throws IllegalConfigException {
		viewConfig.addViewMap(name, null, viewType);
		return this;
	}

	public ActionConfig view(ViewTypeInterface viewType,String value) throws IllegalConfigException {
		viewConfig.viewType(viewType,value);
		return this;
	}
	
	public ActionConfig view(ViewTypeInterface viewType) throws IllegalConfigException {
		viewConfig.viewType(viewType, null);
		return this;
	}
	
	public ActionConfig el(String el) throws IllegalConfigException {
		viewConfig.el(el);
		return this;
	}
	
	
	public ActionConfig json() throws IllegalConfigException {
		viewConfig.Json();
		return this;
	}
	
	public ActionConfig stream() throws IllegalConfigException {
		viewConfig.stream();
		return this;
	}
	
	public Class<?> getActionClass() {
		return actionClass;
	}
	public String getPattern() {
		return pattern;
	}
	public String getMethod() {
		return method;
	}
	public ParamsConfig getParamsConfig() {
		return paramsConfig;
	}
	public LiefCycleType getLiefCycle() {
		return liefCycle;
	}

	public ViewConfig getViewConfig() {
		return viewConfig;
	}
}
