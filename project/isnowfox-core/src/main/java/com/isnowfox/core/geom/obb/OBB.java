package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.BoundsObject;

/**
 * Oriented bounding box
 * 方向包围盒
 * <p>
 * 感谢：http://www.cnblogs.com/iamzhanglei/archive/2012/06/07/2539751.html
 *
 * @author zuoge85
 */
public interface OBB extends BoundsObject {
    OBBVector getCenterPoint();

    OBBVector[] getAxes();

    int getCollisionType();

    double getRadius();

    double getProjectionRadius(OBBVector axis);
}
