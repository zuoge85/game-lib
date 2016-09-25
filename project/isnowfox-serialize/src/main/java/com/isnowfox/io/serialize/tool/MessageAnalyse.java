package com.isnowfox.io.serialize.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.math.NumberUtils;

import com.isnowfox.io.serialize.tool.model.Message;
import com.isnowfox.util.StringExpandUtils;

/**
 * 非线程安全的
 * @author zuoge85
 *
 */
public class MessageAnalyse {
//	private static final Pattern JAVA_PACKAGE_PATTERN = Pattern.compile("javaPackage\\s*+=\\s*+([\\w_$.]+)");
//	private static final Pattern AS_PACKAGE_PATTERN = Pattern.compile("asPackage\\s*+=\\s*+([\\w_$.]+)");
	private static final Pattern COMMENT_PATTERN = Pattern.compile("//(.*)");
	private static final Pattern HEAD_PATTERN = Pattern.compile("\\s*+message\\s*+");
	private static final Pattern HANDLER_PATTERN = Pattern.compile("\\s*+handler\\s*+=\\s*+([\\w_$.,]+)");
	//	boolean testBool
	private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\s*+([\\w_$.]+)(\\[(\\d)*?\\])*?\\s*+([\\w_$]+)");
	
	private Message msg;
	private StringBuilder sb = new StringBuilder();
	//private Config cfg;
	
	public MessageAnalyse(Config cfg) {
		//this.cfg = cfg;
	}

	public Message analyse(LineIterator it, String pack){
		sb.setLength(0);
		msg = new Message();
		while (it.hasNext()) {
			String line = it.nextLine();
			analyseLine(line);
		}
		if(pack == null){
			pack = "";
		}
//		String packName = msg.getPackageName() == null ? pack: msg.getPackageName();
		msg.setPackageName(pack);
//		if(StringUtils.isEmpty(javaPackage)){
//			msg.setJavaPackage(cfg.getJavaRootPackage());
//		}else{
//			msg.setJavaPackage(cfg.getJavaRootPackage() + "." + javaPackage);
//		}
		
//		String asPackage =  msg.getAsPackage() == null ? pack: msg.getAsPackage();
//		msg.setAsPackage(asPackage);
//		if(StringUtils.isEmpty(asPackage)){
//			msg.setAsPackage(cfg.getAsRootPackage());
//		}else{
//			msg.setAsPackage(cfg.getAsRootPackage() + "." + asPackage);
//		}
		return msg;
	}
	
	private void analyseLine(String line){
		Matcher m;
//		if(msg.getJavaPackage() == null){
//			m = JAVA_PACKAGE_PATTERN.matcher(line);
//			if(m.find()){
//				msg.setJavaPackage(m.group(1));
//				return;
//			}
//		}
//		if(msg.getAsPackage() == null){
//			m = AS_PACKAGE_PATTERN.matcher(line);
//			if(m.find()){
//				msg.setAsPackage(m.group(1));
//				return;
//			}
//		}
		m = COMMENT_PATTERN.matcher(line);
		if(m.find()){
			sb.append('\n');
			sb.append(StringExpandUtils.trim(m.group(1)));
			line = line.substring(0,m.start());
		}
		
//		if(msg.getName() == null){
			m = HEAD_PATTERN.matcher(line);
			if(m.find()){
//				msg.setName(m.group(1));
				if(sb.length()>2){
					msg.setComment(sb.substring(1, sb.length()));
				}
				sb.setLength(0);
				return;
			}
//		}
		m = HANDLER_PATTERN .matcher(line);
		if(m.find()){
			String[] array = m.group(1).split(",");
			for (String string : array) {
				if(string.equalsIgnoreCase("client")){
					msg.setClientHandler(true);
				}else if(string.equalsIgnoreCase("server")){
					msg.setServerHandler(true);
				}
			}
		}else{
			m = ATTRIBUTE_PATTERN.matcher(line);
			if(m.find()){
				String comment = null;
				boolean isArray = StringExpandUtils.isNotBlank(m.group(2));
				int arrayNums = NumberUtils.toInt(m.group(3), 0);
				if(sb.length()>2){
					comment = sb.substring(1, sb.length());
				}
				sb.setLength(0);
				msg.add(m.group(1), m.group(4), comment, isArray, arrayNums);
			}
		}
	}
}
