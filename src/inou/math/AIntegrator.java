/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Integral interface */
public interface AIntegrator extends AOperator {

    public AOperator range(double sx, double ex);

}