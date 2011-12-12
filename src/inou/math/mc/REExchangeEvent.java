/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * This class does something at exchanging between replicas.
 */
public abstract class REExchangeEvent {

    protected REManager manager;

    protected void setManager(REManager s) {
        manager = s;
    }

    public REManager getManager() {
        return manager;
    }

    /**
     * if exchanging between replica systems is occured, the RE-Manager calls
     * this method after exchanging.
     */
    public abstract void event(int lowerIndex);

}