/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;
import inou.math.vector.Vector2D;

import java.awt.Graphics;
import java.awt.Rectangle;

/** connect points width allow line */

public class ArrowPlotter extends LinePlotter {

    protected double mag;

    public ArrowPlotter(PlotData p) {
        super(p);
    }

    public ArrowPlotter(Plotter p) {
        super(p);
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        if (cur == null || cur.length == 0)
            return;
        Graphics g = info.getGraphics();
        if (cur instanceof Vector2D[]) {
            // OPTIMIZE!
            Vector2D[] vecs = (Vector2D[]) cur;
            g.setColor(lineColor);
            int x, y;
            int xx, yy;
            xx = info.real2graphicsX(vecs[0].x);
            yy = info.real2graphicsY(vecs[0].y);
            for (int i = 1; i < vecs.length; i++) {
                x = info.real2graphicsX(vecs[i].x);
                y = info.real2graphicsY(vecs[i].y);
                drawAllow(g, x, y, xx - x, yy - y);
                xx = x;
                yy = y;
            }
        } else if (cur[0].getDimension() >= 2) {
            g.setColor(lineColor);
            int x, y;
            int xx, yy;
            xx = info.real2graphicsX(cur[0].v(0));
            yy = info.real2graphicsY(cur[0].v(1));
            for (int i = 1; i < cur.length; i++) {
                x = info.real2graphicsX(cur[i].v(0));
                y = info.real2graphicsY(cur[i].v(1));
                drawAllow(g, x, y, xx - x, yy - y);
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
        g.drawLine(x, y, x + 3, y + 3);
        g.drawLine(x, y, x + 3, y - 3);
    }

    // temporary fields
    protected double lg, ag, cos, sin, posx, posy, a1x, a1y, a2x, a2y, tx, ty;

    protected int px, py, b1x, b1y, b2x, b2y;

    /**
     * draw simple allow
     * 
     * @param g
     *            graphics object
     * @param x
     *            position of graphics axis
     * @param y
     *            position of graphics axis
     * @param dx
     *            x increase of graphics axis
     * @param dy
     *            y increase of graphics axis
     */
    public void drawAllow(Graphics g, int x, int y, double dx, double dy) {
        if (g == null)
            return;
        lg = Math.sqrt(dx * dx + dy * dy);
        cos = dx / lg;
        sin = -dy / lg;
        posx = lg;
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
