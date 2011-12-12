/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;

/** Make a lattice consisting of some points. */
public interface LatticeMaker {

    /**
     * @return lattice points.
     */
    public MathVector[] getLattice();

}