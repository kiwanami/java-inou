/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.integral;

import inou.math.AFunction;
import inou.math.AIntegrator;
import inou.math.AOperator;
import inou.math.Integrator;
import inou.math.MathVector;
import inou.math.Operator;
import inou.math.ScalarFunction;
import inou.math.vector.Vector1D;

import java.io.Serializable;

/** Gauss integral algorithm */
public abstract class GaussIntegrator implements AIntegrator, Integrator,
        Serializable {

    protected MathVector startPosition;

    protected double length;

    protected int column;

    private MathVector temp;

    protected double[] g;

    protected double[] w;

    // ========================
    // constructor
    // ========================

    protected GaussIntegrator(double[] g, double[] w) {
        this.g = g;
        this.w = w;
    }

    // ========================
    // operate
    // ========================

    public Operator range(MathVector startPosition, double length, int column) {
        this.startPosition = startPosition.getCopy();
        temp = startPosition.getCopy();
        this.length = length;
        this.column = column;
        return this;
    }

    public AOperator range(double sx, double ex) {
        if (startPosition == null || startPosition.getDimension() != 1) {
            startPosition = new Vector1D(sx);
        } else {
            startPosition.v(0, sx);
        }
        if (temp == null || temp.getDimension() != 1) {
            temp = startPosition.getCopy();
        }
        column = 0;
        length = ex - sx;
        return this;
    }

    public double operate(AFunction a) {
        return operate((ScalarFunction) a);
    }

    public double operate(ScalarFunction a) {
        temp.substitute(startPosition);

        if (length == 0) {
            return 0.;
        }

        double s = 0;
        for (int i = 0; i < g.length; i++) {
            s += w[i] * (a.f(normal(g[i])) + a.f(normal(-g[i])));
        }
        s *= length / 2.;

        return s;
    }

    /** normalize given range */
    private MathVector normal(double x) {
        temp.v(column,
                (length * x + startPosition.v(column) * 2. + length) / 2.);
        return temp;
    }

}