/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

import inou.math.AFunction;

/** Bolzmann distribution function, used by Acceptor */
public class BoltzmannDistribution extends AFunction {

    private double beta = 1;

    public BoltzmannDistribution(double beta) {
        this.beta = beta;
    }

    public void setBeta(double b) {
        beta = b;
    }

    public double getBeta() {
        return beta;
    }

    public double f(double x) {
        return Math.exp(-x * beta);
    }
}