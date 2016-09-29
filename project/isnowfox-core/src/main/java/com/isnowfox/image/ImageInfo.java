package com.isnowfox.image;


/**
 * 图片信息对象
 *
 * @author zuoge85
 */
public class ImageInfo {
    private int width;
    private int heigth;
    private byte[] data;
    private ImageType type;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }


}
