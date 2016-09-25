package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.Rectangle;


public class OBBCircle implements  OBB{
	private OBBVector centerPoint = new OBBVector();
	private double mRadius = 0;
	
	public OBBCircle() {

	}

	public OBBCircle(OBBVector centerPoint, double mRadius) {
		super();
		this.centerPoint = centerPoint;
		this.mRadius = mRadius;
	}
	
	public void init(double _x, double _y, double _radius){
		this.centerPoint.x = _x;
		this.centerPoint.y = _y;
        this.mRadius = _radius;
    }
	
	public void setCenterPoint(OBBVector centerPoint) {
		this.centerPoint = centerPoint;
	}

	public OBBVector getCenterPoint() {
        return centerPoint;
    }
	
	public double getmRadius() {
		return mRadius;
	}
	public void setmRadius(double mRadius) {
		this.mRadius = mRadius;
	}
	
    @Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public OBBVector[] getAxes() {
		return null;
	}

	@Override
	public int getCollisionType() {
		return OBBUtils.CM_COLLISIONTYPE_CIRCLE;
	}

	@Override
	public double getRadius() {
		return mRadius;
	}

	@Override
	public double getProjectionRadius(OBBVector axis) {
		// TODO Auto-generated method stub
		return 0;
	}


}
