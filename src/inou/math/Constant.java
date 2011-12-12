/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** constant function */
public class Constant extends ScalarFunctionClass implements CalculationOrder {

    private double a;

    public Constant(double a, int d) {
        super(d);
        this.a = a;
    }

    public double f(MathVector x) {
        return a;
    }

    public void setValue(double d) {
        a = d;
    }

    public int getLevel() {
        return TERMINAL;
    }

    public ScalarFunction getDerivedFunction(int c) {
        return new Zero(getDimension());
    }

    public String toString() {
        return Double.toString(a);
    }
}