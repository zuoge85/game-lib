package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.Matrix;

import java.awt.geom.Point2D;

/**
 * 矩形方向包围盒
 *
 * @author zuoge85
 */
public class OBBRectangle extends OOBAbstractRect {
    // public final static create(){
    //
    // }
    private double width;
    private double height;
    private double rotation;
    private final Matrix matrix = new Matrix();

    public OBBRectangle() {

    }

    public OBBRectangle(double centerX, double centerY, double width, double height, double rotation) {
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
        execute();
    }

    public void setCenter(double centerX, double centerY) {
        this.centerPoint.set(centerX, centerY);
        execute();
    }

    public void setCenter(double centerX, double centerY, double rotation) {
        this.centerPoint.set(centerX, centerY);
        this.rotation = rotation;
        execute();
    }

    /**
     * TODO 现在实现了一个可能很差劲的算法计算 选择后的外框矩形
     */
    private void execute() {
        matrix.identity();
        matrix.translate(-extents[0], -extents[1]);
        matrix.rotate(rotation);
        matrix.translate(extents[0] + getX(), extents[1] + this.getY());

        matrix.translate(getX(), getY());

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
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
    }
}
