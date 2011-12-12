/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Abstract plotter class. This class don't know how to draw the data and data
 * dimension.
 */

public abstract class Plotter {

    protected PlotData data;

    protected Plotter decorator;

    // ==========================
    // constructor
    // ==========================

    /** construct from plot data */
    protected Plotter(PlotData p) {
        data = p;
    }

    /** construct from given plotter (decoration) */
    protected Plotter(Plotter p) {
        this(p.getData());
        decorator = p;
    }

    // ==========================
    // access
    // ==========================

    public PlotData getData() {
        return data;
    }

    // ==========================
    // frame work
    // ==========================

    /**
     * Refresh plotter internal cache data. If a subclass manages some internal
     * data, such as internal state or cache data dependent on PlotData, the
     * subclass should override this method and refresh the internal data.
     * Default implementation calls cascaded Plotter's method or
     * PlotData.updateData().
     */
    protected void updateData() {
        if (decorator != null) {
            decorator.updateData();
        } else {
            data.updateData();
        }
    }

    /**
     * Start drawing contents. This method is called by PlotRenderer during
     * drawing
     */
    protected MathVector[] urgeDraw(RenderingInfo info) {
        MathVector[] vertexArray = null;
        if (decorator != null) {
            vertexArray = decorator.urgeDraw(info);
        }
        if (vertexArray == null) {
            // this is first time!
            vertexArray = data.getArray(info.getPlotContext().getActiveRange());
        }
        if (vertexArray == null) {
            // no data to draw
            return null;
        }

        draw(info, vertexArray);
        return vertexArray;
    }

    /**
     * Framework of drawing contents. Sunclass should override this method to
     * draw contents.
     */
    protected abstract void draw(RenderingInfo info, MathVector[] vertexArray);

    /**
     * Start drawing a legend picture. This method is called by PlotRenderer
     * during drawing.
     */
    protected final void urgeDrawLegend(Graphics g, Rectangle r) {
        if (decorator != null) {
            decorator.urgeDrawLegend(g, r);
        }
        drawLegend(g, r);
    }

    /**
     * called by PlotRenderer to determine height of legend picture.
     */
    protected final int getLegendHeightGen(Graphics g) {
        int height = 0;
        if (decorator != null) {
            height = Math.max(decorator.getLegendHeightGen(g), height);
        }
        return Math.max(height, getLegendHeight(g));
    }

    /**
     * Framework make a space to draw the data name on the legend area. If a
     * plotter needs larger space to draw the legend than the string height
     * given by framework, override and return proper value.
     */
    protected int getLegendHeight(Graphics g) {
        return 0;
    }

    /**
     * Start calculating legend height. called by PlotRenderer to determine
     * width of legend
     */
    protected final int getLegendWidthGen(Graphics g) {
        int width = 0;
        if (decorator != null) {
            width = Math.max(decorator.getLegendWidthGen(g), width);
        }
        return Math.max(width, getLegendWidth(g));
    }

    /**
     * Framework make a space to draw a legend symbol. If a plotter needs larger
     * space to draw the legend than the space given by framework, override and
     * return proper value.
     */
    protected int getLegendWidth(Graphics g) {
        return 0;
    }

    /**
     * Framework of drawing a legend picture. Subclass should override this
     * method and draw a legend picture in given rectangle.
     */
    protected abstract void drawLegend(Graphics g, Rectangle r);

}