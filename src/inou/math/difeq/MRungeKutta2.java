/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Runge-Kutta-Nistryeme method. This
 * class solves ddy/dx2 = f(x,y,y') type differential Equation. (default step
 * interval is 0.1)
 */

public class MRungeKutta2 implements DifEqMethod {

    protected double h, hh;// step interval

    double p0, p1, p2, p3;

    VariableSet tp = new VariableSet(0, 0, 0);

    // ====== constructor

    public MRungeKutta2() {
        this(0.1);
    }

    public MRungeKutta2(double h) {
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

    /** a = (x,y,y') */
    final public void step(ScalarFunction df, VariableSet a) {
        tp.substitute(a);
        // 4th order r-k-n
        p0 = df.f(tp) * h;
        tp.set(a.x + hh, a.y + (a.dy + p0 / 4.) * hh, a.dy + p0 / 2.);
        p1 = df.f(tp) * h;
        tp.set(a.x + hh, a.y + (a.dy + p1 / 4.) * hh, a.dy + p1 / 2.);
        p2 = df.f(tp) * h;
        tp.set(a.x + h, a.y + (a.dy + p2 / 2.) * h, a.dy + p2);
        p3 = df.f(tp) * h;
        tp.set(a.x + h, a.y + (a.dy + (p0 + p1 + p2) / 6) * h, a.dy
                + (p0 + 2 * p1 + 2 * p2 + p3) / 6.);
        a.substitute(tp);
    }
}