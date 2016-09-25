package com.isnowfox.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.Maps;

public final class MimeUtils {
	private static Map<String,String> map=Maps.newHashMap();
	private static Exception exception;
	static{
		//加载
		InputStream is=null;
        map.put("xml", "text/xml");
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("png", "image/png");
		try {
//			is = MimeUtils.class.getResourceAsStream("mime.xml");
//			SAXBuilder builder = new SAXBuilder();
//			Document doc = builder.build(is);
//			Element root = doc.getRootElement();
//			@SuppressWarnings("unchecked")
//			List<Element> list=root.getChildren("mime-mapping");
//			for(Element item:list){
//				String ext=item.getChildTextTrim("extension");
//				String mimetype=item.getChildTextTrim("mime-type");
//				if(StringUtils.isNotEmpty(ext)&&StringUtils.isNotEmpty(mimetype)){
//					map.put(ext, mimetype);
//				}
//			}
		} catch (Exception e) {
			exception=e;
		}finally{
			if(null!=is){
				try {
					is.close();
				} catch (IOException e) {
					exception=e;
				}
			}
		}
	}
	public static String getMimeType(String extension) throws Exception{
		if(null==exception){
			return map.get(extension);
		}else{
			throw exception;
		}
	}
}
