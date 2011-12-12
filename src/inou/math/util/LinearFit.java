/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.util;

import inou.math.AFunction;

/**
 * least-square fitting with [f(x) = a*x + b] type linear function.
 */
public class LinearFit {

    double a, b;// best fit value

    double da, db;// probable uncertainties

    /**
     * linear least-square fitting
     * 
     * @param sx
     *            array of sample x
     * @param sy
     *            array of sample y
     * @param ey
     *            error array of sample y
     */
    public LinearFit(double[] sx, double[] sy, double[] ey) {
        fit(sx, sy, ey);
    }

    /**
     * linear least-square fitting
     * 
     * @param sx
     *            sample x values
     * @param sy
     *            sample y values
     */
    public LinearFit(double[] sx, double[] sy) {
        double[] ey = new double[sx.length];
        for (int i = 0; i < ey.length; i++)
            ey[i] = 1;
        fit(sx, sy, ey);
    }

    void fit(double[] sx, double[] sy, double[] ey) {
        double p1 = 0, p2 = 0, q1 = 0, q2 = 0, r1 = 0, r2 = 0;
        for (int i = 0; i < sx.length; i++) {
            double x = sx[i];
            double y = sy[i];
            double ey2 = ey[i];
            ey2 *= ey2;
            p1 += x * x / ey2;
            p2 += x / ey2;
            r1 += y * x / ey2;
            r2 += y / ey2;
            q2 += 1. / ey2;
        }
        q1 = p2;
        double[] ret = new double[2];
        double delta = p1 * q2 - q1 * p2;
        a = (q2 * r1 - q1 * r2) / delta;
        b = (-p2 * r1 + p1 * r2) / delta;
        db = p1 / delta;
        da = q2 / delta;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getDa() {
        return da;
    }

    public double getDb() {
        return db;
    }

    public AFunction getFunction() {
        return new AFunction() {
            public double f(double x) {
                return a * x + b;
            }
        };
    }
}