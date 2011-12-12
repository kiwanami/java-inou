/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2.icon;

import inou.comp.ngraph.d2.IconPainter;

import java.awt.Graphics;

public class OvalIcon implements IconPainter {

    private int size;

    private int bsize;

    public OvalIcon() {
        this(3);
    }

    public OvalIcon(int size) {
        this.size = size;
        bsize = size * 2 + 2;
    }

    public void draw(Graphics g, int cx, int cy) {
        g.drawOval(cx - size, cy - size, bsize, bsize);
    }
}