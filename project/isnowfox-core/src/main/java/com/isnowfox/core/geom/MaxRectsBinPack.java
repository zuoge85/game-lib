package com.isnowfox.core.geom;

import java.util.ArrayList;

/**
 * @author zuoge85 on 14-4-1.
 */
public class MaxRectsBinPack {
    public int binWidth = 0;
    public int binHeight = 0;
    public boolean allowRotations = false;

    public ArrayList<IntRectangle> usedRectangles = new ArrayList<>();
    public ArrayList<IntRectangle> freeRectangles = new ArrayList<>();

    private int score1 = 0; // Unused in this function. We don't need to know the score after finding the position.
    private int score2 = 0;
    private int bestShortSideFit;
    private int bestLongSideFit;

    public MaxRectsBinPack(int width, int height) {
        this(width, height, true);
    }

    public MaxRectsBinPack(int width, int height, boolean rotations) {
        init(width, height, rotations);
    }

    private void init(int width, int height) {
        init(width, height, true);
    }

    private void init(int width, int height, boolean rotations) {
        if (count(width) % 1 != 0 || count(height) % 1 != 0) {
            throw new Error("Must be 2,4,8,16,32,...512,1024,...");
        }
        binWidth = width;
        binHeight = height;
        allowRotations = rotations;

        IntRectangle n = new IntRectangle(0, 0, width, height);

        usedRectangles.clear();

        freeRectangles.clear();
        freeRectangles.add(n);
    }

    private double count(double n) {
        if (n >= 2) {
            return count(n / 2);
        }
        return n;
    }

