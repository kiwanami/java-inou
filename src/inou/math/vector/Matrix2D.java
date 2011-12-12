/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathUtil;
import inou.math.MathVector;
import inou.math.Matrix;

import java.awt.Dimension;

/**
 * The simple implementation of Matrix class of 2 dimension.
 */

public class Matrix2D extends Matrix {

    /** matrix element */
    public double m00 = 0, m10 = 0, m01 = 0, m11 = 0;

    // ===========================
    // Constructor
    // ===========================

    /** cannnot construct with no infomation */
    public Matrix2D() {
        this(0);
    };

    /** 
     * constructs with an arbitrary matrix object.
     */
    public Matrix2D(Matrix m) {
        m00 = m.get(0,0); m01 = m.get(0,1);
        m10 = m.get(1,0); m11 = m.get(1,1);
    }

    /** constructs with array [row][column] */
    public Matrix2D(double[][] arg) {
        if (arg == null)
            throw new ArithmeticException("null matrix argument.");

        if (arg[0].length != 2 || arg.length != 2)
            throw new ArithmeticException("bad matrix argument.");

        m00 = arg[0][0];
        m01 = arg[0][1];
        m10 = arg[1][0];
        m11 = arg[1][1];
    }

    /** construct with base vectors [column] */
    public Matrix2D(MathVector[] bases) {
        m00 = bases[0].v(0);
        m01 = bases[1].v(0);
        m10 = bases[0].v(1);
        m11 = bases[1].v(1);
    }

    /** construct an diagonal element. */
    public Matrix2D(double value) {
        m00 = value;
        m11 = value;
    }

    /** construct all element */
    public Matrix2D(double t00, double t10, double t01, double t11) {
        m00 = t00;
        m01 = t01;
        m10 = t10;
        m11 = t11;
    }

    /** makes a clone matrix */
    final public Matrix getCopy() {
        return new Matrix2D(m00, m10, m01, m11);
    }

    // ===========================
    // Matrix value
    // ===========================

    /**
     * get matrix element
     * 
     * @param r
     *            row
     * @param c
     *            column
     * @return matrix element
     */
    final public double get(int r, int c) {
        switch (r) {
        case 0:
            switch (c) {
            case 0:
                return m00;
            case 1:
                return m01;
            default:
                throw new ArithmeticException("out of dimension : col=" + c);
            }
        case 1:
            switch (c) {
            case 0:
                return m10;
            case 1:
                return m11;
            }
        }
        throw new ArithmeticException("out of dimension : col=" + c);
    }

    /**
     * set matrix element
     * 
     * @param r
     *            row
     * @param c
     *            column
     * @param v
     *            matrix element
     */
    final public void set(int r, int c, double v) {
        switch (r) {
        case 0:
            switch (c) {
            case 0:
                m00 = v;
                return;
            case 1:
                m01 = v;
                return;
            default:
                throw new ArithmeticException("out of dimension : col=" + c);
            }
        case 1:
            switch (c) {
            case 0:
                m10 = v;
                return;
            case 1:
                m11 = v;
                return;
            }
        }
        throw new ArithmeticException("out of dimension : col=" + c);
    }

    public static Dimension dim = new Dimension(2, 2);

    /**
     * returns the dimension size. (class "Dimension" is belong to "java.awt")
     */
    final public Dimension getDimension() {
        return dim;
    }

    /**
     * returns the determinant.
     */
    final public double det() {
        return m00 * m11 - m01 * m10;
    }

    // ===========================
    // fundamental operation
    // ===========================

    /**
     * let this matrix be the same value as given one. OPERATOR(=)
     */
    final public void substitute(Matrix2D xm) {
        m00 = xm.m00;
        m01 = xm.m01;
        m10 = xm.m10;
        m11 = xm.m11;
    }

    /** adds a matrix to this and returns the result matrix. */
    final public Matrix2D add(Matrix2D xm) {
        return new Matrix2D(m00 + xm.m00, m01 + xm.m01, m10 + xm.m10, m11
                + xm.m11);
    }

    /** operate and subsitute the result for this */
    final public Matrix2D adds(Matrix2D xm) {
        m00 += xm.m00;
        m01 += xm.m01;
        m10 += xm.m10;
        m11 += xm.m11;
        return this;
    }

    /** subtracts a mtrix to this and returns the result matrix. */
    final public Matrix2D sub(Matrix2D xm) {
        return new Matrix2D(m00 - xm.m00, m01 - xm.m01, m10 - xm.m10, m11
                - xm.m11);
    }

    /** operate and subsitute the result for this */
    final public Matrix2D subs(Matrix2D xm) {
        m00 -= xm.m00;
        m01 -= xm.m01;
        m10 -= xm.m10;
        m11 -= xm.m11;
        return this;
    }

    /**
     * multiplies this by a value and returns the result matrix. OPERATOR(*)
     */
    final public Matrix mult(double s) {
        return new Matrix2D(m00 * s, m10 * s, m01 * s, m11 * s);
    }

    /**
     * operate and subsitute the result for this. OPERATOR(*=)
     */
    final public Matrix mults(double s) {
        m00 *= s;
        m01 *= s;
        m10 *= s;
        m11 *= s;
        return this;
    }

    /**
     * multiplies this by a matrix from right-hand, and return a result (this
     * matrix value is not modified.)
     */
    final public Matrix2D mult(Matrix2D rm) {
        return new Matrix2D(m00 * rm.m00 + m01 * rm.m10, m10 * rm.m00 + m11
                * rm.m10, m00 * rm.m01 + m01 * rm.m11, m10 * rm.m01 + m11
                * rm.m11);
    }

    /** operate and subsitute the result for this */
    final public Matrix2D mults(Matrix2D rm) {
        double am00 = m00 * rm.m00 + m01 * rm.m10;
        double am10 = m10 * rm.m00 + m11 * rm.m10;
        double am01 = m00 * rm.m01 + m01 * rm.m11;
        double am11 = m10 * rm.m01 + m11 * rm.m11;
        m00 = am00;
        m01 = am01;
        m10 = am10;
        m11 = am11;
        return this;
    }

    // ===========================
    // Matrix operation
    // ===========================

    /**
     * return a new transposed matrix This matrix object is not modified.
     */
    final public Matrix getTrans() {
        return new Matrix2D(m00, m01, m10, m11);
    }

    /**
     * return a new inverse matrix. (The Matrix must be a regular square.) This
     * matrix object is not modified.
     */
    final public Matrix getInverse() {
        double d = det();
        if (MathUtil.nearlyEqual(d, 0))
            throw new ArithmeticException("Singular Matrix at inverse");
        d = 1. / d;
        return new Matrix2D(m11 * d, -m10 * d, -m01 * d, m00 * d);
    }

}
