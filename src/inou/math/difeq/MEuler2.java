/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Euler method. This class solves ddy/dx2 =
 * f(x,y,y') type. (default step interval is 0.05)
 */

public class MEuler2 implements DifEqMethod {

    protected double h;// step interval

    double p;

    // ====== constructor

    public MEuler2() {
        this(0.05);
    }

    public MEuler2(double h) {
        this.h = h;
    }

    public void reset() {
    }

    // ===== operation

    public double getDiscreteSize() {
        return h;
    }

    public void setDiscreteSize(double h) {
        this.h = h;
    }

    /** a = (x,y,y') */
    public void step(ScalarFunction df, VariableSet a) {
        p = df.f(a) * h;
        a.x = a.x + h;
        a.y = a.y + a.dy * h;
        a.dy = a.dy + p;
    }
}
