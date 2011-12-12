/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.solve;

import inou.math.AFunction;
import inou.math.AOperator;
import inou.math.ASolver;
import inou.math.AWrapperFunction;
import inou.math.FunctionUtil;

/**
 * The implementation of ASolver for the non-linear equation, using
 * Newton-Raphson method.
 */
public class ANewtonRaphson implements ASolver {

    double xs, eps;

    int iter, last;

    /**
     * Default constructor Default parameters are xs = 1, eps = 1e-6, iter =
     * 1000.
     */
    public ANewtonRaphson() {
        this(1, 1e-6, 1000);
    }

    /**
     * Standerd constructor
     * 
     * @param xs
     *            search start position
     * @param eps
     *            convergence parameter
     * @param iter
     *            max iteration number
     */
    public ANewtonRaphson(double xs, double eps, int iter) {
        set(xs, eps, iter);
    }

    /** set start position */
    public void set(double xs) {
        this.xs = xs;
    }

    /**
     * set parameter
     * 
     * @param xs
     *            search start position
     * @param eps
     *            convergence parameter
     * @param iter
     *            max iteration number
     */
    public void set(double xs, double eps, int iter) {
        set(xs);
        this.eps = eps;
        this.iter = iter;
    }

    public AOperator copy() {
        return new ANewtonRaphson(xs, eps, iter);
    }

    /**
     * This routine will stop the loop, when delta_n = (x_n+1 - x_n) < eps
     * [found the solution], delta_n+1 > delta_n [low convergence] and the loop
     * iteration number cross over the max iteration [out of range:error].
     * 
     * @param a
     *            target function
     * @return solution of given equation
     * @exception ArithmeticException :
     *                no convergence
     */
    public double operate(AFunction a) {
        int ic = 0;
        double x0 = xs, x1 = 0, ld = 1e20, delta;

        if (eps <= 0. || iter < 1)
            throw new ArithmeticException("Illegal parameter in ANewtonRaphson");

        AFunction dev = new AWrapperFunction(FunctionUtil.getDerivedFunction(a,
                0));
        for (ic = 0; ic < iter; ic++) {
            x1 = x0 - a.f(x0) / dev.f(x0);
            delta = Math.abs(x1 - x0);
            if (delta < eps) {
                last = ic;
                return x1;
            }
            if (delta < ld)
                ld = delta;
            else {
                System.err.println("Low convergence in ANewtonRaphson");
                last = ic;
                return x1;
            }
            x0 = x1;
        }
        throw new ArithmeticException("No convergence in ANewtonRaphson");
    }

    public int lastIteration() {
        return last;
    }

}