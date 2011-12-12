/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * This class retain random data and evalute as double value. Implements this
 * class to have custom random data.
 */
public interface RandomData {

    /**
     * Evalute a this parameter.
     * 
     * @return evalution value, such as Free energy, entropy, potential...[ must
     *         be minimun achievement ]
     */
    public double evaluate();

    /**
     * Some session make a copy to distribute other session implicitly, such as
     * multi-thread parallel computing. Then they call this method. So in this
     * method you make a copy that has no common objects which shared by
     * original and reproduced object.
     */
    public RandomData getCopy();

    /**
     * if this trial data is accepted, the session calls this method.
     */
    public void onAccepted();

    /**
     * if this trial data is failed, the session calls this method.
     */
    public void onFailed();
}