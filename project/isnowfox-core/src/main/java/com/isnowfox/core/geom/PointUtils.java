package com.isnowfox.core.geom;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

public final class PointUtils {
    public static final double len(Point2D p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
    }

    public static final void normalize(Point2D p, double len) {
        double invD = len(p);
        if (invD > 0) {
            invD = len / invD;
            p.setLocation(p.getX() * invD, p.getY() * invD);
        }
    }

    public static final Point2D.Double polarDouble(double len, double angle) {
        return new Point2D.Double(len * Math.cos(angle), len * Math.sin(angle));
    }

    public static final void polarDouble(Point2D p, double len, double angle) {
        p.setLocation(len * Math.cos(angle), len * Math.sin(angle));
    }

    public static int[] toIntArray(List<Point> path) {
        if (path == null) {
            return null;
        }
        int[] intArray = new int[path.size() * 2];
        if (path instanceof RandomAccess) {
            for (int i = 0, j = 0; i < path.size(); i++) {
                Point p = path.get(i);
                intArray[j++] = p.x;
                intArray[j++] = p.y;
            }
        } else {
            int j = 0;
            for (Iterator<Point> iterator = path.iterator(); iterator.hasNext(); ) {
                Point p = iterator.next();
                intArray[j++] = p.x;
                intArray[j++] = p.y;
            }
        }
        return intArray;
    }

    public static LinkedList<Point> pathArrayToList(int[] path) {
        if (path == null) {
            return null;
        }
        int length = path.length;
        if (length % 2 != 0) {
            throw new RuntimeException("错误的长度，长度必须是2的整数倍");
        }
        LinkedList<Point> list = new LinkedList<>();
        for (int j = 0; j < length; ) {
            Point p = new Point();
            p.x = path[j++];
            p.y = path[j++];
            list.add(p);
        }
        return list;
    }
}
