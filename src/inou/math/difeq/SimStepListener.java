/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

/**
 * This inteface is used in the simultaneous differencial equation solver class,
 * [SimDiffEqSolver] [SimDiffEqSolver2].
 */
public interface SimStepListener {

    /**
     * SimDiffEqSolvers will call this method to let you know the values.
     * 
     * @param current
     *            current values. NEVER CHANGE THIS VALUES.
     * @return if false, DiffEqSolvers will stop the loop. And true, continue.
     */
    public boolean step(VariableSet[] current);

}