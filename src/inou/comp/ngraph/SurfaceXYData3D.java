/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.geometry.GeoPolygon;
import inou.math.geometry.MakePolygon2D;
import inou.math.vector.Vector3D;

/**
 * f(x,y) type array data
 */
public class SurfaceXYData3D extends SurfaceData3D implements FixedDataModel {

    private Vector3D[] array;

    private RealRange dataRange;

    // ==========================
    // contructor
    // ==========================

    /**
     * array data
     * 
     * @param dataArray
     *            input array
     */
    public SurfaceXYData3D(MathVector[] dataArray) {
        setDataArray(dataArray);
    }

    // ==========================
    // access method
    // ==========================

    public void setDataArray(MathVector[] dataArray) {
        if (dataArray == null || dataArray.length == 0) {
            System.err.println("Null data array : " + dataArray
                    + " : SurfaceXYData");
            return;
        }
        if (dataArray[0].getDimension() < 3) {
            System.err.println("but array dimension : SurfaceXYData");
            return;
        }
        dataRange = new RealRange(3);
        dataRange.pos().substitute(dataArray[0]);
        array = new Vector3D[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            array[i] = new Vector3D();
            array[i].substitute(dataArray[i]);
            for (int j = 0; j < 3; j++) {
                if (array[i].v(j) < dataRange.pos(j)) {
                    double ex = dataRange.end(j);
                    dataRange.pos(j, array[i].v(j));
                    dataRange.size(j, ex - dataRange.pos(j));
                }
                if (array[i].v(j) > dataRange.end(j)) {
                    dataRange.size(j, array[i].v(j) - dataRange.pos(j));
                }
            }
        }
    }

    public Vector3D[] getDataArray() {
        return array;
    }

    public RealRange getDataRange() {
        return dataRange;
    }

    // ==========================
    // operation
    // ==========================

    public MathVector[] getArray(RealRange activeRange) {
        return array;
    }

    public RealPolygon[] getPolygons(MathVector[] array) {
        MakePolygon2D polygonMaker = new MakePolygon2D(array);
        GeoPolygon[] gpolygons = polygonMaker.getPolygons();
        RealPolygon[] rpolygons = new RealPolygon[gpolygons.length];
        for (int i = 0; i < gpolygons.length; i++) {
            GeoPolygon p = gpolygons[i];
            rpolygons[i] = new RealPolygon(
                    getVertexIndex(array, p.getVertex(0)), getVertexIndex(
                            array, p.getVertex(1)), getVertexIndex(array, p
                            .getVertex(2)), false);
        }
        return rpolygons;
    }

    private int getVertexIndex(MathVector[] array, MathVector target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        throw new InternalError();
    }
}