/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

public class VectorParameter implements VectorFunction {

    private int argDim;

    private MathVector param;

    public VectorParameter(int argDim, MathVector v) {
        this.argDim = argDim;
        param = v;
    }

    public MathVector f(MathVector arg) {
        return param;
    }

    public int getDimension() {
        return param.getDimension();
    }

    public int getArgDimension() {
        return argDim;
    }
}