/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.vector.Vector2D;

/**
 * Manage the plot data. X-Y points and data name. Given data will be sorted by
 * data on primary axis.
 */

public class XYData2D extends AbstractFixedData2D {

    // ==========================
    // contructor
    // ==========================

    protected XYData2D() {
    }

    /**
     * @param d
     *            2D Points data. This array object is used as an internal
     *            object. if you don't want XYData to modify the array object,
     *            you must give a copy of the array.
     */
    public XYData2D(Vector2D[] d) {
        super(d);
    }

    /**
     * 2D Vector data and reverse switch
     * 
     * @param d
     *            2D Points data. This array object is used as an internal
     *            object. if you don't want XYData to modify the array object,
     *            you must give a copy of the array.
     * @param reverse
     *            if true, Y becomes a primary axis. (Default: false)
     */
    public XYData2D(Vector2D[] d, boolean reverse) {
        super(d, reverse);
    }

    /**
     * XYData class copies the given array into internal data array.
     */
    public XYData2D(double[] x, double[] y) {
        super(x, y);
    }

    /**
     * XYData class copies the given array into internal data array.
     * 
     * @param x
     *            data array
     * @param y
     *            data array
     * @param reverse
     *            if true, Y becomes a primary axis. (Default: false)
     */
    public XYData2D(double[] x, double[] y, boolean reverse) {
        super(x, y, reverse);
    }

    /**
     * @param x
     *            1D data
     */
    public XYData2D(double[] y) {
        int num = y.length;
        double[] x = new double[num];
        for (int i = 0; i < num; i++)
            x[i] = i;
        init(x, y);
    }

    // ==========================
    // private area
    // ==========================

    /** sort data, and store max and minimum data */
    protected MathVector[] customNormalize(MathVector[] data) {
        sort(data);
        return data;
    }

    /** bubble sort (but if given sorted arrays, work only n-loop) */
    private void sort(MathVector[] data) {
        boolean finish;
        // bubble sort!!
        for (int i = 0; i < (data.length - 1); i++) {
            finish = true;
            for (int j = i + 1; j < data.length; j++) {
                if (data[i].v(getPrimaryDimension()) > data[j]
                        .v(getPrimaryDimension())) {
                    swap(data, i, j);
                    finish = false;
                }
            }
            if (finish)
                return;
        }
    }

    /** called by sort() */
    private void swap(MathVector[] a, int i, int j) {
        MathVector t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}