package com.isnowfox.core.geom;


public final class IntRectangle {
    public int x;
    public int y;
    public int width;
    public int height;

    public IntRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public IntRectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public IntRectangle() {

    }

    public IntRectangle(IntRectangle intRectangle) {
        this.x = intRectangle.x;
        this.y = intRectangle.y;
        this.width = intRectangle.width;
        this.height = intRectangle.height;
    }

    public void set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getRight() {
        return x + width;
    }

    public int getBottom() {
        return y + height;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean contains(int x, int y) {
        if (x < this.x || y < this.y) {
            return false;
        }
        if (x >= this.x + this.width || y >= this.y + this.height) {
            return false;
        }
        return true;
    }

    @Override
    public IntRectangle clone() {
        try {
            return (IntRectangle) super.clone();
        } catch (CloneNotSupportedException e) {
            return new IntRectangle(this);
        }
    }

    @Override
    public String toString() {
        return "Rectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
    }


}
