/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * Vector function interface.
 */
public interface VectorFunction {

    /** Vector function */
    public MathVector f(MathVector arg);

    /** Return the dimension the function variable. */
    public int getDimension();

    /** Return the dimension the argument variable. */
    public int getArgDimension();

}