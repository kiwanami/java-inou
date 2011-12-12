/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.MatrixGD;

import java.awt.Dimension;
import java.io.Serializable;

/**
 * This is a mathematical matrix class. It provides foundamental operations.
 */
public class MatrixFunctionClass implements MatrixFunction, Serializable {

    // ===========================
    // Matrix value
    // ===========================

    protected int nr, nc;

    protected int argd;

    /** you can operate this array directly. */
    public ScalarFunction[][] m;

    // ===========================
    // Constructor
    // ===========================

    /** cannot construct with no infomation */
    protected MatrixFunctionClass() {
    };

    /** constructs with array */
    public MatrixFunctionClass(ScalarFunction[][] arg) {
        if (arg == null)
            throw new NullPointerException("null matrix argument");
        if (arg[0].length < 1 || arg.length < 1)
            throw new ArithmeticException("bad dimension");
        init(arg);
    }

    protected void init(ScalarFunction[][] arg) {
        nc = arg[0].length;
        nr = arg.length;
        argd = arg[0][0].getDimension();
        m = arg;
    }

    // ===========================
    // Matrix value
    // ===========================

    /**
     * return the dimension size of output matrix. (class "Dimension" is belong
     * to "java.awt")
     */
    public Dimension getDimension() {
        return new Dimension(nr, nc);
    }

    /**
     * return the dimension size of argumet.
     */
    public int getArgDimension() {
        return argd;
    }

    // ===========================
    // operation
    // ===========================

    /** return element function */
    public final ScalarFunction getFunction(int r, int c) {
        return m[r][c];
    }

    /** set element function */
    public final void setFunction(int r, int c, ScalarFunction s) {
        m[r][c] = s;
    }

    /**
     * return a matrix, obtained given functions and parameter.
     */
    public final Matrix f(MathVector x) {
        Matrix mt = new MatrixGD(nr, nc);
        f(x, mt);
        return mt;
    }

    /**
     * return a matrix, obtained given functions and parameter. If you need
     * speed, use this method, not previous one.
     */
    public final void f(MathVector x, Matrix in) {
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                in.set(i, j, m[i][j].f(x));
            }
        }
    }
}