/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.MatrixGD;
import inou.math.vector.VectorGD;

import java.awt.Dimension;
import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Mathmatical matrix class. It provides foundamental operations.
 * 
 * @see inou.math.MathVector
 */

public abstract class Matrix implements Serializable {

    /** makes a clone matrix */
    public abstract Matrix getCopy();

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
    public abstract double get(int r, int c);

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
    public abstract void set(int r, int c, double v);

    /**
     * returns the dimension size. (class "Dimension" is belong to "java.awt")
     */
    public abstract Dimension getDimension();

    /**
     * returns the determinant.
     */
    public double det() {
        // calculate determinant
        return MatrixUtil.det(this);
    }

    // ===========================
    // fundamental operation
    // ===========================

    /** checks whether the given matrix equals this or not. */
    public boolean equals(Matrix xm) {
        return MatrixUtil.equal(this, xm);
    }

    /** let this matrix be the same value as given one. */
    public void substitute(Matrix xm) {
        MatrixUtil.substitute(this, xm);
    }

    /** adds a matrix to this and returns the result matrix. */
    public Matrix add(Matrix xm) {
        Matrix a = getCopy();
        MatrixUtil.add(a, this, xm);
        return a;
    }

    /** operate and subsitute the result for this */
    public Matrix adds(Matrix xm) {
        MatrixUtil.adds(this, xm);
        return this;
    }

    /** subtracts a mtrix to this and returns the result matrix. */
    public Matrix sub(Matrix xm) {
        Matrix a = getCopy();
        MatrixUtil.sub(a, this, xm);
        return a;
    }

    /** operate and subsitute the result for this */
    public Matrix subs(Matrix xm) {
        MatrixUtil.subs(this, xm);
        return this;
    }

    /** multiplies a vector from right-hand. */
    public MathVector mult(MathVector xv) {
        MathVector av = new VectorGD(getDimension().height);
        MatrixUtil.multmv(av, this, xv);
        return av;
    }

    /**
     * multiplies a vector from right-hand. (this mathod makes a temporary
     * vector object.)
     */
    public MathVector mults(MathVector xv) {
        MathVector av = new VectorGD(getDimension().height);
        MatrixUtil.multmv(av, this, xv);
        xv.substitute(av);
        return xv;
    }

    /** multiplies this by a value and returns the result matrix. */
    public Matrix mult(double s) {
        Dimension dm = getDimension();
        Matrix a = getCopy();
        for (int i = 0; i < dm.height; i++) {
            for (int j = 0; j < dm.width; j++) {
                a.set(i, j, get(i, j) * s);
            }
        }
        return a;
    }

    /** operate and subsitute the result for this */
    public Matrix mults(double s) {
        Dimension dm = getDimension();
        for (int i = 0; i < dm.height; i++) {
            for (int j = 0; j < dm.width; j++) {
                set(i, j, get(i, j) * s);
            }
        }
        return this;
    }

    /**
     * multiplies this by a matrix from right-hand, and return a result (this
     * matrix value is not modified.)
     */
    public Matrix mult(Matrix rm) {
        Matrix ans = new MatrixGD(getDimension().height,
                rm.getDimension().width);
        MatrixUtil.mult(ans, this, rm);
        return ans;
    }

    /** operate and subsitute the result for this */
    public Matrix mults(Matrix rm) {
        MatrixUtil.mults(this, rm);
        return this;
    }

    // ===========================
    // Matrix operation
    // ===========================

    /**
     * get transposed matrix. This matrix object is not modified.
     */
    public Matrix getTrans() {
        Dimension dm = getDimension();
        Matrix mt = null;
        if (dm.width == dm.height)
            mt = getCopy();
        else
            mt = new MatrixGD(dm.width, dm.height);
        for (int i = 0; i < dm.height; i++) {
            for (int j = 0; j < dm.width; j++) {
                mt.set(j, i, get(i, j));
            }
        }
        return mt;
    }

    /**
     * returns the inverse matrix. (A matrix must be a regular square.) This
     * matrix object is not modified.
     */
    public Matrix getInverse() {
        // calculate inverse matrix
        Dimension dm = getDimension();
        Matrix ret = new MatrixGD(dm.height, dm.width);
        double det = MatrixUtil.inverse(this, ret);
        if (MathUtil.nearlyEqual(det, 0))
            throw new ArithmeticException("Singular Matrix at inverse");

        return ret;
    }

    // ===========================
    // dump
    // ===========================

    /** returns simple string dump. */
    public String toString() {
        return MatrixUtil.toString(this);
    }

    /** return string dump with given formatter */
    public String toString(NumberFormat nf) {
        return MatrixUtil.toString(this, nf);
    }
}