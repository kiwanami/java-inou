/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathUtil;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.VectorGD;

import java.text.DecimalFormat;

public class UPlotData {

    public static MathVector[] copy(MathVector[] src) {
        if (src == null)
            return null;
        MathVector[] ret = new MathVector[src.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = src[i].getCopy();
        }
        return ret;
    }

    private static int d_num = 0;

    public static String getDefaultDataName() {
        d_num++;
        return "data-" + d_num;
    }

    public static boolean isValidPoint2D(MathVector p) {
        return p != null && !Double.isNaN(p.v(0)) && !Double.isNaN(p.v(1));
    }

    /**
     * search the data array and return maximum and minimum value.
     * 
     * @param dimension
     *            seaching dimension
     * @param dataArray
     *            data array as 1D RealRange
     */
    public static RealRange getPartialRange(int dimension,
            MathVector[] dataArray) {
        if (dataArray == null || dataArray.length == 0)
            return null;
        int sn = MathUtil.min(dataArray, dimension);
        int en = MathUtil.max(dataArray, dimension);
        if (sn == -1 || en == -1) {
            return null;
        }
        double x = dataArray[sn].v(dimension);
        double w = dataArray[en].v(dimension) - x;
        // System.out.println(dimension+" sn:"+sn+"
        // x:"+dataArray[sn].v(dimension)+" en:"+en+"
        // ex:"+dataArray[en].v(dimension));
        return new RealRange(x, w);
    }

    public static RealRange getDataRange(MathVector[] dataArray) {
        if (dataArray == null || dataArray.length == 0 || dataArray[0] == null) {
            return null;
        }
        int dim = dataArray[0].getDimension();
        VectorGD pos = new VectorGD(dim);
        VectorGD size = new VectorGD(dim);
        for (int i = 0; i < dim; i++) {
            RealRange range = UPlotData.getPartialRange(i, dataArray);
            if (range == null)
                return null;
            pos.v(i, range.x());
            size.v(i, range.length());
        }
        return new RealRange(pos, size);
    }

    private static final String nformat = "####0.###";

    private static final String fformat = "0.###";

    private static final String eformat = "#0";// for jdk1.1

    private static final DecimalFormat nform = new DecimalFormat(nformat);

    private static final DecimalFormat fform = new DecimalFormat(fformat);

    private static final DecimalFormat eform = new DecimalFormat(eformat);

    private static final double log10 = Math.log(10);

    public static String defaultNumberFormat(double value) {
        int e = (int) Math.floor(Math.log(Math.abs(value)) / log10);
        if (e >= 5 || e <= -4) {
            if (e < -200)
                return "0";
            return expFormat(value, e);
        }
        return nform.format(value);
    }

    /** for jdk1.1 */
    private static String expFormat(double in, int e) {
        double f = in * Math.pow(10, -e);
        return fform.format(f) + "E" + eform.format(e);
    }

    public static boolean isValidLogValue(double val) {
        if (val <= Double.MIN_VALUE) {
            return false;
        }
        return true;
    }
}