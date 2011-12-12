/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.dif.SimpleDifferentiator;

import java.io.Serializable;

/** Divergence operator for VectorFunction */
public class Divergence implements ScalarFunction, Serializable {

    protected VectorFunction orginalFunction;

    protected ScalarFunction[] elements;

    protected Differentiator diff = new SimpleDifferentiator();

    /** Construct divergence operator */
    public Divergence(VectorFunction vf) {
        orginalFunction = vf;
        int dim = vf.getDimension();
        elements = new ScalarFunction[dim];
        for (int i = 0; i < dim; i++)
            elements[i] = VectorFunctionClass.makeScalarFunction(vf, i);
    }

    public double f(MathVector a) {
        double ret = 0;
        for (int i = 0; i < getDimension(); i++)
            ret += diff.point(a, i).operate(elements[i]);
        return ret;
    }

    public int getDimension() {
        return orginalFunction.getArgDimension();
    }
}
