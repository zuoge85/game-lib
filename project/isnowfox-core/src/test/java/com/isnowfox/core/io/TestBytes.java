package com.isnowfox.core.io;

import com.isnowfox.core.junit.BaseTest;

public class TestBytes extends BaseTest {
    public void test() {

//		byte b = (byte) 200;
//		System.out.println(System.currentTimeMillis());
//		System.out.println(b& 0xFF);
//		System.out.println(Integer.toBinaryString(b& 0xFF));
//		System.out.println(Integer.toBinaryString(200));
    }
//	
//	public void  testBoolean() throws IOException, ProtocolException{
//		checkBoolean(true);//非常没必要，boolean最多一个字节~~~
//		checkBoolean(false);
//	}
//	
//	public void  testString()throws IOException, ProtocolException{
//		String str1 = "@#@#%$rfsaf432423321$#%#$5\n\r";
//		String str2 = "焱3213￥#￥#%$#@$#@%#$%$#% ．，；ＰＰDDFDASFD321as放大放大发达r";
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out);
//		o.writeString(str1);
//		o.writeString(str2);
//		o.writeString("");
//		o.writeString(null);
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in);
//		
//		assertEquals(str1, i.readString());
//		assertEquals(str2, i.readString());
//		assertEquals("", i.readString());
//		assertEquals(null, i.readString());
//	}
//	
//	public void  testInt() throws IOException, ProtocolException{
//		checkInt(false, 255);
//		checkInt(false, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkInt(false,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkInt(false,MarkCompressProtocol.THREE_MIN, MarkCompressProtocol.THREE_MAX);
//		checkInt(false,Integer.MAX_VALUE);
//		checkInt(false,Integer.MIN_VALUE);
//		
//		checkInt(true, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkInt(true,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkInt(true,MarkCompressProtocol.THREE_MIN, MarkCompressProtocol.THREE_MAX);
//		checkInt(true,Integer.MAX_VALUE);
//		checkInt(true,Integer.MIN_VALUE);
//	}
//	
//	public void  testDouble() throws IOException, ProtocolException{
//		checkDouble(false, 255);
//		checkDouble(false, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkDouble(false,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkDouble(false,MarkCompressProtocol.THREE_MIN+0.3, MarkCompressProtocol.THREE_MAX+0.3);
//		checkDouble(false,Integer.MAX_VALUE);
//		checkDouble(false,Integer.MIN_VALUE);
//		
//		checkDouble(true, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkDouble(true,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkDouble(true,MarkCompressProtocol.THREE_MIN+0.3, MarkCompressProtocol.THREE_MAX+0.3);
//		checkDouble(true,Integer.MAX_VALUE);
//		checkDouble(true,Integer.MIN_VALUE);
//		
//		checkDouble(true,Double.MAX_VALUE);
//		checkDouble(true,Double.MIN_VALUE);
//		checkDouble(true,Double.MAX_VALUE-1);
//		checkDouble(true,Double.MIN_VALUE+1);
//		
//		checkDouble(true,Double.NaN);
//		checkDouble(true,Double.NEGATIVE_INFINITY);
//		checkDouble(true,Double.POSITIVE_INFINITY);
//	}
//	
//	public void  testLong() throws IOException, ProtocolException{
//		checkLong(false, 255);
//		checkLong(false, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkLong(false,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkLong(false,MarkCompressProtocol.THREE_MIN, MarkCompressProtocol.THREE_MAX);
//		checkLong(false,Integer.MAX_VALUE);
//		checkLong(false,Integer.MIN_VALUE);
//		
//		checkLong(true, -1, 0);//非常没必要，boolean最多一个字节~~
//		checkLong(true,Byte.MIN_VALUE, Byte.MAX_VALUE);
//		checkLong(true,MarkCompressProtocol.THREE_MIN, MarkCompressProtocol.THREE_MAX);
//		checkLong(true,Integer.MAX_VALUE);
//		checkLong(true,Integer.MIN_VALUE);
//		
//		checkLong(true,Long.MAX_VALUE);
//		checkLong(true,Long.MIN_VALUE);
//		checkLong(true,Long.MAX_VALUE-1);
//		checkLong(true,Long.MIN_VALUE+1);
//	}
//	
//	public void  checkBoolean(boolean isBigEndian) throws IOException, ProtocolException{
//		boolean t = true;
//		boolean f = false;
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		o.writeBoolean(t);
//		o.writeBoolean(f);
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		assertEquals(t, i.readBoolean());
//		assertEquals(f, i.readBoolean());
//	}
//	
//	
//	public void  checkInt(boolean isBigEndian,int v)throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		o.writeInt(v);
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		assertEquals(v, i.readInt());
//		
//	}
//
//	public void  checkInt(boolean isBigEndian,int min, int max) throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		for (int i = min; i <= max; i++) {
//			o.writeInt(i);
//		}
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		for (int j = min; j <= max; j++) {
//			assertEquals(j, i.readInt());
//		}
//		
//	}
//	
//	public void  checkLong(boolean isBigEndian,long min, long max) throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		for (long i = min; i <= max; i++) {
//			o.writeLong(i);
//		}
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		for (long j = min; j <= max; j++) {
//			assertEquals(j, i.readLong());
//		}
//	}
//	
//	public void  checkLong(boolean isBigEndian,long v)throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		o.writeLong(v);
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		assertEquals(v, i.readLong());
//		
//	}
//	
//	public void  checkDouble(boolean isBigEndian,double min, double max) throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		for (double i = min; i <= max; i++) {
//			o.writeDouble(i);
//		}
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		for (double j = min; j <= max; j++) {
//			assertEquals(j, i.readDouble());
//		}
//	}
//	
//	public void  checkDouble(boolean isBigEndian,double v)throws IOException, ProtocolException{
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Output o = MarkCompressOutput.create(out, isBigEndian);
//		o.writeDouble(v);
//		
//		ByteArrayInputStream in =  new ByteArrayInputStream(out.toByteArray());
//		Input i = MarkCompressInput.create(in, isBigEndian);
//		
//		assertEquals(v, i.readDouble());
//		
//	}
////	  private int getBitnums(int i) {
////	        // HD, Figure 5-6
////	        if (i == 0)
////	            return 32;
////	        int n = 1;
////	        if (i >>> 16 == 0) { n += 16; i <<= 16; }
////	        if (i >>> 24 == 0) { n +=  8; i <<=  8; }
////	        if (i >>> 28 == 0) { n +=  4; i <<=  4; }
////	        if (i >>> 30 == 0) { n +=  2; i <<=  2; }
////	        n -= i >>> 31;
////	        return n;
////	    }
}
