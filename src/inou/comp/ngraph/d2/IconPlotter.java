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

/** plot with icon */
public class IconPlotter extends Plotter2D {

    protected Color iconColor = ColorManager.getColor();

    protected IconPainter painter;

    public IconPlotter(PlotData p) {
        this(p, null);
    }

    public IconPlotter(PlotData p, IconPainter ip) {
        super(p);
        initPainter(ip);
    }

    public IconPlotter(Plotter p) {
        this(p, null);
    }

    public IconPlotter(Plotter p, IconPainter ip) {
        super(p);
        initPainter(ip);
    }

    public void setColor(Color p) {
        iconColor = p;
    }

    public Color getColor() {
        return iconColor;
    }

    public void setIcon(IconPainter ip) {
        initPainter(ip);
    }

    private void initPainter(IconPainter ip) {
        painter = ip;
        if (painter == null) {
            painter = IconManager.getPainter();
        }
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] cur) {
        Graphics g = info.getGraphics();
        if (cur == null || cur.length == 0)
            return;
        if (cur instanceof Vector2D[]) {
            // OPTIMIZE!
            Vector2D[] vecs = (Vector2D[]) cur;
            g.setColor(iconColor);
            int x, y;
            for (int i = 0; i < vecs.length; i++) {
                x = info.real2graphicsX(vecs[i].x);
                y = info.real2graphicsY(vecs[i].y);
                painter.draw(g, x, y);
            }
        } else if (cur[0].getDimension() >= 2) {
            g.setColor(iconColor);
            int x, y;
            for (int i = 0; i < cur.length; i++) {
                x = info.real2graphicsX(cur[i].v(0));
                y = info.real2graphicsY(cur[i].v(1));
                painter.draw(g, x, y);
            }
        } else {
            System.err.println("Different PlotData...["
                    + getData().getClass().getName() + "]");
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        g.setColor(iconColor);
        int x = r.x + r.width / 2;
        int y = r.y + r.height / 2;
        painter.draw(g, x, y);
    }

}