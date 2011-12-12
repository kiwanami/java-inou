/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;

/**
 * Manage the plot data. X-Y-Z points and data name.
 */

public class XYZData3D extends PlotData3D implements FixedDataModel {

    private MathVector[] positions;

    private RealRange dataRange;

    /**
     * @param d
     *            3D Points data. This array object is used as an internal
     *            object. if you don't want XYZData to modify the array object,
     *            you must give a copy of the array.
     */
    public XYZData3D(MathVector[] d) {
        positions = d;
        updateData();
    }

    /**
     * XYZData class copies the given array into internal data array.
     */
    public XYZData3D(double[] x, double[] y, double[] z) {
        if (x.length != y.length || y.length != z.length) {
            throw new IllegalArgumentException(
                    "Wrong numbers of the given arrays.");
        }
        positions = new MathVector[x.length];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Vector3D(x[i], y[i], z[i]);
        }
        updateData();
    }

    public void updateData(MathVector[] d) {
        if (positions == null || d.length != positions.length) {
            positions = UPlotData.copy(d);
        } else {
            for (int i = 0; i < d.length; i++) {
                positions[i].substitute(d[i]);
            }
        }
        updateData();
    }

    public final RealRange getDataRange() {
        if (positions == null)
            return null;
        return dataRange;
    }

    protected final void updateData() {
        dataRange = UPlotData.getDataRange(positions);
    }

    /** called by Layer to paint data */
    public MathVector[] getArray(RealRange r) {
        return positions;
    }

}