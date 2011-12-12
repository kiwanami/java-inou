/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.VectorGD;

import java.io.Serializable;

/** gradient operator for ScalarFunction */
public class Gradient implements VectorFunction, Serializable {

    protected ScalarFunction originalFunction;

    protected ScalarFunction[] grads;

    protected double mult = 1;

    /** Construct divergence operator */
    public Gradient(ScalarFunction sf) {
        originalFunction = sf;
        grads = new ScalarFunction[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            grads[i] = FunctionUtil.getDerivedFunction(originalFunction, i);
        }
    }

    /** Construct divergence operator */
    public Gradient(ScalarFunction sf, double mult) {
        this(sf);
        this.mult = mult;
    }

    public MathVector f(MathVector a) {
        MathVector ret = new VectorGD(getDimension());
        for (int i = 0; i < getDimension(); i++)
            ret.v(i, grads[i].f(a) * mult);
        return ret;
    }

    public ScalarFunction getFunction(int c) {
        return grads[c];
    }

    public int getDimension() {
        return originalFunction.getDimension();
    }

    public int getArgDimension() {
        return originalFunction.getDimension();
    }
}