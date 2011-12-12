/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.ScalarFunction;
import inou.math.vector.Vector2D;
import inou.math.vector.Vector3D;

/**
 * f(x,y) type funcion.
 */
public class SurfaceFunctionData3D extends SurfaceData3D implements
        FunctionDataModel {

    protected ScalarFunction function;

    protected int div = 20;

    private RealRange cacheRect;

    private Vector3D[] cacheVertices;

    // ==========================
    // contructor
    // ==========================

    /**
     * function data
     * 
     * @param fs
     *            input functions
     */
    public SurfaceFunctionData3D(ScalarFunction sf) {
        setFunction(sf);
    }

    // ==========================
    // access method
    // ==========================

    public void setFunction(ScalarFunction sf) {
        function = sf;
        // if (sf.getDimension() < 2)
        // System.err.println("bad function dimension : SurfaceFunctionData");
    }

    public ScalarFunction getFunctions() {
        return function;
    }

    public RealRange getReferenceRange(RealRange referenceRange) {
        cacheRect = referenceRange.getCopy();
        MathVector[] data = getArray(referenceRange);
        return UPlotData.getPartialRange(2, data);
    }

    public int[] getOutputDimensions() {
        return new int[] { 2 };
    }

    public void setDivision(int d) {
        if (d < 1) {
            return;
        }
        div = d;
    }

    public int getDivision() {
        return div;
    }

    // ==========================
    // operation
    // ==========================

    protected void updateData() {
        cacheVertices = null;
        // System.out.println("Update*"+getDataName());
    }

    /** make vertices array */
    public MathVector[] getArray(RealRange activeRange) {
        // System.out.println("Array*"+getDataName());
        if (activeRange == null)
            return null;
        if (cacheRect != null && cacheRect.equals(activeRange)
                && cacheVertices != null) {
            return cacheVertices;
        }

        // prepare
        double dx = activeRange.width() / (div - 1), x;
        double dy = activeRange.height() / (div - 1), y;
        Vector3D[] rets = new Vector3D[div * div];
        Vector2D pos = new Vector2D();

        // make [div]*[div] matrics data array
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                pos.set(activeRange.x() + dx * j, activeRange.y() + dy * i);
                double val = function.f(pos);
                Vector3D csv = new Vector3D(pos.x, pos.y, val);
                rets[j + div * i] = csv;
            }
        }

        cacheVertices = rets;
        cacheRect = activeRange.getCopy();
        return rets;
    }

    public RealPolygon[] getPolygons(MathVector[] vertexArray) {
        return makeSimpleGridPolygon(vertexArray, div);
    }

    public static RealPolygon[] makeSimpleGridPolygon(MathVector[] vertexArray,
            int divNum) {
        // System.out.println("Polygon*"+getDataName());
        Vector3D[] vertices = (Vector3D[]) vertexArray;

        if (vertices == null) {
            System.err.println("null error : SurfaceFunctionData");
            return null;
        }

        RealPolygon[] polygons = new RealPolygon[(divNum - 1) * (divNum - 1)
                * 2];

        int count = 0;
        int mdiv = divNum - 1;
        int row;// row : current row offset
        int rowj;// rowj : current position offset
        for (int i = 0; i < mdiv; i++) {
            row = i * divNum;
            for (int j = 0; j < mdiv; j++) {
                rowj = row + j;
                // 0---2
                // | /
                // 1/
                polygons[count] = new RealPolygon(rowj, rowj + divNum,
                        rowj + 1, false);
                count++;

                // /2
                // / |
                // 0---1
                polygons[count] = new RealPolygon(rowj + divNum, rowj + divNum
                        + 1, rowj + 1, false);
                count++;
            }
        }

        return polygons;
    }

}