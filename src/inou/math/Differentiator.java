/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Differential operator for the general function */
public interface Differentiator extends Operator {
    public Differentiator point(MathVector pos, int colm);
}