/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.ColorManager;
import inou.comp.ngraph.PlotData3D;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SurfaceFunctionData3D;
import inou.comp.ngraph.VectorDataModel;
import inou.math.MathVector;
import inou.math.Unit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * plot the allow figure of present vector field (This class is for the 2D
 * VectorData f(x,y))
 */

public class VectorPlotter2D extends Plotter2D {

    private VectorDataModel vectorData;

    private Color lineColor = ColorManager.getColor();

    private boolean autoScale = true;

    private double scaleParameter = 1;

    private int longestArrowLength = 14;

    private MathVector[] vectorCache = null;

    public VectorPlotter2D(VectorDataModel data) {
        this(new SurfaceFunctionData3D(new Unit(2)), data);// dummy data
    }

    public VectorPlotter2D(PlotData3D parentData, VectorDataModel data) {
        super(parentData);
        vectorData = data;
    }

    public VectorPlotter2D(Plotter2D parentPlotter, VectorDataModel data) {
        super(parentPlotter);
        vectorData = data;
    }

    public void setAutoScale(boolean b) {
        autoScale = b;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public void setLongestArrowLength(int length) {
        longestArrowLength = length;
    }

    public void setScaleParameter(double a) {
        scaleParameter = a;
    }

    public double getScaleParameter() {
        return scaleParameter;
    }

    public void setColor(Color p) {
        lineColor = p;
    }

    public Color getColor() {
        return lineColor;
    }

    protected void updateData() {
        super.updateData();
        vectorCache = null;
    }

    private void prepareVectors(MathVector[] vertexArray) {
        if (vectorCache == null) {
            vectorCache = vectorData.getVectors(vertexArray);
            if (autoScale) {
                double maxLength = 0;
                for (int i = 0; i < vectorCache.length; i++) {
                    double length = vectorCache[i].getSquare();
                    if (maxLength < length) {
                        maxLength = length;
                    }
                }
                scaleParameter = 1. / Math.sqrt(maxLength);
            }
        }
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] vertexArray) {
        if (vertexArray == null || vertexArray.length < 1)
            return;
        prepareVectors(vertexArray);
        Graphics g = info.getGraphics();
        g.setColor(lineColor);
        for (int i = 0; i < vertexArray.length; i++) {
            int x = info.real2graphicsX(vertexArray[i].v(0));
            int y = info.real2graphicsY(vertexArray[i].v(1));
            double xx = vectorCache[i].v(0);
            double yy = vectorCache[i].v(1);
            drawAllow(g, x, y, xx, yy);
        }

    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(lineColor);
        int x = r.x;
        int y = r.y + (r.height >> 1);
        g.drawLine(x, y, x + r.width - 1, y);
        g.drawLine(x, y, x + 3, y + 3);
        g.drawLine(x, y, x + 3, y - 3);
    }

    // temporary fields
    private double lg, ag, cos, sin, posx, posy, a1x, a1y, a2x, a2y, tx, ty;

    private int px, py, b1x, b1y, b2x, b2y;

    private void drawAllow(Graphics g, int x, int y, double dx, double dy) {
        lg = Math.sqrt(dx * dx + dy * dy);
        cos = dx / lg;
        sin = dy / lg;
        posx = lg * longestArrowLength * scaleParameter;
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