    /**
     * Insert a new IntRectangle
     *
     * @param width
     * @param height
     * @param method
     * @return
     */
    public IntRectangle insert(int width, int height, int method) {
        IntRectangle newNode = new IntRectangle();
        score1 = 0;
        score2 = 0;
        switch (method) {
            case FreeRectangleChoiceHeuristic.BestShortSideFit:
                newNode = findPositionForNewNodeBestShortSideFit(width, height);
                break;
            case FreeRectangleChoiceHeuristic.BottomLeftRule:
                newNode = findPositionForNewNodeBottomLeft(width, height, score1, score2);
                break;
            case FreeRectangleChoiceHeuristic.ContactPointRule:
                newNode = findPositionForNewNodeContactPo(width, height, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestLongSideFit:
                newNode = findPositionForNewNodeBestLongSideFit(width, height, score2, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestAreaFit:
                newNode = findPositionForNewNodeBestAreaFit(width, height, score1, score2);
                break;
        }

        if (newNode.getHeight() == 0) {
            return newNode;
        }

        placeRectangle(newNode);
//        trace(newNode);
        return newNode;
    }

    private void insert2(ArrayList<IntRectangle> Rectangles, ArrayList<IntRectangle> dst, int method) {
        dst.clear();

        while (Rectangles.size() > 0) {
            int bestScore1 = Integer.MAX_VALUE;
            int bestScore2 = Integer.MAX_VALUE;
            int bestRectangleIndex = -1;
            IntRectangle bestNode = new IntRectangle();

            for (int i = 0; i < Rectangles.size(); ++i) {
                int score1 = 0;
                int score2 = 0;
                IntRectangle rect = Rectangles.get(i);
                IntRectangle newNode = scoreRectangle(rect.getWidth(), rect.getHeight(), method, score1, score2);

                if (score1 < bestScore1 || (score1 == bestScore1 && score2 < bestScore2)) {
                    bestScore1 = score1;
                    bestScore2 = score2;
                    bestNode = newNode;
                    bestRectangleIndex = i;
                }
            }

            if (bestRectangleIndex == -1) {
                return;
            }

            placeRectangle(bestNode);
            Rectangles.remove(bestRectangleIndex);
        }
    }

    private void placeRectangle(IntRectangle node) {
        int numRectanglesToProcess = freeRectangles.size();
        for (int i = 0; i < numRectanglesToProcess; i++) {
            if (splitFreeNode(freeRectangles.get(i), node)) {
                freeRectangles.remove(i);
                --i;
                --numRectanglesToProcess;
            }
        }

        pruneFreeList();

        usedRectangles.add(node);
    }

    private IntRectangle scoreRectangle(int width, int height, int method, int score1, int score2) {
        IntRectangle newNode = new IntRectangle();
        score1 = Integer.MAX_VALUE;
        score2 = Integer.MAX_VALUE;
        switch (method) {
            case FreeRectangleChoiceHeuristic.BestShortSideFit:
                newNode = findPositionForNewNodeBestShortSideFit(width, height);
                break;
            case FreeRectangleChoiceHeuristic.BottomLeftRule:
                newNode = findPositionForNewNodeBottomLeft(width, height, score1, score2);
                break;
            case FreeRectangleChoiceHeuristic.ContactPointRule:
                newNode = findPositionForNewNodeContactPo(width, height, score1);
                // todo: reverse
                score1 = -score1; // Reverse since we are minimizing, but for contact point score bigger is better.
                break;
            case FreeRectangleChoiceHeuristic.BestLongSideFit:
                newNode = findPositionForNewNodeBestLongSideFit(width, height, score2, score1);
                break;
            case FreeRectangleChoiceHeuristic.BestAreaFit:
                newNode = findPositionForNewNodeBestAreaFit(width, height, score1, score2);
                break;
        }

        // Cannot fit the current IntRectangle.
        if (newNode.getHeight() == 0) {
            score1 = Integer.MAX_VALUE;
            score2 = Integer.MAX_VALUE;
        }

        return newNode;
    }

    /// Computes the ratio of used surface area.
    private double occupancy() {
        double usedSurfaceArea = 0;
        for (int i = 0; i < usedRectangles.size(); i++) {
            IntRectangle rect = usedRectangles.get(i);
            usedSurfaceArea += rect.getWidth() * rect.getHeight();
        }

        return usedSurfaceArea / (binWidth * binHeight);
    }

    private IntRectangle findPositionForNewNodeBottomLeft(int width, int height, int bestY, int bestX) {
        IntRectangle bestNode = new IntRectangle();
        //memset(bestNode, 0, sizeof(IntRectangle));

        bestY = Integer.MAX_VALUE;
        IntRectangle rect;
        int topSideY;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the IntRectangle in upright (non-flipped) orientation.
            if (rect.getWidth() >= width && rect.getHeight() >= height) {
                topSideY = rect.getY() + height;
                if (topSideY < bestY || (topSideY == bestY && rect.getX() < bestX)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestY = topSideY;
                    bestX = rect.getX();
                }
            }
            if (allowRotations && rect.getWidth() >= height && rect.getHeight() >= width) {
                topSideY = rect.getY() + width;
                if (topSideY < bestY || (topSideY == bestY && rect.getX() < bestX)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestY = topSideY;
                    bestX = rect.getX();
                }
            }
        }
        return bestNode;
    }

    private IntRectangle findPositionForNewNodeBestShortSideFit(int width, int height) {
        IntRectangle bestNode = new IntRectangle();
        //memset(&bestNode, 0, sizeof(IntRectangle));

        bestShortSideFit = Integer.MAX_VALUE;
        bestLongSideFit = score2;
        IntRectangle rect;
        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int longSideFit;

        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the IntRectangle in upright (non-flipped) orientation.
            if (rect.getWidth() >= width && rect.getHeight() >= height) {
                leftoverHoriz = Math.abs(rect.getWidth() - width);
                leftoverVert = Math.abs(rect.getHeight() - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (shortSideFit < bestShortSideFit || (shortSideFit == bestShortSideFit && longSideFit < bestLongSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }

            if (allowRotations && rect.getWidth() >= height && rect.getHeight() >= width) {
                double flippedLeftoverHoriz = Math.abs(rect.getWidth() - height);
                double flippedLeftoverVert = Math.abs(rect.getHeight() - width);
                double flippedShortSideFit = Math.min(flippedLeftoverHoriz, flippedLeftoverVert);
                double flippedLongSideFit = Math.max(flippedLeftoverHoriz, flippedLeftoverVert);

                if (flippedShortSideFit < bestShortSideFit || (flippedShortSideFit == bestShortSideFit && flippedLongSideFit < bestLongSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = (int) flippedShortSideFit;
                    bestLongSideFit = (int) flippedLongSideFit;
                }
            }
        }

        return bestNode;
    }

    private IntRectangle findPositionForNewNodeBestLongSideFit(int width, int height, int bestShortSideFit, int bestLongSideFit) {
        IntRectangle bestNode = new IntRectangle();
        //memset(&bestNode, 0, sizeof(IntRectangle));
        bestLongSideFit = Integer.MAX_VALUE;
        IntRectangle rect;

        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int longSideFit;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the IntRectangle in upright (non-flipped) orientation.
            if (rect.getWidth() >= width && rect.getHeight() >= height) {
                leftoverHoriz = Math.abs(rect.getWidth() - width);
                leftoverVert = Math.abs(rect.getHeight() - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (longSideFit < bestLongSideFit || (longSideFit == bestLongSideFit && shortSideFit < bestShortSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }

            if (allowRotations && rect.getWidth() >= height && rect.getHeight() >= width) {
                leftoverHoriz = Math.abs(rect.getWidth() - height);
                leftoverVert = Math.abs(rect.getHeight() - width);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                longSideFit = Math.max(leftoverHoriz, leftoverVert);

                if (longSideFit < bestLongSideFit || (longSideFit == bestLongSideFit && shortSideFit < bestShortSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = shortSideFit;
                    bestLongSideFit = longSideFit;
                }
            }
        }
//        trace(bestNode);
        return bestNode;
    }

    private IntRectangle findPositionForNewNodeBestAreaFit(int width, int height, int bestAreaFit, int bestShortSideFit) {
        IntRectangle bestNode = new IntRectangle();
        //memset(&bestNode, 0, sizeof(IntRectangle));

        bestAreaFit = Integer.MAX_VALUE;

        IntRectangle rect;

        int leftoverHoriz;
        int leftoverVert;
        int shortSideFit;
        int areaFit;

        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            areaFit = rect.getWidth() * rect.getHeight() - width * height;

            // Try to place the IntRectangle in upright (non-flipped) orientation.
            if (rect.getWidth() >= width && rect.getHeight() >= height) {
                leftoverHoriz = Math.abs(rect.getWidth() - width);
                leftoverVert = Math.abs(rect.getHeight() - height);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);

                if (areaFit < bestAreaFit || (areaFit == bestAreaFit && shortSideFit < bestShortSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = shortSideFit;
                    bestAreaFit = areaFit;
                }
            }

            if (allowRotations && rect.getWidth() >= height && rect.getHeight() >= width) {
                leftoverHoriz = Math.abs(rect.getWidth() - height);
                leftoverVert = Math.abs(rect.getHeight() - width);
                shortSideFit = Math.min(leftoverHoriz, leftoverVert);

                if (areaFit < bestAreaFit || (areaFit == bestAreaFit && shortSideFit < bestShortSideFit)) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestShortSideFit = shortSideFit;
                    bestAreaFit = areaFit;
                }
            }
        }
        return bestNode;
    }

    /// Returns 0 if the two intervals i1 and i2 are disjoint, or the length of their overlap otherwise.
    private int commonIntervalLength(int i1start, int i1end, int i2start, int i2end) {
        if (i1end < i2start || i2end < i1start) {
            return 0;
        }
        return Math.min(i1end, i2end) - Math.max(i1start, i2start);
    }

    private int contactPointScoreNode(int x, int y, int width, int height) {
        int score = 0;

        if (x == 0 || x + width == binWidth) {
            score += height;
        }
        if (y == 0 || y + height == binHeight) {
            score += width;
        }
        IntRectangle rect;
        for (int i = 0; i < usedRectangles.size(); i++) {
            rect = usedRectangles.get(i);
            if (rect.x == x + width || rect.x + rect.getWidth() == x) {
                score += commonIntervalLength(rect.y, rect.y + rect.getHeight(), y, y + height);
            }
            if (rect.y == y + height || rect.y + rect.getHeight() == y) {
                score += commonIntervalLength(rect.x, rect.x + rect.getWidth(), x, x + width);
            }
        }
        return score;
    }

    private IntRectangle findPositionForNewNodeContactPo(int width, int height, int bestContactScore) {
        IntRectangle bestNode = new IntRectangle();
        //memset(&bestNode, 0, sizeof(IntRectangle));

        bestContactScore = -1;

        IntRectangle rect;
        int score;
        for (int i = 0; i < freeRectangles.size(); i++) {
            rect = freeRectangles.get(i);
            // Try to place the IntRectangle in upright (non-flipped) orientation.
            if (rect.getWidth() >= width && rect.getHeight() >= height) {
                score = contactPointScoreNode(rect.x, rect.y, width, height);
                if (score > bestContactScore) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestContactScore = score;
                }
            }
            if (allowRotations && rect.getWidth() >= height && rect.getHeight() >= width) {
                score = contactPointScoreNode(rect.x, rect.y, height, width);
                if (score > bestContactScore) {
                    bestNode.set(rect.getX(), rect.getY(), width, height);
                    bestContactScore = score;
                }
            }
        }
        return bestNode;
    }

    private boolean splitFreeNode(IntRectangle freeNode, IntRectangle usedNode) {
        // Test with SAT if the Rectangles even intersect.
        if (usedNode.x >= freeNode.x + freeNode.getWidth() || usedNode.x + usedNode.getWidth() <= freeNode.x ||
                usedNode.y >= freeNode.y + freeNode.getHeight() || usedNode.y + usedNode.getHeight() <= freeNode.y) {
            return false;
        }
        IntRectangle newNode;
        if (usedNode.x < freeNode.x + freeNode.getWidth() && usedNode.x + usedNode.getWidth() > freeNode.x) {
            // New node at the top side of the used node.
            if (usedNode.y > freeNode.y && usedNode.y < freeNode.y + freeNode.getHeight()) {
                newNode = freeNode.clone();
                newNode.setHeight(usedNode.y - newNode.y);
                freeRectangles.add(newNode);
            }

            // New node at the bottom side of the used node.
            if (usedNode.y + usedNode.getHeight() < freeNode.y + freeNode.getHeight()) {
                newNode = freeNode.clone();
                newNode.y = usedNode.y + usedNode.getHeight();
                newNode.setHeight(freeNode.y + freeNode.getHeight() - (usedNode.y + usedNode.getHeight()));
                freeRectangles.add(newNode);
            }
        }

        if (usedNode.y < freeNode.y + freeNode.getHeight() && usedNode.y + usedNode.getHeight() > freeNode.y) {
            // New node at the left side of the used node.
            if (usedNode.x > freeNode.x && usedNode.x < freeNode.x + freeNode.getWidth()) {
                newNode = freeNode.clone();
                newNode.setWidth(usedNode.x - newNode.x);
                freeRectangles.add(newNode);
            }

            // New node at the right side of the used node.
            if (usedNode.x + usedNode.getWidth() < freeNode.x + freeNode.getWidth()) {
                newNode = freeNode.clone();
                newNode.x = usedNode.x + usedNode.getWidth();
                newNode.setWidth(freeNode.x + freeNode.getWidth() - (usedNode.x + usedNode.getWidth()));
                freeRectangles.add(newNode);
            }
        }

        return true;
    }

    private void pruneFreeList() {
        for (int i = 0; i < freeRectangles.size(); i++) {
            for (int j = i + 1; j < freeRectangles.size(); j++) {
                if (isContainedIn(freeRectangles.get(i), freeRectangles.get(j))) {
                    freeRectangles.remove(i);
                    break;
                }
                if (isContainedIn(freeRectangles.get(j), freeRectangles.get(i))) {
                    freeRectangles.remove(j);
                }
            }
        }
    }

    private boolean isContainedIn(IntRectangle a, IntRectangle b) {
        return a.x >= b.x && a.y >= b.y
                && a.x + a.getWidth() <= b.x + b.getWidth()
                && a.y + a.getHeight() <= b.y + b.getHeight();
    }


    public class FreeRectangleChoiceHeuristic {
        public static final int BestShortSideFit = 0; ///< -BSSF: Positions the IntRectangle against the short side of a free IntRectangle into which it fits the best.
        public static final int BestLongSideFit = 1; ///< -BLSF: Positions the IntRectangle against the long side of a free IntRectangle into which it fits the best.
        public static final int BestAreaFit = 2; ///< -BAF: Positions the IntRectangle into the smallest free IntRectangle into which it fits.
        public static final int BottomLeftRule = 3; ///< -BL: Does the Tetris placement.
        public static final int ContactPointRule = 4; ///< -CP: Choosest the placement where the IntRectangle touches other (much)Rectangles as possible.
    }
}
