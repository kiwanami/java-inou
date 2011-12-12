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

public class MatrixQD extends Matrix3D {

    /** matrix element */
    // public double m00=0,m01=0,m02=0,m03=0;
    // public double m10=0,m11=0,m12=0,m13=0;
    // public double m20=0,m21=0,m22=0,m23=0;
    // public double m30=0,m31=0,m32=0,m33=0;
    public double m03 = 0;
    public double m13 = 0;
    public double m23 = 0;
    public double m30 = 0, m31 = 0, m32 = 0, m33 = 0;

    // ===========================
    // Constructor
    // ===========================

    /** cannnot construct with no infomation */
    public MatrixQD() {
        super();
    };

    /** 
     * constructs with an arbitrary matrix object.
     */
    public MatrixQD(Matrix m) {
        m00 = m.get(0,0); m01 = m.get(0,1); m02 = m.get(0,2); m03 = m.get(0,3);
        m10 = m.get(1,0); m11 = m.get(1,1); m12 = m.get(1,2); m13 = m.get(1,3);
        m20 = m.get(2,0); m21 = m.get(2,1); m22 = m.get(2,2); m23 = m.get(2,3);
        m30 = m.get(3,0); m31 = m.get(3,1); m32 = m.get(3,2); m33 = m.get(3,3);
    }

    /** constructs with array [row][column] */
    public MatrixQD(double[][] arg) {
        if (arg == null)
            throw new ArithmeticException("null matrix argument.");

        if (arg[0].length != 4 || arg.length != 4)
            throw new ArithmeticException("bad matrix argument.");

        m00 = arg[0][0];
        m01 = arg[0][1];
        m02 = arg[0][2];
        m03 = arg[0][3];
        m10 = arg[1][0];
        m11 = arg[1][1];
        m12 = arg[1][2];
        m13 = arg[1][3];
        m20 = arg[2][0];
        m21 = arg[2][1];
        m22 = arg[2][2];
        m23 = arg[2][3];
        m30 = arg[3][0];
        m31 = arg[3][1];
        m32 = arg[3][2];
        m33 = arg[3][3];
    }

    /** construct with base vectors [column] */
    public MatrixQD(MathVector[] bases) {
        m00 = bases[0].v(0);
        m01 = bases[1].v(0);
        m02 = bases[2].v(0);
        m03 = bases[3].v(0);
        m10 = bases[0].v(1);
        m11 = bases[1].v(1);
        m12 = bases[2].v(1);
        m13 = bases[3].v(1);
        m20 = bases[0].v(2);
        m21 = bases[1].v(2);
        m22 = bases[2].v(2);
        m23 = bases[3].v(2);
        m30 = bases[0].v(3);
        m31 = bases[1].v(3);
        m32 = bases[2].v(3);
        m33 = bases[3].v(3);
    }

    /** construct an diagonal element. */
    public MatrixQD(double value) {
        m00 = value;
        m11 = value;
        m22 = value;
        m33 = value;
    }

    public MatrixQD(double t00, double t10, double t20, double t30, double t01,
            double t11, double t21, double t31, double t02, double t12,
            double t22, double t32, double t03, double t13, double t23,
            double t33) {
        m00 = t00;
        m01 = t01;
        m02 = t02;
        m03 = t03;
        m10 = t10;
        m11 = t11;
        m12 = t12;
        m13 = t13;
        m20 = t20;
        m21 = t21;
        m22 = t22;
        m23 = t23;
        m30 = t30;
        m31 = t31;
        m32 = t32;
        m33 = t33;
    }

