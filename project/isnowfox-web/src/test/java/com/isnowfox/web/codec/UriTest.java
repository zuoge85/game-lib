package com.isnowfox.web.codec;

import java.nio.charset.Charset;

import com.isnowfox.core.junit.BaseTest;

public class UriTest extends BaseTest{
	/**
	 * 测试基本信息
	 */
	public void testBas(){
		Charset charset = Charset.forName("UTF-8");
		String defaultFileName = "index.html";
		int maxParams = 1024;
		Uri u = new Uri("/login?id=123123", charset, maxParams, defaultFileName);
		
		assertEquals("login", u.getFileName());
		assertFalse(u.isDir());
		assertTrue(u.isExtensionType(""));
		assertTrue(u.isExtensionType(null));
		assertEquals(u.getFileExtensionName() ,null);
		
		
		u.reset("/?id=123123", charset, maxParams, defaultFileName);
		
		assertTrue(u.isDir());
		assertTrue(u.isExtensionType("html"));
		assertFalse(u.isExtensionType(null));
		assertTrue(u.getParameters().containsKey("id"));
	}
}
