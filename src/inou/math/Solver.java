/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** algebraic equation */
public interface Solver {

    /**
     * solve scalar function.
     * 
     * @param input
     *            function to solve
     * @return result vector.
     */
    public MathVector solves(ScalarFunction in);

}