/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Differential interface */
public interface ADifferentiator extends AOperator {

    public AOperator point(double x);

}