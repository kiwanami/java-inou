/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

/*
 * This abstract class presents 4 dimensional data. A concrete class should
 * implements FixedDataModel or FunctionDataModel.
 */
public abstract class PlotData4D extends PlotData {

    protected PlotData4D() {
    }

    protected PlotData4D(String title) {
        super(title);
    }

}