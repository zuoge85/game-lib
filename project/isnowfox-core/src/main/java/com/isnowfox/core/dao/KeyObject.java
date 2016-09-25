package com.isnowfox.core.dao;



public interface KeyObject {
	
	public abstract <T extends EntityObject,K extends KeyObject> TableInfo<T,K> getTableInfo();
}