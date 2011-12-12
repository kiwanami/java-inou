/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.RealRange;

/**
 * This interface represents function data model. This class is responsible to
 * tell function interface and output data range.
 */
public interface FunctionDataModel {

    /**
     * Return output data range. called by PlotModel during auto scaling if
     * the funtion is y=2x and given referenceRange is (0,3), this method should
     * return RealRange(0,0,3,6).
     * 
     * @param referenceRange
     *            reference range to calculate data range
     * @retrun output data range calculated from reference range.
     */
    public RealRange getReferenceRange(RealRange referenceRange);

    /**
     * Return output dimensions. Here x == 0, y == 1, and z == 2. The rest
     * dimensions mean input parameter. For example, if the data model is like
     * 'y=f(x)', getOutputDimension() == {1}. If 'x=f(y,z)', the result is {0}.
     * 
     * @retrun output dimensions.
     */
    public int[] getOutputDimensions();

}