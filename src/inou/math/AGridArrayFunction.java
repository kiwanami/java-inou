/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/**
 * Optimized array access function like AArrayFunction.
 * <ul>
 * <li>Grid access and Linear interpolator
 * <li>exception throwing with access for out of range
 * </ul>
 */

public class AGridArrayFunction extends AFunction implements FiniteRange,
        Serializable {

    private double sx, ex, dx;

    private double[] y;

    private int num; // number of grid points

    private int numm;// number of intervals between grid points (i.e. num-1)

    // ==================================
    // constructor
    // ==================================

    /**
     * Construct a function from array
     * 
     * @param start
     *            start position on x axis
     * @param end
     *            end position on x axis (end-start)/(num-1)
     * @param y
     *            value array
     */
    public AGridArrayFunction(double start, double end, double[] y) {
        sx = start;
        ex = end;
        num = y.length;
        numm = num - 1;
        dx = (end - start) / numm;
        if (dx <= 0) {
            throw new RuntimeException(
                    "Illeagal parameter in AArrayalFunction.");
        }
        this.y = y;
    }

    /**
     * Construct a function from given function
     * 
     * @param start
     *            start position on x axis
     * @param end
     *            end position on x axis
     * @param num
     *            number of fragments
     * @param in
     *            source function
     */
    public AGridArrayFunction(double start, double end, int num, AFunction in) {
        this(start, end, AArrayFunction.toArray(start, end, num, in));
    }

    // ==================================
    // access to property
    // ==================================

    /** @return number of given array */
    public int getNumber() {
        return num;
    }

    public double getStart() {
        return sx;
    }

    public double getEnd() {
        return ex;
    }

    public RealRange getDefinedRange() {
        return new RealRange(sx, ex - sx);
    }

    // ==================================
    // function method
    // ==================================

    /** play as a function */
    public double f(double ix) {
        if (sx > ix || ex < ix) {
            System.out.println("sx:" + sx + "  ex:" + ex + "  dx:" + dx);
            throw new RuntimeException("Out of range : " + ix);
        }
        int index = (int) (ix / dx);
        if (index == numm)
            return y[numm];
        double ratio = (ix - dx * index) / dx;
        return y[index + 1] * ratio + y[index] * (1.0 - ratio);
    }

    /** play as an array */
    public double fi(int n) {
        if (n < 0 || n > num) {
            throw new RuntimeException("Out of range : " + n);
        }
        return y[n];
    }

    // ==================================
    // private area
    // ==================================

    /** test method */
    public static void main(String[] arg) {
        double[] in = { 0, 1, 4, 9 };
        double start = 0;
        double end = 3;
        AGridArrayFunction a = new AGridArrayFunction(start, end, in);
        int num = 10;
        double dx = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            double x = dx * i;
            System.out.println(x + "  " + a.f(x));
        }
    }
}