package com.isnowfox.image;


/**
 * 1	jpg/jpeg	基本上支持所有平台
 * 2	gif		支持单帧
 * 3	bmp		非所有平台都支持
 * 4	png		非所有平台都支持
 *
 * @author zuoge85
 */
public enum ImageType {
    unknown(0), jpg(1), gif(2), bmp(3), png(4);

    ImageType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public static final ImageType forName(String name) {
        if ("jpg".equalsIgnoreCase(name)) {
            return ImageType.jpg;
        } else if ("jpeg".equalsIgnoreCase(name)) {
            return ImageType.jpg;
        } else if ("gif".equalsIgnoreCase(name)) {
            return ImageType.gif;
        } else if ("bmp".equalsIgnoreCase(name)) {
            return ImageType.png;
        } else if ("png".equalsIgnoreCase(name)) {
            return ImageType.png;
        } else {
            return ImageType.unknown;
        }
    }

    public static final ImageType forValue(int value) {
        ImageType[] is = ImageType.values();
        if (value < is.length) {
            return is[value];
        } else {
            return unknown;
        }
    }
}
