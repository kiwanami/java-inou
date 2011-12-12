/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;
import inou.math.Matrix;

import java.awt.Dimension;

/**
 * The simple implementation of Matrix class of 3 dimension.
 */

public class Matrix3D extends Matrix {

    /** matrix element */
    public double m00 = 0, m01 = 0, m02 = 0;
    public double m10 = 0, m11 = 0, m12 = 0;
    public double m20 = 0, m21 = 0, m22 = 0;

    // ===========================
    // Constructor
    // ===========================

    /** cannnot construct with no infomation */
    public Matrix3D() {
    }

    /** 
     * constructs with an arbitrary matrix object.
     */
	public Matrix3D(Matrix m) {
		m00 = m.get(0,0); m01 = m.get(0,1); m02 = m.get(0,2);
		m10 = m.get(1,0); m11 = m.get(1,1); m12 = m.get(1,2);
		m20 = m.get(2,0); m21 = m.get(2,1); m22 = m.get(2,2);
	}

    /** constructs with array [row][column] */
    public Matrix3D(double[][] arg) {
        if (arg == null)
            throw new ArithmeticException("null matrix argument.");

        if (arg[0].length != 3 || arg.length != 3)
            throw new ArithmeticException("bad matrix argument.");

        m00 = arg[0][0];
        m01 = arg[0][1];
        m02 = arg[0][2];
        m10 = arg[1][0];
        m11 = arg[1][1];
        m12 = arg[1][2];
        m20 = arg[2][0];
        m21 = arg[2][1];
        m22 = arg[2][2];
    }

    /** construct with base vectors [column] */
    public Matrix3D(MathVector[] bases) {
        m00 = bases[0].v(0);
        m01 = bases[1].v(0);
        m02 = bases[2].v(0);
        m10 = bases[0].v(1);
        m11 = bases[1].v(1);
        m12 = bases[2].v(1);
        m20 = bases[0].v(2);
        m21 = bases[1].v(2);
        m22 = bases[2].v(2);
    }

    /** construct an diagonal element. */
    public Matrix3D(double value) {
        m00 = value;
        m11 = value;
        m22 = value;
    }

    /** construct all element */
    public Matrix3D(double t00, double t10, double t20, 
					double t01, double t11, double t21, 
					double t02, double t12, double t22) {
        m00 = t00;
        m01 = t01;
        m02 = t02;
        m10 = t10;
        m11 = t11;
        m12 = t12;
        m20 = t20;
        m21 = t21;
        m22 = t22;
    }

