/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** general scalar function */
public interface ScalarFunction {

    /** Scalar function. */
    public double f(MathVector arg);

    /** The dimension of the argument. */
    public int getDimension();
}