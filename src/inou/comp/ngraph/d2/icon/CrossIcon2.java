/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2.icon;

import inou.comp.ngraph.d2.IconPainter;

import java.awt.Graphics;

public class CrossIcon2 implements IconPainter {

    private int size;

    public CrossIcon2() {
        this(3);
    }

    public CrossIcon2(int size) {
        this.size = size;
    }

    public void draw(Graphics g, int cx, int cy) {
        g.drawLine(cx - size, cy - size, cx + size, cy + size);
        g.drawLine(cx - size, cy + size, cx + size, cy - size);
    }

}