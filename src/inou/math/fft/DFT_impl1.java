/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.fft;

import inou.math.Complex;

/**
 * Discrete Fourie Transformation utility class
 * 
 * <pre>
 *  This is a translation of C program in
 * 
 *     Tomy [tomy@tk.airnet.ne.jp]
 *     C program source for the Technical calculation
 *     http://www5.airnet.ne.jp/tomy/
 *     
 *     1999/9/14 translated by sakurai
 * </pre>
 */

public class DFT_impl1 extends DFTEngine {

    // ==================================
    // main routine
    // ==================================

    /**
     * Discrete Fourie Transform (Cooley-Tukey method) Original interface. X[k] =
     * ?½¦?½²_j=0^n-1 x[j]*exp(-2*?½¦?¾?*j*k/n), ( 0 <= k < n)<br>
     * 
     * @param ac
     *            complex 2^n data array. (this routine replaces argument data
     *            into result.)
     * @param flag
     *            inverse switch. if true, work as inverse transformation.
     */
    public final void dft_main(Complex[] ac, boolean flag) {
        int i, it, j, k, xp, xp2;
        double arg, sign, w;
        Complex pc, qc, t1c, t2c, wwc;

        wwc = new Complex();
        t1c = new Complex();
        t2c = new Complex();

        int n = ac.length;
        int iter = 0;
        i = n;
        while ((i /= 2) != 0)
            iter++;
        j = 1;
        for (i = 0; i < iter; i++)
            j *= 2;
        if (n != j) {
            System.err.println("Error : n != 2 ^ k  in DFT");
            return;
        }

        sign = (!flag) ? 1. : -1.;
        xp2 = n;
        for (it = 0; it < iter; it++) {
            xp = xp2;
            xp2 = xp / 2;
            w = Math.PI / xp2;
            for (k = 0; k < xp2; k++) {
                arg = k * w;
                wwc.set(Math.cos(arg), sign * Math.sin(arg));// @
                i = k - xp;
                int ip = i + xp;
                int iq = i + xp + xp2;
                for (j = xp; j <= n; j += xp, ip += xp, iq += xp) {
                    pc = ac[ip];
                    qc = ac[iq];
                    t1c.substitute(pc);
                    t1c.subs(qc);// t1c = pc-qc
                    t2c.substitute(pc);
                    t2c.adds(qc);// t2c = pc+qc
                    ac[ip].substitute(t2c);
                    ac[iq].substitute(t1c.mults(wwc));
                }
            }
        }
        int j1 = n / 2;
        int j2 = n - 1;
        j = 1;
        int ip = 0;
        for (i = 1; i <= j2; i++, ip++) {
            if (i < j)
                ac[ip].swap(ac[j - 1]);
            k = j1;
            while (k < j) {
                j -= k;
                k /= 2;
            }
            j += k;
        }

        return;
    }

}