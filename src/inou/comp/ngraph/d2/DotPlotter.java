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
import inou.math.vector.Vector2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** dot plotter implementation */
public class DotPlotter extends Plotter2D {

    protected Color dotColor = ColorManager.getColor();

    public DotPlotter(PlotData p) {
        super(p);
    }

    public DotPlotter(Plotter p) {
        super(p);
    }

    public void setColor(Color p) {
        dotColor = p;
    }

    public Color getColor() {
        return dotColor;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        if (cur == null || cur.length == 0)
            return;
        Graphics g = info.getGraphics();
        if (cur instanceof Vector2D[]) {
            // OPTIMIZE!
            Vector2D[] vecs = (Vector2D[]) cur;
            g.setColor(dotColor);
            int x, y;
            for (int i = 0; i < vecs.length; i++) {
                x = info.real2graphicsX(vecs[i].x);
                y = info.real2graphicsY(vecs[i].y);
                g.drawLine(x, y, x, y);
            }
        } else if (cur[0].getDimension() >= 2) {
            g.setColor(dotColor);
            int x, y;
            for (int i = 0; i < cur.length; i++) {
                x = info.real2graphicsX(cur[i].v(0));
                y = info.real2graphicsY(cur[i].v(1));
                g.drawLine(x, y, x, y);
            }
        } else {
            System.err.println("Different PlotData...["
                    + getData().getClass().getName() + "]");
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(dotColor);
        int x = r.x + r.width / 2;
        int y = r.y + r.height / 2;
        g.drawLine(x, y, x, y);
    }

}