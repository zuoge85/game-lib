package com.isnowfox.core;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 抽象的使用spring注入的工厂
 * @author zuoge85
 *
 */
public  class SpringIocFactory implements IocFactory{
	private int autowireType= AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
	
	public void initBean(Object o){
		getAutowireCapableBeanFactory().autowireBeanProperties(o, autowireType, false);
	}
	
	private ClassPathXmlApplicationContext ctx;
	
	public SpringIocFactory(String... configsPath){
		ctx = new ClassPathXmlApplicationContext(configsPath);
	}
	
	protected AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
		return ctx.getAutowireCapableBeanFactory();
	}
	
	public <T> T getBean(Class<T> cls) {
		String[] str= ctx.getBeanNamesForType(cls);
		if(!ArrayUtils.isEmpty(str)){
			return ctx.getBean(str[0],cls);
		}
		return null;
	}
	
	public <T> T getBean(String name,Class<T> cls) {
		return ctx.getBean(name, cls);
	}
	
	public void setAutowireType(int autowireType) {
		this.autowireType = autowireType;
	}
	
	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> cls) {
		return ctx.getBeansOfType(cls);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name) {
		return (T)ctx.getBean(name);
	}
	
	public void destroy(){
		ctx.destroy();
	}
}
