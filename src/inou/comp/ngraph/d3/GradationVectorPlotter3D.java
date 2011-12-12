/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.ColorSet;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.ngraph.PlotData3D;
import inou.comp.ngraph.PlotData4D;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.RenderingInfo3D;
import inou.comp.ngraph.VectorDataModel;
import inou.comp.ngraph.VolumeFunctionData4D;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.Unit;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GradationVectorPlotter3D extends Plotter3D {

    private VectorDataModel vectorData;

    private ColorSet colorSet = null;

    private boolean autoScale = true;

    private boolean normalizeSize = true;

    private RealRange valueRange = new RealRange(1);

    private double sizeParameter = 0.8;

    private double minLimit = -1;

    private double maxLimit = -1;

    private MathVector[] vectorCache = null;

    public GradationVectorPlotter3D(VectorDataModel vectorData) {
        this(new VolumeFunctionData4D(new Unit(3)), vectorData);
    }

    public GradationVectorPlotter3D(PlotData4D data, VectorDataModel vectorData) {
        super(data);
        this.vectorData = vectorData;
    }

    public GradationVectorPlotter3D(PlotData3D data, VectorDataModel vectorData) {
        super(data);
        this.vectorData = vectorData;
    }

    public GradationVectorPlotter3D(Plotter3D plotter,
            VectorDataModel vectorData) {
        super(plotter);
        this.vectorData = vectorData;
    }

    public void setNormalizeSize(boolean b) {
        normalizeSize = b;
    }

    public boolean isNormalizeSize() {
        return normalizeSize;
    }

    public void setAutoScale(boolean b) {
        autoScale = b;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    /**
     * if vector length is shorter than this value, the vector is not shown. if
     * negative value is given, the minimum limit is ignored. (default:-1)
     */
    public void setMinLimit(double m) {
        minLimit = m;
    }

    /**
     * if vector length is longer than this value, the vector is not shown. if
     * negative value is given, the maximum limit is ignored. (default:-1)
     */
    public void setMaxLimit(double m) {
        maxLimit = m;
    }

    /**
     * This method cancels auto scaling.
     */
    public void setValueRange(RealRange range) {
        valueRange.substitute(range);
        setAutoScale(false);
    }

    public RealRange getValueRange(double a) {
        return valueRange;
    }

    public void setGradation(ColorSet set) {
        colorSet = set;
    }

    protected void updateData() {
        super.updateData();
        vectorCache = null;
    }

    /**
     * arrow size ratio. ( 0 -- 1.0 : defualt 0.8) The arrow size is calculate
     * from "(sizeParam)*(length)/(number)^0.333". Here (sizeParam) is this
     * value, (length) is context size along the shortest axis and (number) is
     * sampling number of plot data.
     */
    public void setSizeParameter(double r) {
        if (r <= 1.0 && r > 0) {
            sizeParameter = r;
        }
    }

    public double getSizeParameter() {
        return sizeParameter;
    }

    private void prepareVectors(MathVector[] vertexArray) {
        if (vectorCache == null) {
            vectorCache = vectorData.getVectors(vertexArray);
            autoScaling();
        }
        if (colorSet == null) {
            colorSet = new ColorSet(ColorSet.rainbowIndex, 32);
        }
    }

    private void autoScaling() {
        if (autoScale) {
            double maxLength = 0;
            double minLength = Double.MAX_VALUE;
            for (int i = 0; i < vectorCache.length; i++) {
                double length = vectorCache[i].getLength();
                if (maxLength < length) {
                    maxLength = length;
                }
                if (minLength > length) {
                    minLength = length;
                }
            }
            if (minLimit > 0 && minLimit > minLength) {
                minLength = minLimit;
            }
            if (maxLimit > 0 && maxLimit < maxLength) {
                maxLength = maxLimit;
            }
            valueRange.x(minLength);
            valueRange.width(maxLength - minLength);
        }
    }

    private Vector3D arrowSize = null;

    private Vector3D orgArrowSize = null;

    private void calculateArrowSize(RenderingInfo3D info) {
        double n = Math.pow(vectorCache.length, 0.3333);
        arrowSize = new Vector3D(info.getSceneBorder().size().mult(
                sizeParameter / n));

        Vector3D sample1 = new Vector3D(0, 0, 0);
        Vector3D sample2 = new Vector3D(1, 1, 1);
        info.real2scene(sample1);
        info.real2scene(sample2);
        orgArrowSize = sample2.subs(sample1);
    }

    private Color getColor(MathVector vector) {
        double ratio = (vector.getLength() - valueRange.x())
                / valueRange.width();
        return colorSet.getColor(ratio);
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        if (vectorCache == null)
            return;
        g.setColor(colorSet.getColor(0.5));
        int x = r.x;
        int y = r.y + (r.height >> 1);
        g.drawLine(x, y, x + r.width - 1, y);
        g.drawLine(x, y, x + 3, y + 3);
        g.drawLine(x, y, x + 3, y - 3);
    }

    protected void draw3D(RenderingInfo3D info, MathVector[] vertices) {
        if (vertices == null || vertices.length == 0)
            return;
        prepareVectors(vertices);
        calculateArrowSize(info);
        makeArrowObjects(info, vertices);
    }

    private void makeArrowObjects(RenderingInfo3D info, MathVector[] vertices) {
        for (int i = 0; i < vertices.length; i++) {
            double length = vectorCache[i].getLength();
            if (length < minLimit || ((length > maxLimit) && (maxLimit > 0))) {
                continue;
            }
            VectorQD vector = new VectorQD(vectorCache[i]);
            VectorQD pos = new VectorQD(vertices[i]);
            info.real2scene(pos);
            info.addSceneObject(makeOneAllow(info, pos, vector));
        }
    }

    private IGeometricObject makeOneAllow(RenderingInfo3D info, VectorQD pos,
            VectorQD vector) {
        Color color = getColor(vector);
        if (normalizeSize) {
            vector.normalize();
            vector.x *= arrowSize.x;
            vector.y *= arrowSize.y;
            vector.z *= arrowSize.z;
        } else {
            vector.x *= orgArrowSize.x;
            vector.y *= orgArrowSize.y;
            vector.z *= orgArrowSize.z;
        }
        vector.adds(pos);
        return PrimitiveObjectMaker.arrow(pos, vector, color);
    }
}