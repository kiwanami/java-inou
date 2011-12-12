/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * Diffenentiatable interface, which can return the partial derived function
 */
public interface Differentiatable {
    public ScalarFunction getDerivedFunction(int colm);
}