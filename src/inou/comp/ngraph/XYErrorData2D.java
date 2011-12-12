/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.vector.VectorGD;

/**
 * Manage the plot data. X-Y points, error and data name. Given data will be
 * sorted by data on primary axis.
 */

public class XYErrorData2D extends XYData2D implements ErrorBarDataModel {

    public XYErrorData2D(double[] xs, double[] ys, double[] errors) {
        if (xs == null || ys == null || errors == null || xs.length == 0
                || ys.length == 0 || errors.length == 0
                || xs.length != ys.length || xs.length != errors.length) {
            throw new IllegalArgumentException("Invalid error input. : ue:"
                    + xs + ", le:" + ys + ", er:" + errors);
        }
        VectorGD[] array = new VectorGD[xs.length];
        for (int i = 0; i < array.length; i++) {
            VectorGD row = new VectorGD(4);
            row.v(0, xs[i]);
            row.v(1, ys[i]);
            row.v(2, errors[i] / 2.);
            row.v(3, errors[i] / 2.);
            array[i] = row;
        }
        init(array);
    }

    public XYErrorData2D(double[] xs, double[] ys, double[] upperErrors,
            double[] lowerErrors) {
        if (xs == null || ys == null || upperErrors == null
                || lowerErrors == null || xs.length == 0 || ys.length == 0
                || upperErrors.length == 0 || lowerErrors.length == 0
                || xs.length != ys.length || xs.length != upperErrors.length
                || xs.length != lowerErrors.length) {
            throw new IllegalArgumentException("Invalid error input. : ue:"
                    + xs + ", le:" + ys + ", er:" + upperErrors + " , "
                    + lowerErrors);
        }
        VectorGD[] array = new VectorGD[xs.length];
        for (int i = 0; i < array.length; i++) {
            VectorGD row = new VectorGD(4);
            row.v(0, xs[i]);
            row.v(1, ys[i]);
            row.v(2, upperErrors[i]);
            row.v(3, lowerErrors[i]);
            array[i] = row;
        }
        init(array);
    }

    public int getUpperErrorDimension() {
        return 2;
    }

    public int getLowerErrorDimension() {
        return 3;
    }

}