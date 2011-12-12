/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ColorSet;
import inou.comp.ngraph.PlotData3D;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SurfaceFunctionData3D;
import inou.comp.ngraph.VectorDataModel;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.Unit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * plot the allow figure of present vector field (This class is for the 2D
 * VectorData f(x,y))
 */

public class GradationVectorPlotter2D extends Plotter2D {

    private VectorDataModel vectorData;

    private ColorSet colorSet = null;

    private boolean autoScale = true;

    private RealRange valueRange = new RealRange(1);

    private Dimension arrowSize = new Dimension();

    private double sizeParameter = 0.8;

    private MathVector[] vectorCache = null;

    public GradationVectorPlotter2D(VectorDataModel data) {
        this(new SurfaceFunctionData3D(new Unit(2)), data);
    }

    public GradationVectorPlotter2D(PlotData3D parentData, VectorDataModel data) {
        super(parentData);
        vectorData = data;
    }

    public GradationVectorPlotter2D(Plotter2D parentPlotter,
            VectorDataModel data) {
        super(parentPlotter);
        vectorData = data;
    }

    public void setAutoScale(boolean b) {
        autoScale = b;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    /**
     * arrow size ratio. ( 0 -- 1.0 : defualt 0.8) The arrow size is calculate
     * from "(sizeParam)*(width)/(number)^0.5". Here width is a smaller value of
     * the width and height of context area, and number is vertex number.
     */
    public void setSizeParameter(double r) {
        if (r <= 1.0 && r > 0) {
            sizeParameter = r;
        }
    }

    public double getSizeParameter() {
        return sizeParameter;
    }

    /**
     * This method cancels auto scaling.
     */
    public void setValueRange(RealRange range) {
        valueRange.substitute(range);
        setAutoScale(false);
    }

    public RealRange getValueRange(double a) {
        return valueRange;
    }

    public void setGradation(ColorSet set) {
        colorSet = set;
    }

    protected void updateData() {
        super.updateData();
        vectorCache = null;
    }

    private void prepareVectors(MathVector[] vertexArray) {
        if (vectorCache == null) {
            vectorCache = vectorData.getVectors(vertexArray);
            autoScaling();
        }
        if (colorSet == null) {
            colorSet = new ColorSet(ColorSet.rainbowIndex, 32);
        }
    }

    private void autoScaling() {
        if (autoScale) {
            double maxLength = 0;
            double minLength = Double.MAX_VALUE;
            for (int i = 0; i < vectorCache.length; i++) {
                double length = vectorCache[i].getLength();
                if (maxLength < length) {
                    maxLength = length;
                }
                if (minLength > length) {
                    minLength = length;
                }
            }
            valueRange.x(minLength);
            valueRange.width(maxLength - minLength);
        }
    }

    private void calculateArrowSize(RenderingInfo2D info) {
        double n = Math.sqrt(vectorCache.length);
        double width = info.getContentArea().width / n;
        double height = info.getContentArea().height / n;
        arrowSize.setSize((int) width, (int) height);
    }

    private Color getColor(MathVector vector) {
        double ratio = (vector.getLength() - valueRange.x())
                / valueRange.width();
        return colorSet.getColor(ratio);
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] vertexArray) {
        if (vertexArray == null || vertexArray.length == 0)
            return;
        prepareVectors(vertexArray);
        calculateArrowSize(info);
        Graphics g = info.getGraphics();
        for (int i = 0; i < vertexArray.length; i++) {
            MathVector vector = vectorCache[i];
            g.setColor(getColor(vector));
            int x = info.real2graphicsX(vertexArray[i].v(0));
            int y = info.real2graphicsY(vertexArray[i].v(1));
            double xx = vector.v(0);
            double yy = vector.v(1);
            drawAllow(g, x, y, xx, yy);
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        if (vectorCache == null)
            return;
        g.setColor(colorSet.getColor(0.5));
        int x = r.x;
        int y = r.y + (r.height >> 1);
        g.drawLine(x, y, x + r.width - 1, y);
        g.drawLine(x, y, x + 3, y + 3);
        g.drawLine(x, y, x + 3, y - 3);
    }

    // temporary fields
    private double lg, ag, cos, sin, posx, posy, a1x, a1y, a2x, a2y, tx, ty,
            asize;

    private int px, py, b1x, b1y, b2x, b2y;

    // different from VectorPlotter2D
    private void drawAllow(Graphics g, int x, int y, double dx, double dy) {
        asize = Math.min(arrowSize.width, arrowSize.height) * sizeParameter;
        lg = Math.sqrt(dx * dx + dy * dy);
        cos = dx / lg;
        sin = dy / lg;
        posx = asize;
        posy = 0;
        px = (int) (cos * posx + sin * posy);
        py = (int) (-sin * posx + cos * posy);
        if (px == 0 && py == 0) {
            g.drawLine(x, y, x, y);
            return;
        }
        px += x;
        py += y;
        a1x = posx - 3;
        a1y = posy + 3;
        b1x = (int) (cos * a1x + sin * a1y) + x;
        b1y = (int) (-sin * a1x + cos * a1y) + y;
        a2x = posx - 3;
        a2y = posy - 3;
        b2x = (int) (cos * a2x + sin * a2y) + x;
        b2y = (int) (-sin * a2x + cos * a2y) + y;
        g.drawLine(x, y, px, py);
        g.drawLine(px, py, b1x, b1y);
        g.drawLine(px, py, b2x, b2y);
    }
}