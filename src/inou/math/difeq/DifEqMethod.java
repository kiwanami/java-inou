/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.Discrete;
import inou.math.ScalarFunction;

/**
 * DiffEqMethod provide foundamental interface to solve the equation, dy/dx =
 * f(x,y) with a numerical integration.
 */

public interface DifEqMethod extends Discrete {

    /**
     * called to prepare a method environment for new equation.
     */
    public void reset();

    /**
     * calculate next step.
     * 
     * @param df
     *            differential equation
     * @param a
     *            variable set (x,y), (x,y,y') or etc... And in exit, the
     *            parameter is set the result. <br>
     *            <br>
     *            Note : This class is sometimes used by some equations, not
     *            only one. So you should be carefull, if your implementation
     *            class has field valus. (See the example class [MAdamsPC, and
     *            SimDiffEqSolver classes])
     * 
     */
    public void step(ScalarFunction df, VariableSet a);

}