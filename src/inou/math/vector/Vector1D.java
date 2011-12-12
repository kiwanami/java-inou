/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;

/**
 * This is a Simple implementation of 1D-MathVector class.
 */
public class Vector1D extends MathVector {

    /** vector element */
    public double x;

    /** construct with zero */
    public Vector1D() {
        x = 0;
    }

    /** construct initial value */
    public Vector1D(double init) {
        x = init;
    }

    /** make a new copy */
    final public MathVector getCopy() {
        return new Vector1D(x);
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * set the value of the column c
     * 
     * @param c
     *            column
     * @param value
     *            the value
     */
    final public void v(int c, double value) {
        x = value;
    }

    /**
     * get the value of column c
     * 
     * @param c
     *            column
     */
    final public double v(int c) {
        return x;
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    final public boolean equals(Vector1D xv) {
        if (xv.x != x)
            return false;
        return true;
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(Vector1D xv) {
        x = xv.x;
    }

    /**
     * multiplies this by a double value and returns result vector. OPERATOR(*)
     */
    final public MathVector mult(double s) {
        return new Vector1D(s * x);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(double s) {
        x *= s;
        return this;
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public Vector1D add(Vector1D xv) {
        return new Vector1D(x + xv.x);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public Vector1D adds(Vector1D xv) {
        x += xv.x;
        return this;
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public Vector1D sub(Vector1D xv) {
        return new Vector1D(x - xv.x);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public Vector1D subs(Vector1D xv) {
        x -= xv.x;
        return this;
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(Vector1D xv) {
        return x * xv.x;
    }

    /**
     * return the outer production. << c = a x x >>
     */
    final public Vector1D outerProduct(Vector1D xv) {
        return new Vector1D(x * xv.x);
    }

    /** normalize and substitute result for this. */
    final public MathVector normalize() {
        x = 1;
        return this;
    }

    /** returns an normal vector along this vector. */
    final public MathVector getUnit() {
        return new Vector1D(1);
    }

    /** set all element zero */
    final public MathVector zero() {
        x = 0;
        return this;
    }

    // =====================
    // Vector values
    // =====================

    /** returns the dimension size. */
    final public int getDimension() {
        return 1;
    }

    /** returns the length of this vector lengh. */
    final public double getLength() {
        return x;
    }

    /** returns the square value of this vector length */
    final public double getSquare() {
        return x * x;
    }

}