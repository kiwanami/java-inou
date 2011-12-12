/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.dif.SimpleDifferentiator;

/** Numerically Partial Derived Function */

public class DerivedFunction extends ScalarFunctionClass {

    private ScalarFunction function;

    private Differentiator dif = new SimpleDifferentiator();

    private int colm = 0;

    public DerivedFunction(ScalarFunction f, int colm) {
        super(f.getDimension());
        function = f;
        this.colm = colm;
    }

    public String toString() {
        return "(d/dx_" + colm + ")(" + function.toString() + ")";
    }

    public Differentiator getMethod() {
        return dif;
    }

    public void setMethod(Differentiator d) {
        dif = d;
    }

    public double f(MathVector x) {
        return dif.point(x, colm).operate(function);
    }

    public ScalarFunction getIntegratedFunction(int c) {
        if (c == colm)
            return function;
        return FunctionUtil.getIntegratedFunction(this, c);
    }

}