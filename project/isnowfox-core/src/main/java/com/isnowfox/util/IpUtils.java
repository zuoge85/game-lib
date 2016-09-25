package com.isnowfox.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class IpUtils {
	private static final Log log = LogFactory.getLog(IpUtils.class);
	/**
	 * 错误情况返回0,不抛出异常
	 * @param addr
	 * @return
	 */
	public static final int parseIp4(String addr){
		try {
			InetAddress ip = InetAddress.getByName(addr);
			byte b[] = ip.getAddress();
			if(b.length==4){
				return ((b[0] & 0xFF) << 24) + ((b[1] & 0xFF) << 16)
				+ ((b[2] & 0xFF) << 8) + ((b[3]) << 0);
			}
			return 0;
		} catch (UnknownHostException e) {
			log.info("错误的ip地址:"+addr,e);
			return 0;
		}
	}
	public static final String formatIp4(int i){
		byte[] addr = new byte[4];
		addr[0] = (byte) ((i >>> 24) & 0xFF);
		addr[1] = (byte) ((i >>> 16) & 0xFF);
		addr[2] = (byte) ((i >>> 8) & 0xFF);
		addr[3] = (byte) (i & 0xFF);
		return (addr[0] & 0xff) + "." + (addr[1] & 0xff) + "." + (addr[2] & 0xff) + "." + (addr[3] & 0xff);
	}
	public static final int inet4AddressToInt(Inet4Address add){
		byte[] b = add.getAddress();
		if(b.length==4){
			return ((b[0] & 0xFF) << 24) + ((b[1] & 0xFF) << 16)
			+ ((b[2] & 0xFF) << 8) + ((b[3]) << 0);
		}
		return 0;
	}
	public static final int InetSocketAddressToInt(InetSocketAddress add){
		byte[] b = add.getAddress().getAddress();
		if(b.length==4){
			return ((b[0] & 0xFF) << 24) + ((b[1] & 0xFF) << 16)
					+ ((b[2] & 0xFF) << 8) + ((b[3]) << 0);
		}
		return add.hashCode();
	}
	
}