    /** makes a clone matrix */
    public Matrix getCopy() {
        return new Matrix3D(m00, m10, m20, m01, m11, m21, m02, m12, m22);
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
    public double get(int r, int c) {
        switch (r) {
        case 0:
            switch (c) {
            case 0:
                return m00;
            case 1:
                return m01;
            case 2:
                return m02;
            default:
                throw new ArithmeticException("out of dimension : col=" + c);
            }
        case 1:
            switch (c) {
            case 0:
                return m10;
            case 1:
                return m11;
            case 2:
                return m12;
            default:
                throw new ArithmeticException("out of dimension : col=" + c);
            }
        case 2:
            switch (c) {
            case 0:
                return m20;
            case 1:
                return m21;
            case 2:
                return m22;
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
    public void set(int r, int c, double v) {
        switch (r) {
        case 0:
            switch (c) {
            case 0:
                m00 = v;
                return;
            case 1:
                m01 = v;
                return;
            case 2:
                m02 = v;
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
            case 2:
                m12 = v;
                return;
            default:
                throw new ArithmeticException("out of dimension : col=" + c);
            }
        case 2:
            switch (c) {
            case 0:
                m20 = v;
                return;
            case 1:
                m21 = v;
                return;
            case 2:
                m22 = v;
                return;
            }
        }
        throw new ArithmeticException("out of dimension : col=" + c);
    }

    final public static Dimension dim = new Dimension(3, 3);

    /**
     * returns the dimension size. (class "Dimension" is belong to "java.awt")
     */
    public Dimension getDimension() {
        return dim;
    }

    // ===========================
    // fundamental operation
    // ===========================

    /**
     * let this matrix be the same value as given one. OPERATOR(=)
     */
    final public void substitute(Matrix xm) {
        if (xm instanceof Matrix3D)
            substitute((Matrix3D) xm);
        else
            super.substitute(xm);
    }

    /**
     * let this matrix be the same value as given one. OPERATOR(=)
     */
    final public void substitute(Matrix3D xm) {
        m00 = xm.m00;
        m01 = xm.m01;
        m02 = xm.m02;
        m10 = xm.m10;
        m11 = xm.m11;
        m12 = xm.m12;
        m20 = xm.m20;
        m21 = xm.m21;
        m22 = xm.m22;
    }

    /** adds a matrix to this and returns the result matrix. */
    final public Matrix add(Matrix xm) {
        if (xm instanceof Matrix3D)
            return add((Matrix3D) xm);
        return super.add(xm);
    }

    /** adds a matrix to this and returns the result matrix. */
    final public Matrix3D add(Matrix3D xm) {
        return new Matrix3D(m00 + xm.m00, m10 + xm.m10, m20 + xm.m20,
							m01 + xm.m01, m11 + xm.m11, m21 + xm.m21, 
							m02 + xm.m02, m12 + xm.m12, m22 + xm.m22);
    }

    /** operate and subsitute the result for this */
    final public Matrix adds(Matrix xm) {
        if (xm instanceof Matrix3D)
            return adds((Matrix3D) xm);
        return super.adds(xm);
    }

    /** operate and subsitute the result for this */
    final public Matrix3D adds(Matrix3D xm) {
        m00 += xm.m00;
        m01 += xm.m01;
        m02 += xm.m02;
        m10 += xm.m10;
        m11 += xm.m11;
        m12 += xm.m12;
        m20 += xm.m20;
        m21 += xm.m21;
        m22 += xm.m22;
        return this;
    }

    /** subtracts a mtrix to this and returns the result matrix. */
    final public Matrix sub(Matrix xm) {
        if (xm instanceof Matrix3D)
            return sub((Matrix3D) xm);
        return super.sub(xm);
    }

    /** subtracts a mtrix to this and returns the result matrix. */
    final public Matrix3D sub(Matrix3D xm) {
        return new Matrix3D(m00 - xm.m00, m10 - xm.m10, m20 - xm.m20, 
							m01 - xm.m01, m11 - xm.m11, m21 - xm.m21,
							m02 - xm.m02, m12 - xm.m12, m22 - xm.m22);
    }

    /** operate and subsitute the result for this */
    final public Matrix subs(Matrix xm) {
        if (xm instanceof Matrix3D)
            return subs((Matrix3D) xm);
        return super.subs(xm);
    }

    /** operate and subsitute the result for this */
    final public Matrix3D subs(Matrix3D xm) {
        m00 -= xm.m00;
        m01 -= xm.m01;
        m02 -= xm.m02;
        m10 -= xm.m10;
        m11 -= xm.m11;
        m12 -= xm.m12;
        m20 -= xm.m20;
        m21 -= xm.m21;
        m22 -= xm.m22;
        return this;
    }

    /**
     * multiplies this by a value and returns the result matrix. OPERATOR(*)
     */
    final public Matrix mult(double s) {
        return new Matrix3D(m00 * s, m10 * s, m20 * s, 
							m01 * s, m11 * s, m21 * s,
							m02 * s, m12 * s, m22 * s);
    }

    /**
     * operate and subsitute the result for this. OPERATOR(*=)
     */
    final public Matrix mults(double s) {
        m00 *= s;
        m01 *= s;
        m02 *= s;
        m10 *= s;
        m11 *= s;
        m12 *= s;
        m20 *= s;
        m21 *= s;
        m22 *= s;
        return this;
    }

    /**
     * multiplies vector from RIGHT-hand and return the result vector.
     * OPERATOR(*)
     */
    final public MathVector mult(MathVector xm) {
        if (xm instanceof Vector3D)
            return mult((Vector3D) xm);
        return super.mult(xm);
    }

    /**
     * multiplies vector from RIGHT-hand and return the result vector.
     * OPERATOR(*)
     */
    final public Vector3D mult(Vector3D xv) {
        return new Vector3D(m00 * xv.x + m01 * xv.y + m02 * xv.z, 
							m10 * xv.x + m11 * xv.y + m12 * xv.z,
							m20 * xv.x + m21 * xv.y + m22 * xv.z);
    }

    /**
     * multiplies vector from RIGHT-hand and set the result. OPERATOR(*)
     */
    final public MathVector mults(MathVector xm) {
        if (xm instanceof Vector3D)
            return mults((Vector3D) xm);
        return super.mults(xm);
    }

    /**
     * multiplies vector from RIGHT-hand and set the result. OPERATOR(*)
     */
    final public Vector3D mults(Vector3D xv) {
        double dx = m00 * xv.x + m01 * xv.y + m02 * xv.z;
        double dy = m10 * xv.x + m11 * xv.y + m12 * xv.z;
        double dz = m20 * xv.x + m21 * xv.y + m22 * xv.z;
        xv.x = dx;
        xv.y = dy;
        xv.z = dz;
        return xv;
    }

    /**
     * multiplies this by a matrix from right-hand, and return a result (this
     * matrix value is not modified.)
     */
    final public Matrix mult(Matrix xm) {
        if (xm instanceof Matrix3D)
            return mult((Matrix3D) xm);
        return super.mult(xm);
    }

    /**
     * multiplies this by a matrix from right-hand, and return a result (this
     * matrix value is not modified.)
     */
    final public Matrix3D mult(Matrix3D rm) {
        return new Matrix3D(m00 * rm.m00 + m01 * rm.m10 + m02 * rm.m20, 
							m10 * rm.m00 + m11 * rm.m10 + m12 * rm.m20,
							m20 * rm.m00 + m21 * rm.m10 + m22 * rm.m20,
							m00 * rm.m01 + m01 * rm.m11 + m02 * rm.m21,
							m10 * rm.m01 + m11 * rm.m11 + m12 * rm.m21,
							m20 * rm.m01 + m21 * rm.m11 + m22 * rm.m21,
							m00 * rm.m02 + m01 * rm.m12 + m02 * rm.m22,
							m10 * rm.m02 + m11 * rm.m12 + m12 * rm.m22,
							m20 * rm.m02 + m21 * rm.m12 + m22 * rm.m22);
    }

    /* operate and subsitute the result for this */
    /*
     * final public Matrix mults(Matrix xm) { if (xm instanceof Matrix3D) return
     * mults((Matrix3D)xm); return super.mults(xm); }
     */
    /** operate and subsitute the result for this */
    final public Matrix3D mults(Matrix3D rm) {
        double am00 = m00 * rm.m00 + m01 * rm.m10 + m02 * rm.m20;
        double am10 = m10 * rm.m00 + m11 * rm.m10 + m12 * rm.m20;
        double am20 = m20 * rm.m00 + m21 * rm.m10 + m22 * rm.m20;
        double am01 = m00 * rm.m01 + m01 * rm.m11 + m02 * rm.m21;
        double am11 = m10 * rm.m01 + m11 * rm.m11 + m12 * rm.m21;
        double am21 = m20 * rm.m01 + m21 * rm.m11 + m22 * rm.m21;
        double am02 = m00 * rm.m02 + m01 * rm.m12 + m02 * rm.m22;
        double am12 = m10 * rm.m02 + m11 * rm.m12 + m12 * rm.m22;
        double am22 = m20 * rm.m02 + m21 * rm.m12 + m22 * rm.m22;
        m00 = am00;
        m01 = am01;
        m02 = am02;
        m10 = am10;
        m11 = am11;
        m12 = am12;
        m20 = am20;
        m21 = am21;
        m22 = am22;
        return this;
    }

}
