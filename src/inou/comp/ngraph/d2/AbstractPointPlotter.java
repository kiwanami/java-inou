/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathVector;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/** search the maximum point given region */
public abstract class AbstractPointPlotter extends Plotter2D {

    private ArrayList objects = new ArrayList();

    protected AbstractPointPlotter(PlotData p) {
        super(p);
    }

    protected AbstractPointPlotter(Plotter p) {
        super(p);
    }

    public void clearObjects() {
        objects.clear();
    }

    public void addObject(PlotObject2D object) {
        objects.add(object);
    }

    protected abstract MathVector getPosition(RenderingInfo2D info,
            MathVector[] dataArray);

    protected void draw2D(RenderingInfo2D info, MathVector[] dataArray) {
        if (dataArray == null || dataArray.length == 0)
            return;

        MathVector position = getPosition(info, dataArray);
        if (position == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            PlotObject2D obj = (PlotObject2D) objects.get(i);
            obj.setPosition(position);
            obj.drawObject2D(info);
        }
    }

    protected void drawLegend(Graphics g, Rectangle r) {
    }

}
