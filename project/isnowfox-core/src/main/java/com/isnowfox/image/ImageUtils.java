package com.isnowfox.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * 图片工具类,使用iio，需要优化！
 * @author zuoge85
 *
 */
public final class ImageUtils {
	public static final String PNG="png";
	public static final String JPG="jpeg";
	public static final String BMP="bmp";
	public static final String GIF="gif";
	public static byte[] readFromFile(String path) throws IOException {
		InputStream is = new FileInputStream(new File(path));
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		return buf;
	}

	/**
	 * 构建一个image对象
	 * 
	 * @param img
	 * @return
	 * @throws IOException
	 */
	public static ImageInfo getImageInfo(byte[] img) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		MemoryCacheImageInputStream is=new MemoryCacheImageInputStream(bais);
		Iterator<ImageReader> it=ImageIO.getImageReaders(is);
		ImageReader r=null;
		while(it.hasNext()){
			r=it.next();
			break;
		}
		if(r==null){
			return null;
		}
		ImageInfo i=new ImageInfo();
		i.setType(ImageType.forName(r.getFormatName().toLowerCase()));
		int index=r.getMinIndex();
		/**
		 * 对于ImageReader的线程安全是不确定的
		 */
		synchronized (r) {
			r.setInput(is);
			i.setData(img);
			i.setHeigth(r.getHeight(index));
			i.setWidth(r.getWidth(index));
		}
		return i;
	}
	public static BufferedImage getImage(byte[] img) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		BufferedImage src = ImageIO.read(bais);
		return src;
	}
	public static int getHeight(int width,int sourceWidth,int sourceHight){
		int height=(int) (((float)width/sourceWidth)*sourceHight);
		return height;
	}
	public static String getImageUrl(int id,int width,int height){
		return "/images.vm?id="+id+"&width="+width+"&height="+height;
	}
	public static String getImageUrl(String id,String width,String height){
		return "/images.vm?id="+id+"&width="+width+"&height="+height;
	}
	public static String getImageUrl(int id,int width){
		return "/images.vm?id="+id+"&width="+width;
	}
	public static String getImageUrl(String id,String width){
		return "/images.vm?id="+id+"&width="+width;
	}
	public static String getHeadImageUrl(Object id,Object sex,String width,String height){
		if(Integer.valueOf(0).equals(id)||"0".equals(id)||"".equals(id)){
			return "/face/"+width+"x"+height+".jpg";
		}
		return "/images.vm?id="+id+"&width="+width+"&height="+height;
	}
	/**
	 * 等比例缩放
	 * @param img
	 * @param width
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getScaleImage(byte[] img,int width,String outType) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		BufferedImage src = ImageIO.read(bais);
		int w=src.getWidth();
		int h=src.getHeight();
		int height=(int) (((float)width/w)*h);
		Image im=src.getScaledInstance(width, height,Image.SCALE_SMOOTH);
		BufferedImage bi=new BufferedImage(width, height, getType(src,outType));
		bi.getGraphics().drawImage(im, 0, 0,null);
		return bi;
	}
	/**
	 * 等比例缩放填充白色
	 * @param img
	 * @param cwidth
	 * @param cheight
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getScaleImage(byte[] img,int cwidth,int cheight,String outType) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(img);
		BufferedImage src = ImageIO.read(bais);
		int w=src.getWidth();
		int h=src.getHeight();
		int height=(int) (((float)cwidth/w)*h);
		int width=cwidth;
		if(height>cheight){
			width=(int) (((float)cheight/h)*w);
			height=cheight;
		}
		Image im=src.getScaledInstance(width, height,Image.SCALE_SMOOTH);
		BufferedImage bi=new BufferedImage(cwidth, cheight,getType(src,outType));
		int left=(cwidth-width)/2;
		int top=(cheight-height)/2;
		Graphics g=bi.getGraphics();
		g.setColor(Color.WHITE);

		g.fillRect(0, 0, cwidth, cheight);
		bi.getGraphics().drawImage(im, left, top,null);
		return bi;
	}
	private static int getType(BufferedImage bi,String outType){
		if(outType.equalsIgnoreCase("gif")){
			return BufferedImage.TYPE_4BYTE_ABGR_PRE;
		}
		return BufferedImage.TYPE_3BYTE_BGR;
	}
	public static byte[] getScaleImageBytes(byte[] img,String outType,int width) throws IOException {
		BufferedImage bi=getScaleImage(img,width,outType);
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ImageIO.write(bi, outType, out);
		return out.toByteArray();
	}
	public static byte[] getScaleImageBytes(byte[] img,String outType,int width,int height) throws IOException {
		BufferedImage bi=getScaleImage(img,width,height,outType);
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ImageIO.write(bi, outType, out);
		return out.toByteArray();
	}
	/**
	 * 获取文件类型,没找到返回null,这方法太高效了,可能不准确,
	 * 这个是我看的网上的，有bug不准确
	 * @param byte1
	 * @return
	 */
	public static String fastParseFileType(byte[] byte1) {
		if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70)
				&& (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57))
				&& (byte1[5] == 97)) {
			return GIF;
		}
		if ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73)
				&& (byte1[9] == 70)) {
			return JPG;
		}
		if ((byte1[0] == 66) && (byte1[1] == 77)) {
			return BMP;
		}
		if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
			return PNG;
		}
		return null;
	};
}
