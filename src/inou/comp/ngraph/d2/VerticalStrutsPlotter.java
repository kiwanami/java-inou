/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */
package inou.comp.ngraph.d2;

import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.StrutElement;
import inou.comp.ngraph.StrutsData;
import inou.math.MathVector;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class VerticalStrutsPlotter extends Plotter2D {

    private Color borderColor = Color.black;

    private double strutWidth = 0.3;

    public VerticalStrutsPlotter(StrutsData data) {
        super(data);
    }

    public VerticalStrutsPlotter(Plotter2D plotter) {
        super(plotter);
    }

    public double getStrutWidth() {
        return strutWidth;
    }

    public void setBorderColor(Color color) {
        borderColor = color;
    }

    private StrutsData getStrutsData() {
        return (StrutsData) getData();
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] array) {
        Graphics g = info.getGraphics();
        Color[] colors = getStrutsData().getRegionColors();
        int num = getStrutsData().getStrutsNum();
        for (int i = 0; i < num; i++) {
            StrutElement element = getStrutsData().getStrutElement(i);
            double[] points = element.getElements();
            for (int j = 0; j < (points.length - 1); j++) {
                int leftTopX = info.real2graphicsX(0.5 - strutWidth + i);
                int leftTopY = info.real2graphicsY(points[j + 1]);
                int rightBottomX = info.real2graphicsX(0.5 + strutWidth + i);
                int rightBottomY = info.real2graphicsY(points[j]);
                g.setColor(colors[j]);
                g.fillRect(leftTopX, Math.min(leftTopY, rightBottomY),
                        rightBottomX - leftTopX, Math.abs(rightBottomY
                                - leftTopY));
                if (borderColor != null) {
                    g.setColor(borderColor);
                    g.drawRect(leftTopX, Math.min(leftTopY, rightBottomY),
                            rightBottomX - leftTopX, Math.abs(rightBottomY
                                    - leftTopY));
                }
            }
        }
    }

    protected int getLegendHeight(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent() + 1;
        return getStrutsData().getRegionColors().length * height;
    }

    protected int getLegendWidth(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int wd = 0;
        String[] titles = getStrutsData().getRegionTitles();
        for (int i = 0; i < titles.length; i++) {
            if (wd < fm.stringWidth(titles[i])) {
                wd = fm.stringWidth(titles[i]);
            }
        }
        return wd + 2;
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        String[] titles = getStrutsData().getRegionTitles();
        Color[] colors = getStrutsData().getRegionColors();
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent() + fm.getDescent() + 1;
        int des = fm.getDescent();
        for (int i = 0; i < titles.length; i++) {
            g.setColor(colors[i]);
            g.fillRect(r.x, r.y + r.height - height * (i + 1), r.width, height);
            if (borderColor != null) {
                g.setColor(borderColor);
                g.drawRect(r.x, r.y + r.height - height * (i + 1), r.width,
                        height);
            }
            g.setColor(Color.white);
            int wd = fm.stringWidth(titles[i]);
            g.drawString(titles[i], r.x + (r.width - wd) / 2, r.y + r.height
                    - des - height * (i));
        }
    }

}