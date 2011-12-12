/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Matrix3D;
import inou.math.vector.MatrixGD;

import java.awt.Dimension;
import java.text.NumberFormat;

/**
 * Matrix caliculation utility
 */

public class MatrixUtil {

    /** let left matrix be the same value as right one. */
    public static void substitute(Matrix lm, Matrix rm) {
        Dimension ldm = lm.getDimension();
        Dimension rdm = rm.getDimension();
        if (ldm.height != rdm.height || ldm.width != rdm.width)
            throw new ArithmeticException(
                    "different dimensions at matrix substitution.");
        for (int i = 0; i < ldm.height; i++) {
            for (int j = 0; j < ldm.width; j++) {
                lm.set(i, j, rm.get(i, j));
            }
        }
    }

    /** checks whether the given matrixes equal or not. */
    public static boolean equal(Matrix a, Matrix b) {
        Dimension adm = a.getDimension();
        Dimension bdm = b.getDimension();
        if (adm.height != bdm.height || adm.width != bdm.width)
            return false;
        for (int i = 0; i < adm.height; i++) {
            for (int j = 0; j < adm.width; j++) {
                if (MathUtil.nearlyEqual(a.get(i, j), b.get(i, j)))
                    return false;
            }
        }
        return true;
    }

    /**
     * multiplies matrix from RIGHT-hand
     * 
     * @param vt
     *            vector
     * @param mt
     *            matrix
     * @param at
     *            result vector
     */
    public static void multvm(MathVector at, MathVector vt, Matrix mt) {
        Dimension dm = mt.getDimension();
        if (dm.height != vt.getDimension())
            throw new ArithmeticException(
                    "different dimension at vector-matrix multiply");
        double t = 0;
        for (int i = 0; i < dm.width; i++) {
            t = 0;
            for (int j = 0; j < dm.height; j++) {
                t += mt.get(j, i) * vt.v(j);
            }
            at.v(i, t);
        }
    }

    /**
     * multiplies vector from RIGHT-hand
     * 
     * @param at
     *            result vector
     * @param mt
     *            matrix
     * @param vt
     *            vector
     */
    public static void multmv(MathVector at, Matrix mt, MathVector vt) {
        Dimension dm = mt.getDimension();
        if (dm.width != vt.getDimension())
            throw new ArithmeticException(
                    "different dimension at vector-matrix multiply");
        double t = 0;
        for (int i = 0; i < dm.height; i++) {
            t = 0;
            for (int j = 0; j < dm.width; j++) {
                t += mt.get(i, j) * vt.v(j);
            }
            at.v(i, t);
        }
    }

    /**
     * add matrixes.
     * 
     * @param am
     *            result matrix
     * @param lm
     *            left-hand matrix
     * @param rm
     *            right-hand matrix
     */
    public static void add(Matrix am, Matrix lm, Matrix rm) {
        Dimension ldm, rdm;
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        for (int i = 0; i < ldm.width; i++) {
            for (int j = 0; j < ldm.height; j++) {
                am.set(j, i, lm.get(j, i) + rm.get(j, i));
            }
        }
    }

    /**
     * add matrixes.
     * 
     * @param lm
     *            left-hand matrix, on exit the result
     * @param rm
     *            right-hand matrix
     */
    public static void adds(Matrix lm, Matrix rm) {
        Dimension ldm, rdm;
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        for (int i = 0; i < ldm.width; i++) {
            for (int j = 0; j < ldm.height; j++) {
                lm.set(j, i, lm.get(j, i) + rm.get(j, i));
            }
        }
    }

    /**
     * sub matrixes.
     * 
     * @param am
     *            result matrix
     * @param lm
     *            left-hand matrix
     * @param rm
     *            right-hand matrix
     */
    public static void sub(Matrix am, Matrix lm, Matrix rm) {
        Dimension ldm, rdm;
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        for (int i = 0; i < ldm.width; i++) {
            for (int j = 0; j < ldm.height; j++) {
                am.set(j, i, lm.get(j, i) - rm.get(j, i));
            }
        }
    }

    /**
     * sub matrixes.
     * 
     * @param lm
     *            left-hand matrix, on exit the result
     * @param rm
     *            right-hand matrix
     */
    public static void subs(Matrix lm, Matrix rm) {
        Dimension ldm, rdm;
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        for (int i = 0; i < ldm.width; i++) {
            for (int j = 0; j < ldm.height; j++) {
                lm.set(j, i, lm.get(j, i) - rm.get(j, i));
            }
        }
    }

