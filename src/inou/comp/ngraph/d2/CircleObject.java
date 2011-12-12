/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;

import java.awt.Color;

public class CircleObject extends PlotObject2D {

    private MathVector position;

    private int diameter = 10;

    private Color color = Color.black;

    public CircleObject(MathVector pos) {
        position = pos;
    }

    public MathVector getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setPosition(MathVector v) {
        position = v;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setDiameter(int d) {
        diameter = d;
    }

    public void drawObject2D(RenderingInfo2D info) {
        info.getGraphics().setColor(getColor());
        int r = diameter / 2;
        int spotx = info.real2graphicsX(position.v(0)) - r;
        int spoty = info.real2graphicsY(position.v(1)) - r;
        info.getGraphics().drawOval(spotx, spoty, diameter, diameter);
    }

}