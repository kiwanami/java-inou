/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2.icon;

import inou.comp.ngraph.d2.IconPainter;

import java.awt.Graphics;
import java.awt.Polygon;

public class TriangleIcon implements IconPainter {

    private static double[] genx = { -1, 1, 0 };

    private static double[] geny = { -0.577, -0.577, 1.155 };

    private int[] xp = { 0, 0, 0 };

    private int[] yp = { 0, 0, 0 };

    private Polygon pg = new Polygon(xp, yp, 3);

    public TriangleIcon() {
        this(3);
    }

    public TriangleIcon(int size) {
        for (int i = 0; i < xp.length; i++) {
            xp[i] = (int) (genx[i] * size);
            yp[i] = (int) (geny[i] * size);
        }
    }

    public void draw(Graphics g, int cx, int cy) {
        pg.translate(cx, cy);
        g.drawPolygon(pg);
        pg.translate(-cx, -cy);
    }
}