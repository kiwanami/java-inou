/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/**
 * This class supports some utilities between function and array object. <br>
 * [SERVICE]
 * <ol>
 * <li> make a function from given array. <br>
 * (you can choose interpolation argorythm. LinearInterpolater used as default.)
 * <li>.make an array object from given function.
 * </ol>
 * 
 * And this class has 2 switches, [periodical] and [zero]. <br>
 * (In default, [periodical] is off and [zero] is on.) [periodical]: play
 * periodical function <br>
 * [zero] : return 0 in the out of given range. <br>
 * If both switches are on, [zero] has priority. And if both are off and the
 * value out of range, return 0 and print error message.
 */

public class AArrayFunction extends AFunction implements FiniteRange,
        Serializable {

    protected double sx, ex;

    protected double[] y;

    protected double[] x;

    protected boolean period = false;

    protected boolean zero = true;

    protected Interpolater interpolater;

    // ==================================
    // constructor
    // ==================================

    /**
     * Construct a function from array
     * 
     * @param start
     *            start position on x axis
     * @param ph
     *            fragment size
     * @param y
     *            value array
     */
    public AArrayFunction(double start, double ph, double[] y) {
        sx = start;
        if (ph <= 0) {
            ph *= -1;
            sx = start - ph * (y.length - 1);
        }
        x = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            x[i] = sx + ph * i;
        }
        this.y = y;
        ex = sx + ph * (y.length - 1);
        commonInit();
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
    public AArrayFunction(double start, double end, int num, AFunction in) {
        this(start, (end - start) / (num - 1), toArray(start, end, num, in));
    }

    /**
     * Construct a function from given array
     * 
     * @param x
     *            data array
     * @param y
     *            data array
     */
    public AArrayFunction(double[] x, double[] y) {
        if (x.length != y.length)
            System.out.println("AArrayalFunction:Different"
                    + " between numbers of given array.");
        this.x = x;
        this.y = y;
        sx = x[0];
        ex = x[x.length - 1];
        commonInit();
    }

    /**
     * Construct a function from given MathVector array
     * 
     * @param m
     *            data array
     */
    public AArrayFunction(MathVector[] m) {
        int num = m.length;
        x = new double[num];
        y = new double[num];
        for (int i = 0; i < num; i++) {
            x[i] = m[i].v(0);
            y[i] = m[i].v(1);
        }
        sx = x[0];
        ex = x[x.length - 1];
        commonInit();
    }

    private void commonInit() {
        interpolater = new LinearInterpolater(x, y);
    }

    // ==================================
    // access to property
    // ==================================

    /**
     * Periodical switch (default = false)
     * 
     * @param b
     *            if true, this class becomes periodical function.
     */
    public void setPeriodical(boolean b) {
        period = b;
    }

    /**
     * Zero switch (default = true [so, no warning]) This swtich make a value of
     * out of range zero.
     * 
     * @param b
     *            if true, this class returns zero in the out of range.
     */
    public void setZero(boolean b) {
        zero = b;
    }

    /** @return number of given array */
    public int getNumber() {
        return x.length;
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

    public void setInterpolater(Interpolater in) {
        interpolater = in;
        interpolater.update(x, y);
    }

    public void updateArray() {
        interpolater.update(x, y);
    }

    public double[][] getArrays() {
        double[][] g = new double[2][];
        g[0] = x;
        g[1] = y;
        return g;
    }

    // ==================================
    // function method
    // ==================================

    /** play as a function */
    public double f(double ix) {
        if (sx > ix || ex < ix) {
            if (zero)
                return 0;
            if (!period) {
                System.out.println("Illeagal input in AArrayalFunction.");
                return 0;
            }
        }
        return interpolater.get(ix);
    }

    /** play as an array */
    public double fi(int n) {
        if (period)
            return y[n % y.length];
        if (zero && (n < 0 || n > y.length))
            return 0;
        return y[n];
    }

    /** get x value from array */
    public double ix(int n) {
        if (period)
            return x[n % x.length];
        if (zero && (n < 0 || n > x.length))
            return 0;
        return x[n];
    }

    // ==================================
    // utility
    // ==================================

    /**
     * make array from given function
     * 
     * @param start
     *            start position on x axis
     * @param end
     *            end position on x axis
     * @param num
     *            number of fragments
     * @param in
     *            source function
     * @return result array
     */
    public static double[] toArray(double start, double end, int num,
            AFunction in) {
        double[] rets = new double[num];
        double hf = (end - start) / num;
        double x;
        for (int i = 0; i < num; i++) {
            x = start + hf * i;
            rets[i] = in.f(x);
        }
        return rets;
    }

    /**
     * make array from given function
     * 
     * @param start
     *            start position on x axis
     * @param end
     *            end position on x axis
     * @param num
     *            number of fragments
     * @param in
     *            source function
     * @return result array [dimension][sampling points]
     */
    public static double[][] toArrays(double start, double end, int num,
            AFunction in) {
        double[][] rets = new double[2][num];
        double hf = (end - start) / num;
        double x;
        for (int i = 0; i < num; i++) {
            x = start + hf * i;
            rets[0][i] = x;
            rets[1][i] = in.f(x);
        }
        return rets;
    }

    // ==================================
    // private area
    // ==================================

    /** test method */
    public static void main(String[] arg) {
        double[] in = { 0, 1, 4, 9 };
        AArrayFunction a = new AArrayFunction(0, 1, in);
        double x = 0;
        for (int i = 0; i < 10; i++) {
            x = 0.3 * i;
            System.out.println("x=" + x + " f=" + a.f(x));
        }
    }
}