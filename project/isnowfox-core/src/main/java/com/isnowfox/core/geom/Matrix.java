package com.isnowfox.core.geom;

import java.awt.geom.Point2D;

/**
 * 开支as3的算法
 *
 * @author zuoge85 on 14-3-25.
 */
public class Matrix {

    public double a;

    public double b;

    public double c;

    public double d;

    public double tx;

    public double ty;

    public Matrix() {
        this.a = this.d = 1;
        this.b = this.c = 0.0;
        this.tx = this.ty = 0.0;
    }

    public Matrix(double a, double b, double c, double d, double tx, double ty) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
    }

    ;

    public Matrix clone() {
        return new Matrix(this.a, this.b, this.c, this.d, this.tx, this.ty);
    }


    public void identity() {
        this.a = this.d = 1;
        this.b = this.c = 0.0;
        this.tx = this.ty = 0.0;
    }

    public void rotate(double angle) {
        double u = Math.cos(angle);
        double v = Math.sin(angle);
        double result_a = u * this.a - v * this.b;
        double result_b = v * this.a + u * this.b;
        double result_c = u * this.c - v * this.d;
        double result_d = v * this.c + u * this.d;
        double result_tx = u * this.tx - v * this.ty;
        double result_ty = v * this.tx + u * this.ty;
        this.a = result_a;
        this.b = result_b;
        this.c = result_c;
        this.d = result_d;
        this.tx = result_tx;
        this.ty = result_ty;
    }


    public void translate(double dx, double dy) {
        this.tx = this.tx + dx;
        this.ty = this.ty + dy;
    }

    public void scale(double sx, double sy) {
        this.a = this.a * sx;
        this.b = this.b * sy;
        this.c = this.c * sx;
        this.d = this.d * sy;
        this.tx = this.tx * sx;
        this.ty = this.ty * sy;
    }


    public Point2D transformPoint(Point2D point) {
        return new Point2D.Double(this.a * point.getX() + this.c * point.getY() + this.tx, this.d * point.getY() + this.b * point.getX() + this.ty);
    }

    public void transformPointSet(Point2D point) {
        point.setLocation(this.a * point.getX() + this.c * point.getY() + this.tx, this.d * point.getY() + this.b * point.getX() + this.ty);
    }

    @Override
    public String toString() {
        return "(a=" + this.a + ", b=" + this.b + ", c=" + this.c + ", d=" + this.d + ", tx=" + this.tx + ", ty=" + this.ty + ")";
    }
}
