/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public abstract class AbstractPlotRenderer implements PlotRenderer {

    private PlotComponent plotComponent;

    private PlotModel plotContext;

    protected AbstractPlotRenderer(PlotModel context) {
        this.plotContext = context;
        this.plotContext.addRenderer(this);
    }

    final public PlotModel getPlotContext() {
        return plotContext;
    }

    final protected PlotComponent getPlotComponent() {
        return plotComponent;
    }

    final public void setParentComponent(PlotComponent plotComponent) {
        this.plotComponent = plotComponent;
    }

    final public void update() {
        plotComponent.repaintOrder();
    }

}