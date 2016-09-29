package com.isnowfox.core.geom;

/**
 * @author zuoge85 on 2014/5/29.
 */
public class FourBoolean {
    public boolean top;
    public boolean right;
    public boolean bottom;
    public boolean left;

    public FourBoolean() {

    }

    public FourBoolean(boolean top, boolean right, boolean bottom, boolean left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public void setTo(boolean top, boolean right, boolean bottom, boolean left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }


    public void topBottom(boolean top, boolean bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public void rightLeft(boolean right, boolean left) {
        this.right = right;
        this.left = left;
    }


    @Override
    public String toString() {
        return "FourBoolean{" +
                "top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", left=" + left +
                '}';
    }
}
