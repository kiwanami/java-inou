/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/** Simple implementation of Acceptor */
public class DStepAcceptor implements Acceptor {

    public DStepAcceptor() {
    }

    /**
     * decide acceptannce
     */
    public boolean accept(double f) {
        if (f < 0)
            return true;
        return false;
    }

    public void reset() {
    }

}