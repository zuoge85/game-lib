package com.isnowfox.core.geom;

import java.util.ArrayList;
import java.util.List;

public class BoundsQuadTree {
    public static final int DEFAULT_OBJECTS_MAX = 10;
    public static final int DEFAULT_MIN_WIDTH = 240;

    private int objectsMax = DEFAULT_OBJECTS_MAX;
    private int minWidth;
    //    private int MAX_LEVELS = 5;
//
//    private int level;
    private List<BoundsObject> objects;
    private List<BoundsObject> childObjects;

    private Rectangle bounds;
    private BoundsQuadTree[] nodes;

    //	public BoundsQuadTree(Rectangle pBounds) {
//		this(0, pBounds ,10);
//	}
//
    public BoundsQuadTree(int width, int height) {
        this(new Rectangle(width, height), DEFAULT_OBJECTS_MAX, DEFAULT_MIN_WIDTH);
    }

    public BoundsQuadTree(int width, int height, int objectsMax) {
        this(new Rectangle(width, height), objectsMax, DEFAULT_MIN_WIDTH);
    }

    public BoundsQuadTree(int width, int height, int objectsMax, int minWidth) {
        this(new Rectangle(width, height), objectsMax, minWidth);
    }

    /*
     * Constructor
     */
    private BoundsQuadTree(Rectangle pBounds, int objectsMax, int minWidth) {
        this.minWidth = minWidth;
        objects = new ArrayList<>();
        childObjects = new ArrayList<>();
        bounds = pBounds;
        nodes = new BoundsQuadTree[4];
        this.objectsMax = objectsMax;
    }

    /*
     * Clears the quadtree
     */
    public void clear() {
        objects.clear();
        childObjects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
            }
        }
    }

    /*
     * Splits the node into 4 subnodes
     */
    private void split() {
        int subWidth = (int) (bounds.getWidth())  >> 1;
        int subHeight = (int) (bounds.getHeight())  >>1;
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new BoundsQuadTree(new Rectangle(x + subWidth, y, subWidth, subHeight), objectsMax, minWidth);
        nodes[1] = new BoundsQuadTree(new Rectangle(x, y, subWidth, subHeight), objectsMax, minWidth);
        nodes[2] = new BoundsQuadTree(new Rectangle(x, y + subHeight, subWidth, subHeight), objectsMax, minWidth);
        nodes[3] = new BoundsQuadTree(new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight), objectsMax, minWidth);
    }

    /*
     * Determine which node the object belongs to. -1 means object cannot
     * completely fit within a child node and is part of the parent node
     */
    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Object can completely fit within the top quadrants
        double pRectY = pRect.getY();
        boolean topQuadrant = (pRectY < horizontalMidpoint && pRectY + pRect.getHeight() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRectY > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        double pRectX = pRect.getX();
        if (pRectX < verticalMidpoint && pRectX + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        else if (pRectX > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    /*
     * Insert the object into the quadtree. If the node exceeds the capacity, it
     * will split and add all objects to their corresponding nodes.
     */
    public void insert(BoundsObject pObj) {
        if (nodes[0] != null) {
            int index = getIndex(pObj.getBounds());
            if (index != -1) {
                childObjects.add(pObj);
                nodes[index].insert(pObj);
                return;
            }
        }

        objects.add(pObj);

        if (objects.size() > objectsMax && bounds.width > minWidth) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i).getBounds());
                if (index != -1) {
                    BoundsObject item = objects.remove(i);
                    childObjects.add(item);
                    nodes[index].insert(item);
                } else {
                    i++;
                }
            }
        }
    }

    /*
     * Return all objects that could collide with the given object
     */
    public void retrieve(List<BoundsObject> returnObjects, Rectangle pRect) {
        if (!objects.isEmpty()) {
            returnObjects.addAll(objects);
        }
        if (nodes[0] == null) {
            return;
        }
        int index = getIndex(pRect);
        if (index != -1) {
            nodes[index].retrieve(returnObjects, pRect);
        } else {
            returnObjects.addAll(childObjects);
        }
    }
}
