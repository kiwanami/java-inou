/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * Decide acceptance.
 */
public interface Acceptor {

    /**
     * @param cur
     *            value to decide
     * @return if the value is acceptable, return true. Otherwise, false.
     */
    public boolean accept(double cur);

    /**
     * reset internal state
     */
    public void reset();
}