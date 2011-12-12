/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.awt.Color;

public class ColorValueInfo implements Comparable {

    private Color color;

    private double value;

    public ColorValueInfo(Color color, double value) {
        this.color = color;
        this.value = value;
    }

    final public double getValue() {
        return value;
    }

    public void setValue(double d) {
        value = d;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    final public int compareTo(Object com) {
        // execute inverse sort!
        double ret = getValue() - ((ColorValueInfo) com).getValue();
        if (ret == 0)
            return 0;
        return (ret < 0) ? -1 : 1;
    }

    final public boolean equals(Object com) {
        return getValue() == ((ColorValueInfo) com).getValue();
    }

}
