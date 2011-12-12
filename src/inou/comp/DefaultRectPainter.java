/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Simple implementation of RectPainter. <br>
 * This class fills the whole rectangle with given color.
 */
public class DefaultRectPainter extends RectPainter {

    protected Color bgColor = Color.white;

    public DefaultRectPainter() {
    }

    public DefaultRectPainter(Color bgc) {
        bgColor = bgc;
    }

    protected void work(Graphics g, Rectangle r) {
        if (bgColor == null)
            return;
        g.setColor(bgColor);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    public void setColor(Color c) {
        bgColor = c;
    }
}