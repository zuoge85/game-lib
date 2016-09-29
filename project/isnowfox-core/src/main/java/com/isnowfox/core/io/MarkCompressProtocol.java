package com.isnowfox.core.io;

/**
 * 协议格式定义
 * 第一个位表示标识位,读取到1表示结束
 *
 * @author zuoge85
 */
public interface MarkCompressProtocol {
    public static final String DEFAULT_CHARSET = "utf-8";
    public static final boolean DEFAULT_IS_BIG_ENDIAN = true;

    public static final int THREE_MAX = 0x7fffff;

    public static final int THREE_MIN = -8388608;
    /**
     * 第一个字节只有7位数可以用
     */
    public static final int FIRST_WIDTH = 7;

    /**
     * 其他字节宽度
     */
    public static final int OTHER_WIDTH = 8;

    public static final int TYPE_NULL_OR_ZERO_OR_FALSE = 0xFF - 1;
    public static final int TYPE_TRUE = 0xFF - 2;

    public static final int TYPE_INT_1BYTE = 0xFF - 3;
    public static final int TYPE_INT_2BYTE = 0xFF - 4;
    public static final int TYPE_INT_3BYTE = 0xFF - 5;
    public static final int TYPE_INT_4BYTE = 0xFF - 6;

    public static final int TYPE_INT_5BYTE = 0xFF - 7;
    public static final int TYPE_INT_6BYTE = 0xFF - 8;
    public static final int TYPE_INT_7BYTE = 0xFF - 9;
    public static final int TYPE_INT_8BYTE = 0xFF - 10;


    public static final int TYPE_STRING = 0xFF - 11;
    public static final int TYPE_BYTES = 0xFF - 12;


    public static final int TYPE_MAX = 0xFF - 14;
    public static final int TYPE_MIN = 0;

    public static final int TYPE_MINUS = 4;

    public static final int TYPE_ONE_BYTE_MAX = TYPE_MAX - TYPE_MINUS;
    public static final int TYPE_ONE_BYTE_MIN = 0 - TYPE_MINUS;
//	public static final int TYPE_DOUBLE_1BYTE 				= 4; 
//	public static final int TYPE_DOUBLE_1BYTE 				= 4; 
//	public static final int TYPE_DOUBLE_1BYTE 				= 4; 
//	public static final int TYPE_DOUBLE_1BYTE 				= 4; 
//	public static final int TYPE_DOUBLE_1BYTE 				= 4; 
//	public static final int TYPE_DOUBLE_1BYTE					= 4; 

}
