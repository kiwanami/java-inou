/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;

public class CircleObject extends GeometricObject {

    private double diameter;

    private Color borderColor, surfaceColor;

    public CircleObject(VectorQD position, double diameter, Color borderColor,
            Color surfaceColor) {
        super(new VectorQD[] { position });
        this.diameter = diameter;
        this.borderColor = borderColor;
        this.surfaceColor = surfaceColor;
    }

    public void setDiameter(double d) {
        diameter = d;
    }

    public double getDiameter() {
        return diameter;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color c) {
        borderColor = c;
    }

    public Color getSurfaceColor() {
        return surfaceColor;
    }

    public void setSurfaceColor(Color c) {
        surfaceColor = c;
    }

}