/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.fft;

import inou.math.Complex;

/**
 * Discrete Fourie Transformation abstract class
 * 
 */

public abstract class DFTEngine {

    // ==================================
    // main routine
    // ==================================

    /**
     * Discrete Fourie Transform Original interface. X[k] = ?_j=0^n-1
     * x[j]*exp(-2*?*j*k/n), ( 0 <= k < n)<br>
     * 
     * @param ac
     *            complex 2^n data array. (this routine replaces argument data
     *            into result.)
     * @param inverse
     *            if true, work as inverse transformation.
     */
    public abstract void dft_main(Complex[] ac, boolean flag);
}