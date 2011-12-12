/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector2D;

/**
 * Manage the plot data. X-Y points and data name. This data depends on order.
 * (Given data will not be sorted.)
 */

public class ParametricData2D extends AbstractFixedData2D {

    // ==========================
    // contructor
    // ==========================

    /**
     * sub-class can use this default constructor. But must call
     * "setData(MathVector [] d)" method. (it set max and min datas (they are
     * refered when auto scale))
     */
    protected ParametricData2D() {
        super();
    }

    /** 2D Vector data */
    public ParametricData2D(Vector2D[] d) {
        super(d);
    }

    /** 2D data */
    public ParametricData2D(double[] x, double[] y) {
        super(x, y);
    }

    // ==========================
    // operation
    // ==========================

    /** called by Layer to paint data */
    public MathVector[] getArray(RealRange r) {
        return getWholeData();
    }

}