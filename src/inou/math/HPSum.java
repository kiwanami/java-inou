/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** High-precision summation */
public class HPSum {

    private double rest = 0;

    private double current = 0;

    private double temp = 0;

    public HPSum() {
    }

    public HPSum(double init) {
        current = init;
    }

    public void reset() {
        rest = 0;
        current = 0;
    }

    public void set(double val) {
        rest = 0;
        current = val;
    }

    public void add(double t) {
        rest += t;
        temp = current;
        current += rest;
        temp = current - temp;
        rest -= temp;
    }

    public double get() {
        return current;
    }
}