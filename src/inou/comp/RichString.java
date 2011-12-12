/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * string object with a font and a color information.
 * 
 * <pre>
 *  +----------------------------------------+
 *  |	vertical offset | (default 3dot)	 |
 *  |  +----------------------------------+	 |
 *  |  |								  |	 |
 *  |  |   contents (foreground)		  |	 |
 *  |  |								  |	 |
 *  |  |		   (background)			  |	 |
 *  | =+----------------------------------+	 |
 *  | holizontal offset (default 3dot)		 |
 *  +----------------------------------------+
 * </pre>
 */

public class RichString {

    private Font font;

    private Color fontColor, backgroundColor;

    private int fontNumber = 0;

    private String contents;

    private int voff = 3; // vertical offset

    private int hoff = 3; // holizontal offset

    private int ac, dc; // used in drawContents

    private boolean changed = true;// used in getSize

    private Dimension currentSize;

    // ===============
    // constructor
    // ===============

    /**
     * default constructor (easy access) <br>
     * (null string,black color,transparent background,<br>
     * SansSerif,PLAIN style,11 point)
     */
    public RichString() {
        this("", Color.black, null, new Font("SansSerif", Font.PLAIN, 12));
    }

    public RichString(RichString c) {
        this(c.getContent(), c.getForeground(), c.getBackground(), c.getFont());
    }

    /**
     * @param c
     *            contents string
     * @param f
     *            foreground color object. if null, default color is used.
     * @param b
     *            background color object. if null, background is not filled.
     * @param a
     *            font object. if null, default font is used.
     */
    public RichString(String c, Color f, Color b, Font a) {
        contents = c;
        fontColor = f;
        backgroundColor = b;
        font = a;
    }

    // ===============
    // access method
    // ===============

    public void setFont(Font f) {
        font = f;
        changed = true;
    }

    public void setContent(String s) {
        contents = s;
        changed = true;
    }

    public void setForeground(Color c) {
        fontColor = c;
    }

    public void setBackground(Color c) {
        backgroundColor = c;
    }

    public String getContent() {
        return contents;
    }

    public Font getFont() {
        return font;
    }

    public Color getForeground() {
        return fontColor;
    }

    public Color getBackground() {
        return backgroundColor;
    }

    /** access to offset parameters */
    public int getVerticalOffset() {
        return voff;
    }

    /** access to offset parameters */
    public int getHolizontalOffset() {
        return hoff;
    }

    /** access to offset parameters */
    public void setVerticalOffset(int v) {
        voff = v;
    }

    /** access to offset parameters */
    public void setHolizontalOffset(int h) {
        hoff = h;
    }

    // ===============
    // operation
    // ===============

    /** return demension size */
    public Dimension getSize(Component c) {
        if (changed == true) {
            mesureSize(c.getFontMetrics(getFont()));
        }
        return currentSize;
    }

    /** return demension size */
    public Dimension getSize(Graphics g) {
        if (changed == true) {
            mesureSize(g.getFontMetrics(getFont()));
        }
        return currentSize;
    }

    /** mesure String size */
    private void mesureSize(FontMetrics fm) {
        currentSize = new Dimension();
        if (fm != null) {
            currentSize.height = fm.getHeight() + voff * 2;
            currentSize.width = fm.stringWidth(contents) + hoff * 2;
            ac = fm.getAscent();
            dc = fm.getDescent();
            changed = false;
        } else {
            System.out.println("Null FontMetrics");
            // approximate size (not equal!)
            currentSize.height = getFont().getSize() + hoff * 2;
            currentSize.width = getFont().getSize() * (contents.length());
        }
    }

    /**
     * draw method
     * 
     * @param g
     *            graphics context
     * @param x
     *            left position
     * @param y
     *            top position
     * @return size of this string label
     */
    public Dimension drawContents(Graphics g, int x, int y) {
        Dimension d = getSize(g);
        Color bc = getBackground();
        if (bc != null) {
            Color old = g.getColor();
            g.setColor(bc);
            g.fillRect(x, y, d.width, d.height);
            g.setColor(old);
        }
        Color fc = getForeground();
        if (fc != null) {
            g.setColor(fc);
        }
        Font font = getFont();
        if (font != null) {
            g.setFont(font);
        }
        y += ac + voff;
        x += hoff;
        g.drawString(contents, x, y);
        return d;
    }

}
