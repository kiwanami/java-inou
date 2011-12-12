/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Vector1D;
import inou.math.vector.Vector2D;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorGD;

import java.text.NumberFormat;

/**
 * Vector caliculation utility
 */

public class VectorUtil {

    /**
     * return the inner production. << c = <x1|x2> >>
     */
    public static double innerProduct(MathVector x1, MathVector x2) {
        if (x1.getDimension() != x2.getDimension()) {
            throw new ArithmeticException(
                    "different dimension at the inner production.");
        }
        double s = 0;
        for (int i = 0; i < x1.getDimension(); i++)
            s += x1.v(i) * x2.v(i);
        return s;
    }

    /**
     * return the outer production. << c = x1 x x2 >> (This operation is under
     * construction.)
     */
    public static MathVector outerProduct(MathVector x1, MathVector x2) {
        if (x1.getDimension() != x2.getDimension()) {
            throw new ArithmeticException(
                    "different dimension at the outer production.");
        }
        if (x1.getDimension() == 2) {
            MathVector x = new VectorGD(1);
            x.v(0, x1.v(0) * x2.v(1) - x1.v(1) * x2.v(0));
            return x;
        }
        if (x1.getDimension() == 3) {
            MathVector x = new VectorGD(3);
            x.v(2, x1.v(0) * x2.v(1) - x1.v(1) * x2.v(0));
            x.v(0, x1.v(1) * x2.v(2) - x1.v(2) * x2.v(1));
            x.v(1, x1.v(2) * x2.v(0) - x1.v(0) * x2.v(2));
            return x;
        }
        if (x1.getDimension() == 4) {
            MathVector x = new VectorGD(4);
            x.v(2, x1.v(0) * x2.v(1) - x1.v(1) * x2.v(0));
            x.v(0, x1.v(1) * x2.v(2) - x1.v(2) * x2.v(1));
            x.v(1, x1.v(2) * x2.v(0) - x1.v(0) * x2.v(2));
            return x;
        }

        System.err
                .println("sorry. for more than 4 dimension's vector, outer production is not supported.");
        return null;
    }

    public static String toString(MathVector x) {
        String a = "vec: ";
        for (int i = 0; i < x.getDimension(); i++)
            a += x.v(i) + " ";
        return a;
    }

    public static MathVector randomVector(int dim, double length) {
        MathVector vec = createVector(dim);
        for (int i = 0; i < dim; i++) {
            vec.v(i, MathUtil.random(1));
        }
        vec.normalize();
        vec.mults(length);
        return vec;
    }

    public static MathVector copyVector(MathVector v) {
        MathVector ret = createVector(v.getDimension());
        ret.substitute(v);
        return ret;
    }

    public static MathVector createVector(int dim) {
        switch (dim) {
        case 1:
            return new Vector1D();
        case 2:
            return new Vector2D();
        case 3:
            return new Vector3D();
        default:
        }
        return new VectorGD(dim);
    }

    /** return string dump with given formatter */
    public static String toString(MathVector mt, NumberFormat nf) {
        int d = mt.getDimension();
        StringBuffer a = new StringBuffer("vec: ");
        for (int i = 0; i < d; i++) {
            a.append(nf.format(mt.v(i))).append(" ");
        }
        return a.toString();
    }

    // ==============
    // array utility
    // ==============

    /**
     * array util
     * 
     * <pre>
     *  double arg [column][data index] 
     * 		   ---&gt; 
     * 			  MathVector [data index] ret
     * </pre>
     * 
     * (all columns must have the same number of data index.)
     */
    public static MathVector[] translate(double[][] arg) {
        int num = arg[0].length;
        int colm = arg.length;
        MathVector[] rets = new MathVector[num];
        for (int i = 0; i < num; i++) {
            rets[i] = createVector(colm);
            for (int j = 0; j < colm; j++)
                rets[i].v(j, arg[j][i]);
        }
        return rets;
    }

}