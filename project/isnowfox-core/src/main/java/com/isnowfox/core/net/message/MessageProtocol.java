package com.isnowfox.core.net.message;

import com.isnowfox.core.io.MarkCompressProtocol;

/**
 * @author zuoge85
 *
 */
public interface MessageProtocol {
	public static final int MESSAGE_MAX 						= 0xFFFFFF;
	
	/**
	 * 注意，修改这个必须修改decoder和encoder
	 */
	public static final int LENGTH_BYTE_NUMS 				= 3;
	public static final int HEAD_LENGTH			 				= 4;
	
	public static final int TYPE_NORMAL	 						= 0;
	public static final int TYPE_GZIP			 						= 1;
	
	public static final int TYPE_OR_ID_MAX						= MarkCompressProtocol.TYPE_ONE_BYTE_MAX;
	
	/**
	 * 自定义消息id
	 */
	public static final int EXPAND_MESSAGE_TYPE	= 0;


    public static final int LOGIN_MESSAGE_TYPE	= 7;
}
