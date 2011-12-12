/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2.icon;

import inou.comp.ngraph.d2.IconPainter;

import java.awt.Graphics;
import java.awt.Polygon;

/** implementation of star icon */
public class StarIcon implements IconPainter {

    private Polygon pg;

    public StarIcon() {
        this(5, 8);
    }

    public StarIcon(int n, int r) {
        int r1;
        int r2;
        int num;
        int[] x;
        int[] y;
        r1 = r;
        r2 = r >> 1;
        num = n;
        x = new int[num * 2];
        y = new int[num * 2];
        double dt, t, dth;
        dt = 2 * Math.PI / num;
        dth = dt / 2;
        for (int i = 0; i < num; i++) {
            t = dt * i;
            x[i * 2] = (int) (r1 * Math.sin(t));
            y[i * 2] = (int) (-r1 * Math.cos(t));
            t += dth;
            x[i * 2 + 1] = (int) (r2 * Math.sin(t));
            y[i * 2 + 1] = (int) (-r2 * Math.cos(t));
        }
        pg = new Polygon(x, y, num * 2);
    }

    public void draw(Graphics g, int cx, int cy) {
        pg.translate(cx, cy);
        g.drawPolygon(pg);
        pg.translate(-cx, -cy);
    }
}