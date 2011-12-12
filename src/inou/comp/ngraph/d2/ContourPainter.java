/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.ColorValueGenerator;
import inou.comp.ngraph.ColorValueInfo;
import inou.comp.ngraph.DefaultColorValueGenerator;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RealPolygon;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SurfaceData3D;
import inou.comp.ngraph.UPlotData;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * plots contour on 2D graph.
 * 
 * This class assumes that the data array is Vector3D[], because of calculation
 * performance.
 */

public class ContourPainter extends Plotter2D {

    private ColorValueGenerator colorValueGenerator = null;

    private StrokeSet[] strokeSets;

    private boolean modifiedData = true;

    private double limit = 1e-20;

    public ContourPainter(SurfaceData3D p) {
        super(p, true);
    }

    public ContourPainter(SurfaceData3D p, ColorValueGenerator cvg) {
        super(p, true);
        setColorValueGenerator(cvg);
    }

    public ContourPainter(Plotter2D p) {
        super(p, true);
    }

    public ContourPainter(Plotter2D p, ColorValueGenerator cvg) {
        super(p, true);
        setColorValueGenerator(cvg);
    }

    public void setColorValueGenerator(ColorValueGenerator cvg) {
        colorValueGenerator = cvg;
    }

    private SurfaceData3D getSurfaceData() {
        return (SurfaceData3D) getData();
    }

    /**
     * Drawing contour limit. If the limit is larger than height between
     * contours to draw, this painter gives up drawing. (Default : 1e-20)
     */
    public void setLimit(double s) {
        limit = s;
    }

    protected void updateData() {
        super.updateData();
        modifiedData = true;
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] dataArray) {
        if (modifiedData) {
            initStrokes(info, dataArray);
            modifiedData = false;
        }

        drawStrokes(info);
    }

    protected int getLegendHeight(Graphics g) {
        return getColorValueInfo(0, 1).length;
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        ColorValueInfo[] colors = getColorValueInfo(0, 1);
        for (int i = 0; i < colors.length; i++) {
            if (i >= r.height)
                break;
            g.setColor(colors[colors.length - i - 1].getColor());
            g.drawLine(r.x, r.y + i, r.x + r.width, r.y + i);
        }
    }

    private ColorValueInfo[] getColorValueInfo(double start, double width) {
        if (colorValueGenerator == null) {
            colorValueGenerator = new DefaultColorValueGenerator();
        }
        return colorValueGenerator.getColorValueInfo(start, width + start);
    }

    // =================================
    // prepare strokes
    // =================================

    private void initStrokes(RenderingInfo2D info, MathVector[] dataArray) {
        RealRange range = UPlotData.getPartialRange(2, dataArray);
        if (range.width() < limit) {
            System.err.println("too small height : " + getClass().getName());
            return;
        }
        ColorValueInfo[] colorValues = getColorValueInfo(range.x(), range
                .width());

        ArrayList[] strokeSetLists = new ArrayList[colorValues.length];
        for (int i = 0; i < strokeSetLists.length; i++) {
            strokeSetLists[i] = new ArrayList();
        }
        RealPolygon[] polygons = getSurfaceData().getPolygons(dataArray);
        for (int i = 0; i < polygons.length; i++) {
            RealPolygon polygon = polygons[i];
            addContourStroke(info, colorValues, (Vector3D) dataArray[polygon
                    .getVertexIndexById(0)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(1)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(2)], strokeSetLists);
        }
        strokeSets = new StrokeSet[strokeSetLists.length];
        for (int i = 0; i < strokeSets.length; i++) {
            Stroke[] strokes = new Stroke[strokeSetLists[i].size()];
            for (int j = 0; j < strokes.length; j++) {
                strokes[j] = (Stroke) strokeSetLists[i].get(j);
            }
            strokeSets[i] = new StrokeSet(strokes, colorValues[i].getColor());
        }
    }

    // work area
    private Vector3D ct1 = new Vector3D();

    private Vector3D ct2 = new Vector3D();

    private void addContourStroke(RenderingInfo2D info,
            ColorValueInfo[] colorValues, Vector3D p1, Vector3D p2,
            Vector3D p3, ArrayList[] strokeSetsAsOutput) {
        double dmin = Math.min(p1.z, Math.min(p2.z, p3.z));
        double dmax = Math.max(p1.z, Math.max(p2.z, p3.z));

        for (int n = 0; n < colorValues.length; n++) {
            if (colorValues[n].getValue() < dmin
                    || colorValues[n].getValue() > dmax)
                continue;

            double currentValue = colorValues[n].getValue();

            // search start position (1st time)
            if (search(currentValue, p1, p2, ct1)) {
                // search end position
                if (!search(currentValue, p1, p3, ct2)) {
                    if (!search(currentValue, p2, p3, ct2)) {
                        continue;// wrong?
                    }
                }
                strokeSetsAsOutput[n]
                        .add(new Stroke(info.real2graphicsX(ct1.x), info
                                .real2graphicsY(ct1.y), info
                                .real2graphicsX(ct2.x), info
                                .real2graphicsY(ct2.y)));
            } else {
                // search start position (2nd time)
                if (search(currentValue, p1, p3, ct1)) {
                    // search end position
                    if (!search(currentValue, p1, p2, ct2)) {
                        if (!search(currentValue, p2, p3, ct2)) {
                            continue;
                        }
                    }
                    strokeSetsAsOutput[n].add(new Stroke(info
                            .real2graphicsX(ct1.x), info.real2graphicsY(ct1.y),
                            info.real2graphicsX(ct2.x), info
                                    .real2graphicsY(ct2.y)));
                }
            }
        }
    }

    private boolean search(double z, Vector3D p1, Vector3D p2, Vector3D output) {
        Vector3D lowVec, highVec;
        if (p1.z > p2.z) {
            highVec = p1;
            lowVec = p2;
        } else {
            highVec = p2;
            lowVec = p1;
        }
        if (z > lowVec.z && z <= highVec.z) {
            // liner interpolation
            double length = highVec.z - lowVec.z;
            double highRatio = (z - lowVec.z) / length;
            double lowRatio = 1. - highRatio;
            output.set(lowVec.x * lowRatio + highVec.x * highRatio, lowVec.y
                    * lowRatio + highVec.y * highRatio, z);
            return true;
        }
        return false;
    }

    // =================================
    // draw strokes
    // =================================

    void drawStrokes(RenderingInfo2D info) {
        if (strokeSets == null || strokeSets.length == 0)
            return;
        for (int i = 0; i < strokeSets.length; i++) {
            strokeSets[i].draw(info.getGraphics());
        }
    }

    class StrokeSet {
        Stroke[] strokes;

        Color color;

        StrokeSet(Stroke[] strokes, Color color) {
            this.strokes = strokes;
            this.color = color;
        }

        void draw(Graphics g) {
            if (strokes == null || strokes.length == 0)
                return;
            g.setColor(color);
            for (int i = 0; i < strokes.length; i++) {
                strokes[i].draw(g);
            }
        }
    }

    class Stroke {
        int sx, sy, ex, ey;

        Stroke(int sx, int sy, int ex, int ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
        }

        void draw(Graphics g) {
            g.drawLine(sx, sy, ex, ey);
        }
    }
}
