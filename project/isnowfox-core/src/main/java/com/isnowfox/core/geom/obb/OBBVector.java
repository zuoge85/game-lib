package com.isnowfox.core.geom.obb;

import java.awt.*;

public final class OBBVector extends Point.Double {
    //    public double x;
//    public double y;
//
    public OBBVector() {
    }

    public OBBVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public OBBVector sub(OBBVector v) {
        return new OBBVector(this.x - v.x, this.y - v.y);
    }

    public OBBVector sub(OBBVector v, OBBVector target) {
        target.x = this.x - v.x;
        target.y = this.y - v.y;
        return v;
    }

    /**
     * 若b为单位矢量，则a与b的点积即为a在方向b的投影。
     *
     * @param v
     * @return
     */
    public double dot(OBBVector v) {
        return this.x * v.x + this.y * v.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLenth() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vector [x=" + x + ", y=" + y + "]";
    }
}
