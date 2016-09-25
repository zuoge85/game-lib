package com.isnowfox.web;

import java.nio.charset.Charset;

import com.isnowfox.web.codec.Uri;
import com.isnowfox.web.config.ActionConfig;

/**
 * 配置
 * @author zuoge85@gmail.com
 *
 */
public final class Config {
	public enum StaticHandleType{
		NONE,CACHE,NOT_CACHE
	}
	
	private Charset requestCharset=Charset.forName("utf-8");
	private Charset responseCharset=Charset.forName("utf-8");
	private int[] ports={80};
	private int responseBufferSize=2048;
	private String templateFilePath = "src/main/resources/template/";
	private String staticFilePath = "src/main/resources/page/";
	private String httlTemplateFileSuffix = "httl";

	private StaticHandleType staticType = StaticHandleType.NOT_CACHE;
	private boolean isDebug = false;
	private int outBufferSize = 8*1024;
	private ViewTypeInterface defaultViewType = ViewType.HTTL ;
	private int ParamsMax = Uri.DEFAULT_MAX_PARAMS;
	private String indexPage ="index.html";
	private String suffix = "do";
	
	private boolean isIocCreateObject;
	private boolean enableHttl = true;
	
	//private static Config config=new Config();
	public Config(){
		
	}
	

	public ActionConfig reg(Class<?> cls, String methodName) {
		return null;
	}
//	public static Config getConfig(){
//		return config;
//	}
	
	public Charset getRequestCharset() {
		return requestCharset;
	}
	
	public void setRequestCharset(Charset requestCharset) {
		this.requestCharset = requestCharset;
	}
	
	public Charset getResponseCharset() {
		return responseCharset;
	}
	
	public void setResponseCharset(Charset responseCharset) {
		this.responseCharset = responseCharset;
	}
	
	public void setCharset(Charset charset) {
		this.responseCharset = charset;
		this.requestCharset = charset;
	}

	public int[] getPorts() {
		return ports;
	}

	public void setPorts(int ...ports) {
		this.ports = ports;
	}

	public int getResponseBufferSize() {
		return responseBufferSize;
	}
	void setResponseBufferSize(int responseBufferSize) {
		this.responseBufferSize = responseBufferSize;
	}
	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
	/**
	 * 模板路径
	 * @return
	 */
	String getTemplateFilePath() {
		return templateFilePath;
	}
	public void setStaticFilePath(String staticFilePath) {
		this.staticFilePath = staticFilePath;
	}
	/**
	 * 静态文件路径
	 * @return
	 */
	public String getStaticFilePath() {
		return staticFilePath;
	}
	public String getSuffix() {
		return suffix;
	}
	/**
	 * 需要动态处理的后缀
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public StaticHandleType getStaticType() {
		return staticType;
	}
	
	public void setStaticType(StaticHandleType staticType) {
		this.staticType = staticType;
	}
	
	public boolean isDebug() {
		return isDebug;
	}
	
	public void setDebug(boolean debug) {
		this.isDebug = debug;
	}
	
	public void setOutBufferSize(int outBufferSize) {
		this.outBufferSize = outBufferSize;
	}
	
	public int getOutBufferSize() {
		return outBufferSize;
	}
	
	public ViewTypeInterface getDefaultViewType() {
		return defaultViewType;
	}
	
	public void setDefaultViewType(ViewTypeInterface defaultViewType) {
		this.defaultViewType = defaultViewType;
	}

	public int getParamsMax() {
		return ParamsMax;
	}
	
	/**
	 * 参数最大个数,设置最大个数有助于防备hash攻击!
	 * @param paramsMax
	 */
	public void setParamsMax(int paramsMax) {
		ParamsMax = paramsMax;
	}
	
	public String getIndexPage() {
		return indexPage;
	}
	/**
	 * 默认主页的名称.默认是index.html
	 * @param indexPage
	 */
	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}
	
	public boolean isIocCreateObject() {
		return isIocCreateObject;
	}
	
	/**
	 * 是否通过icon创建 action
	 */
	public void setIocCreateObject(boolean isIocCreateObject) {
		this.isIocCreateObject = isIocCreateObject;
	}
	
	public void setHttlTemplateFileSuffix(String httlTemplateFileSuffix) {
		this.httlTemplateFileSuffix = httlTemplateFileSuffix;
	}
	
	public String getHttlTemplateFileSuffix() {
		return httlTemplateFileSuffix;
	}


	public void setEnableHttl(boolean enableHttl) {
		this.enableHttl = enableHttl;
	}

	public boolean isEnableHttl() {
		return enableHttl;
	}
}
