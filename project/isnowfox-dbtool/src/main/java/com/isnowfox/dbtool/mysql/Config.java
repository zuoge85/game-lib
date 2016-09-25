package com.isnowfox.dbtool.mysql;

import java.io.File;

import org.apache.velocity.app.VelocityEngine;

public class Config {
	private VelocityEngine velocityEngine = new VelocityEngine();
	
	private String pack="test";
	private String objectPack="entity";
	private String daoPack="dao";
	private String charset="utf8";
	private String contextFileName="daoContext.xml";
	private File dir;
	private File resourcesDir;
	public File getPackPath(String childPack){
		return new File(dir,pack.replace('.', File.separatorChar)
				+ File.separatorChar+childPack.replace('.', File.separatorChar) );
	}
	public String getPack(String childPack) {
		return pack+"."+childPack;
	}
	public String getPack(String pack,String childPack) {
		return pack+"."+childPack;
	}
	public Config(File dir,File resourcesDir) throws Exception{
		this.dir=dir;
		this.resourcesDir=resourcesDir;
		// =  
		velocityEngine.init();
	}
	public String getPack() {
		return pack;
	}
	public void setPack(String pack) {
		this.pack = pack;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public File getDir() {
		return dir;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setObjectPack(String objectPack) {
		this.objectPack = objectPack;
	}
	public String getObjectPack() {
		return objectPack;
	}
	public void setDaoPack(String daoPack) {
		this.daoPack = daoPack;
	}
	public String getDaoPack() {
		return daoPack;
	}
	public File getResourcesDir() {
		return resourcesDir;
	}
	public String getContextFileName() {
		return contextFileName;
	}
	public void setContextFileName(String contextFileName) {
		this.contextFileName = contextFileName;
	}
}
