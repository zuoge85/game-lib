package com.isnowfox.web.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.google.common.collect.Maps;
import com.isnowfox.web.Request;


/**
 * TODO 现在的实现不支持上传图片
 * @author zuoge85@gmail.com
 *
 */
public abstract class HttpRequest extends Request{
	private static final String HEADER_NAME_COOKIE="Cookie";
	
	protected org.jboss.netty.handler.codec.http.HttpRequest request;
	protected String uri;
	protected Charset charset;
	protected String path;
	protected Channel channel;
	private Map<String,Cookie> cookieMap;
	private boolean initCookie=false;
	/**
	 * 初始化
	 * @param request
	 * @param channel 
	 * @throws IOException 
	 */
	public static Request create(org.jboss.netty.handler.codec.http.HttpRequest  request, byte[] post, Channel channel,Charset charsert) throws IOException {
		HttpMethod method=request.getMethod();
		HttpRequest re;
		if (method==HttpMethod.GET) {
			re=new HttpGetRequest(request,channel,charsert);
		}else if (method.equals(HttpMethod.POST)) {
			re=new HttpPostRequest(request,post,channel,charsert);
		} else {
			throw new IOException("No Support");
		}
		return re;
	}
	/**
	 * 对cookie的解析不是线程安全的,但是这不影响,一般情况下,action的处理应该是单线程的,
	 * 特殊情况下需要加锁调用这个函数比较安全
	 * @param key
	 * @return
	 */
	private Cookie getNettyCookie(String key){
		CookieDecoder cd=new CookieDecoder();
		if(cookieMap==null){
			if(initCookie){
				return null;
			}else{
				initCookie=true;
				String cookieString=request.getHeader(HEADER_NAME_COOKIE);
				if(cookieString!=null){
					Set<Cookie> set=cd.decode(cookieString);
					cookieMap=Maps.newHashMapWithExpectedSize(set.size());
					for(Cookie c:set){
						cookieMap.put(c.getName(), c);
					}
					return cookieMap.get(key);
				}
			}
		}else{
			return cookieMap.get(key);
		}
		return null;
	}
	
