package com.isnowfox.core.junit;


import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class BaseTest extends TestCase{
	protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
	public BaseTest(){
	}
	
	@Override
	protected void setUp() throws Exception {
		try{
			super.setUp();
		}catch(Exception t){
			log.error(t.getMessage(), t);
			throw t;
		}
	}
	
	@Override
	protected void runTest() throws Throwable {
		try{
			super.runTest();
		}catch(Throwable t){
			log.error(t.getMessage(), t);
			throw t;
		}
	}
}
