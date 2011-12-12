/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * Integratable interface, which can return the partial integrated function
 */
public interface Integratable {
    public ScalarFunction getIntegratedFunction(int colm);
}