	@Override
	public String getCookieString(String key){
		Cookie c= getNettyCookie(key);
		if(c != null){
			return c.getValue();
		}
		return null;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getHeader(String name){
		return request.getHeader(name);
	}
	
	public List<String> getHeaders(String name){
		return request.getHeaders(name);
	}

	public List<Map.Entry<String, String>> getHeaders(){
		return request.getHeaders();
	}

	public boolean containsHeader(String name){
		return request.containsHeader(name);
	}

	public Set<String> getHeaderNames(){
		return request.getHeaderNames();
	}
	
	@Override
	public byte[] getBytes(String key) {
		throw new UnsupportedOperationException("No Support");
	}

	

	@Override
	public boolean getBoolean(String key) {
		return Boolean.valueOf(getString(key));
	}

	@Override
	public String getStringByCharset(String key, String charset) {
		Charset inCharset=Charset.forName(charset);
		String value=getString(key);
		if(!this.charset.contains(inCharset)){
			value= new String(value.getBytes(this.charset),inCharset);
		}
		return value;
	}

	@Override
	public int getInt(String key) {
		return NumberUtils.toInt(getString(key));
	}


	@Override
	public int getInt(String key, int defaultValue) {
		return NumberUtils.toInt(getString(key),defaultValue);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return NumberUtils.toFloat(getString(key), defaultValue);
	}
	
	@Override
	public int getInt(String key, int defaultValue, int max) {
		return Math.min(getInt(key,defaultValue),max);
	}
	@Override
	public int getIntMin(String key, int defaultValue, int min) {
		return Math.max(getInt(key,defaultValue),min);
	}
	@Override
	public int getInt(String key, int defaultValue, int min, int max) {
		int i=NumberUtils.toInt(getString(key),defaultValue);
		i=(i<min?min:i);
		return i>max?max:i;
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public String getRemoteInfo() {
		Object o=channel.getRemoteAddress();
		return o==null?null:o.toString();
	}
	@Override
	public int getLocalPort() {
		SocketAddress localAddress = channel.getLocalAddress();
		if(localAddress instanceof InetSocketAddress){
			return ((InetSocketAddress) localAddress).getPort();
		}
		return -1;
	}

	@Override
	public String getPath() {
		return path;
	}
	
	public static class HttpGetRequest extends HttpRequest{
		private Map<String,List<String>> params;
		//private final Config config=Context.getInstance().getConfig();
		private HttpGetRequest (org.jboss.netty.handler.codec.http.HttpRequest  request, Channel channel, Charset charset) throws IOException{
			uri=request.getUri();
			this.charset =charset;
			this.request=request;
			QueryStringDecoder decoder=new QueryStringDecoder(uri,charset);
			//准备参数
			params =decoder.getParameters();
			path=decoder.getPath();
			this.channel=channel;
		}
		@Override
		public boolean isPost() {
			return false;
		}

		public  Map<String,List<String>> getAllParams() {
			return params;
		}

		public final String getString(String key){
			List<String> list=params.get(key);
			if(list!=null&&!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}
		
		public String getString(String key, String defaultValue) {
			List<String> list=params.get(key);
			if(list!=null&&!list.isEmpty()){
				return list.get(0);
			}
			return defaultValue;
		}

		@Override
		public List<String> getStringList(String key) {
			return params.get(key);
		}
	}
	public static class HttpPostRequest extends HttpRequest{
		private Map<String,List<String>> params;
		//private final Config config=Context.getInstance().getConfig();
		private HttpPostRequest(org.jboss.netty.handler.codec.http.HttpRequest  request, byte[] post, Channel channel, Charset charset) throws IOException{
			uri=request.getUri();
			this.charset=charset;
			this.request=request;
			QueryStringDecoder decoder=new QueryStringDecoder(uri,charset);
			//准备参数
			params =decoder.getParameters();
			if(isMultipartContent(request)){
				
			}else if(post!=null){//处理post
				String str=new String(post,charset);
				//QueryStringDecoder解析出为null的map是不能插入任何东西的
				if (params.isEmpty()) {
					 params = new LinkedHashMap<String, List<String>>();
				}
				decodeParams(params,str);
			}
			path=decoder.getPath();
			this.channel=channel;
		}

		public  Map<String,List<String>> getAllParams() {
			return params;
		}

		private Map<String, List<String>> decodeParams( Map<String, List<String>> params,String s) {
	         String name = null;
	        int pos = 0; // Beginning of the unprocessed region
	        int i;       // End of the unprocessed region
	        char c = 0;  // Current character
	        for (i = 0; i < s.length(); i++) {
	            c = s.charAt(i);
	            if (c == '=' && name == null) {
	                if (pos != i) {
	                    name = decodeComponent(s.substring(pos, i), charset);
	                }
	                pos = i + 1;
	            } else if (c == '&') {
	                if (name == null && pos != i) {
	                    // We haven't seen an `=' so far but moved forward.
	                    // Must be a param of the form '&a&' so add it with
	                    // an empty value.
	                    addParam(params, decodeComponent(s.substring(pos, i), charset), "");
	                } else if (name != null) {
	                    addParam(params, name, decodeComponent(s.substring(pos, i), charset));
	                    name = null;
	                }
	                pos = i + 1;
	            }
	        }

	        if (pos != i) {  // Are there characters we haven't dealt with?
	            if (name == null) {     // Yes and we haven't seen any `='.
	                addParam(params, decodeComponent(s.substring(pos, i), charset), "");
	            } else {                // Yes and this must be the last value.
	                addParam(params, name, decodeComponent(s.substring(pos, i), charset));
	            }
	        } else if (name != null) {  // Have we seen a name without value?
	            addParam(params, name, "");
	        }

	        return params;
	    }

	    private static String decodeComponent(String s, Charset charset) {
	        if (s == null) {
	            return "";
	        }

	        try {
	            return URLDecoder.decode(s, charset.name());
	        } catch (UnsupportedEncodingException e) {
	            throw new UnsupportedCharsetException(charset.name());
	        }
	    }

	    private static void addParam(Map<String, List<String>> params, String name, String value) {
	        List<String> values = params.get(name);
	        if (values == null) {
	            values = new ArrayList<String>(1);  // Often there's only 1 value.
	            params.put(name, values);
	        }
	        values.add(value);
	    }
		private static final boolean isMultipartContent(org.jboss.netty.handler.codec.http.HttpRequest request) {
	        String contentType = request.getHeader("Content-Type");
	        if (contentType == null) {
	            return false;
	        }
	        if (contentType.toLowerCase().startsWith("multipart/")) {
	            return true;
	        }
	        return false;
	    }
		@Override
		public boolean isPost() {
			return true;
		}

		public final String getString(String key){
			List<String> list=params.get(key);
			if(list!=null&&!list.isEmpty()){
				return list.get(0);
			}
			return null;
		}
		
		public String getString(String key, String defaultValue) {
			List<String> list=params.get(key);
			if(list!=null&&!list.isEmpty()){
				return list.get(0);
			}
			return defaultValue;
		}

		@Override
		public List<String> getStringList(String key) {
			return params.get(key);
		}
		
	}
	
}
