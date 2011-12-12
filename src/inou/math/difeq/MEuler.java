/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Euler method. This class solves dy/dx2 =
 * f(x,y) type. (default discrete size is 0.05)
 */

public class MEuler implements DifEqMethod {

    protected double h;// discrete size

    protected ScalarFunction df;

    // ====== constructor

    public MEuler() {
        this(0.05);
    }

    public MEuler(double h) {
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

    public void step(ScalarFunction df, VariableSet a) {
        a.y = a.y + df.f(a) * h;
        a.x = a.x + h;
    }
}