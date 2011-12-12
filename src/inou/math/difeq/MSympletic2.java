/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Sympletic Integral method. This class
 * solves ddy/dx2 = f(x,y,y') type. (default step interval is 0.1)
 */

public class MSympletic2 implements DifEqMethod {

    private double h;// step interval

    // sympletic integrator coefficients

    // 2nd fractal decomposition
    /*
     * double symp_m = 2; double symp_r = 2; double symp_k [] = { 0.5, 0.5};
     * double symp_u [] = { 0.0, 1.0};
     */

    // 4th fractal decomposition
    final static private double symp_m = 4;

    final static private double symp_r = 6;

    static final private double symp_k[] = { 0.207245385897188,
            0.414490771794376, -0.121736157691564, -0.121736157691564,
            0.414490771794376, 0.207245385897188, };

    static final private double symp_u[] = { 0.000000000000000,
            0.414490771794376, 0.414490771794376, -0.657963087177503,
            0.414490771794376, 0.414490771794376, };

    // ====== constructor

    public MSympletic2() {
        this(0.1);
    }

    public MSympletic2(double h) {
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

    private double f, p, q;

    /** a = (x,y,y') */
    public void step(ScalarFunction df, VariableSet a) {
        p = a.dy;
        q = a.y;
        for (int r = 0; r < symp_r; r++) {
            f = df.f(a);
            p += h * symp_u[r] * f;
            q += h * symp_k[r] * p;
            a.dy = p;
            a.y = q;
        }
        a.x = a.x + h;
    }
}
