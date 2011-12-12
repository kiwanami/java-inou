/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** 1D Scalar function */
public abstract class AFunction implements ScalarFunction {

    public final double f(MathVector arg) {
        return f(arg.v(0));
    }

    public final int getDimension() {
        return 1;
    }

    public abstract double f(double x);

}