/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface PlotRenderer {

    /**
     * This method is called by parent component (PlotComponent) to draw
     * contents on Graphics.
     * 
     * @param g
     *            graphics context
     * @param wholeArea
     *            rectangle to draw
     */
    public void paint(Graphics g, Rectangle wholeArea);

    /**
     * PlotModel calls this method to update contents on Graphics. This object
     * must call PlotComponent.updateOrder() of parent component.
     */
    public void update();

    public void setParentComponent(PlotComponent com);
}