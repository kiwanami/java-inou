/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.PlotObject;
import inou.comp.ngraph.RenderingInfo;
import inou.comp.ngraph.RenderingInfo2D;

public abstract class PlotObject2D implements PlotObject {

    protected abstract void drawObject2D(RenderingInfo2D info);

    final public void drawObject(RenderingInfo info) {
        drawObject2D((RenderingInfo2D) info);
    }

}