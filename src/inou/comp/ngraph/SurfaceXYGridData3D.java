/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;

/**
 * f(x,y) type array data
 */
public class SurfaceXYGridData3D extends SurfaceData3D implements
        FixedDataModel {

    private int xnum, ynum;

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
    public SurfaceXYGridData3D(MathVector[] dataArray, int xn, int yn) {
        this.xnum = xn;
        this.ynum = yn;
        setDataArray(dataArray);
    }

    /**
     * this constructor copies the given array into internal data array.
     */
    public SurfaceXYGridData3D(double[] x, double[] y, double[] z, int xn,
            int yn) {
        if (x.length != y.length || y.length != z.length) {
            throw new IllegalArgumentException(
                    "Wrong numbers of the given arrays.");
        }
        this.xnum = xn;
        this.ynum = yn;
        MathVector[] positions = new MathVector[x.length];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Vector3D(x[i], y[i], z[i]);
        }
        setDataArray(positions);
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
        int pnum = (xnum - 1) * (ynum - 1) * 2;
        RealPolygon[] rpolygons = new RealPolygon[pnum];
        int pcount = 0;
        for (int i = 0; i < (ynum - 1); i++) {
            for (int j = 0; j < (xnum - 1); j++) {
                int ad0 = j + i * xnum;
                rpolygons[pcount++] = new RealPolygon(ad0, ad0 + 1, ad0 + xnum,
                        true);
                rpolygons[pcount++] = new RealPolygon(ad0 + 1, ad0 + xnum + 1,
                        ad0 + xnum, true);
            }
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