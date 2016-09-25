package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.obb.OBBRectangle;
import com.isnowfox.core.junit.BaseTest;

public class OBBTest extends BaseTest {
	public void test(){
		OBBRectangle rect0 = new OBBRectangle();
		rect0.setByLeftTop(0, 0, 10, 10, 0);
		
		OBBRectangle rect1 = new OBBRectangle();
		rect1.setByLeftTop(11, 11, 10, 10, 0);
		
		System.out.println(OBBUtils.detector(rect0, rect1));
		
		
		rect1.setByLeftTop(9, 9, 10, 10, Math.PI/4);
		System.out.println(OBBUtils.detector(rect0, rect1));
	}
}
