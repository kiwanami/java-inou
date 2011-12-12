/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.RealRange;

import java.awt.Component;

public interface PlotComponent {

    public void repaintOrder();

    public Component getComponent();

    /**
     * @param renderer
     *            PlotRenderer object
     * @param configRatio
     *            configuration ratio that must be 2D object.
     * 
     * <pre>
     *      configRatio.x : 0.0(left) - 1.0(right)
     *      configRatio.y : 0.0(top) - 1.0(bottom)
     *      configRatio.width : 0.0(zero) - 1.0(full width)
     *      configRatio.height : 0.0(zero) - 1.0(full height)
     * </pre>
     */
    public void addRenderer(PlotRenderer renderer, RealRange configRatio);

    public void addRenderer(PlotRenderer renderer);

}