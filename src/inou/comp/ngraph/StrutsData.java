/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;

import java.awt.Color;

public class StrutsData extends PlotData implements FixedDataModel {

    private StrutElement[] elements;

    private boolean vertical = true;

    private Color[] colors = null;

    private String[] titles = null;

    public StrutsData(StrutElement[] elements) {
        super("");
        this.elements = elements;
    }

    public boolean isVerticalModel() {
        return vertical;
    }

    public void setVerticalModel(boolean b) {
        vertical = b;
    }

    public GridGenerator getGridGenerator() {
        double[] values = new double[elements.length];
        String[] names = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            values[i] = 0.5 + i;
            names[i] = elements[i].getTitle();
        }
        return new StringGridGenerator(values, names);
    }

    /**
     * normal plotters can not show this data model.
     */
    public MathVector[] getArray(RealRange range) {
        return new MathVector[0];
    }

    public void setRegionColors(Color[] cls) {
        colors = cls;
    }

    protected int getMaxRegionNumber() {
        int num = 1;
        for (int i = 0; i < elements.length; i++) {
            if (num < elements[i].getRegionNum()) {
                num = elements[i].getRegionNum();
            }
        }
        return num;
    }

    public Color[] getRegionColors() {
        if (colors == null) {
            colors = new Color[getMaxRegionNumber()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = ColorManager.getColor().brighter();
            }
        }
        return colors;
    }

    public void setRegionTitles(String[] rt) {
        titles = rt;
    }

    public String[] getRegionTitles() {
        if (titles == null) {
            titles = new String[getMaxRegionNumber()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = Integer.toString(i + 1);
            }
        }
        return titles;
    }

    public int getStrutsNum() {
        return elements.length;
    }

    public StrutElement getStrutElement(int index) {
        return elements[index];
    }

    public RealRange getDataRange() {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < elements.length; i++) {
            if (min > elements[i].getMinElement()) {
                min = elements[i].getMinElement();
            }
            if (max < elements[i].getMaxElement()) {
                max = elements[i].getMaxElement();
            }
        }
        if (vertical) {
            return new RealRange(0, min, elements.length, max - min);
        } else {
            return new RealRange(min, 0, max - min, elements.length);
        }
    }
}