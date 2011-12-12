/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/** interface of evaluation between sessions. */
public interface RESessionFunction {

    /**
     * @param low
     *            low temperature session
     * @param high
     *            high temperature session
     * @return If F(low) < F(high), return positive value. F(low) > F(high),
     *         return negative value.
     */
    public double evaluate(RESession low, RESession high);
}