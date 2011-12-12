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
 * 	  Tomy [tomy@tk.airnet.ne.jp]
 * 	  C program source for the Technical calculation
 * 	  http://www5.airnet.ne.jp/tomy/
 * 	  
 * 	  1999/9/14 translated by sakurai
 * </pre>
 */

public class Gauss10Integrator extends GaussIntegrator implements Serializable {

    private static double ag[] = { 0.148874338981631, 0.433395394129247,
            0.679409568299024, 0.865063366688985, 0.973906528517172 };

    private static double aw[] = { 0.295524224714753, 0.269266719309996,
            0.219086362515982, 0.149451349150581, 0.066671344308688 };

    public Gauss10Integrator() {
        super(ag, aw);
    }

}