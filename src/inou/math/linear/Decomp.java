/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.linear;

import inou.math.MathVector;
import inou.math.Matrix;

import java.awt.Dimension;

/**
 * Linear system solver by LU decomposition. (Simple implementation)
 */

public class Decomp extends LinearEngine {

    /**
     * calculate linear algebraical equations. [ A*x = b ]
     * 
     * @param A
     *            coefficient matrix (On exit, the argument is changed)
     * @param b
     *            constant vector, on exit, the result, x.
     * @return result x.
     */
    public MathVector solves(Matrix A, MathVector b) {
        Dimension d = A.getDimension();
        if (d.width != d.height) {
            throw new ArithmeticException("Matrix A is not square.");
        }
        if (d.width != b.getDimension())
            throw new ArithmeticException(
                    "The dimension of vector b is not the same as Matrix A.");
        int[] pivt = new int[d.width];// pivot array
        ludcmp(A, pivt);
        lubksb(A, pivt, b);
        return b;
    }

    /**
     * LU decomposition
     * 
     * @param A
     *            input matrix, and on exit, decomposit result
     * @param pivt
     *            pivot table
     */
    public void ludcmp(Matrix A, int[] pivt) {
        int n = A.getDimension().width;
        int nn = n - 1;
        int imax = 0;
        double w, dum, sum, temp, d;

        // pivot
        for (int i = 0; i < n; i++) {
            imax = i;
            dum = Math.abs(A.get(i, i));
            if (i < nn) {
                for (int j = i + 1; j < n; j++)
                    if (Math.abs(A.get(j, i)) > dum) {
                        imax = j;
                        dum = Math.abs(A.get(j, i));
                    }
                if (imax != i) {
                    for (int k = 0; k < n; k++) {
                        temp = A.get(imax, k);
                        A.set(imax, k, A.get(i, k));
                        A.set(i, k, temp);
                    }
                }
            }
            pivt[i] = imax;
        }

        for (int k = 0; k < nn; k++) {
            if (A.get(k, k) == 0.)
                throw new ArithmeticException("Singular Matrix : " + k);
            w = 1. / A.get(k, k);
            for (int i = (k + 1); i < n; i++) {
                A.set(i, k, A.get(i, k) * w);
                for (int j = (k + 1); j < n; j++)
                    A.set(i, j, A.get(i, j) - A.get(i, k) * A.get(k, j));
            }
        }
    }

    /**
     * forward substitution and backsubstitution A * X = b
     * 
     * @param A
     *            decomposited matrix
     * @param pivt
     *            pivot table
     * @param b
     *            input vector, and on exit, result vector X
     */
    public void lubksb(Matrix A, int[] pivt, MathVector b) {
        int ii = -1, ip;
        int n = b.getDimension();
        int nn = n - 1;
        double sum, t;

        /*
         * debug System.out.println(A.toString());
         * 
         * System.out.print("pivt :"); for (int i=0;i<n;i++) System.out.print("
         * "+pivt[i]); System.out.println();
         */

        // compute y
        for (int i = 0; i < n; i++) {
            ip = pivt[i];
            t = b.v(ip);
            if (ip != i) {
                b.v(ip, b.v(i));
                b.v(i, t);
            }
            if (i == 0)
                continue;

            for (int j = 0; j < i; j++) {
                t -= A.get(i, j) * b.v(j);
            }
            b.v(i, t);
        }

        // System.out.println(b.toString());//debug

        // compute x
        b.v(nn, b.v(nn) / A.get(nn, nn));
        for (int i = (nn - 1); i >= 0; i--) {
            t = b.v(i);
            for (int j = i + 1; j < n; j++)
                t -= A.get(i, j) * b.v(j);
            b.v(i, t / A.get(i, i));
        }
    }

}