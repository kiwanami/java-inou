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
import inou.math.MathVector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * draw BarChart
 */

public class BarChartPlotter extends Plotter2D {

    protected Color brushColor = ColorManager.getColor().brighter();

    protected Color lineColor = ColorManager.getColor();

    int barMaxWidth = 45;

    int barMinSpace = 4;

    public static final int LEFT = 0;

    public static final int CENTER = 1;

    public static final int RIGHT = 2;

    int basePosition = LEFT;

    public BarChartPlotter(PlotData p) {
        super(p);
    }

    public BarChartPlotter(Plotter p) {
        super(p);
    }

    /**
     * @param b
     *            LEFT, CENTER, RIGHT
     */
    public void setBasePosition(int b) {
        basePosition = b;
    }

    public void setBrushColor(Color p) {
        brushColor = p;
    }

    public Color getBrushColor() {
        return brushColor;
    }

    public void setBorderColor(Color p) {
        lineColor = p;
    }

    public Color getBorderColor() {
        return lineColor;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        if (cur == null || cur.length < 2)
            return;
        Graphics g = info.getGraphics();
        if (cur[0].getDimension() >= 2) {
            int num = cur.length;
            Rectangle contentArea = info.getContentArea();
            int iwidth = contentArea.width / num;
            int barWidth;
            if (iwidth > (barMinSpace + barMaxWidth)) {
                barWidth = barMaxWidth;
            } else if (iwidth > barMinSpace) {
                barWidth = iwidth - barMinSpace;
            } else {
                barWidth = iwidth;
            }
            int offset = 0;
            if (basePosition == CENTER) {
                offset = -barWidth / 2;
            } else if (basePosition == RIGHT) {
                offset = -barWidth;
            }
            int bottom = info.real2graphicsY(info.getPlotContext()
                    .getActiveRange().y());
            if (info.getPlotContext().getActiveRange().y() < 0) {
                bottom = info.real2graphicsY(0);
            }
            int x, ht;
            for (int i = 0; i < cur.length; i++) {
                x = info.real2graphicsX(cur[i].v(0)) + offset;
                ht = info.real2graphicsY(cur[i].v(1));
                g.setColor(brushColor);
                g.fillRect(x, Math.min(bottom, ht), barWidth, Math.abs(ht
                        - bottom));
                g.setColor(lineColor);
                g.drawRect(x, Math.min(bottom, ht), barWidth, Math.abs(ht
                        - bottom));
            }
        } else {
            System.err.println("Different PlotData...["
                    + getData().getClass().getName() + "]");
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(brushColor);
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(lineColor);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

}