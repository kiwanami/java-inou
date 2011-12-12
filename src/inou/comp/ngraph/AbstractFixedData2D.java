/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathUtil;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector2D;

/**
 * 2D plot data. X-Y points and data name.
 */

public abstract class AbstractFixedData2D extends PlotData2D implements
        FixedDataModel {

    private MathVector[] data;

    private RealRange dataRange; // for cache

    // if reverseAxis = true, Y is primary axis.
    private boolean reverseAxis = false;

    // ==========================
    // contructor
    // ==========================

    /**
     * sub-class can use this default constructor. But must call
     * "setData(MathVector [] d)" method. (it set max and min data (they are
     * refered when auto scale))
     */
    protected AbstractFixedData2D() {
    }

    /** 2D Vector data */
    protected AbstractFixedData2D(Vector2D[] d) {
        init(d);
    }

    /**
     * 2D Vector data and reverse switch
     * 
     * @param d
     *            data array
     * @param reverse
     *            if true, Y becomes a primary axis. (Default: false)
     */
    protected AbstractFixedData2D(Vector2D[] d, boolean reverse) {
        reverseAxis = reverse;
        init(d);
    }

    /** 2D data */
    protected AbstractFixedData2D(double[] x, double[] y) {
        init(x, y);
    }

    /**
     * 2D data
     * 
     * @param x
     *            data array
     * @param y
     *            data array
     * @param reverse
     *            if true, Y becomes a primary axis. (Default: false)
     */
    protected AbstractFixedData2D(double[] x, double[] y, boolean reverse) {
        reverseAxis = reverse;
        init(x, y);
    }

    // ==========================
    // access method
    // ==========================

    /**
     * Change the primary axis, with which axis the data array will be sorted.
     * 
     * @param rev
     *            if true, Y becomes a primary axis. (Default: false)
     */
    public final void setReverseAxis(boolean rev) {
        reverseAxis = rev;
        normalize();
    }

    /** chenge or set new data */
    public final void setData(Vector2D[] d) {
        init(d);
    }

    /** chenge or set new data */
    public final void setData(double[] xx, double[] yy) {
        init(xx, yy);
    }

    protected final MathVector[] getWholeData() {
        return data;
    }

    // ==========================
    // operation
    // ==========================

    public final RealRange getDataRange() {
        if (data == null)
            return null;
        return dataRange;
    }

    /** called by Layer to paint data */
    public MathVector[] getArray(RealRange r) {
        if (r == null || dataRange == null) {
            return data;
        }

        double start = r.pos(getPrimaryDimension());
        double end = r.end(getPrimaryDimension());

        // invaild range?
        if (start > dataRange.end(getPrimaryDimension())
                || end < dataRange.pos(getPrimaryDimension()))
            return null;

        // is whole area?
        if (start <= dataRange.pos(getPrimaryDimension())
                && end >= dataRange.end(getPrimaryDimension()))
            return data;

        // make new array object
        MathVector[] rets;
        int sx = 0, ex = data.length - 1;
        // start x search
        if (start > dataRange.pos(getPrimaryDimension())) {
            sx = MathUtil.binarySearchN(data, start, getPrimaryDimension());
            if (sx < 0)
                sx = 0;
        }
        // terminal x search
        if (end < dataRange.end(getPrimaryDimension())) {
            ex = MathUtil.binarySearchN(data, end, getPrimaryDimension()) - 1;
            if (ex >= data.length)
                ex = data.length - 1;
        }
        //
        int num = ex - sx + 1;
        rets = new MathVector[num];
        for (int i = 0; i < num; i++) {
            rets[i] = data[sx + i];
        }
        return rets;
    }

    // ==========================
    // private area
    // ==========================

    protected int getPrimaryDimension() {
        return (reverseAxis) ? 1 : 0;
    }

    /** make vector data and initialize */
    protected final void init(double[] x, double[] y) {
        int num = x.length;
        if (y.length != num) {
            System.err.println("bad input data array.");
            // do nothing?
        }
        Vector2D[] d = new Vector2D[num];
        for (int i = 0; i < num; i++)
            d[i] = new Vector2D(x[i], y[i]);
        init(d);
    }

    /** initialize and normalize */
    protected final void init(MathVector[] d) {
        data = d;
        normalize();
    }

    protected final void updateData() {
        normalize();
    }

    /** normalize data, and store max and minimum data */
    protected final void normalize() {
        data = customNormalize(data);
        dataRange = UPlotData.getDataRange(data);
    }

    /**
     * this method is called by normalize(). default implementation has nothing
     * to do.
     */
    protected MathVector[] customNormalize(MathVector[] data) {
        // do nothing
        return data;
    }

}