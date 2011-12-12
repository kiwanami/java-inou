/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;

/**
 * This is a Simple implementation of 3D(+1)-MathVector class.
 */
public class VectorQD extends Vector3D {

    /** vector element */
    // public double x,y,z,t;
    public VectorQD() {
        super();
    }

    /** construct from Vector3D */
    public VectorQD(Vector3D v) {
        super(v.x, v.y, v.z);
    }

    /** construct initial value */
    public VectorQD(double ix, double iy, double iz) {
        super(ix, iy, iz);
    }

    /** construct from some vector */
    public VectorQD(MathVector ex) {
        super();
        for (int i = 0; i < ex.getDimension(); i++)
            v(i, ex.v(i));
    }

    /** make a new copy */
    public MathVector getCopy() {
        return new VectorQD(x, y, z);
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
        if (c == 3)
            return;
        super.v(c, value);
    }

    /**
     * get the value of column c
     * 
     * @param c
     *            column
     */
    final public double v(int c) {
        if (c == 3)
            return 1;
        return super.v(c);
    }

    public static VectorQD getVectorQD(Vector3D vec) {
        return new VectorQD(vec.x, vec.y, vec.z);
    }

    public Vector3D getVector3D() {
        return new Vector3D(x, y, z);
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * multiplies this by a double value and returns result vector. OPERATOR(*)
     */
    final public VectorQD qmult(double s) {
        return new VectorQD(s * x, s * y, s * z);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public VectorQD qmults(double s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.) OPERATOR(*)
     */
    final public VectorQD qmult(MatrixQD mt) {
        return new VectorQD(x * mt.m00 + y * mt.m10 + z * mt.m20 + mt.m30, x
                * mt.m01 + y * mt.m11 + z * mt.m21 + mt.m31, x * mt.m02 + y
                * mt.m12 + z * mt.m22 + mt.m32);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public VectorQD qmults(MatrixQD mt) {
        double tx = x * mt.m00 + y * mt.m10 + z * mt.m20 + mt.m30;
        double ty = x * mt.m01 + y * mt.m11 + z * mt.m21 + mt.m31;
        double tz = x * mt.m02 + y * mt.m12 + z * mt.m22 + mt.m32;
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public VectorQD qadd(VectorQD xv) {
        return new VectorQD(x + xv.x, y + xv.y, z + xv.z);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public VectorQD qadds(VectorQD xv) {
        x += xv.x;
        y += xv.y;
        z += xv.z;
        return this;
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public VectorQD qsub(VectorQD xv) {
        return new VectorQD(x - xv.x, y - xv.y, z - xv.z);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public VectorQD qsubs(VectorQD xv) {
        x -= xv.x;
        y -= xv.y;
        z -= xv.z;
        return this;
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the outer production. << c = a x x >>
     */
    final public VectorQD qouterProduct(VectorQD xv) {
        return new VectorQD(y * xv.z - z * xv.y, z * xv.x - x * xv.z, x * xv.y
                - y * xv.x);
    }

    /**
     * return the outer production. << c = a x x >>
     * 
     * @param xv
     *            argument vector
     * @param ans
     *            result vector
     */
    final public void qouterProduct(VectorQD xv, VectorQD ans) {
        ans.z = x * xv.y - y * xv.x;
        ans.x = y * xv.z - z * xv.y;
        ans.y = z * xv.x - x * xv.z;
    }

}