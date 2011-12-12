/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

// import java.util.Arrays;

/** Simple Linear ineterpolater */
public class LinearInterpolater implements Interpolater, Serializable {

    double[] x;

    double[] y;

    LinearInterpolater(double[] x, double[] y) {
        this.x = x;
        this.y = y;
    }

    public void update(double[] xx, double[] yy) {
    }

    public double get(double ix) {
        int i;
        i = MathUtil.binarySearch(x, ix);// FOR JDK1.1
        if (i == 0 || i == -1)
            return y[0];// less than
        if (i == x.length)
            return y[x.length - 1]; // more than
        if (i < -1)
            i = -i - 1; // correct

        double t = (x[i] - ix) / (x[i] - x[i - 1]);
        return (y[i] * (1.0 - t) + y[i - 1] * t);
    }
}