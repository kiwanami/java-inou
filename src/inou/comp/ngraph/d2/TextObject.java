/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TextObject extends PlotObject2D {

    private MathVector position;

    private String text;

    private Font font = new Font("Serif", Font.PLAIN, 12);

    private Color color = Color.blue;

    private int verticalSpot = TOP, horizontalSpot = LEFT;

    public static final int CENTER = 0;

    public static final int TOP = 1;

    public static final int BOTTOM = 2;

    public static final int LEFT = 1;

    public static final int RIGHT = 2;

    public TextObject(String text, MathVector pos) {
        this.text = text;
        position = pos;
    }

    public MathVector getPosition() {
        return position;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public void setPosition(MathVector v) {
        position = v;
    }

    public void setFont(Font f) {
        font = f;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setText(String t) {
        text = t;
    }

    /**
     * @param h
     *            horizontal adjustment (LEFT,CENTER,RIGHT)
     * @param v
     *            vertical adjustment (BOTTOM,CENTER,TOP)
     */
    public void setSpotPosition(int h, int v) {
        verticalSpot = v;
        horizontalSpot = h;
    }

    public void drawObject2D(RenderingInfo2D info) {
        if (text == null || position == null)
            return;
        Graphics g = info.getGraphics();
        Font oldFont = g.getFont();
        g.setFont(getFont());
        Color oldColor = g.getColor();
        g.setColor(getColor());
        int spotx = info.real2graphicsX(position.v(0));
        spotx += getHorizontalCorrect(g);
        int spoty = info.real2graphicsY(position.v(1));
        spoty += getVerticalCorrect(g);
        g.drawString(getText(), spotx, spoty);
        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    private int getHorizontalCorrect(Graphics g) {
        switch (horizontalSpot) {
        case LEFT:
            break;
        case RIGHT:
            return -getWidth(g);
        case CENTER:
        default:
            return -getWidth(g) / 2;
        }
        return 0;
    }

    private int getVerticalCorrect(Graphics g) {
        switch (verticalSpot) {
        case TOP:
            return getAscent(g);
        case BOTTOM:
            return -getDescent(g);
        case CENTER:
        default:
        }
        return (getAscent(g) - getDescent(g)) / 2;
    }

    private int getHeight(Graphics g) {
        FontMetrics fm = g.getFontMetrics(getFont());
        return fm.getHeight();
    }

    private int getAscent(Graphics g) {
        FontMetrics fm = g.getFontMetrics(getFont());
        return fm.getAscent();
    }

    private int getDescent(Graphics g) {
        FontMetrics fm = g.getFontMetrics(getFont());
        return fm.getDescent();
    }

    private int getWidth(Graphics g) {
        FontMetrics fm = g.getFontMetrics(getFont());
        return fm.stringWidth(getText());
    }
}