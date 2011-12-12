/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;
import inou.math.Matrix;

import java.awt.Dimension;

/**
 * A simple implementation of Matrix class of general dimension.
 */

public class MatrixGD extends Matrix {

    // ===========================
    // Matrix value
    // ===========================

    protected final int nr;// number of rows (tate)
    protected final int nc;// number of columns (yoko)

    /** you can operate this array directly. */
    public final double[][] m;

    private Dimension dimension;

    // ===========================
    // Constructor
    // ===========================

    /** 
     * constructs with an arbitrary matrix object.
     */
    public MatrixGD(Matrix m) {
        this(m.getDimension().width, m.getDimension().height);
        substitute(m);
    }

    /** constructs ZERO matrix with dimension size \ */
    public MatrixGD(int row, int column) {
        if (column < 1 || row < 1)
            throw new ArithmeticException("bad matrix dimension.");
        nr = row;
        nc = column;
        m = new double[nr][nc];
        dimension = new Dimension(nr, nc);
    }

    /** constructs with array [row][column] */
    public MatrixGD(double[][] arg) {
        if (arg == null)
            throw new ArithmeticException("bad matrix dimension.");

        if (arg[0].length < 1 || arg.length < 1)
            throw new ArithmeticException("bad matrix dimension.");

        nc = arg[0].length;
        nr = arg.length;
        m = arg;
        dimension = new Dimension(nr, nc);
    }

    /** construct with base vectors [column] */
    public MatrixGD(MathVector[] bases) {
        this(bases[0].getDimension(), bases.length);
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++)
                set(j, i, bases[i].v(j));
        }
    }

    /** construct an zero matrix. */
    public MatrixGD(int dimension) {
        this(dimension, dimension);
    }

    /** makes a clone matrix */
    final public Matrix getCopy() {
        Matrix cp = new MatrixGD(nr, nc);
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++)
                cp.set(j, i, get(j, i));
        }
        return cp;
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
        return m[r][c];
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
        m[r][c] = v;
    }

    /**
     * returns the dimension size. (class "Dimension" is belong to "java.awt")
     */
    final public Dimension getDimension() {
        return dimension;
    }

}
