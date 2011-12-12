/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/** This class is used in MCSession to do something. */

public abstract class MCEvent {

    private MCSession session;

    protected void setSession(MCSession s) {
        session = s;
    }

    public final MCSession getSession() {
        return session;
    }

    public abstract void event(RandomData d, boolean s);

}