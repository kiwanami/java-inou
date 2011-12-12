/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

/**
 * This abstract class presents two dimensional data. A concrete class should
 * implements FixedDataModel or FunctionDataModel.
 */
public abstract class PlotData2D extends PlotData {

    protected PlotData2D() {
    }

    protected PlotData2D(String title) {
        super(title);
    }

}