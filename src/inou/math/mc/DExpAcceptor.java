/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * this object decides differential value dF. if (f<0 || exp(-f)>random() )
 * return true.
 * 
 */
public class DExpAcceptor implements Acceptor {

    public boolean accept(double f) {
        if (f < 0 || Math.exp(-f) > Math.random()) {
            return true;
        }
        return false;
    }

    public void reset() {
    }

}
