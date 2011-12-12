/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.solve;

import inou.math.AFunction;
import inou.math.AOperator;
import inou.math.ASolver;

/**
 * The implementation of ASolver for the linear argebraical equation, using
 * Regula-Falsi method
 * 
 * <pre>
 *  This is a translation of C program in
 * 
 *     Tomy [tomy@tk.airnet.ne.jp]
 *     C program source for the Technical calculation
 *     http://www5.airnet.ne.jp/tomy/
 *     
 *     1999/9/16 translated by sakurai
 * </pre>
 */

public class ARegulaFalsi implements ASolver {

    private double xs, xe, h, eps;

    private int iter;

    public ARegulaFalsi() {
        this(-10, 10, 0.1, 1e-6, 1000);
    }

    public ARegulaFalsi(double xs, double xe, double h, double eps, int iter) {
        set(xs, xe, h, eps, iter);
    }

    public void set(double xs, double xe) {
        this.xs = xs;
        this.xe = xe;
    }

    public void set(double xs, double xe, double h, double eps, int iter) {
        set(xs, xe);
        this.h = h;
        this.eps = eps;
        this.iter = iter;
    }

    public AOperator copy() {
        return new ARegulaFalsi(xs, xe, h, eps, iter);
    }

    /*
     * <pre> �?越代数方程�? : レギュラ・ファルシ�?
     * 
     * 関数 _f(x) ?�? 0 となる実�?�を１つ求めます�?? xs, xe は探索区間�?�下限と上限?�?
     * h はきざみ�?、eps は収束判定�?��? iter は繰返し回数許容値です�?? </pre>
     */
    public double operate(AFunction a) {
        int ic;
        double x0 = 0, x1 = 0, x2 = 0, y0 = 0, y1 = 0, y2 = 0;

        if (xs == xe || h <= 0. || eps <= 0. || iter < 1) {
            System.err.println("Error : Illegal parameter in ASlvRegula");
            return 0.;
        }
        if (xs > xe) {
            // swap xs and xe
            double t = xe;
            xe = xs;
            xs = t;
        }
        x0 = xs;
        x1 = xe;
        y1 = a.f(x1);
        do {
            y0 = a.f(x0);
            if ((y0 * y1) > 0.)
                x0 += h;
            else {
                for (ic = 0; ic < iter; ic++) {
                    x2 = x0 - y0 * (x1 - x0) / (y1 - y0);
                    y2 = a.f(x2);
                    if ((y0 * y2) >= 0.) {
                        x0 = x2;
                        y0 = y2;
                    } else {
                        x1 = x2;
                        y1 = y2;
                    }
                    if (Math.abs(y2) <= eps) {
                        last = ic;
                        return x2;
                    }
                }
                System.err.println("Error : No convergence in ASlvRegula");
                return x2;
            }
        } while (x0 <= xe);
        System.err.println("Error : No root in range(" + xs + " - " + xe
                + ") on ASlvRegula");
        return 0.;
    }

    int last;

    public int lastIteration() {
        return last;
    }
}