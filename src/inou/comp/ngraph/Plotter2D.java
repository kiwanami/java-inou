/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

public abstract class Plotter2D extends Plotter {

    private boolean background = false;

    protected Plotter2D(PlotData data) {
        this(data, false);
    }

    protected Plotter2D(PlotData data, boolean background) {
        super(data);
        this.background = background;
    }

    protected Plotter2D(Plotter plotter) {
        this(plotter, false);
    }

    protected Plotter2D(Plotter plotter, boolean background) {
        super(plotter);
        this.background = background;
    }

    boolean isBackground() {
        return background;
    }

    protected final void draw(RenderingInfo info, MathVector[] array) {
        draw2D((RenderingInfo2D) info, array);
    }

    abstract protected void draw2D(RenderingInfo2D renderer, MathVector[] array);

}