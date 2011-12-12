/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;
import inou.math.Matrix;

/**
 * This is a Simple implementation of 3D-MathVector class.
 */
public class Vector3D extends MathVector {

    /** vector element */
    public double x, y, z;

    /** construct with zero */
    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    /** construct initial value */
    public Vector3D(double ix, double iy, double iz) {
        x = ix;
        y = iy;
        z = iz;
    }

    public Vector3D(MathVector vec) {
        if (vec instanceof Vector3D) {
            Vector3D v3 = (Vector3D) vec;
            x = v3.x;
            y = v3.y;
            z = v3.z;
        } else {
            x = vec.v(0);
            y = vec.v(1);
            z = vec.v(2);
        }
    }

    /** make a new copy */
    public MathVector getCopy() {
        return new Vector3D(x, y, z);
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
    public void v(int c, double value) {
        switch (c) {
        case 0:
            x = value;
            return;
        case 1:
            y = value;
            return;
        case 2:
            z = value;
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
    public double v(int c) {
        switch (c) {
        case 0:
            return x;
        case 1:
            return y;
        case 2:
            return z;
        }
        throw new ArithmeticException("out of dimension : " + c);
    }

    /** access to all members */
    final public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    public boolean equals(MathVector xv) {
        if (xv instanceof Vector3D)
            return equals((Vector3D) xv);
        return super.equals(xv);
    }

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    final public boolean equals(Vector3D xv) {
        if (xv.x != x || xv.y != y || xv.z != z)
            return false;
        return true;
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(MathVector xv) {
        if (xv instanceof Vector3D)
            substitute((Vector3D) xv);
        else
            super.substitute(xv);
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(Vector3D xv) {
        x = xv.x;
        y = xv.y;
        z = xv.z;
    }

    /**
     * multiplies this by a double value and returns result vector. OPERATOR(*)
     */
    final public MathVector mult(double s) {
        return new Vector3D(s * x, s * y, s * z);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(double s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.) OPERATOR(*)
     */
    final public MathVector mult(Matrix xm) {
        if (xm instanceof Matrix3D)
            return mult((Matrix3D) xm);
        return super.mult(xm);
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.) OPERATOR(*)
     */
    final public Vector3D mult(Matrix3D mt) {
        return new Vector3D(x * mt.m00 + y * mt.m10 + z * mt.m20, x * mt.m01
                + y * mt.m11 + z * mt.m21, x * mt.m02 + y * mt.m12 + z * mt.m22);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(Matrix xm) {
        if (xm instanceof Matrix3D)
            return mults((Matrix3D) xm);
        return super.mults(xm);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public Vector3D mults(Matrix3D mt) {
        double tx = x * mt.m00 + y * mt.m10 + z * mt.m20;
        double ty = x * mt.m01 + y * mt.m11 + z * mt.m21;
        double tz = x * mt.m02 + y * mt.m12 + z * mt.m22;
        x = tx;
        y = ty;
        z = tz;
        return this;
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public MathVector add(MathVector xm) {
        if (xm instanceof Vector3D)
            return add((Vector3D) xm);
        return super.add(xm);
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public Vector3D add(Vector3D xv) {
        return new Vector3D(x + xv.x, y + xv.y, z + xv.z);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public MathVector adds(MathVector xm) {
        if (xm instanceof Vector3D)
            return adds((Vector3D) xm);
        return super.adds(xm);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public Vector3D adds(Vector3D xv) {
        x += xv.x;
        y += xv.y;
        z += xv.z;
        return this;
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public MathVector sub(MathVector xm) {
        if (xm instanceof Vector3D)
            return sub((Vector3D) xm);
        return super.sub(xm);
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public Vector3D sub(Vector3D xv) {
        return new Vector3D(x - xv.x, y - xv.y, z - xv.z);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public MathVector subs(MathVector xm) {
        if (xm instanceof Vector3D)
            return subs((Vector3D) xm);
        return super.subs(xm);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public Vector3D subs(Vector3D xv) {
        x -= xv.x;
        y -= xv.y;
        z -= xv.z;
        return this;
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(MathVector xm) {
        if (xm instanceof Vector3D)
            return innerProduct((Vector3D) xm);
        return super.innerProduct(xm);
    }

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(Vector3D xv) {
        return x * xv.x + y * xv.y + z * xv.z;
    }

    /**
     * return the outer production. << c = a x x >>
     */
    final public MathVector outerProduct(MathVector xm) {
        if (xm instanceof Vector3D)
            return outerProduct((Vector3D) xm);
        return super.outerProduct(xm);
    }

    /**
     * return the outer production. << c = a x x >>
     */
    final public Vector3D outerProduct(Vector3D xv) {
        return new Vector3D(y * xv.z - z * xv.y, z * xv.x - x * xv.z, x * xv.y
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
    final public void outerProduct(Vector3D xv, Vector3D ans) {
        ans.z = x * xv.y - y * xv.x;
        ans.x = y * xv.z - z * xv.y;
        ans.y = z * xv.x - x * xv.z;
    }

    /** normalize and substitute result for this. */
    final public MathVector normalize() {
        double l = getLength();
        x /= l;
        y /= l;
        z /= l;
        return this;
    }

    final public MathVector zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    // =====================
    // Vector values
    // =====================

    /** returns the dimension size. */
    final public int getDimension() {
        return 3;
    }

    /** returns the length of this vector lengh. */
    final public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /** returns the square value of this vector length */
    final public double getSquare() {
        return x * x + y * y + z * z;
    }

}