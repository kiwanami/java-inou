/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.dif.SimpleDifferentiator;

import java.io.Serializable;

/** General Derived Function */
public class DifferentialFunction implements ScalarFunction, Serializable {

    private ScalarFunction function;

    private int colm;

    private Differentiator diff = new SimpleDifferentiator();

    public DifferentialFunction(ScalarFunction sf, int c) {
        function = sf;
        colm = c;
    }

    public double f(MathVector arg) {
        return diff.point(arg, colm).operate(function);
    }

    public int getDimension() {
        return function.getDimension();
    }
}