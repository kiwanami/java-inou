/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Vector1D;
import inou.math.vector.VectorGD;

import java.io.Serializable;

/** This class supports double rectangle */

public class RealRange implements Serializable {
    /** free dimension range */
    private MathVector pos;

    private MathVector size;

    // ========================
    // constructor
    // ========================

    /** free dimension constructor */
    public RealRange(MathVector orginalPosition, MathVector size) {
        this.pos = orginalPosition.getCopy();
        this.size = size.getCopy();
    }

    /** any dimenstion constructor */
    public RealRange(int d) {
        d = Math.abs(d);
        pos = new VectorGD(d);
        size = new VectorGD(d);
    }

    /**
     * 2d constructor
     */
    public RealRange(double x, double y, double width, double height) {
        pos = new VectorGD(2);
        pos.v(0, x);
        pos.v(1, y);
        size = new VectorGD(2);
        size.v(0, Math.abs(width));
        size.v(1, Math.abs(height));
    }

    /**
     * 1d constructor
     */
    public RealRange(double x, double width) {
        pos = new Vector1D(1);
        pos.v(0, x);
        size = new Vector1D(1);
        size.v(0, Math.abs(width));
    }

    /**
     * 3d constructor
     */
    public RealRange(double x, double y, double z, double xsize, double ysize,
            double zsize) {
        pos = new VectorGD(3);
        pos.v(0, x);
        pos.v(1, y);
        pos.v(2, z);
        size = new VectorGD(3);
        size.v(0, Math.abs(xsize));
        size.v(1, Math.abs(ysize));
        size.v(2, Math.abs(zsize));
    }

    public RealRange(RealRange copyFrom) {
        this(copyFrom.pos(), copyFrom.size());
    }

    /** cannot access */
    private RealRange() {
    }

    // ========================
    // standerd access method
    // ========================

    public MathVector pos() {
        return pos;
    }

    public MathVector size() {
        return size;
    }

    /** get original position of this range on d axis */
    public double pos(int d) {
        return pos.v(d);
    }

    /** get size of this range on d axis */
    public double size(int d) {
        return size.v(d);
    }

    /** set original position of this range on d axis */
    public void pos(int d, double p) {
        pos.v(d, p);
    }

    /** set size of this range on d axis */
    public void size(int d, double p) {
        size.v(d, p);
    }

    /** get end position of this range on d axis */
    public double end(int d) {
        return pos.v(d) + size.v(d);
    }

    /** get center position of this range on d axis */
    public double center(int d) {
        return pos.v(d) + size.v(d) / 2;
    }

    /** dimension */
    public int getDimension() {
        return pos.getDimension();
    }

    /** equal check */
    public boolean equals(RealRange r) {
        return (pos.equals(r.pos) && size.equals(r.size));
    }

    public void substitute(RealRange r) {
        pos.substitute(r.pos);
        size.substitute(r.size);
    }

    // ========================
    // utility (short cut)
    // ========================

    /** set x value */
    public void x(double a) {
        pos.v(0, a);
    }

    /** get x value */
    public double x() {
        return pos.v(0);
    }

    /** set y value */
    public void y(double a) {
        pos.v(1, a);
    }

    /** get y value */
    public double y() {
        return pos.v(1);
    }

    /** set z value */
    public void z(double a) {
        pos.v(2, a);
    }

    /** get z value */
    public double z() {
        return pos.v(2);
    }

    /** set width value */
    public void width(double a) {
        size.v(0, a);
    }

    /** get width value */
    public double width() {
        return size.v(0);
    }

    public double ex() {
        if (x() == Double.NEGATIVE_INFINITY && width() == 0)
            return 0;
        return width() + x();
    }

    /** set height value */
    public void height(double a) {
        size.v(1, a);
    }

    /** get height value */
    public double height() {
        return size.v(1);
    }

    public double ey() {
        if (y() == Double.NEGATIVE_INFINITY && height() == 0)
            return 0;
        return height() + y();
    }

    /** set height value */
    public void length(double a) {
        size.v(2, a);
    }

    /** get height value */
    public double length() {
        return size.v(2);
    }

    public double ez() {
        if (z() == Double.NEGATIVE_INFINITY && length() == 0) {
            return 0;
        }
        return length() + z();
    }

    public double centerx() {
        return x() + width() / 2;
    }

    public double centery() {
        return y() + height() / 2;
    }

    public double centerz() {
        return z() + length() / 2;
    }

    /**
     * Check position containing
     * 
     * @param pos
     *            position
     * @return if this range contains given position, return true.
     */
    public boolean contains(MathVector pos) {
        if (pos.getDimension() == getDimension()) {
            for (int i = 0; i < getDimension(); i++) {
                if (pos.v(i) < pos(i) || pos.v(i) >= end(i))
                    return false;
            }
            return true;
        }
        throw new ArithmeticException(
                "Different dimension at RealRange.contain");
    }

    /**
     * make unified range that include both ranges.
     */
    public RealRange getUnion(RealRange r) {
        if (r == null)
            return this;
        int n = getDimension();
        if (n > r.getDimension())
            throw new ArithmeticException("Different dimensions");
        RealRange nr = new RealRange(n);
        for (int i = 0; i < n; i++) {
            double sx = Math.min(pos(i), r.pos(i));
            double ex = Math.max(end(i), r.end(i));
            if (ex < sx)
                throw new ArithmeticException(
                        "Found illegal range at getUnion.");
            nr.pos(i, sx);
            nr.size(i, ex - sx);
        }
        return nr;
    }

    /**
     * make common range.
     */
    public RealRange getCommon(RealRange r) {
        if (r == null)
            return this;
        int n = getDimension();
        if (n != r.getDimension())
            throw new ArithmeticException("Different dimensions");
        RealRange nr = new RealRange(n);
        for (int i = 0; i < n; i++) {
            double sx = Math.max(pos(i), r.pos(i));
            double ex = Math.min(end(i), r.end(i));
            if (ex < sx)
                throw new ArithmeticException(
                        "Cannot find the common valid range.");
            nr.pos(i, sx);
            nr.size(i, ex - sx);
        }
        return nr;
    }

    public String toString() {
        switch (getDimension()) {
        case 0:
            return "";
        case 1:
            return "rect:(" + x() + ")-(" + ex() + ")";
        case 2:
            return "rect:(" + x() + ", " + y() + ")-(" + ex() + ", " + ey()
                    + ")";
        case 3:
            return "rect:(" + x() + ", " + y() + ", " + z() + ")-(" + ex()
                    + ", " + ey() + ", " + ez() + ")";
        }
        return "";
    }

    public RealRange getCopy() {
        return new RealRange(pos.getCopy(), size.getCopy());
    }
}