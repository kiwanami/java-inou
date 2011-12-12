/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.integral;

import java.io.Serializable;

/**
 * Numerical integral class
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

public class Gauss20Integrator extends GaussIntegrator implements Serializable {

    private static double[] ag = { 0.076526521133497333, 0.227785851141645078,
            0.373706088715419561, 0.510867001950827098, 0.636053680726515025,
            0.746331906460150793, 0.839116971822218823, 0.912234428251325906,
            0.963971927277913791, 0.993128599185094925 };

    private static double[] aw = { 0.152753387130725851, 0.149172986472603747,
            0.142096109318382051, 0.131688638449176627, 0.118194531961518417,
            0.101930119817240435, 0.083276741576704749, 0.062672048334109064,
            0.040601429800386941, 0.017614007139152118 };

    public Gauss20Integrator() {
        super(ag, aw);
    }

}