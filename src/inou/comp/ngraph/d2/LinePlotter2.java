/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;


import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Stroke;

/** plot with the line */

public class LinePlotter2 extends LinePlotter {

    protected float lineWidth = 1;

    public LinePlotter2(PlotData p) {
        super(p);
    }

    public LinePlotter2(Plotter p) {
        super(p);
    }

    public LinePlotter2(PlotData p, float f) {
        super(p);
        setWidth(f);
    }

    public LinePlotter2(Plotter p, float f) {
        super(p);
        setWidth(f);
    }

    public void setWidth(float i) {
        lineWidth = i;
    }

    public float getWidth() {
        return lineWidth;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        Graphics g = info.getGraphics();
 		Stroke old = null;
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D)g;
			old = g2.getStroke();
			g2.setStroke(new BasicStroke(lineWidth));
		}
		super.draw2D(info,cur);
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(old);
		}
        super.draw2D(info, cur);
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(lineColor);
        int x = r.x;
        int y = r.y + (r.height >> 1);
        g.drawLine(x, y, x + r.width - 1, y);
    }

}
