/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

public abstract class Plotter3D extends Plotter {

    protected Plotter3D(PlotData data) {
        super(data);
    }

    protected Plotter3D(Plotter plotter) {
        super(plotter);
    }

    protected final void draw(RenderingInfo info, MathVector[] array) {
        draw3D((RenderingInfo3D) info, array);
    }

    abstract protected void draw3D(RenderingInfo3D info, MathVector[] array);

}