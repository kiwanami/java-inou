/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;

/**
 * This is a Simple implementation of 4D-MathVector (x,y,z,t) class.
 */
public class Vector4D extends MathVector {

    /** vector element */
    public double x, y, z, t;

    /** construct with zero */
    public Vector4D() {
        x = 0;
        y = 0;
        z = 0;
        t = 0;
    }

    /** construct initial value */
    public Vector4D(double ix, double iy, double iz, double it) {
        x = ix;
        y = iy;
        z = iz;
        t = it;
    }

    public Vector4D(MathVector vec) {
        if (vec instanceof Vector4D) {
            Vector4D v3 = (Vector4D) vec;
            x = v3.x;
            y = v3.y;
            z = v3.z;
            t = v3.t;
        } else {
            x = vec.v(0);
            y = vec.v(1);
            z = vec.v(2);
            t = vec.v(3);
        }
    }

    /** make a new copy */
    public MathVector getCopy() {
        return new Vector4D(x, y, z, t);
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
        case 3:
            t = value;
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
        case 3:
            return t;
        }
        throw new ArithmeticException("out of dimension : " + c);
    }

    /** access to all members */
    final public void set(double x, double y, double z, double t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    public boolean equals(MathVector xv) {
        if (xv instanceof Vector4D)
            return equals((Vector4D) xv);
        return super.equals(xv);
    }

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    final public boolean equals(Vector4D xv) {
        if (xv.x != x || xv.y != y || xv.z != z || xv.t != t)
            return false;
        return true;
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(MathVector xv) {
        if (xv instanceof Vector4D)
            substitute((Vector4D) xv);
        else
            super.substitute(xv);
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(Vector4D xv) {
        x = xv.x;
        y = xv.y;
        z = xv.z;
        t = xv.t;
    }

    /**
     * multiplies this by a double value and returns result vector. OPERATOR(*)
     */
    final public MathVector mult(double s) {
        return new Vector4D(s * x, s * y, s * z, s * t);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(double s) {
        x *= s;
        y *= s;
        z *= s;
        t *= s;
        return this;
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public MathVector add(MathVector xm) {
        if (xm instanceof Vector4D)
            return add((Vector4D) xm);
        return super.add(xm);
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public Vector4D add(Vector4D xv) {
        return new Vector4D(x + xv.x, y + xv.y, z + xv.z, t + xv.t);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public MathVector adds(MathVector xm) {
        if (xm instanceof Vector4D)
            return adds((Vector4D) xm);
        return super.adds(xm);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public Vector4D adds(Vector4D xv) {
        x += xv.x;
        y += xv.y;
        z += xv.z;
        t += xv.t;
        return this;
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public MathVector sub(MathVector xm) {
        if (xm instanceof Vector4D)
            return sub((Vector4D) xm);
        return super.sub(xm);
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public Vector4D sub(Vector4D xv) {
        return new Vector4D(x - xv.x, y - xv.y, z - xv.z, t - xv.t);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public MathVector subs(MathVector xm) {
        if (xm instanceof Vector4D)
            return subs((Vector4D) xm);
        return super.subs(xm);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public Vector4D subs(Vector4D xv) {
        x -= xv.x;
        y -= xv.y;
        z -= xv.z;
        t -= xv.t;
        return this;
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(MathVector xm) {
        if (xm instanceof Vector4D)
            return innerProduct((Vector4D) xm);
        return super.innerProduct(xm);
    }

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(Vector4D xv) {
        return x * xv.x + y * xv.y + z * xv.z + z * xv.t;
    }

    /** normalize and substitute result for this. */
    final public MathVector normalize() {
        double l = getLength();
        x /= l;
        y /= l;
        z /= l;
        t /= l;
        return this;
    }

    // =====================
    // Vector values
    // =====================

    /** returns the dimension size. */
    final public int getDimension() {
        return 4;
    }

    /** returns the length of this vector lengh. */
    final public double getLength() {
        return Math.sqrt(x * x + y * y + z * z + t * t);
    }

    /** returns the square value of this vector length */
    final public double getSquare() {
        return x * x + y * y + z * z + t * t;
    }

}