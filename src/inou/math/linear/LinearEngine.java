/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.linear;

import inou.math.MathVector;
import inou.math.Matrix;

/**
 * Abstract class of the Linear simultaneous equation solver
 */

public abstract class LinearEngine {

    /** return default solver engine (Decomp) */
    public static LinearEngine getDefaultEngine() {
        return new Decomp();
    }

    // ==================================
    // main routine
    // ==================================

    /**
     * calculate linear system equation. [ A*x = b ]
     * 
     * @param A
     *            coefficient matrix (On exit, maybe the argument be changed)
     * @param b
     *            constant value vector. (On exit, maybe the argument be
     *            changed)
     * @return result x
     */
    public abstract MathVector solves(Matrix A, MathVector b);
}