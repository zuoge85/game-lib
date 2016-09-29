package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.Rectangle;

import java.util.Arrays;

public abstract class OOBAbstractRect implements OBB {
    protected final OBBVector centerPoint = new OBBVector();
    protected final double[] extents = new double[2];
    protected final OBBVector[] axes = new OBBVector[2];
    protected double mRadius = 0;
    protected final Rectangle bounds = new Rectangle();

    public OOBAbstractRect() {

    }

    public OOBAbstractRect(double centerX, double centerY, double width, double height, double rotation) {
        innerSet(centerX, centerY, width, height, rotation);
    }

    protected void innerSet(double centerX, double centerY, double width, double height, double rotation) {
        this.centerPoint.set(centerX, centerY);
        this.extents[0] = width / 2;
        this.extents[1] = height / 2;

        this.axes[0] = new OBBVector(Math.cos(rotation), Math.sin(rotation));
        this.axes[1] = new OBBVector(-1 * Math.sin(rotation), Math.cos(rotation));
        this.mRadius = Math.sqrt(this.extents[0] * this.extents[0] + this.extents[1] * this.extents[1]);
    }

    public final double getHalfWidth() {
        return extents[0];
    }

    public final double getHalfHeight() {
        return extents[1];
    }

    @Override
    public double getProjectionRadius(OBBVector axis) {
        return this.extents[0] * Math.abs(axis.dot(this.axes[0])) + this.extents[1] * Math.abs(axis.dot(this.axes[1]));
    }


    @Override
    public OBBVector getCenterPoint() {
        return centerPoint;
    }

    public double getX() {
        return centerPoint.getX() - this.extents[0];
    }

    public double getY() {
        return centerPoint.getY() - this.extents[1];
    }

    public double getRight() {
        return centerPoint.getX() + this.extents[0];
    }

    public double getBottom() {
        return centerPoint.getY() + this.extents[1];
    }

    @Override
    public OBBVector[] getAxes() {
        return axes;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public int getCollisionType() {
        return OBBUtils.CM_COLLISIONTYPE_RECT;
    }

    @Override
    public double getRadius() {
        return mRadius;
    }

    @Override
    public String toString() {
        return "OOBAbstractRect{" +
                "centerPoint=" + centerPoint +
                ", extents=" + Arrays.toString(extents) +
                ", axes=" + Arrays.toString(axes) +
                ", bounds=" + bounds +
                '}';
    }
}
