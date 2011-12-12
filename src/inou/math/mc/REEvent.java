/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/** This class is used in REManager to do something. */
public abstract class REEvent {

    protected REManager manager;

    protected void setManager(REManager s) {
        manager = s;
    }

    public REManager getManager() {
        return manager;
    }

    public abstract void event();

}