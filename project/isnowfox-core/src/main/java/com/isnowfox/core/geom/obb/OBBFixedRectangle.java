package com.isnowfox.core.geom.obb;

public class OBBFixedRectangle extends OOBAbstractRect {
	public OBBFixedRectangle() {

	}

	public OBBFixedRectangle(double centerX, double centerY, double width, double height) {
		super(centerX, centerY, width, height, 0);
	}

	public void setByLeftTop(double leftTopX, double leftTopY, double width, double height) {
		innerSet(leftTopX + width / 2, leftTopY + height / 2, width, height, 0);
	}

	public void set(double centerX, double centerY, double width, double height) {
		innerSet(centerX, centerY, width, height, 0);
	}

	@Override
	protected void innerSet(double centerX, double centerY, double width, double height, double rotation) {
		super.innerSet(centerX, centerY, width, height, rotation);
		bounds.set(centerX - this.extents[0], centerY - this.extents[1], width, height);
	}

	public void setCenter(double centerX, double centerY) {
		this.centerPoint.set(centerX, centerY);
		bounds.setLocation(centerX - this.extents[0], centerY - this.extents[1]);
	}

	public void setLeftTop(double x, double y) {
		setCenter(x + this.extents[0], y + this.extents[1]);
		bounds.setLocation(x, y);
	}
	
	public void setX(double x) {
		this.centerPoint.setX(x + this.extents[0]);
		bounds.setX(x);
	}

	public void setY(double y) {
		this.centerPoint.setY(y + this.extents[1]);
		bounds.setY(y);
	}
	
	public final double getX() {
		return bounds.getX();
	}
	
	public final double getY() {
		return bounds.getY();
	}
	
	public final double getWidth() {
		return bounds.getWidth();
	}

	public final double getHeight() {
		return bounds.getHeight();
	}
}
