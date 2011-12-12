/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.VectorGD;

/** Solver for the Fredholm integral equation */
public class FredholmIntegralEq {

    double sx = 0, ex = 1;

    int num = 10;

    /** test method */
    public static void main(String[] a) {
        FredholmIntegralEq eq = new FredholmIntegralEq();
        double[] f;// obtained function
        // given function "q"
        AFunction q = new AFunction() {
            public double f(double x) {
                return x * x * 21;
            }
        };
        // given function "p"
        ScalarFunction p = new ScalarFunction() {
            public double f(MathVector arg) {
                // v[0]:x
                // v[1]:t
                return arg.v(0) * arg.v(0) * arg.v(1);
            }

            public int getDimension() {
                return 2;
            }
        };
        double tsx = 1, tex = 3;
        int tnum = 10;
        f = eq.range(tsx, tex, tnum).solve(q, p);
        // print
        double x, h = (tex - tsx) / tnum;
        for (int i = 0; i < f.length; i++) {
            x = tsx + h * i;
            System.out.println("x=" + x + "  f=" + f[i]);
        }
    }

    /** set solve range and fragment number */
    public FredholmIntegralEq range(double start, double end, int number) {
        if (start <= end) {
            sx = start;
            ex = end;
        } else {
            ex = start;
            sx = end;
        }
        number = Math.abs(number);
        num = number + 1 - number % 2;// even number
        return this;
    }

    /** solve the equation */
    public double[] solve(AFunction q, ScalarFunction p) {
        if (p.getDimension() < 2) {
            System.out.println("Illeagal input function.");
            return null;
        }
        MathVector arg = new VectorGD(p.getDimension());
        double[][] work = new double[num][num + 1];// work space
        double[] f = new double[num]; // obtained function
        double h = (ex - sx) / (num - 1); // discrete size
        double t = sx; // parameter "t"
        double x; // parameter "x"
        double w; // temporary value

        // initialize workspace

        for (int j = 0; j < num; j++) {
            // w =1(j=0,num), =2(j:even), =4(j:odd)
            w = 2 + 2 * odd(j) - (zero(j) + zero(j - num + 1));
            x = sx;
            for (int i = 0; i < num; i++) {
                arg.v(0, x);
                arg.v(1, t);
                work[i][j] = zero(i - j) + w * p.f(arg) * h / 3;
                x += h;
            }
            t += h;
        }
        x = sx;
        for (int i = 0; i < num; i++) {
            work[i][num] = q.f(x);
            x += h;
        }

        // main loop (triangle loop)

        int m;
        double y, pool;
        for (int k = 0; k < num; k++) {
            m = k;
            for (int i = k + 1; i < num; i++) {
                if (Math.abs(work[m][k]) < Math.abs(work[i][k]))
                    m = i;
            }
            y = work[m][k];
            for (int j = k; j < num + 1; j++) {
                pool = work[m][j];
                work[m][j] = work[k][j];
                work[k][j] = pool / y;
            }
            for (int i = k + 1; i < num; i++) {
                y = work[i][k];
                for (int j = k; j < num + 1; j++) {
                    work[i][j] = work[i][j] - y * work[k][j];
                }
            }
        }

        for (int i = num - 1; i >= 0; i--) {
            f[i] = work[i][num];
            for (int j = i + 1; j < num; j++) { // warning
                f[i] = f[i] - work[i][j] * f[j];
            }
        }
        return f;
    }

    private static int zero(int i) {
        if (i == 0)
            return 1;
        return 0;
    }

    private static int odd(int i) {
        return i % 2;
    }
}