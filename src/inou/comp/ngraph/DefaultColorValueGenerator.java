/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.ColorSet;

import java.awt.Color;

public class DefaultColorValueGenerator implements ColorValueGenerator {

    private ColorSet colorSet;

    private ColorValueInfo[] infos;

    public DefaultColorValueGenerator() {
        this(16);
    }

    public DefaultColorValueGenerator(int num) {
        this(num, ColorSet.rainbowIndex);
    }

    public DefaultColorValueGenerator(int numberOfColors, Color[] colorIndex) {
        // numberOfColors++;
        this.colorSet = new ColorSet(colorIndex, numberOfColors);
        infos = new ColorValueInfo[numberOfColors];
        for (int i = 0; i < numberOfColors; i++) {
            infos[i] = new ColorValueInfo(colorSet.getColor(i), 0);
        }
    }

    public ColorValueInfo[] getColorValueInfo(double startValue, double endValue) {
        double dx = (endValue - startValue) / (colorSet.getNumberOfColors());
        for (int i = 0; i < colorSet.getNumberOfColors(); i++) {
            infos[i].setValue(startValue + dx * i);
        }
        return infos;
    }

}