    /**
     * multiplies matrixs, between left-hand and right-hand. And on exit,on
     * Matrix tm, the result is set.
     * 
     * @param tm
     *            result matrix
     * @param lm
     *            left-hand matrix
     * @param rm
     *            right-hand matrix
     */
    public final static void mult(Matrix tm, Matrix lm, Matrix rm) {
        Dimension ldm, rdm, tdm;
        tdm = tm.getDimension();
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width
                || tdm.height != ldm.height || tdm.width != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        double t;
        for (int i = 0; i < ldm.height; i++) {
            for (int j = 0; j < ldm.width; j++) {
                t = 0;
                for (int k = 0; k < rdm.height; k++) {
                    t += lm.get(i, k) * rm.get(k, j);
                }
                tm.set(i, j, t);
            }
        }
    }

    /**
     * multiplies matrixs, between left-hand and right-hand. And on exit,on
     * Matrix tm, the result is set.
     * 
     * @param lm
     *            left-hand matrix and on exit, the result
     * @param rm
     *            right-hand matrix
     */
    public final static void mults(Matrix lm, Matrix rm) {
        Dimension ldm, rdm;
        ldm = lm.getDimension();
        rdm = rm.getDimension();
        if (ldm.width != rdm.height || ldm.height != rdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        double t;
        double[] cs = new double[rdm.height];

        for (int i = 0; i < ldm.height; i++) {
            for (int j = 0; j < ldm.width; j++) {
                t = 0;
                for (int k = 0; k < rdm.height; k++) {
                    t += lm.get(i, k) * rm.get(k, j);
                }
                cs[j] = t;
            }
            for (int j = 0; j < ldm.width; j++)
                lm.set(i, j, cs[j]);
        }
    }

    /**
     * transpose a matrix.
     * 
     * @param am
     *            result matrix (m * n)
     * @param tm
     *            argument matrix (n * m)
     */
    public static void trans(Matrix am, Matrix tm) {
        Dimension adm, tdm;
        adm = am.getDimension();
        tdm = tm.getDimension();
        if (adm.width != tdm.height || adm.height != tdm.width)
            throw new ArithmeticException(
                    "different dimension at matrix multiply");

        for (int i = 0; i < adm.height; i++) {
            for (int j = 0; j < adm.width; j++) {
                am.set(i, j, tm.get(j, i));
            }
        }
    }

    /** returns simple string dump. */
    public static String toString(Matrix mt) {
        Dimension dm;
        dm = mt.getDimension();
        StringBuffer a = new StringBuffer("matrix ----");
        String n = System.getProperty("line.separator") + " ";
        for (int i = 0; i < dm.height; i++) {
            a.append(n);
            for (int j = 0; j < dm.width; j++)
                a.append(mt.get(i, j) + " ");
        }
        return a.toString();
    }

    /** return string dump with given formatter */
    public static String toString(Matrix mt, NumberFormat nf) {
        Dimension dm;
        dm = mt.getDimension();
        StringBuffer a = new StringBuffer("matrix ----");
        String n = System.getProperty("line.separator") + " ";
        for (int i = 0; i < dm.height; i++) {
            a.append(n);
            for (int j = 0; j < dm.width; j++)
                a.append(nf.format(mt.get(i, j)) + " ");
        }
        return a.toString();
    }

    /** calculate a inverse matrix by gauss-jordan method */
    public static double det(Matrix in) {
        double eps = 1e-6;//
        double det;
        Dimension d = in.getDimension();
        int n = d.width;
        // copy to work matrix
        Matrix wk = in.getCopy();
        det = 1;
        // triangle loop
        for (int k = 0; k < n; k++) {
            double max;
            int imax; // search max row in k-column
            max = Math.abs(wk.get(k, k));
            imax = k;
            for (int j = k + 1; j < n; j++)
                if (max < Math.abs(wk.get(j, k))) {
                    max = Math.abs(wk.get(j, k));
                    imax = j;
                }
            // check the ZERO matrix
            if (max < eps) {
                det = 0;
                return det;
            }
            // swap imax-row and k-row
            // (k-row in k-column must be max)
            if (imax != k) {
                for (int i = 0; i < n; i++) {
                    double tmp = wk.get(k, i);
                    wk.set(k, i, wk.get(imax, i));
                    wk.set(imax, i, tmp);
                }
                det = -det;
            }
            // calc determinant
            det *= wk.get(k, k);

            // make zero elements in k-column, and let k-row be 1.
            double tmp;
            tmp = wk.get(k, k);
            for (int i = 0; i < n; i++) {// "i" seems to begin from k?
                wk.set(k, i, wk.get(k, i) / tmp);
            }
            for (int j = 0; j < n; j++)
                if (j != k) {
                    tmp = wk.get(j, k);
                    for (int i = 0; i < n; i++) {
                        wk.set(j, i, wk.get(j, i) - tmp * wk.get(k, i));
                    }
                }
        }
        return det;
    }

    /** calculate a inverse matrix by gauss-jordan method */
    public static double inverse(Matrix in, Matrix out) {
        double eps = 1e-6;//
        int n;
        Dimension d = in.getDimension();
        n = d.width;
        // copy to work matrix
        Matrix wk = in.getCopy();
        for (int j = 0; j < n; j++)
            for (int i = 0; i < n; i++) {
                out.set(i, j, (i == j) ? 1 : 0);
            }
        // triangle loop
        for (int k = 0; k < n; k++) {
            double max;
            int imax; // search max row in k-column
            max = Math.abs(wk.get(k, k));
            imax = k;
            for (int j = k + 1; j < n; j++)
                if (max < Math.abs(wk.get(j, k))) {
                    max = Math.abs(wk.get(j, k));
                    imax = j;
                }
            // check the ZERO matrix
            if (max < eps) {
                return 0;
            }
            // swap imax-row and k-row
            // (k-row in k-column must be max)
            if (imax != k) {
                for (int i = 0; i < n; i++) {
                    double tmp = wk.get(k, i);
                    wk.set(k, i, wk.get(imax, i));
                    wk.set(imax, i, tmp);
                    tmp = out.get(k, i);
                    out.set(k, i, out.get(imax, i));
                    out.set(imax, i, tmp);
                }
            }

            // make zero elements in k-column, and let k-row be 1.
            double tmp;
            tmp = wk.get(k, k);
            for (int i = 0; i < n; i++) {
                wk.set(k, i, wk.get(k, i) / tmp);
                out.set(k, i, out.get(k, i) / tmp);
            }
            for (int j = 0; j < n; j++)
                if (j != k) {
                    tmp = wk.get(j, k);
                    for (int i = 0; i < n; i++) {
                        wk.set(j, i, wk.get(j, i) - tmp * wk.get(k, i));
                        out.set(j, i, out.get(j, i) - tmp * out.get(k, i));
                    }
                }
        }
        return 1;
    }

    public static void main(String[] args) {
        testGDAnd3D(4000000);
        testLargeMatrix(200, 40);
        testReuseObject(new Matrix3D(), 400000);
        testReuseObject(new MatrixGD(3), 400000);
    }

    private static void testGDAnd3D(int num) {
        int times = 5;
        testGD(3, num);
        test3D(num);
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            testGD(3, num);
        }
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            test3D(num);
        }
        long end = System.currentTimeMillis();
        System.out.println("GD : " + (start2 - start1));
        System.out.println("3D : " + (end - start2));
    }

    private static void testGD(int size, int num) {
        MatrixGD m1 = new MatrixGD(size);
        MatrixGD m2 = new MatrixGD(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m1.set(i, j, Math.random());
                m2.set(i, j, Math.random());
            }
        }
        MatrixGD res = new MatrixGD(size);
        for (int i = 0; i < num; i++) {
            res.substitute(m1);
            res.adds(m2);
        }
    }

    private static void test3D(int num) {
        Matrix3D m1 = new Matrix3D();
        Matrix3D m2 = new Matrix3D();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m1.set(i, j, Math.random());
                m2.set(i, j, Math.random());
            }
        }
        Matrix3D res = new Matrix3D();
        for (int i = 0; i < num; i++) {
            res.substitute(m1);
            res.adds(m2);
        }
    }

    private static void testLargeMatrix(int size, int num) {
        long start1 = System.currentTimeMillis();
        testGD(size, num);
        long end = System.currentTimeMillis();
        System.out.println("Large : " + (end - start1));
    }

    private static void testReuseObject(Matrix sample, int num) {
        int times = 5;
        reuseObject(sample, num);
        createObject(sample, num);

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            reuseObject(sample, num);
        }
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            createObject(sample, num);
        }
        long end = System.currentTimeMillis();
        System.out.println("## Object : " + sample.getClass().getName());
        System.out.println("re-use : " + (start2 - start1));
        System.out.println("create : " + (end - start2));
    }

    private static void reuseObject(Matrix m, int num) {
        Matrix m1 = m.getCopy();
        Matrix m2 = m.getCopy();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m1.set(i, j, Math.random());
                m2.set(i, j, Math.random());
            }
        }
        Matrix res = m1.getCopy();
        for (int i = 0; i < num; i++) {
            // res.mults(m2);
            res.adds(m2);
            res.adds(m1);
        }
    }

    private static void createObject(Matrix m, int num) {
        Matrix m1 = m.getCopy();
        Matrix m2 = m.getCopy();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m1.set(i, j, Math.random());
                m2.set(i, j, Math.random());
            }
        }
        Matrix res = m1.getCopy();
        for (int i = 0; i < num; i++) {
            // res = res.mult(m2);
            res = res.add(m2);
            res = res.add(m1);
        }
    }

}