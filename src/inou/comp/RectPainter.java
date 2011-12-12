/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Graphics;
import java.awt.Rectangle;

/** a interface that paints the specified rectangle */
public abstract class RectPainter {

    protected RectPainter prepainter = null;

    /** default constructor with no pre-paint object. */
    protected RectPainter() {
        this(null);
    }

    /**
     * construct with pre-paint object.
     * 
     * @param r
     *            pre-paint object (RectPainter)
     */
    protected RectPainter(RectPainter r) {
        prepainter = r;
    }

    /**
     * called by parent object to paint the given rectangle area.
     * 
     * @param g
     *            graphics context
     * @param r
     *            the reactangle to paint
     */
    public final void paint(Graphics g, Rectangle r) {
        if (prepainter != null)
            prepainter.paint(g, r);
        work(g, r);
    }

    /** set pre-paint object. */
    public final void setPrepainter(RectPainter r) {
        prepainter = r;
    }

    /** get pre-paint object */
    public final RectPainter getPrepainter() {
        return prepainter;
    }

    /**
     * paint method that should be implemented by subclass
     * 
     * @param g
     *            graphics context
     * @param r
     *            the rectangle to paint
     */
    protected abstract void work(Graphics g, Rectangle r);
}