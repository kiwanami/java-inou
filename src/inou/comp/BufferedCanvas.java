/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;

/** this class support double buffer. */

public class BufferedCanvas extends Canvas {
    // Graphics
    protected Graphics backGraphics = null;

    protected Image backImage = null;

    protected Dimension size = null;

    protected RectPainter bgPainter = null;

    protected boolean heavyContents = false;

    protected boolean needsUpdate = false;

    private boolean whole = false;

    // ====================
    // operation
    // ====================

    /** set background painter */
    public void setBackground(RectPainter r) {
        bgPainter = r;
    }

    /** set background painter (same as [setBackground()]) */
    public void setRectPainter(RectPainter r) {
        bgPainter = r;
    }

    /** return background painter */
    public RectPainter getRectPainter() {
        return bgPainter;
    }

    /** overrided method */
    public Dimension getSize() {
        if (size == null) // protect from null pointer exception
            size = super.getSize();
        return size;
    }

    public Dimension getPreferredSize() {
        return getSize();
    }

    /**
     * if contents needs long time to repaint, check true. Then BufferedCanvas
     * will never call 'bpaint' until you call 'repaint'.
     */
    public void setHeavyContents(boolean b) {
        heavyContents = b;
        needsUpdate = true;
    }

    public void repaint() {
        needsUpdate = true;
        super.repaint();
    }

    // ====================
    // private area
    // ====================

    /** over-ride update method */
    public final void update(Graphics g) {
        paint(g);
    }

    /** buffered routine */
    public final void paint(Graphics g) {
        if (backImage == null) {
            fillBackground(g);
        }
        if (heavyContents) {
            if (!needsUpdate) {
                g.drawImage(backImage, 0, 0, this);
                return;
            }
        }
        Graphics bg = getBackGraphics();
        fillBackground(bg);
        bpaint(bg);
        g.drawImage(backImage, 0, 0, this);
        if (whole) {
            // JDK's bug?
            whole = false;
            repaint();
        }
    }

    /** buffered paint method (you can override this method) */
    public void bpaint(Graphics g) {
        if (bgPainter == null) {
            Dimension d = getSize();
            int w = 10;
            g.drawRect(w, w, d.width - w * 2, d.height - w * 2);
            g.drawString("(" + d.width + " - " + d.height + ")", w * 2, w * 2);
        }
    }

    private Graphics getBackGraphics() {
        Dimension d = super.getSize();
        if (backImage == null || !d.equals(size)) {
            size.width = d.width;
            size.height = d.height;
            backImage = this.createImage(d.width, d.height);
            backGraphics = backImage.getGraphics();
            whole = true;
            if (backImage != null) {
                onResizeCanvas();
            }
            return backGraphics;
        }
        return backGraphics;
    }

    protected void onResizeCanvas() {
    }

    private void fillBackground(Graphics g) {
        Color prev = g.getColor();
        Dimension d = getSize();
        if (bgPainter == null) {
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
        } else {
            bgPainter.paint(g, new Rectangle(0, 0, d.width, d.height));
        }
        g.setColor(prev);
    }

    /** usage example */
    public static void main(String[] arg) {
        JFrame f = SwingUtils.getTestFrame("buffered canvas test");
        f.getContentPane().add(new BufferedCanvas());
        f.setSize(500, 400);
        f.show();
    }
}
