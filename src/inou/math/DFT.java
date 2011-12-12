/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.fft.DFTEngine;
import inou.math.fft.DFT_impl1;

/** Discrete Fourie Transformation utility class */

public class DFT {

    private DFTEngine engine = new DFT_impl1();

    /** construct with default DFT engine */
    public DFT() {
    }

    /** construct */
    public DFT(String name) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        engine = (DFTEngine) (Class.forName(name).newInstance());
    }

    // ==================================
    // 1D complex fourie transformation
    // ==================================

    /**
     * Discrete Fourie Transform X[k] = Σ_j=0^n-1 x[j]*exp(-2*PI*j*k/n), ( 0 <=
     * k < n)<br>
     * 
     * @param ac
     *            complex 2^n data array (this routine doesn't change argument
     *            data.)
     * @return transformed complex array
     */
    public final Complex[] dft(Complex[] ac) {
        // make buffer array
        Complex[] rc = new Complex[ac.length];
        for (int i = 0; i < ac.length; i++)
            rc[i] = ac[i].getCopy();
        engine.dft_main(rc, false);
        return rc;
    }

    /**
     * Discrete Fourie Transform X[k] = Σ_j=0^n-1 x[j]*exp(-2*PI*j*k/n), ( 0 <=
     * k < n)<br>
     * 
     * @param ac
     *            complex 2^n data array. (this routine replaces argument data
     *            into result.) Little faster than complex FFT
     */
    public final void dfts(Complex[] ac) {
        engine.dft_main(ac, false);
    }

    /**
     * Inverse Discrete Fourie Transform X[k] = (1/n)*Σ_j=0^n-1
     * x[j]*exp(2*PI*j*k/n), ( 0 <= k < n)<br>
     * 
     * @param ac
     *            complex data array ( this routine doesn't change argument
     *            data.)
     * @return transformed complex array
     */
    public final Complex[] idft(Complex[] ac) {
        // make buffer array
        Complex[] rc = new Complex[ac.length];
        for (int i = 0; i < ac.length; i++)
            rc[i] = ac[i].getCopy();

        idfts(rc);

        return rc;
    }

    /**
     * Inverse Discrete Fourie Transform X[k] = (1/n)*Σ_j=0^n-1
     * x[j]*exp(2*PI*j*k/n), ( 0 <= k < n)<br>
     * 
     * @param ac
     *            complex data array. and return the transformed array
     */
    public final void idfts(Complex[] ac) {
        engine.dft_main(ac, true);
        int n = ac.length;
        double wr = 1. / (double) n;
        for (int i = 0; i < n; i++) {
            ac[i].mults(wr);
        }
    }

    /** test method (usage) */
    public static void main(String[] arg) {
        final int num = 64;
        double ar[] = new double[num];
        double ai[] = new double[num];
        Complex[] ac;
        // square function
        /*
         * for (int i=0;i<(num/2);i++) ar[i] = 1; ac =
         * Complex.translate(ar,ai); print(ac);
         */
        /*
         * //exponential function num = 64; ar = new double [num]; AFunction q =
         * new AFunction() { public double f(double x) { return Math.exp(x); } };
         * double dx = 10.0/num; for (int i=0;i<num;i++) ar[i] = q.f(dx*i); ac =
         * Complex.translate(ar); print(ac);
         */
        // sin function
        ar = new double[num];
        AFunction q = new AFunction() {
            public double f(double x) {
                // return Math.exp(-x*x);
                return Math.cos(x);
            }
        };
        double dx = 10.0 / num;
        for (int i = 0; i < num; i++) {
            ar[i] = q.f(dx * i);
        }
        ac = Complex.translate(ar);
        print(ac, 0, dx);
    }

    private static void print(Complex[] ac, double sx, double dx) {
        int num = ac.length;
        DFT fft = new DFT();
        double x;
        // System.out.println("------translated");
        // Complex [] ac2 = fft.dfts(ac);
        fft.dfts(ac);
        Complex[] ac2 = ac;
        // System.out.println("------inverse translated");
        fft.idfts(ac2);

        for (int i = 0; i < num; i++) {
            x = sx + dx * i;
            System.out.println("" + x + " " + ac2[i].r + " " + ac2[i].i);
        }
    }
}
