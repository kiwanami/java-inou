/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.RenderingInfo2D;
import inou.math.MathUtil;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector2D;

/** search the maximum point given region */
public class MaxPointPlotter extends AbstractPointPlotter {

    private RealRange region;

    public MaxPointPlotter(PlotData p) {
        super(p);
        initObjects();
    }

    public MaxPointPlotter(Plotter p) {
        super(p);
        initObjects();
    }

    public void setRegion(RealRange region) {
        this.region = region;
    }

    private void initObjects() {
        addObject(new CircleObject(new Vector2D()));
        addObject(new TextObject("Maximum", new Vector2D()));
    }

    protected MathVector getPosition(RenderingInfo2D info,
            MathVector[] dataArray) {
        int ps = max(dataArray);
        if (ps == -1)
            return null;
        return dataArray[ps];
    }

    private int max(MathVector[] d) {
        if (region == null) {
            return MathUtil.max(d, 1);
        }
        int num = d.length;
        int max = -1;
        for (int i = 1; i < num; i++) {
            if (max == -1 || d[max].v(1) < d[i].v(1)) {
                if (region.x() <= d[i].v(0) && region.ex() >= d[i].v(0)) {
                    max = i;
                }
            }
        }
        return max;
    }

}