/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.awt.Color;

public class SingleColorValueGenerator implements ColorValueGenerator {

    private ColorValueInfo[] colorValues = new ColorValueInfo[1];

    public SingleColorValueGenerator(Color color, double value) {
        colorValues[0] = new ColorValueInfo(color, value);
    }

    public ColorValueInfo[] getColorValueInfo(double startValue, double endValue) {
        return colorValues;
    }

}