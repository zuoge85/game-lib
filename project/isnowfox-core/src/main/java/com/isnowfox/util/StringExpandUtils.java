package com.isnowfox.util;

import org.apache.commons.lang.CharUtils;

public final class  StringExpandUtils extends org.apache.commons.lang.StringUtils{
	public static String unicodeEncode(String str){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<str.length();i++){
			sb.append(CharUtils.unicodeEscaped(str.charAt(i)));
		}
		return sb.toString();
	}
	/**
	 * 注意字处理<>两个符号,其他不处理
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str){
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c=str.charAt(i);
			if('<'==c){
				sb.append("&lt;");
			}else if('>'==c){
				sb.append("&gt;");
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static boolean checkLengthRange(String str,int min,int max) {
		int l = (str == null ? 0 : str.length());
	    return l >= min && l <= max;
	}
}
