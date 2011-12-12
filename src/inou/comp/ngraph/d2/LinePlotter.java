/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.ColorManager;
import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.UPlotData;
import inou.math.MathVector;
import inou.math.vector.Vector2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** plot with the line */

public class LinePlotter extends Plotter2D {

    protected Color lineColor = ColorManager.getColor();

    public LinePlotter(PlotData p) {
        super(p);
    }

    public LinePlotter(Plotter p) {
        super(p);
    }

    public void setColor(Color p) {
        lineColor = p;
    }

    public Color getColor() {
        return lineColor;
    }

    private int getFirstIndex(MathVector[] array) {
        for (int i = 0; i < array.length; i++) {
            if (UPlotData.isValidPoint2D(array[i]))
                return i;
        }
        return -1;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        if (cur == null || cur.length < 2)
            return;
        Graphics g = info.getGraphics();
        int firstIndex = getFirstIndex(cur);
        if (firstIndex == -1)
            return;
        if (cur instanceof Vector2D[]) {
            // OPTIMIZE!
            Vector2D[] vecs = (Vector2D[]) cur;
            g.setColor(lineColor);
            int x, y, xx, yy;
            xx = info.real2graphicsX(vecs[firstIndex].x);
            yy = info.real2graphicsY(vecs[firstIndex].y);
            for (int i = firstIndex + 1; i < vecs.length; i++) {
                if (!UPlotData.isValidPoint2D(vecs[i]))
                    continue;
                x = info.real2graphicsX(vecs[i].x);
                y = info.real2graphicsY(vecs[i].y);
                g.drawLine(xx, yy, x, y);
                xx = x;
                yy = y;
            }
        } else if (cur[0].getDimension() >= 2) {
            // general function
            g.setColor(lineColor);
            int x, y, xx, yy;
            xx = info.real2graphicsX(cur[firstIndex].v(0));
            yy = info.real2graphicsY(cur[firstIndex].v(1));
            for (int i = 1; i < cur.length; i++) {
                if (!UPlotData.isValidPoint2D(cur[i]))
                    continue;
                x = info.real2graphicsX(cur[i].v(0));
                y = info.real2graphicsY(cur[i].v(1));
                g.drawLine(xx, yy, x, y);
                xx = x;
                yy = y;
            }
        } else {
            System.err.println("Different PlotData...["
                    + getData().getClass().getName() + "]");
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(lineColor);
        int x = r.x;
        int y = r.y + (r.height >> 1);
        g.drawLine(x, y, x + r.width - 1, y);
    }

}