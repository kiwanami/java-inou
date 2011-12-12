/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Runge-Kutta method. This class is used
 * to solves dy/dx = f(x,y) type equation. (Default step interval is 0.1)
 */

public class MRungeKutta implements DifEqMethod {

    protected double h, hh;// step interval

    // calculation coefficients
    static double s2 = 1. / Math.sqrt(2.);

    static double g1 = -0.5 + s2;

    static double g2 = 1. - s2;

    static double g3 = -s2;

    static double g4 = 1. + s2;

    static double g5 = 2. - s2;

    static double g6 = 2. + s2;

    // temporary value
    VariableSet tp = new VariableSet(0, 0);

    double p0, p1, p2, p3;

    // ====== constructor

    public MRungeKutta() {
        this(0.1);
    }

    public MRungeKutta(double h) {
        setDiscreteSize(h);
    }

    // ===== operation

    public double getDiscreteSize() {
        return h;
    }

    public void setDiscreteSize(double h) {
        this.h = h;
        hh = h / 2.;
    }

    public void reset() {
    }

    final public void step(ScalarFunction df, VariableSet a) {
        tp.substitute(a);
        // 4th order r-k-g
        p0 = df.f(tp) * h;
        tp.set(a.x + hh, a.y + p0 / 2.);

        p1 = df.f(tp) * h;
        tp.y = a.y + p0 * g1 + p1 * g2;

        p2 = df.f(tp) * h;
        tp.set(a.x + h, a.y + p1 * g3 + p2 * g4);

        p3 = df.f(tp) * h;
        a.set(a.x + h, a.y + (p0 + g5 * p1 + g6 * p2 + p3) / 6.);
    }
}