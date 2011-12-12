/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;
import inou.math.vector.VectorGD;

import java.util.ArrayList;

/** Make a simple lattice. */
public class SimpleLatticeMaker implements LatticeMaker {

    private int d;

    private double dx;

    private int num;

    /**
     * make simple lattice (square or cubic) around the origine.
     * 
     * @param dim
     *            dimension of lattice
     * @param dx
     *            distance of points
     * @param num
     *            number of the side
     */
    public SimpleLatticeMaker(int dim, double dx, int num) {
        this.d = dim;
        this.dx = dx;
        this.num = num;
    }

    /**
     * @return lattice points.
     */
    public MathVector[] getLattice() {
        ArrayList points = new ArrayList();
        VectorGD pos = new VectorGD(d);
        pos.setAll(-dx * num / 2);
        subLattice(d, points, pos);
        MathVector[] ret = new MathVector[points.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (MathVector) points.get(i);
        }
        return ret;
    }

    private void subLattice(int dim, ArrayList points, MathVector pos) {
        int cdim = dim - 1;
        for (int i = 0; i < num; i++) {
            pos.v(cdim, -dx * ((num - 1) / 2 - i));
            if (cdim != 0) {
                subLattice(cdim, points, pos);
            } else {
                points.add(pos.getCopy());
            }
        }
    }
}
