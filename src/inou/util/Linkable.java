/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.util;

public interface Linkable {

    /**
     * return next linkable object. if null, this object is the last object.
     */
    public Linkable next();

    /**
     * set next object. this method is called by LinkList.
     */
    public void setNext(Linkable nextObject);
}
