package com.isnowfox.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isnowfox.cms.action.IndexAction;
import com.isnowfox.core.SpringIocFactory;
import com.isnowfox.web.Config;
import com.isnowfox.web.ParameterType;
import com.isnowfox.web.Server;

public class CmsMain {
	private static final Logger log = LoggerFactory.getLogger(CmsMain.class);
	
	public static void main(String[] args) {
		Config cfg = new Config();
		SpringIocFactory ioc = new SpringIocFactory("CmsContext.xml");
		//LSConfig lsconfig = ioc.getBean(LSConfig.class);
		cfg.setDebug(true);
		cfg.setPorts(8080);
		cfg.setSuffix(null);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		cfg.setTemplateFilePath("template");
		cfg.setStaticFilePath(loader.getResource("page").getFile());
		//启动rmi服务器
		//启动http服务器
		log.info("启动http服务器");
		Server server=new Server(cfg);
		server.setIocFactory(ioc);
		try {
			//server.reg(IndexAction.class);
			server.regSingleton(IndexAction.class, "index","index1")
				.json().param(String.class, ParameterType.HEADER, "User-Agent");
			
//			server.regSingleton(IndexAction.class, "index","index2")
//				.viewJson(Date.class).view(Map.class, "index").param(String.class, "userAgent");;
		} catch (Exception e) {
			log.info("验证错误,action注册失败!",e);
		}
		try {
			server.start();
		} catch (Exception e) {
			log.error("严重错误,启动失败!", e);
		}
		//cfg.setStaticFilePath(cl.getResource("page").getFile());
	}
}
