/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.MathVector;

/**
 * This is the implementation for the initial value of differential equation.
 */
public class VariableSet extends MathVector {

    /** vector element */
    public double x, y, dy;

    int dim = 3;

    /** cannot construct without initial value */
    public VariableSet() {
    }

    /** copy constructor */
    protected VariableSet(VariableSet v) {
        this(v.x, v.y, v.dy);
        dim = v.dim;
    }

    /** construct initial value */
    public VariableSet(double ix, double iy, double idy) {
        x = ix;
        y = iy;
        dy = idy;
        dim = 3;
    }

    /** construct initial value */
    public VariableSet(double ix, double iy) {
        x = ix;
        y = iy;
        dim = 2;
    }

    /** make a new copy */
    final public MathVector getCopy() {
        return new VariableSet(this);
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /** set values */
    final public void set(double tx, double ty, double tdy) {
        x = tx;
        y = ty;
        dy = tdy;
    }

    /** set values */
    final public void set(double tx, double ty) {
        x = tx;
        y = ty;
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(VariableSet xv) {
        x = xv.x;
        y = xv.y;
        dy = xv.dy;
    }

    /**
     * set the value of the column c
     * 
     * @param c
     *            column
     * @param value
     *            the value
     */
    final public void v(int c, double value) {
        switch (c) {
        case 0:
            x = value;
            return;
        case 1:
            y = value;
            return;
        case 2:
            dy = value;
            return;
        }
        throw new ArithmeticException("out of dimension : " + c);
    }

    /**
     * get the value of column c
     * 
     * @param c
     *            column
     */
    final public double v(int c) {
        switch (c) {
        case 0:
            return x;
        case 1:
            return y;
        case 2:
            return dy;
        }
        throw new ArithmeticException("out of dimension : " + c);
    }

    final public int getDimension() {
        return dim;
    }
}