package com.isnowfox.web.impl;

import java.io.FileNotFoundException;
import java.util.List;

import com.isnowfox.web.config.HeaderConfig;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isnowfox.core.IocFactory;
import com.isnowfox.web.ActionObjectPool;
import com.isnowfox.web.Config;
import com.isnowfox.web.Context;
import com.isnowfox.web.Dispatcher;
import com.isnowfox.web.Request;
import com.isnowfox.web.Response;
import com.isnowfox.web.Server;
import com.isnowfox.web.codec.Uri;
import com.isnowfox.web.proxy.ActionProxy;
import com.isnowfox.web.proxy.ProxyManager;

/**
 * 一个很基本的分发器
 * @author zuoge85
 *
 */
public abstract class BaseDispatcher implements Dispatcher {
	private final static Logger log = LoggerFactory.getLogger(BaseDispatcher.class);
	private final Config config;
	private final Server server;
	private final Context context =  Context.getInstance();
	private final ProxyManager proxyManager = new ProxyManager();
	private final IocFactory iocFactory;
	private final ActionObjectPool actionObjectPool;
	BaseDispatcher(Config config,final Server server) throws Exception{
		this.config = config;
		this.server = server;
		this.iocFactory = server.getIocFactory();
		proxyManager.initAction(server.getActionList(), config);
		actionObjectPool = new ActionObjectPool(iocFactory);
	}
	/**
	 * @param path
	 * @param resp
	 * @return 是否找到文件
	 * @throws Exception
	 */
	@Override
	public abstract boolean disposeStaticFile(Uri uri,Response resp) throws Exception ;
	
//	@Override
//	public boolean isStatic(Uri uri){
//		String suffix = config.getSuffix();
//		if(StringUtils.isEmpty(suffix)){
//			return uri.getFileExtensionName()!=null;
//		}else{
//			return !suffix.equals(uri.getFileExtensionName());
//		}
//	}
	
	@Override
	public void service(Uri uri, Request req, Response resp){
		try{
			if(uri.isExtensionType(config.getSuffix())){
				ActionProxy p = proxyManager.get(uri.getPattern());
				p.invoke(iocFactory, actionObjectPool, req, resp);
				HeaderConfig headerConfig = p.getActionConfig().getHeaderConfig();
				List<HeaderConfig.Item> list = headerConfig.getList();
				for (int i = 0; i < list.size(); i++) {
					HeaderConfig.Item item = list.get(i);
					resp.setHeader(item.name, item.value);
				}
				resp.flushAndClose();
			}else{
				disposeStaticFile(uri, resp);
				resp.flushAndClose();
			}
		}catch (FileNotFoundException e) {
			resp.sendError(HttpResponseStatus.NOT_FOUND);
			log.error("文件没找到"+e.getMessage(),e);
		}catch (Exception e) {
			resp.sendError(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			log.error("服务器错误",e);
		}
	}
	
}
