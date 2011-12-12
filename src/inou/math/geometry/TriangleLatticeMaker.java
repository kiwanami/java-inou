/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;
import inou.math.vector.Vector2D;

import java.util.ArrayList;

/** Make a triangle lattice for 2D. */
public class TriangleLatticeMaker implements LatticeMaker {

    private double dx;

    private int num;

    /**
     * make triangle lattice around the origine. When dx = 1 and num = 7, make
     * the lattice as following:
     * 
     * <pre>
     *      O O O O  
     *      O O O O O  
     *      O O O O O O
     *      O O O * O O O
     *      O O O O O O
     *      O O O O O  
     *      O O O O  
     * </pre>
     * 
     * ("*" is the origine.)
     * 
     * @param dx
     *            distance of points
     * @param num
     *            number of the side
     */
    public TriangleLatticeMaker(double dx, int num) {
        this.dx = dx;
        this.num = num;
    }

    /**
     * @return lattice points.
     */
    public MathVector[] getLattice() {
        ArrayList points = new ArrayList();
        Vector2D a = new Vector2D(dx, 0);
        Vector2D b = new Vector2D(dx / 2, dx / 2 * Math.sqrt(3));
        int halfNum = num / 2;
        for (int i = -halfNum; i <= halfNum; i++) {
            for (int j = Math.max(-halfNum, -halfNum - i); j <= Math.min(
                    halfNum, halfNum - i); j++) {
                MathVector aa = a.mult(j);
                MathVector bb = b.mult(i);
                points.add(aa.adds(bb));
            }
        }
        MathVector[] ret = new MathVector[points.size()];
        for (int i = 0; i < points.size(); i++)
            ret[i] = (MathVector) points.get(i);
        return ret;
    }

}
