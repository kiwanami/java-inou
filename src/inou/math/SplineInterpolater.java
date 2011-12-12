/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/*
 * <pre> This is a translation of C program in
 * 
 * Tomy [tomy@tk.airnet.ne.jp] C program source for the Technical calculation
 * http://www5.airnet.ne.jp/tomy/
 * 
 * 1999/9/14 translated by m.sakurai </pre>
 */

/** Spline interpolater */
public class SplineInterpolater implements Interpolater, Serializable {

    private double[] tx;// regular data(made in update())

    private double[] ty;// regular data

    private double[] h; // coefficient

    private double[] sp;// coefficient

    public SplineInterpolater() {
    }

    SplineInterpolater(double[] x, double[] y) {
        update(x, y);
    }

    public void update(double[] xx, double[] yy) {
        int i, k, n = xx.length;
        int[] jun;
        boolean flag = false;

        tx = new double[n];
        ty = new double[n];
        // sort check
        for (i = 0; i < n - 1; i++) {
            if (xx[i] >= xx[i + 1]) {
                flag = true;
                break;
            }
        }
        // sort and copy to buffer
        if (flag) {
            jun = new int[n];
            sort(xx, jun);
            for (i = 0; i < n; i++) {
                tx[i] = xx[jun[i]];
                ty[i] = yy[jun[i]];
            }
        } else {
            for (i = 0; i < n; i++) {
                tx[i] = xx[i];
                ty[i] = yy[i];
            }
        }

        h = new double[n];
        sp = subspl(tx, ty, h);
    }

    public double get(double xi) {
        int i, n = tx.length;
        double dxp, dxm, hi, hi2, sm, si, yi;

        i = MathUtil.binarySearch(tx, xi);// FOR JDK1.1
        if (i == tx.length)
            return ty[tx.length - 1]; // less than
        if (i > 0)
            return ty[i];
        if (i == 0 || i == -1)
            return ty[0];// more than
        if (i < -1)
            i = -i - 1; // correct

        if (tx[i - 1] <= xi && xi < tx[i]) {
            sm = sp[i - 1];
            si = sp[i];
            hi = h[i];
            hi2 = hi * hi;
            dxp = tx[i] - xi;
            dxm = xi - tx[i - 1];
            yi = (sm * dxp * dxp * dxp + si * dxm * dxm * dxm
                    + (6. * ty[i - 1] - hi2 * sm) * dxp + (6. * ty[i] - hi2
                    * si)
                    * dxm)
                    / hi / 6.;
            return yi;
        }
        System.out.println("Sprine : bug");
        return 0;
    }

    private void sort(double[] a, int[] jun) {
        int i, j, k, l, r, s, wi;
        int[] st1 = new int[32];
        int[] st2 = new int[32];
        double x, w;
        int n = a.length;

        s = 0;
        st1[0] = 0;
        st2[0] = n - 1;
        do {
            l = st1[s];
            r = st2[s];
            s--;
            if (r - l < 11) {
                i = l;
                while (i < r) {
                    k = i++;
                    j = i;
                    while (a[jun[k]] > a[jun[j]]) {
                        swap(a[jun[k]], a[jun[j]]);
                        if (j <= l + 1)
                            break;
                        j = k--;
                    }
                }
            } else {
                for (;;) {
                    i = l;
                    j = r;
                    x = a[jun[(l + r) >> 1]];
                    for (;;) {
                        while (x > a[jun[i]])
                            i++;
                        while (a[jun[j]] > x)
                            j--;
                        if (i > j)
                            break;
                        wi = jun[i];
                        jun[i++] = jun[j];
                        jun[j--] = wi;
                        if (i > j)
                            break;
                    }
                    if (j - l < r - i) {
                        if (i < r) {
                            s++;
                            st1[s] = i;
                            st2[s] = r;
                        }
                        r = j;
                    } else {
                        if (l < j) {
                            s++;
                            st1[s] = l;
                            st2[s] = j;
                        }
                        l = i;
                    }
                    if (l >= r)
                        break;
                }
            }
        } while (s >= 0);
        return;
    }

    private final static void swap(double a, double b) {
        double t = a;
        a = b;
        b = t;
    }

    /** spline utility */
    public static double[] subspl(double x[], double y[], double h[]) {
        int i, n = x.length - 1;
        double[] dc;
        double[] du;
        double[] sp;
        double g, hip1;
        dc = new double[n + 1];
        du = new double[n + 1];
        sp = new double[n + 1];

        for (i = 1; i <= n; i++)
            h[i] = x[i] - x[i - 1];
        for (i = 1; i < n; i++) {
            hip1 = x[i + 1] - x[i];
            sp[i] = 6. * ((y[i + 1] - y[i]) / h[i] / hip1 - (y[i] - y[i - 1])
                    / h[i] / h[i]);
            du[i] = h[i + 1] / h[i];
            dc[i] = 2. * (1. + du[i]);
        }
        dc[n] += 1.;
        dc[n - 1] += (h[n] / h[n - 1]);
        du[1] /= dc[1];
        sp[1] /= dc[1];
        for (i = 2; i <= n; i++) {
            g = 1. / (dc[i] - du[i - 1]);
            du[i] *= g;
            sp[i] = (sp[i] - sp[i - 1]) * g;
        }
        for (i = n - 1; i >= 1; i--)
            sp[i] -= sp[i + 1] * du[i];
        sp[0] = sp[1];
        sp[n] = sp[n - 1];
        return sp;
    }

    /** test method */
    public static void main(String[] arg) {
        double[] in = { 0, 1, 4, 9 };
        AArrayFunction a = new AArrayFunction(0, 1, in);
        a.setInterpolater(new SplineInterpolater());
        double x = 0;
        for (int i = 0; i < 10; i++) {
            x = 0.3 * i;
            System.out.println("x=" + x + " f=" + a.f(x));
        }
    }
}