    /** makes a clone matrix */
    public Matrix getCopy() {
        return new MatrixQD(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12,
                m22, m32, m03, 013, m23, m33);
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
            case 3:
                return m03;
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
            case 3:
                return m13;
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
            case 3:
                return m23;
            }
        case 3:
            switch (c) {
            case 0:
                return m30;
            case 1:
                return m31;
            case 2:
                return m32;
            case 3:
                return m33;
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
            case 3:
                m03 = v;
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
            case 3:
                m13 = v;
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
            case 3:
                m23 = v;
                return;
            }
        case 3:
            switch (c) {
            case 0:
                m30 = v;
                return;
            case 1:
                m31 = v;
                return;
            case 2:
                m32 = v;
                return;
            case 3:
                m33 = v;
                return;
            }
        }
        throw new ArithmeticException("out of dimension : col=" + c);
    }

    public static Dimension dim = new Dimension(4, 4);

    /**
     * returns the dimension size. (class "Dimension" is belong to "java.awt")
     */
    final public Dimension getDimension() {
        return dim;
    }

    // ===========================
    // fundamental operation
    // ===========================

    /**
     * let this matrix be the same value as given one. OPERATOR(=)
     */
    final public void qsubstitute(MatrixQD xm) {
        m00 = xm.m00;
        m01 = xm.m01;
        m02 = xm.m02;
        m03 = xm.m03;
        m10 = xm.m10;
        m11 = xm.m11;
        m12 = xm.m12;
        m13 = xm.m13;
        m20 = xm.m20;
        m21 = xm.m21;
        m22 = xm.m22;
        m23 = xm.m23;
        m30 = xm.m30;
        m31 = xm.m31;
        m32 = xm.m32;
        m33 = xm.m33;
    }

    /**
     * multiplies this by a value and returns the result matrix. OPERATOR(*)
     */
    final public MatrixQD qmult(double s) {
        return new MatrixQD(m00 * s, m10 * s, m20 * s, m30 * s, m01 * s, m11
                * s, m21 * s, m31 * s, m02 * s, m12 * s, m22 * s, m32 * s, m03
                * s, m13 * s, m23 * s, m33 * s);
    }

    /**
     * operate and subsitute the result for this. OPERATOR(*=)
     */
    final public MatrixQD qmults(double s) {
        m00 *= s;
        m01 *= s;
        m02 *= s;
        m03 *= s;
        m10 *= s;
        m11 *= s;
        m12 *= s;
        m13 *= s;
        m20 *= s;
        m21 *= s;
        m22 *= s;
        m23 *= s;
        m30 *= s;
        m31 *= s;
        m32 *= s;
        m33 *= s;
        return this;
    }

    /**
     * multiplies vector from RIGHT-hand and return the result vector.
     * OPERATOR(*)
     */
    final public VectorQD qmult(VectorQD xv) {
        return new VectorQD(m00 * xv.x + m01 * xv.y + m02 * xv.z + m03, m10
                * xv.x + m11 * xv.y + m12 * xv.z + m13, m20 * xv.x + m21 * xv.y
                + m22 * xv.z + m23);
    }

    /**
     * multiplies vector from RIGHT-hand and set the result. OPERATOR(*)
     */
    final public VectorQD qmults(VectorQD xv) {
        double dx = m00 * xv.x + m01 * xv.y + m02 * xv.z + m03;
        double dy = m10 * xv.x + m11 * xv.y + m12 * xv.z + m13;
        double dz = m20 * xv.x + m21 * xv.y + m22 * xv.z + m23;
        xv.x = dx;
        xv.y = dy;
        xv.z = dz;
        return xv;
    }

    /**
     * multiplies this by a matrix from right-hand, and return a result (this
     * matrix value is not modified.)
     */
    final public MatrixQD qmult(MatrixQD rm) {
        return new MatrixQD(m00 * rm.m00 + m01 * rm.m10 + m02 * rm.m20 + m03
                * rm.m30, m10 * rm.m00 + m11 * rm.m10 + m12 * rm.m20 + m13
                * rm.m30, m20 * rm.m00 + m21 * rm.m10 + m22 * rm.m20 + m23
                * rm.m30, m30 * rm.m00 + m31 * rm.m10 + m32 * rm.m20 + m33
                * rm.m30,

        m00 * rm.m01 + m01 * rm.m11 + m02 * rm.m21 + m03 * rm.m31, m10 * rm.m01
                + m11 * rm.m11 + m12 * rm.m21 + m13 * rm.m31, m20 * rm.m01
                + m21 * rm.m11 + m22 * rm.m21 + m23 * rm.m31, m30 * rm.m01
                + m31 * rm.m11 + m32 * rm.m21 + m33 * rm.m31,

        m00 * rm.m02 + m01 * rm.m12 + m02 * rm.m22 + m03 * rm.m32, m10 * rm.m02
                + m11 * rm.m12 + m12 * rm.m22 + m13 * rm.m32, m20 * rm.m02
                + m21 * rm.m12 + m22 * rm.m22 + m23 * rm.m32, m30 * rm.m02
                + m31 * rm.m12 + m32 * rm.m22 + m33 * rm.m32,

        m00 * rm.m03 + m01 * rm.m13 + m02 * rm.m23 + m03 * rm.m33, m10 * rm.m03
                + m11 * rm.m13 + m12 * rm.m23 + m13 * rm.m33, m20 * rm.m03
                + m21 * rm.m13 + m22 * rm.m23 + m23 * rm.m33, m30 * rm.m03
                + m31 * rm.m13 + m32 * rm.m23 + m33 * rm.m33);
    }

    /** operate and subsitute the result for this */
    final public MatrixQD qmults(MatrixQD rm) {
        double am00 = m00 * rm.m00 + m01 * rm.m10 + m02 * rm.m20 + m03 * rm.m30;
        double am10 = m10 * rm.m00 + m11 * rm.m10 + m12 * rm.m20 + m13 * rm.m30;
        double am20 = m20 * rm.m00 + m21 * rm.m10 + m22 * rm.m20 + m23 * rm.m30;
        double am30 = m30 * rm.m00 + m31 * rm.m10 + m32 * rm.m20 + m33 * rm.m30;

        double am01 = m00 * rm.m01 + m01 * rm.m11 + m02 * rm.m21 + m03 * rm.m31;
        double am11 = m10 * rm.m01 + m11 * rm.m11 + m12 * rm.m21 + m13 * rm.m31;
        double am21 = m20 * rm.m01 + m21 * rm.m11 + m22 * rm.m21 + m23 * rm.m31;
        double am31 = m30 * rm.m01 + m31 * rm.m11 + m32 * rm.m21 + m33 * rm.m31;

        double am02 = m00 * rm.m02 + m01 * rm.m12 + m02 * rm.m22 + m03 * rm.m32;
        double am12 = m10 * rm.m02 + m11 * rm.m12 + m12 * rm.m22 + m13 * rm.m32;
        double am22 = m20 * rm.m02 + m21 * rm.m12 + m22 * rm.m22 + m23 * rm.m32;
        double am32 = m30 * rm.m02 + m31 * rm.m12 + m32 * rm.m22 + m33 * rm.m32;

        double am03 = m00 * rm.m03 + m01 * rm.m13 + m02 * rm.m23 + m03 * rm.m33;
        double am13 = m10 * rm.m03 + m11 * rm.m13 + m12 * rm.m23 + m13 * rm.m33;
        double am23 = m20 * rm.m03 + m21 * rm.m13 + m22 * rm.m23 + m23 * rm.m33;
        double am33 = m30 * rm.m03 + m31 * rm.m13 + m32 * rm.m23 + m33 * rm.m33;
        m00 = am00;
        m01 = am01;
        m02 = am02;
        m03 = am03;
        m10 = am10;
        m11 = am11;
        m12 = am12;
        m13 = am13;
        m20 = am20;
        m21 = am21;
        m22 = am22;
        m23 = am23;
        m30 = am30;
        m31 = am31;
        m32 = am32;
        m33 = am33;
        return this;
    }

    final public void clear(double z) {
        m00 = z;
        m01 = z;
        m02 = z;
        m03 = z;
        m10 = z;
        m11 = z;
        m12 = z;
        m13 = z;
        m20 = z;
        m21 = z;
        m22 = z;
        m23 = z;
        m30 = z;
        m31 = z;
        m32 = z;
        m33 = z;
    }

    // ===========================
    // Rotation Utility
    // ===========================

    /** (create new object) */
    public static MatrixQD getRotMatrix(double xr, double yr, double zr) {
        MatrixQD a = new MatrixQD(0);
        a.qsubstitute(zRot(zr, new MatrixQD(0)));
        a.qmults(yRot(yr, new MatrixQD(0))).qmults(xRot(xr, new MatrixQD(0)));
        return a;
    }

    public static void getRotMatrix(double xr, double yr, double zr,
            MatrixQD arg, MatrixQD tmp) {
        arg.qsubstitute(zRot(zr, tmp));
        arg.qmults(yRot(yr, tmp)).qmults(xRot(xr, tmp));
    }

    /** (create new object) */
    public static MatrixQD getInvRotMatrix(double xr, double yr, double zr) {
        MatrixQD a = new MatrixQD(0);
        a.qsubstitute(xRot(-xr, new MatrixQD(0)));
        a.qmults(yRot(-yr, new MatrixQD(0))).qmults(zRot(-zr, new MatrixQD(0)));
        return a;
    }

    public static void getInvRotMatrix(double xr, double yr, double zr,
            MatrixQD arg, MatrixQD tmp) {
        arg.qsubstitute(xRot(-xr, tmp));
        arg.qmults(yRot(-yr, tmp)).qmults(zRot(-zr, tmp));
    }

    public static MatrixQD xRot(double xr, MatrixQD m) {
        m.clear(0);
        double s, c;
        s = Math.sin(xr);
        c = Math.cos(xr);
        m.set(0, 0, 1);
        m.set(1, 1, c);
        m.set(2, 1, -s);
        m.set(1, 2, s);
        m.set(2, 2, c);
        m.set(3, 3, 1);
        return m;
    }

    public static MatrixQD yRot(double yr, MatrixQD m) {
        m.clear(0);
        double s, c;
        s = Math.sin(yr);
        c = Math.cos(yr);
        m.set(1, 1, 1);
        m.set(2, 2, c);
        m.set(0, 2, -s);
        m.set(2, 0, s);
        m.set(0, 0, c);
        m.set(3, 3, 1);
        return m;
    }

    public static MatrixQD zRot(double zr, MatrixQD m) {
        m.clear(0);
        double s, c;
        s = Math.sin(zr);
        c = Math.cos(zr);
        m.set(2, 2, 1);
        m.set(0, 0, c);
        m.set(1, 0, -s);
        m.set(0, 1, s);
        m.set(1, 1, c);
        m.set(3, 3, 1);
        return m;
    }

    // ===========================
    // Translation Utility
    // ===========================

    /** (create new object) */
    public static MatrixQD getTransMatrix(double x, double y, double z) {
        MatrixQD a = new MatrixQD(0);
        getTransMatrix(x, y, z, a);
        return a;
    }

    /** (re-use given object) */
    public static void getTransMatrix(double x, double y, double z, MatrixQD arg) {
        arg.clear(0);
        arg.set(0, 0, 1);
        arg.set(1, 1, 1);
        arg.set(2, 2, 1);
        arg.set(3, 3, 1);
        arg.set(3, 0, x);
        arg.set(3, 1, y);
        arg.set(3, 2, z);
    }
}
