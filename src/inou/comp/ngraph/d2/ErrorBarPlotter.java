/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.ColorManager;
import inou.comp.ngraph.ErrorBarDataModel;
import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ErrorBarPlotter extends Plotter2D {

    protected Color lineColor = ColorManager.getColor();

    public ErrorBarPlotter(PlotData p) {
        super(p);
    }

    public ErrorBarPlotter(Plotter p) {
        super(p);
    }

    public void setColor(Color p) {
        lineColor = p;
    }

    public Color getColor() {
        return lineColor;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        if (cur == null || cur.length == 0
                || !(getData() instanceof ErrorBarDataModel))
            return;

        ErrorBarDataModel errModel = (ErrorBarDataModel) getData();
        Graphics g = info.getGraphics();

        g.setColor(lineColor);
        for (int i = 1; i < cur.length; i++) {
            int x = info.real2graphicsX(cur[i].v(0));
            int uy = info.real2graphicsY(cur[i].v(1)
                    + cur[i].v(errModel.getUpperErrorDimension()));
            int ly = info.real2graphicsY(cur[i].v(1)
                    - cur[i].v(errModel.getLowerErrorDimension()));
            g.drawLine(x, uy, x, ly);
            g.drawLine(x - 2, uy, x + 2, uy);
            g.drawLine(x - 2, ly, x + 2, ly);
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(lineColor);
        int x = r.x + (r.width >> 1);
        int uy = r.y;
        int ly = r.y + r.height;
        g.drawLine(x, uy, x, ly);
        g.drawLine(x - 2, uy, x + 2, uy);
        g.drawLine(x - 2, ly, x + 2, ly);
    }

}