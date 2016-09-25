package com.isnowfox.dbtool.mysql;


public class NameUtils {
	
	public static final String toClassName(String str){
		StringBuilder sb=new StringBuilder();
		boolean isUp=false;
		for(int i=0;i<str.length();i++){
			char c=str.charAt(i);
			if (0==i) {
				sb.append(Character.toUpperCase(c));
			}else if(c=='_'){
				isUp=true;
			}else if(isUp){
				isUp=false;
				sb.append(Character.toUpperCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static final String toFieldName(String str){
		StringBuilder sb=new StringBuilder();
		boolean isUp=false;
		for(int i=0;i<str.length();i++){
			char c=str.charAt(i);
			if(i ==0){
				c = Character.toLowerCase(c);
			}
			if(c=='_'){
				isUp=true;
			}else if(isUp){
				isUp=false;
				sb.append(Character.toUpperCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
