/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.vector;

import inou.math.MathVector;
import inou.math.Matrix;

/**
 * This is a Simple implementation of 2D-MathVector class.
 */
public class Vector2D extends MathVector {

    /** vector element */
    public double x, y;

    /** construct with zero */
    public Vector2D() {
        x = 0;
        y = 0;
    }

    /** construct initial value */
    public Vector2D(double ix, double iy) {
        x = ix;
        y = iy;
    }

    public Vector2D(MathVector vec) {
        if (vec instanceof Vector2D) {
            Vector2D v2 = (Vector2D) vec;
            x = v2.x;
            y = v2.y;
        } else {
            x = vec.v(0);
            y = vec.v(1);
        }
    }

    /** make a new copy */
    final public MathVector getCopy() {
        return new Vector2D(x, y);
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
        switch (c) {
        case 0:
            x = value;
            return;
        case 1:
            y = value;
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
    final public double v(int c) {
        switch (c) {
        case 0:
            return x;
        case 1:
            return y;
        }
        throw new ArithmeticException("out of dimension : " + c);
    }

    /** access to all members */
    final public void set(double ix, double iy) {
        x = ix;
        y = iy;
    }

    // =====================================
    // Mathematiocal foundamental operation
    // =====================================

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    final public boolean equals(Vector2D xv) {
        if (xv.x != x || xv.y != y)
            return false;
        return true;
    }

    /**
     * checks whether given vector equals or not. OPERATOR(==)
     */
    final public boolean equals(MathVector xv) {
        if (xv instanceof Vector2D)
            return equals((Vector2D) xv);
        return super.equals(xv);
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(Vector2D xv) {
        x = xv.x;
        y = xv.y;
    }

    /**
     * let this vector values be the same as given vector. OPERATOR(=)
     */
    final public void substitute(MathVector xv) {
        if (xv instanceof Vector2D)
            substitute((Vector2D) xv);
        else
            super.substitute(xv);
    }

    /**
     * multiplies this by a double value and returns result vector. OPERATOR(*)
     */
    final public MathVector mult(double s) {
        return new Vector2D(s * x, s * y);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(double s) {
        x *= s;
        y *= s;
        return this;
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.) OPERATOR(*)
     */
    final public Vector2D mult(Matrix2D mt) {
        return new Vector2D(x * mt.m00 + y * mt.m10, x * mt.m01 + y * mt.m11);
    }

    /**
     * multiplies matrix from RIGHT-hand and return the result vector. (This
     * vector value would not be changed.) OPERATOR(*)
     */
    final public MathVector mult(Matrix xv) {
        if (xv instanceof Matrix2D)
            return mult((Matrix2D) xv);
        return super.mult(xv);
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public Vector2D mults(Matrix2D mt) {
        double tx = x * mt.m00 + y * mt.m10;
        double ty = x * mt.m01 + y * mt.m11;
        x = tx;
        y = ty;
        return this;
    }

    /**
     * operate and substitute result for this. OPERATOR(*=)
     */
    final public MathVector mults(Matrix xv) {
        if (xv instanceof Matrix2D)
            return mults((Matrix2D) xv);
        return super.mults(xv);
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public Vector2D add(Vector2D xv) {
        return new Vector2D(x + xv.x, y + xv.y);
    }

    /**
     * adds some vector to this and returns result vector. OPERATOR(+)
     */
    final public MathVector add(MathVector xv) {
        if (xv instanceof Vector2D)
            return add((Vector2D) xv);
        return super.add(xv);
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public Vector2D adds(Vector2D xv) {
        x += xv.x;
        y += xv.y;
        return this;
    }

    /**
     * operate and substitute result for this. OPERATOR(+=)
     */
    final public MathVector adds(MathVector xv) {
        if (xv instanceof Vector2D)
            return adds((Vector2D) xv);
        return super.adds(xv);
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public Vector2D sub(Vector2D xv) {
        return new Vector2D(x - xv.x, y - xv.y);
    }

    /**
     * subtracts some vector from this and returns result vector. OPERATOR(=)
     */
    final public MathVector sub(MathVector xv) {
        if (xv instanceof Vector2D)
            return sub((Vector2D) xv);
        return super.sub(xv);
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public Vector2D subs(Vector2D xv) {
        x -= xv.x;
        y -= xv.y;
        return this;
    }

    /**
     * operate and substitute result for this. OPERATOR(-=)
     */
    final public MathVector subs(MathVector xv) {
        if (xv instanceof Vector2D)
            return subs((Vector2D) xv);
        return super.subs(xv);
    }

    // =====================
    // Vector operation
    // =====================

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(Vector2D xv) {
        return x * xv.x + y * xv.y;
    }

    /**
     * return the inner production. << c = <a|x> >>
     */
    final public double innerProduct(MathVector xv) {
        if (xv instanceof Vector2D)
            return innerProduct((Vector2D) xv);
        return super.innerProduct(xv);
    }

    /**
     * return the outer production. << c = a x x >>
     */
    final public Vector1D outerProduct(Vector2D xv) {
        return new Vector1D(x * xv.y - y * xv.x);
    }

    /**
     * return the outer production. << c = a x x >>
     */
    final public MathVector outerProduct(MathVector xv) {
        if (xv instanceof Vector2D)
            return outerProduct((Vector2D) xv);
        return super.outerProduct(xv);
    }

    /** normalize and substitute result for this. */
    final public MathVector normalize() {
        double z = getLength();
        x /= z;
        y /= z;
        return this;
    }

    final public MathVector zero() {
        x = 0;
        y = 0;
        return this;
    }

    final public Vector2D getNormalVector() {
        Vector2D nv = new Vector2D();
        getNormalVector(this, nv);
        nv.normalize();
        return nv;
    }

    final public static void getNormalVector(MathVector gen, MathVector norm) {
        norm.v(0, gen.v(1));
        norm.v(1, -gen.v(0));
    }

    // =====================
    // Vector values
    // =====================

    /** returns the dimension size. */
    final public int getDimension() {
        return 2;
    }

    /** returns the length of this vector lengh. */
    final public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    /** returns the square value of this vector length */
    final public double getSquare() {
        return x * x + y * y;
    }

}