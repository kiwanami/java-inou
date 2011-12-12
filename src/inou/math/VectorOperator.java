/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** General operator for VectorFunction */
public interface VectorOperator {

    public MathVector operate(VectorFunction f);

}