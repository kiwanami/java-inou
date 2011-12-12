/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.VectorGD;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * This is a mathmatical vector class. It provides foundamental operations.
 * 
 * @see inou.math.Matrix
 */
public abstract class MathVector implements Serializable {

    /** make a new copy */
    public abstract MathVector getCopy();

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
    public abstract void v(int c, double value);

    /**
     * get the value of column c
     * 
     * @param c
     *            column
     */
    public abstract double v(int c);

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /** checks whether given vector equals or not. */
    public boolean equals(MathVector xv) {
        if (xv.getDimension() != getDimension())
            return false;
        for (int i = 0; i < getDimension(); i++) {
            if (!MathUtil.nearlyEqual(xv.v(i), v(i)))
                return false;
        }
        return true;
    }

    /** let this vector values be the same as given vector. */
    public void substitute(MathVector xv) {
        if (xv.getDimension() != getDimension()) {
            throw new ArithmeticException(
                    "Different dimension at vector substitution.");
        }
        for (int i = 0; i < getDimension(); i++) {
            v(i, xv.v(i));
        }
    }

    /** multiplies this by a double value and returns result vector. */
    public MathVector mult(double s) {
        MathVector a = getCopy();
        for (int i = 0; i < getDimension(); i++)
            a.v(i, a.v(i) * s);
        return a;
    }

    /** operate and substitute result for this. */
    public MathVector mults(double s) {
        for (int i = 0; i < getDimension(); i++)
            v(i, v(i) * s);
        return this;
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.)
     */
    public MathVector mult(Matrix mt) {
        MathVector av = new VectorGD(mt.getDimension().width);
        MatrixUtil.multvm(av, this, mt);
        return av;
    }

    /** operate and substitute result for this. */
    public MathVector mults(Matrix mt) {
        this.substitute(mult(mt));
        return this;
    }

    /** adds some vector to this and returns result vector. */
    public MathVector add(MathVector xv) {
        if (xv.getDimension() != getDimension()) {
            throw new ArithmeticException(
                    "different dimension at vector addition.");
        }
        MathVector a = getCopy();
        for (int i = 0; i < getDimension(); i++)
            a.v(i, a.v(i) + xv.v(i));
        return a;
    }

    /** operate and substitute result for this. */
    public MathVector adds(MathVector xv) {
        if (xv.getDimension() != getDimension()) {
            throw new ArithmeticException(
                    "different dimension at vector addition.");
        }
        for (int i = 0; i < getDimension(); i++)
            v(i, v(i) + xv.v(i));
        return this;
    }

    /** subtracts some vector from this and returns result vector. */
    public MathVector sub(MathVector xv) {
        if (xv.getDimension() != getDimension()) {
            throw new ArithmeticException(
                    "different dimension at vector subtraction.");
        }
        MathVector a = getCopy();
        for (int i = 0; i < getDimension(); i++)
            a.v(i, a.v(i) - xv.v(i));
        return a;
    }

    /** operate and substitute result for this. */
    public MathVector subs(MathVector xv) {
        if (xv.getDimension() != getDimension()) {
            throw new ArithmeticException(
                    "different dimension at vector subtraction.");
        }
        for (int i = 0; i < getDimension(); i++)
            v(i, v(i) - xv.v(i));
        return this;
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the inner production. << c = <a|x> >>
     */
    public double innerProduct(MathVector x) {
        return VectorUtil.innerProduct(this, x);
    }

    /**
     * return the outer production. << c = a x x >>
     */
    public MathVector outerProduct(MathVector x) {
        return VectorUtil.outerProduct(this, x);
    }

    /** normalize and substitute result for this. */
    public MathVector normalize() {
        double q = getLength();
        if (q == 0)
            return mults(0);
        return mults(1 / q);
    }

    /** returns an normal vector along this vector. */
    public MathVector getUnit() {
        return getCopy().normalize();
    }

    /** set all element zero */
    public MathVector zero() {
        for (int i = 0; i < getDimension(); i++) {
            v(i, 0);
        }
        return this;
    }

    // =====================
    // Vector values
    // =====================
    /** returns the dimension size. */
    public abstract int getDimension();

    /** returns the length of this vector lengh. */
    public double getLength() {
        return Math.sqrt(getSquare());
    }

    /** returns the square value of this vector length */
    public double getSquare() {
        double d = 0;
        for (int i = 0; i < getDimension(); i++)
            d += v(i) * v(i);
        return d;
    }

    /** simple string dump */
    public String toString() {
        return VectorUtil.toString(this);
    }

    /** return string dump with given formatter */
    public String toString(NumberFormat nf) {
        return VectorUtil.toString(this, nf);
    }
}