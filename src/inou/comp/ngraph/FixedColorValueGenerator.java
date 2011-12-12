/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.awt.Color;

public class FixedColorValueGenerator extends DefaultColorValueGenerator {

    private ColorValueInfo[] colorInfos = null;

    public FixedColorValueGenerator(int numberOfColors, Color[] colorIndex,
            double start, double end) {
        super(numberOfColors, colorIndex);
        colorInfos = super.getColorValueInfo(start, end);
    }

    public ColorValueInfo[] getColorValueInfo(double startValue, double endValue) {
        return colorInfos;
    }

}