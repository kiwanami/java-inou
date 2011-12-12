/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;

/**
 * Manage the plot data and plotter. n-dimensional points and data name. This
 * class doesn't decide the way to have a data and dimension. A concrete class
 * should implements FixedDataModel or FunctionDataModel.
 */

public abstract class PlotData {

    protected String dataname;

    // ==========================
    // contructor
    // ==========================

    /** sub-class can use this default constructor */
    protected PlotData() {
        this(UPlotData.getDefaultDataName());
    }

    protected PlotData(String a) {
        dataname = a;
    }

    // ==========================
    // access method
    // ==========================

    public void setDataName(String t) {
        dataname = t;
    }

    public String getDataName() {
        return dataname;
    }

    // ==========================
    // frame work area
    // ==========================

    /**
     * Return data array to plot. This method will be called by Plotter to paint
     * data.
     * 
     * @param r
     *            active range to show on the plot area.
     * @retrun data array to plot. Plotter object decides whether should plot or
     *         not.
     */
    abstract public MathVector[] getArray(RealRange r);

    /**
     * Refresh data. PlotModel order to refresh data through
     * Plotter.updateData(). If a subclass holds some cache data, override this
     * method and refresh data.
     */
    protected void updateData() {
    }
}