package com.isnowfox.core.junit;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AppTest extends TestCase {
	protected static final Logger log = LoggerFactory.getLogger(AppTest.class);

	public AppTest(String configLocation) {
		this.configLocation = configLocation;
	}

	@Override
	protected void setUp() throws Exception {
		try {
			super.setUp();
			new ClassPathXmlApplicationContext(new String[] { configLocation })
					.getAutowireCapableBeanFactory().autowireBeanProperties(
							this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
							false);
		} catch (Exception t) {
			log.error(t.getMessage(), t);
			throw t;
		}
	}

	@Override
	protected void runTest() throws Throwable {
		try {
			super.runTest();
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
			throw t;
		}
	}

	private String configLocation;
}
