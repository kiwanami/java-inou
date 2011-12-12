/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * Default implementation.
 */
public abstract class RandomDataClass implements RandomData {

    /**
     * Evalute a this parameter.
     * 
     * @return evalution value, such as Free energy, entropy, potential...[ must
     *         be minimun achievement ]
     */
    // public double evalute();
    /**
     * Some session make a copy to distribute other session implicitly, such as
     * multi-thread parallel computing. Then they call this method. So in this
     * method you make a copy that has no common objects which shared by
     * original and reproduced object.
     */
    // public RandomData getCopy();
    public void onAccepted() {
        // do nothing
    }

    public void onFailed() {
        // do nothing
    }
}