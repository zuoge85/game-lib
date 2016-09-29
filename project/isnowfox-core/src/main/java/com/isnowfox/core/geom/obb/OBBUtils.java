package com.isnowfox.core.geom.obb;

import java.awt.geom.Point2D;

public final class OBBUtils {
    public static final int CM_COLLISIONTYPE_RECT = 1;
    public static final int CM_COLLISIONTYPE_CIRCLE = 2;

    public static boolean detector(OBB obb0, OBB obb1) {
        if (obb0.getCollisionType() == obb1.getCollisionType()) {
            switch (obb0.getCollisionType()) {
                case CM_COLLISIONTYPE_CIRCLE:
                    return checkCollidesCircleAndCircle((OBBCircle) obb0, (OBBCircle) obb1);
                case CM_COLLISIONTYPE_RECT:
                    return detectorRect(obb0, obb1);
            }
        } else {
            switch (obb0.getCollisionType()) {
                case CM_COLLISIONTYPE_CIRCLE:
                    return detectorCircleAndRect((OBBCircle) obb0, obb1);
                case CM_COLLISIONTYPE_RECT:
                    return detectorCircleAndRect((OBBCircle) obb1, obb0);
            }
        }
        return false;
    }

    private static boolean checkCollidesCircleAndCircle(OBBCircle _circle1, OBBCircle _circle2) {
        OBBVector nv = _circle1.getCenterPoint().sub(_circle2.getCenterPoint());
        if (nv.getLenth() < _circle1.getRadius() + _circle2.getRadius()) {
            return true;
        }
        return false;
    }

    private static boolean detectorRect(OBB obb0, OBB obb1) {
        OBBVector nv = obb0.getCenterPoint().sub(obb1.getCenterPoint());

        if (nv.getLenth() > obb0.getRadius() + obb1.getRadius()) {
            return false;
        }
        for (OBBVector a : obb0.getAxes()) {
            if (obb0.getProjectionRadius(a) + obb1.getProjectionRadius(a) <= Math.abs(nv.dot(a))) {
                return false;
            }
        }
        for (OBBVector a : obb1.getAxes()) {
            if (obb0.getProjectionRadius(a) + obb1.getProjectionRadius(a) <= Math.abs(nv.dot(a))) {
                return false;
            }
        }
        return true;
    }

    //人物的一般都是不旋转的，为了节省，直接计算吧
    public static boolean detectorCircleAndRect(OBBCircle obbCircle, OBB obbRect) {
        Point2D.Double mULPoint = new Point2D.Double();
        Point2D.Double mURPoint = new Point2D.Double();
        Point2D.Double mDLPoint = new Point2D.Double();
        Point2D.Double mDRPoint = new Point2D.Double();
        OBBVector nv = obbRect.getCenterPoint().sub(obbCircle.getCenterPoint());

        if (nv.getLenth() > obbCircle.getRadius() + obbRect.getRadius()) {
            return false;
        }

        OBBVector tObbVector;
        OBBVector rectCenter = obbRect.getCenterPoint();
        double halfRectW = obbRect.getBounds().getWidth() / 2;
        double halfRectH = obbRect.getBounds().getHeight() / 2;
        mULPoint.x = rectCenter.x - halfRectW;
        mULPoint.y = rectCenter.y - halfRectH;

        mURPoint.x = rectCenter.x + halfRectW;
        mURPoint.y = rectCenter.y - halfRectH;

        mDLPoint.x = rectCenter.x - halfRectW;
        mDLPoint.y = rectCenter.y + halfRectH;

        mDRPoint.x = rectCenter.x + halfRectW;
        mDRPoint.y = rectCenter.y + halfRectH;

        if (checkDistance(obbCircle.getCenterPoint().x, obbCircle.getCenterPoint().y, mULPoint.x, mULPoint.y, obbCircle.getmRadius())) {
            return true;
        } else if (checkDistance(obbCircle.getCenterPoint().x, obbCircle.getCenterPoint().y, mURPoint.x, mURPoint.y, obbCircle.getmRadius())) {
            return true;
        } else if (checkDistance(obbCircle.getCenterPoint().x, obbCircle.getCenterPoint().y, mDLPoint.x, mDLPoint.y, obbCircle.getmRadius())) {
            return true;
        } else if (checkDistance(obbCircle.getCenterPoint().x, obbCircle.getCenterPoint().y, mDRPoint.x, mDRPoint.y, obbCircle.getmRadius())) {
            return true;
        }

        if (obbCircle.getCenterPoint().x > mULPoint.x && obbCircle.getCenterPoint().x < mURPoint.x) {
            tObbVector = obbRect.getAxes()[1];
            //if (_rect.getProjectionRadius(tObbVector) + _circle.mRadius > Math.abs(nv.dot(tObbVector)))
            if (halfRectH + obbCircle.getmRadius() > Math.abs(nv.dot(tObbVector))) {
                return true;
            }
        }
        if (obbCircle.getCenterPoint().y > mULPoint.y && obbCircle.getCenterPoint().y < mDLPoint.y) {
            tObbVector = obbRect.getAxes()[0];
            //if (_rect.getProjectionRadius(tObbVector) + _circle.mRadius > Math.abs(nv.dot(tObbVector)))
            if (halfRectW + obbCircle.getmRadius() > Math.abs(nv.dot(tObbVector))) {
                return true;
            }
        }
        return false;
    }

    public static Boolean checkDistance(double _x1, double _y1, double _x2, double _y2, double _maxDis) {
        double tX = _x2 - _x1;
        double tY = _y2 - _y1;
        if ((tX * tX + tY * tY) < (_maxDis * _maxDis)) {
            return true;
        }
        return false;
    }
}
