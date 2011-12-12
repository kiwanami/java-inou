/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

/*
 * This abstract class presents three (maybe more than three) dimensional data.
 * A concrete class should implements FixedDataModel or FunctionDataModel.
 */
public abstract class PlotData3D extends PlotData {

    protected PlotData3D() {
    }

    protected PlotData3D(String title) {
        super(title);
    }

}