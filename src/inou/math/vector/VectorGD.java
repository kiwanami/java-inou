/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;

/**
 * This is a simple implementation of the mathmatical vector class of the
 * general dimension.
 */

public class VectorGD extends MathVector {

    // =====================
    // vector data
    // =====================

    public double[] v;

    // =====================
    // constructor
    // =====================

    /** Vector cannot be constructed with no information. */
    private VectorGD() {
    }

    /** given only dimension size */
    public VectorGD(int dimension) {
        v = new double[dimension];
    }

    /** given an initial value */
    public VectorGD(double[] arg) {
        if (arg == null)
            System.err.println("null vector argument.");
        v = arg;
    }

    /** makes a clone. */
    final public MathVector getCopy() {
        MathVector cp = new VectorGD(getDimension());
        for (int i = 0; i < getDimension(); i++)
            cp.v(i, v(i));
        return cp;
    }

    final public void setAll(double t) {
        for (int i = 0; i < getDimension(); i++)
            v[i] = t;
    }

    final public void v(int c, double value) {
        v[c] = value;
    }

    final public double v(int c) {
        return v[c];
    }

    // =====================
    // Vector values
    // =====================

    /** returns the dimension size. */
    public int getDimension() {
        return v.length;
    }

}