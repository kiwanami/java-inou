/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Partial integral operator */
public interface Integrator extends Operator {

    public Operator range(MathVector sp, double w, int colm);

}