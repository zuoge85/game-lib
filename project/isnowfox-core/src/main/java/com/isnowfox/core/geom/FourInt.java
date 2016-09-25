package com.isnowfox.core.geom;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author zuoge85 on 2014/5/29.
 */
public class FourInt {
    public int top;
    public int right;
    public int bottom;
    public int left;

    public FourInt() {

    }

    public FourInt(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public void setTo(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }


    public void topBottom(int top,int bottom)
    {
        this.top = top;
        this.bottom = bottom;
    }

    public void rightLeft(int right,int left)
    {
        this.right = right;
        this.left = left;
    }


    @Override
    public String toString() {
        return "FourInt{" +
                "top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", left=" + left +
                '}';
    }

    public void setByString(String entityRectStr) {
        if (entityRectStr != null)
        {
            String[] entityRectArray = entityRectStr.split(",");
            if(entityRectArray.length == 4)
            {
                int i =0;
                top =  NumberUtils.toInt(entityRectArray[i++]);
                right = NumberUtils.toInt(entityRectArray[i++]);
                bottom = NumberUtils.toInt(entityRectArray[i++]);
                left = NumberUtils.toInt(entityRectArray[i]);
            }
        }
    }
}
