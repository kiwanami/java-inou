/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.ColorValueGenerator;
import inou.comp.ngraph.ColorValueInfo;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RealPolygon;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SurfaceData3D;
import inou.comp.ngraph.UGraph;
import inou.comp.ngraph.UPlotData;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/** plot gradation on 2D graph */

public class GradationPainter extends Plotter2D {

    private ColorValueGenerator colorValueGenerator = null;

    private GPolygonSet[] polygonSets;

    private boolean modifiedData = true;

    private double limit = 1e-20;

    private RealRange valueRange = null;

    public GradationPainter(SurfaceData3D p) {
        super(p, true);
    }

    public GradationPainter(SurfaceData3D p, ColorValueGenerator cvg) {
        super(p, true);
        setColorValueGenerator(cvg);
    }

    private SurfaceData3D getSurfaceData() {
        return (SurfaceData3D) getData();
    }

    /**
     * if range is null, the value range is calculate from given data array each
     * painting time. (default : null)
     */
    public void setValueRange(RealRange range) {
        valueRange = range;
    }

    public void setColorValueGenerator(ColorValueGenerator cvg) {
        colorValueGenerator = cvg;
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
            initPolygons(info, dataArray);
            modifiedData = false;
        }

        drawPolygons(info);
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        ColorValueInfo[] colors = getColorValueInfo(0, 1);
        double delta = ((double) colors.length) / r.height;
        for (int i = 0; i < r.height; i++) {
            g
                    .setColor(colors[colors.length - 1 - (int) (delta * i)]
                            .getColor());
            g.drawLine(r.x, r.y + i, r.x + r.width, r.y + i);
        }
    }

    private ColorValueInfo[] getColorValueInfo(double start, double width) {
        return UGraph.getColorValueInfo(colorValueGenerator, 64, start, width);
    }

    // =================================
    // prepare polygons
    // =================================

    private void initPolygons(RenderingInfo2D info, MathVector[] dataArray) {
        RealRange range = valueRange;
        if (range == null) {
            range = UPlotData.getPartialRange(2, dataArray);
        }
        if (range.width() < limit) {
            System.err.println("too small height : " + getClass().getName());
            return;
        }
        ColorValueInfo[] colorValues = getColorValueInfo(range.x(), range
                .width());
        if (colorValues.length < 2) {
            System.err.println("too few colors : " + getClass().getName());
            return;
        }
        ArrayList[] polygonSetLists = new ArrayList[colorValues.length];
        for (int i = 0; i < polygonSetLists.length; i++) {
            polygonSetLists[i] = new ArrayList();
        }
        RealPolygon[] polygons = getSurfaceData().getPolygons(dataArray);
        for (int i = 0; i < polygons.length; i++) {
            RealPolygon polygon = polygons[i];
            addContourPolygon(info, colorValues, (Vector3D) dataArray[polygon
                    .getVertexIndexById(0)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(1)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(2)], polygonSetLists);
        }
        polygonSets = new GPolygonSet[polygonSetLists.length];
        for (int i = 0; i < polygonSets.length; i++) {
            GPolygon[] gpolygons = new GPolygon[polygonSetLists[i].size()];
            for (int j = 0; j < gpolygons.length; j++) {
                gpolygons[j] = (GPolygon) polygonSetLists[i].get(j);
            }
            polygonSets[i] = new GPolygonSet(gpolygons, colorValues[i]
                    .getColor());
        }
    }

    // work area
    private Vector3D ct1 = new Vector3D();

    private Vector3D ct2 = new Vector3D();

    private Vector3D cc1 = new Vector3D();

    private Vector3D cc2 = new Vector3D();

    private ArrayList vertexList = new ArrayList();

    private void addContourPolygon(RenderingInfo2D info,
            ColorValueInfo[] colorValues, Vector3D a1, Vector3D a2,
            Vector3D a3, ArrayList[] polygonSetsAsOutput) {

        // sort ==> p1.z < p2.z < p3.z
        Vector3D p1 = null, p2 = null, p3 = null;
        if (a1.z < a2.z) {
            if (a2.z < a3.z) {
                p1 = a1;
                p2 = a2;
                p3 = a3;
            } else if (a3.z < a1.z) {
                p1 = a3;
                p2 = a1;
                p3 = a2;
            } else {
                p1 = a1;
                p2 = a3;
                p3 = a2;
            }
        } else {
            if (a1.z < a3.z) {
                p1 = a2;
                p2 = a1;
                p3 = a3;
            } else if (a3.z < a2.z) {
                p1 = a3;
                p2 = a2;
                p3 = a1;
            } else {
                p1 = a2;
                p2 = a3;
                p3 = a1;
            }
        }
        /*
         * if (!(p1.z <= p2.z && p2.z <= p3.z)) { throw new InternalError("not
         * ordered! "+p1+p2+p3); }
         */

        if (p3.z < colorValues[1].getValue()) {
            polygonSetsAsOutput[0].add(new GPolygon(info.real2graphicsX(p1.x),
                    info.real2graphicsY(p1.y), info.real2graphicsX(p2.x), info
                            .real2graphicsY(p2.y), info.real2graphicsX(p3.x),
                    info.real2graphicsY(p3.y)));
            return;
        } else if (p1.z > colorValues[colorValues.length - 1].getValue()) {
            polygonSetsAsOutput[colorValues.length - 1].add(new GPolygon(info
                    .real2graphicsX(p1.x), info.real2graphicsY(p1.y), info
                    .real2graphicsX(p2.x), info.real2graphicsY(p2.y), info
                    .real2graphicsX(p3.x), info.real2graphicsY(p3.y)));
            return;
        }

        vertexList.clear();
        vertexList.add(p1);
        int lastLevel = -1;
        boolean secondLine = false;

        for (int n = 1; n < colorValues.length; n++) {
            lastLevel = n;
            if (colorValues[n].getValue() < p1.z)
                continue;
            if (colorValues[n].getValue() > p3.z)
                break;

            double currentValue = colorValues[n].getValue();
            // System.out.println("----------------");
            // System.out.println("value:"+currentValue);
            // System.out.println("n:"+n);

            // search start position (1st time)
            if (search(currentValue, p1, p2, ct1)) {
                // search end position
                if (search(currentValue, p1, p3, ct2)) {
                    // System.out.println("first:");
                    addPolygon(info, vertexList, ct1, ct2,
                            polygonSetsAsOutput[n - 1]);
                } else {
                    throw new InternalError("Wrong search : n=" + n + "\n" + p1
                            + p2 + p3);
                }
            } else {
                // search start position (2nd time)
                if (search(currentValue, p2, p3, ct1)) {
                    // search end position
                    if (search(currentValue, p1, p3, ct2)) {
                        // System.out.println("second:");
                        if (!secondLine) {
                            vertexList.add(0, p2);
                            secondLine = true;
                            // System.out.println("second vertex!:");
                        }
                        addPolygon(info, vertexList, ct1, ct2,
                                polygonSetsAsOutput[n - 1]);
                    } else {
                        throw new InternalError("Wrong search : n=" + n + "\n"
                                + p1 + p2 + p3);
                    }
                }
            }
        }
        if (lastLevel != -1) {
            if (!secondLine) {
                vertexList.add(0, p2);
            }
            addPolygon(info, vertexList, p3, p3,
                    polygonSetsAsOutput[lastLevel - 1]);
        }
    }

    private void addPolygon(RenderingInfo2D info, ArrayList vertexList,
            Vector3D ct1, Vector3D ct2, ArrayList polygonList) {
        int[] xx = new int[2 + vertexList.size()];
        int[] yy = new int[2 + vertexList.size()];
        xx[0] = info.real2graphicsX(ct2.x);
        yy[0] = info.real2graphicsY(ct2.y);
        xx[1] = info.real2graphicsX(ct1.x);
        yy[1] = info.real2graphicsY(ct1.y);
        int count = 2;
        // System.out.println("ct1:"+ct1);
        // System.out.println("ct2:"+ct2);
        for (int i = 0; i < vertexList.size(); i++) {
            Vector3D vec = (Vector3D) vertexList.get(i);
            // System.out.println("vec:"+vec);
            xx[count] = info.real2graphicsX(vec.x);
            yy[count] = info.real2graphicsY(vec.y);
            count++;
        }
        polygonList.add(new GPolygon(xx, yy));
        vertexList.clear();
        cc1.substitute(ct1);
        cc2.substitute(ct2);
        vertexList.add(cc1);
        vertexList.add(cc2);
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
        if (z >= lowVec.z && z <= highVec.z) {
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
    // draw polygons
    // =================================

    void drawPolygons(RenderingInfo2D info) {
        if (polygonSets == null || polygonSets.length == 0)
            return;
        for (int i = 0; i < polygonSets.length; i++) {
            polygonSets[i].draw(info.getGraphics());
        }
    }

    class GPolygonSet {
        GPolygon[] polygons;

        Color color;

        GPolygonSet(GPolygon[] polygons, Color color) {
            this.polygons = polygons;
            this.color = color;
        }

        void draw(Graphics g) {
            if (polygons == null || polygons.length == 0)
                return;
            g.setColor(color);
            for (int i = 0; i < polygons.length; i++) {
                polygons[i].draw(g);
            }
        }
    }

    class GPolygon {
        int[] xx;

        int[] yy;

        GPolygon(int x1, int y1, int x2, int y2, int x3, int y3) {
            xx = new int[] { x1, x2, x3 };
            yy = new int[] { y1, y2, y3 };
        }

        GPolygon(int[] x, int[] y) {
            xx = x;
            yy = y;
        }

        void draw(Graphics g) {
            g.fillPolygon(xx, yy, xx.length);
        }
    }

}
