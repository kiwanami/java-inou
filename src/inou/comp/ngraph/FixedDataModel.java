/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.RealRange;

/**
 * This class represents fixed data model, such as array data and some closed
 * function model. This class is responsible to tell data range to a
 * PlotModel.
 */
public interface FixedDataModel {

    /** called by PlotModel during auto scaling */
    public RealRange getDataRange();

}