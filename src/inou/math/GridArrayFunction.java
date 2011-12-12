/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Vector1D;
import inou.math.vector.Vector2D;
import inou.math.vector.VectorGD;

import java.io.Serializable;

/**
 * Scalar function wrapper for a general dimension grid array.
 * <ul>
 * <li>Grid access and Linear interpolator
 * <li>exception throwing with access for out of range
 * </ul>
 */
public class GridArrayFunction extends ScalarFunctionClass implements
        FiniteRange, Serializable {

    private MathVector start, end, delta;

    private double[] vals;

    private int[] nums; // number of grid points

    private int[] numms;// number of intervals between grid points

    private int[] offset;// offset of the grid point

    // ==================================
    // constructor
    // ==================================

    /**
     * Construct a function from array
     * 
     * @param _start
     *            start position vector
     * @param _end
     *            end position vector
     * @param _nums
     *            number of the grid points
     * @param _vals
     *            value array [x_i: Sigma_{i=0}^D x_i*(Pi_{j=0}^{i} nums[j])]
     *            for example [x,y,z: x+y*nums[0]+z*nums[0]*nums[1]]
     */
    public GridArrayFunction(MathVector _start, MathVector _end, int[] _nums,
            double[] _vals) {
        super(_start.getDimension());
        if (_start.getDimension() != _end.getDimension()) {
            throw new IllegalArgumentException(
                    "argument dimensions are incorrect. start:"
                            + _start.getDimension() + ", end:"
                            + _end.getDimension());
        }
        int totalSize = 1;
        int[] _numms = new int[_nums.length];
        int[] _offset = new int[_nums.length];
        VectorGD _delta = new VectorGD(_nums.length);
        for (int i = 0; i < _nums.length; i++) {
            totalSize *= _nums[i];
            _numms[i] = _nums[i] - 1;
            if (_numms[i] > 0) {
                _delta.v(i, (_end.v(i) - _start.v(i)) / _numms[i]);
                if (_delta.v(i) <= 0) {
                    throw new RuntimeException(
                            "Illeagal parameter in AArrayalFunction.");
                }
            }
            _offset[i] = (i == 0) ? 1 : (_offset[i - 1] * _nums[i - 1]);
        }
        if (totalSize != _vals.length) {
            throw new IllegalArgumentException(
                    "the number of grid points is incorrect. nums:" + totalSize
                            + ", vals:" + vals.length);
        }

        this.start = VectorUtil.copyVector(_start);
        this.end = VectorUtil.copyVector(_end);
        this.nums = _nums;
        this.numms = _numms;
        this.vals = _vals;
        this.delta = _delta;
        this.offset = _offset;
        initCalculator();
    }

    private Calculator calculator = null;

    private void initCalculator() {
        switch (delta.getDimension()) {
        case 1:
            calculator = new Calculator2D();
            break;
        case 2:
            calculator = new Calculator3D();
            break;
        default:
            calculator = new CalculatorGD();
        }
    }

    private interface Calculator {
        double f(MathVector v);
    }

    private class Calculator2D implements Calculator {
        private double sx, ex, dx;

        private int num, numm;

        Calculator2D() {
            sx = start.v(0);
            ex = end.v(0);
            dx = delta.v(0);
            num = nums[0];
            numm = num - 1;
        }

        public final double f(MathVector ix) {
            double x = ix.v(0);
            if (sx > x || ex < x) {
                outOfRange(ix, 0);
            }
            x -= sx;
            int index = (int) (x / dx);
            if (index == numm)
                return vals[numm];
            double ratio = (x - dx * index) / dx;
            return vals[index + 1] * ratio + vals[index] * (1.0 - ratio);
        }
    }

    private class Calculator3D implements Calculator {
        private double sx, ex, dx;

        private double sy, ey, dy;

        private int numx, nummx, numy, nummy;

        Calculator3D() {
            sx = start.v(0);
            ex = end.v(0);
            dx = delta.v(0);
            numx = nums[0];
            nummx = numx - 1;

            sy = start.v(1);
            ey = end.v(1);
            dy = delta.v(1);
            numy = nums[1];
            nummy = numy - 1;
        }

        public final double f(MathVector ix) {
            double x = ix.v(0);
            double y = ix.v(1);
            if (sx > x || ex < x) {
                outOfRange(ix, 0);
            }
            if (sy > y || ey < y) {
                outOfRange(ix, 1);
            }
            x -= sx;
            y -= sy;
            int indexx = (int) (x / dx);
            int indexy = (int) (y / dy);
            int nindexx = indexx + 1;
            int nindexy = indexy + 1;
            if (indexx == nummx)
                nindexx = nummx;
            if (indexy == nummy)
                nindexy = nummy;

            double ratiox = (x - dx * indexx) / dx;
            double ratioy = (y - dy * indexy) / dy;
            if ((ratiox + ratioy) < 1.0) {
                int addr = indexx + indexy * numx;
                double v0 = vals[addr];

                int naddrx = nindexx + indexy * numx;
                double dvix = vals[naddrx] - v0;

                int naddry = indexx + nindexy * numx;
                double dviy = vals[naddry] - v0;

                return v0 + ratiox * dvix + ratioy * dviy;
            } else {
                int addr = nindexx + nindexy * numx;
                double v0 = vals[addr];

                int naddrx = indexx + nindexy * numx;
                double dvix = v0 - vals[naddrx];

                int naddry = nindexx + indexy * numx;
                double dviy = v0 - vals[naddry];

                return v0 - (1.0 - ratiox) * dvix - (1.0 - ratioy) * dviy;
            }
        }
    }

    private class CalculatorGD implements Calculator {

        public final double f(MathVector ix) {
            MathVector rr = ix.getCopy();
            rr.subs(start);
            int[] nn = new int[nums.length];
            double[] aa = new double[nums.length];
            double alen = 0;
            for (int i = 0; i < nums.length; i++) {
                if (start.v(i) > ix.v(i) || end.v(i) < ix.v(i)) {
                    outOfRange(ix, i);
                }
                nn[i] = (int) (rr.v(i) / delta.v(i));
                aa[i] = (rr.v(i) - delta.v(i) * nn[i]) / delta.v(i);
                alen += aa[i];
            }
            if (alen < 0.5 * nums.length) {
                int addr = 0;
                for (int i = 0; i < nums.length; i++) {
                    addr += offset[i] * nn[i];
                }
                double v0 = vals[addr];
                double ret = v0;
                for (int i = 0; i < nums.length; i++) {
                    int naddr = 0;
                    for (int j = 0; j < nums.length; j++) {
                        if (i == j) {
                            naddr += offset[i] * (nn[i] + 1);
                        } else {
                            naddr += offset[i] * nn[i];
                        }
                    }
                    if (naddr > vals.length)
                        naddr = addr;
                    double dvi = vals[naddr] - v0;
                    ret += aa[i] * dvi;
                }
                return ret;
            } else {
                int addr = 0;
                for (int i = 0; i < nums.length; i++) {
                    nn[i] = Math.min(nn[i] + 1, numms[i]);
                    addr += offset[i] * nn[i];
                }
                double v0 = vals[addr];
                double ret = v0;
                for (int i = 0; i < nums.length; i++) {
                    int naddr = 0;
                    for (int j = 0; j < nums.length; j++) {
                        if (i == j) {
                            naddr += offset[i] * (nn[i] - 1);
                        } else {
                            naddr += offset[i] * nn[i];
                        }
                    }
                    if (naddr < 0)
                        naddr = 0;
                    double dvi = v0 - vals[naddr];
                    ret -= (1.0 - aa[i]) * dvi;
                }
                return ret;
            }
        }
    }

    private void outOfRange(MathVector ix, int i) {
        throw new RuntimeException("Out of range : " + ix + "  (dim:" + i
                + "  sx:" + start.v(i) + "  ex:" + end.v(i) + "  dx:"
                + delta.v(i) + ")");
    }

    // ==================================
    // access to property
    // ==================================

    /**
     * @param i
     *            dimension
     * @return number of given array
     */
    public int getGridNumber(int i) {
        return nums[i];
    }

    public double getStart(int i) {
        return start.v(i);
    }

    public double getEnd(int i) {
        return end.v(i);
    }

    public RealRange getDefinedRange() {
        return new RealRange(start.getCopy(), end.sub(start));
    }

    // ==================================
    // function method
    // ==================================

    /** play as a function */
    public double f(MathVector ix) {
        return calculator.f(ix);
    }

    /** play as an array */
    public double fi(int[] indexes) {
        int addr = indexes[0];
        for (int i = 1; i < nums.length; i++) {
            addr += indexes[i] * nums[i - 1];
        }
        return vals[addr];
    }

    // ==================================
    // private area
    // ==================================

    /** test method */
    public static void main(String[] arg) {
        test2D();
        test3D();
    }

    private static void test2D() {
        double[] in = { 1, 3, 3, 5 };

        int[] nums = { in.length };
        MathVector start = new VectorGD(new double[] { 0 });
        MathVector end = new VectorGD(new double[] { 2 });
        GridArrayFunction a = new GridArrayFunction(start, end, nums, in);

        int num = 10;
        double dx = (end.v(0) - start.v(0)) / (num - 1);
        for (int i = 0; i < num; i++) {
            MathVector x = new Vector1D(dx * i);
            System.out.println(x + " --> " + a.f(x));
        }
        System.out.println("=============");
    }

    private static void test3D() {
        double[] in = { 0, 1, 2, 4 };
        int[] nums = { 2, 2 };
        MathVector start = new VectorGD(new double[] { 0, 0 });
        MathVector end = new VectorGD(new double[] { 1, 1 });
        GridArrayFunction a = new GridArrayFunction(start, end, nums, in);

        int num = 10;

        double dx = (end.v(0) - start.v(0)) / (num - 1);
        for (int i = 0; i < num; i++) {
            MathVector x = new Vector2D(dx * i, 0);
            System.out.println(x + " --> " + a.f(x));
        }
        System.out.println("===========--");

        double dy = (end.v(1) - start.v(1)) / (num - 1);
        for (int i = 0; i < num; i++) {
            MathVector y = new Vector2D(0, dy * i);
            System.out.println(y + " --> " + a.f(y));
        }

        System.out.println("===========--");
        for (int i = 0; i < num; i++) {
            MathVector y = new Vector2D(dy * i, dx * i);
            System.out.println(y + " --> " + a.f(y));
        }
    }
}