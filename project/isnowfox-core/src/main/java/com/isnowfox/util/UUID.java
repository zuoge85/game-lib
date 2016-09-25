package com.isnowfox.util;

import java.net.InetAddress;

/**
 * 借鉴hibernate 的方式
 * @author zuoge85@gmail.com
 *
 */
public final class UUID {
    private static final char sep = '-';  
    private static final int ip;
    static {  
        int ipadd;  
        try {
            ipadd = toInt( InetAddress.getLocalHost().getAddress() );  
        }  
        catch (Exception e) {  
            ipadd = 0;
        }  
        ip = ipadd;  
    }  
    private static short counter =  0;  
    
    private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );  
    /** 
     */  
    protected static int getJVM() {  
        return JVM;  
    }  
  
    /** 
     * 计数
     */  
    protected static short getCount() {  
        synchronized(UUID.class) {  
            if (counter<0) counter=0;  
            return counter++;  
        }  
    }  
  
    /** 
     * Unique in a local network 
     */  
    protected static int getIP() {  
        return ip;  
    }  
  
    /** 
     * Unique down to millisecond 
     */  
    protected static short getHiTime() {  
        return (short) ( System.currentTimeMillis() >>> 32 );  
    }  
    protected static int getLoTime() {  
        return (int) System.currentTimeMillis();  
    }  
    public static String generate() {  
        return new StringBuilder( 36 )  
                .append( format( getIP() ) ).append( sep )  
                .append( format( getJVM() ) ).append( sep )  
                .append( format( getHiTime() ) ).append( sep )  
                .append( format( getLoTime() ) ).append( sep )  
                .append( format( getCount() ) )  
                .toString();  
    }  
    public static String generateNoSep() {  
        return new StringBuilder( 32 )  
                .append( format( getIP() ) )
                .append( format( getJVM() ) )
                .append( format( getHiTime() ) )
                .append( format( getLoTime() ) )
                .append( format( getCount() ) )  
                .toString();  
    } 
    protected static String format(int intValue) {  
        String formatted = Integer.toHexString( intValue );  
        StringBuilder buf = new StringBuilder( "00000000" );  
        buf.replace( 8 - formatted.length(), 8, formatted );  
        return buf.toString();  
    }  
  
    protected static String format(short shortValue) {  
        String formatted = Integer.toHexString( shortValue );  
        StringBuilder buf = new StringBuilder( "0000" );  
        buf.replace( 4 - formatted.length(), 4, formatted );  
        return buf.toString();  
    }  
    public static int toInt(byte[] bytes) {  
        int result = 0;
        //将result每次乘256 -128+ bytes[i]  
        for ( int i = 0; i < 4; i++ ) {  
            result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];  
        }  
        return result;  
    } 
    
   
}
