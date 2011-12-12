/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

/**
 * This inteface is used in the differencial equation solver class,
 * [DifEqSolver] [DifEqSolver2].
 */
public interface StepListener {

    /**
     * DifEqSolvers will call this method to let you know the values.
     * 
     * @param current
     *            current values. NEVER CHANGE THIS VALUES.
     * @return if false, DifEqSolvers will stop the loop. And true, continue.
     */
    public boolean step(VariableSet current);

}