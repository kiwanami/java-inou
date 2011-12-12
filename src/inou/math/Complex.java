/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/**
 * Complex number class. This class provides fondamental complex-number
 * operations.
 */
public class Complex implements Serializable {
    /** number of real part */
    public double r;

    /** number of imaginary part */
    public double i;

    // ============================
    // constructor
    // ============================

    /** construct zero complex number */
    public Complex() {
        this(0);
    }

    /** construct the complex number given real number */
    public Complex(double real) {
        this(real, 0);
    }

    /** construct the complex number with given numbers */
    public Complex(double real, double imaginary) {
        r = real;
        i = imaginary;
    }

    /** construct with given complex number */
    public Complex(Complex c) {
        r = c.r;
        i = c.i;
    }

    // ============================
    // complex operator
    // ============================
    private final static double p2 = Math.PI / 2.;

    private final static double dp = Math.PI * 2.;

    public Complex getCopy() {
        return new Complex(this);
    }

    /** get real part */
    public double getReal() {
        return r;
    }

    /** get imaginary part */
    public double getImaginary() {
        return i;
    }

    /** get scalar length */
    public double getLength() {
        return Math.sqrt(r * r + i * i);
    }

    /** get angle [-PI ~ +PI] (radian) */
    public double getAngle() {
        if (r == 0) {
            if (i == 0)
                return 0;// error?
            if (i > 0)
                return p2;
            else
                return -p2;
        }
        double d = Math.atan(i / r);
        if (r >= 0) {
            return d;
        }
        if (i >= 0)
            return Math.PI + d;
        return d + Math.PI;
    }

    /** get square of scalar length */
    public double getSquareLength() {
        return r * r + i * i;
    }

    /** make a new complex conjugate object */
    public Complex getConjugate() {
        return new Complex(r, -i);
    }

    /** substitute complex value */
    public void substitute(Complex c) {
        r = c.r;
        i = c.i;
    }

    /** set complex value */
    public void set(double ar, double ai) {
        r = ar;
        i = ai;
    }

    public String toString() {
        return Double.toString(r) + " + " + Double.toString(i) + "i";
    }

    /** swap complex value */
    public void swap(Complex a) {
        swap(this, a);
    }

    // ============================
    // Argeburaical operation
    // ============================

    /** swap the numbers between a and b */
    public static void swap(Complex a, Complex b) {
        double t;
        t = a.r;
        a.r = b.r;
        b.r = t;
        t = a.i;
        a.i = b.i;
        b.i = t;
    }

    /** addition */
    public Complex add(Complex c) {
        return new Complex(r + c.r, i + c.i);
    }

    /** addition and substitution */
    public Complex adds(Complex c) {
        r += c.r;
        i += c.i;
        return this;
    }

    /** subtraction */
    public Complex sub(Complex c) {
        return new Complex(r - c.r, i - c.i);
    }

    /** subtraction and substitution */
    public Complex subs(Complex c) {
        r -= c.r;
        i -= c.i;
        return this;
    }

    /** multiplication by real number */
    public Complex mult(double d) {
        return new Complex(r * d, i * d);
    }

    /** multiplication by real number and substitution */
    public Complex mults(double d) {
        r *= d;
        i *= d;
        return this;
    }

    /** multiplication */
    public Complex mult(Complex c) {
        return new Complex(r * c.r - i * c.i, r * c.i + i * c.r);
    }

    /** multiplication and substitution */
    public Complex mults(Complex c) {
        double rr = r * c.r - i * c.i;
        double ii = r * c.i + i * c.r;
        r = rr;
        i = ii;
        return this;
    }

    /** division by real number */
    public Complex div(double d) {
        return new Complex(r / d, i / d);
    }

    /** division by real number and substitution */
    public Complex divs(double d) {
        r /= d;
        i /= d;
        return this;
    }

    /** division */
    public Complex div(Complex c) {
        return mult(c.getConjugate()).divs(c.getSquareLength());
    }

    /** division and substitution */
    public Complex divs(Complex c) {
        mults(c.getConjugate()).divs(c.getSquareLength());
        return this;
    }

    /**
     * transform "double" array objects into Complex array object
     * 
     * @param ar
     *            REAL array
     * @param ai
     *            IMAGINARY array
     * @return Complex array
     */
    public static Complex[] translate(double[] ar, double[] ai) {
        Complex[] ac = new Complex[ar.length];
        for (int i = 0; i < ac.length; i++)
            ac[i] = new Complex(ar[i], ai[i]);
        return ac;
    }

    /**
     * transform Complex array object into "double" array object
     * 
     * @param ac
     *            Complex array [index]
     * @return "double" array [0:RE, 1:IM][index]
     */
    public static double[][] translate(Complex[] ac) {
        double[][] ret = new double[2][ac.length];
        for (int i = 0; i < ac.length; i++) {
            ret[0][i] = ac[i].r;
            ret[1][i] = ac[i].i;
        }
        return ret;
    }

    /**
     * transform "double "array object into Complex array object
     * 
     * @param ar
     *            REAL array
     * @return Complex array
     */
    public static Complex[] translate(double[] ar) {
        Complex[] ac = new Complex[ar.length];
        for (int i = 0; i < ac.length; i++)
            ac[i] = new Complex(ar[i], 0);
        return ac;
    }
}