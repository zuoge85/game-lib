package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Point2D;

/**
 * 矩形方向包围盒
 * 需要调用commit 生效修改
 *
 * @author zuoge85
 */
public class OBBTransformRectangle extends OOBAbstractRect {

    private static final Logger log = LoggerFactory.getLogger(OBBTransformRectangle.class);
    // public final static create(){
    //
    // }
    private double width;
    private double height;
    private double rotation;
    private double rotationX = 0;
    private double rotationY = 0;


    private boolean isReverse = false;

    private final Matrix matrix = new Matrix();

    public OBBTransformRectangle() {

    }

    public OBBTransformRectangle(double centerX, double centerY, double width, double height, double rotation) {
        super(centerX, centerY, width, height, rotation);
    }

    public void setByLeftTop(double leftTopX, double leftTopY, double width, double height, double rotation) {
        innerSet(leftTopX + width / 2, leftTopY + height / 2, width, height, rotation);
    }

    public void set(double centerX, double centerY, double width, double height, double rotation) {
        innerSet(centerX, centerY, width, height, rotation);
    }

    @Override
    protected void innerSet(double centerX, double centerY, double width, double height, double rotation) {
        super.innerSet(centerX, centerY, width, height, rotation);
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public void setCenter(double centerX, double centerY) {
        this.centerPoint.set(centerX, centerY);
    }


    public void setRotationPoint(double rotationX, double rotationY) {
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }

    public void commit() {
        execute();
    }

    public void commit2() {
        matrix.identity();
        matrix.scale(1, 1);
        matrix.rotate(rotation);
        matrix.translate(getCenterPoint().getX(), getCenterPoint().getY());
        Point2D.Double[] array = new Point2D.Double[4];

        // left top
        array[0] = new Point2D.Double(-width / 2, -height / 2);
        matrix.transformPointSet(array[0]);

        // right top
        array[1] = new Point2D.Double(width / 2, -height / 2);
        matrix.transformPointSet(array[1]);

        // left bottom
        array[2] = new Point2D.Double(-width / 2, height / 2);
        matrix.transformPointSet(array[2]);

        // right bottom
        array[3] = new Point2D.Double(width / 2, height / 2);
        matrix.transformPointSet(array[3]);

        Point2D.Double leftTop = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
        Point2D.Double rightBottom = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);
        for (Point2D.Double point : array) {
            if (point.x < leftTop.x) {
                leftTop.x = point.x;
            }
            if (point.y < leftTop.y) {
                leftTop.y = point.y;
            }

            if (point.x > rightBottom.x) {
                rightBottom.x = point.x;
            }
            if (point.y > rightBottom.y) {
                rightBottom.y = point.y;
            }
        }
        bounds.set(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
        super.innerSet(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2, width, height, rotation);
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
    }

    /**
     * TODO 现在实现了一个可能很差劲的算法计算 选择后的外框矩形
     */
    private void execute() {
        matrix.identity();
        if (isReverse) {
            matrix.scale(-1, 1);
            matrix.rotate(-rotation);
        } else {
            matrix.scale(1, 1);
            matrix.rotate(rotation);
        }
        matrix.translate(getX() + rotationX, getY() + rotationY);


        Point2D.Double[] array = new Point2D.Double[4];

        // left top
        array[0] = new Point2D.Double(0, 0);
        matrix.transformPointSet(array[0]);

        // right top
        array[1] = new Point2D.Double(width, 0);
        matrix.transformPointSet(array[1]);

        // left bottom
        array[2] = new Point2D.Double(0, height);
        matrix.transformPointSet(array[2]);

        // right bottom
        array[3] = new Point2D.Double(width, height);
        matrix.transformPointSet(array[3]);

        Point2D.Double leftTop = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
        Point2D.Double rightBottom = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);
        for (Point2D.Double point : array) {
            if (point.x < leftTop.x) {
                leftTop.x = point.x;
            }
            if (point.y < leftTop.y) {
                leftTop.y = point.y;
            }

            if (point.x > rightBottom.x) {
                rightBottom.x = point.x;
            }
            if (point.y > rightBottom.y) {
                rightBottom.y = point.y;
            }
        }
        bounds.set(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
        if (isReverse) {
            super.innerSet(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2, width, height, -rotation);
        } else {
            super.innerSet(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2, width, height, rotation);
        }
    }

    public double getNativeWidth() {
        return width;
    }

    public double getNativeHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return "OBBTransformRectangle{" +
                "width=" + width +
                ", height=" + height +
                ", rotation=" + rotation +
                ", rotationX=" + rotationX +
                ", rotationY=" + rotationY +
                ", isReverse=" + isReverse +
                ", matrix=" + matrix +
                '}' + super.toString();
    }
}
