/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import java.awt.Graphics;

/** paint the icon */
public interface IconPainter {

    /**
     * @param g
     *            graphics object, which is set some color.
     * @param cx
     *            clitical position
     * @param cy
     *            clitical position
     */
    public void draw(Graphics g, int cx, int cy);

}