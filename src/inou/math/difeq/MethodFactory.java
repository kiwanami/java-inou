/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The class factory of methods class of differential equation numerical solver.
 * Subclass must initialize table object.
 */
public abstract class MethodFactory {

    protected Hashtable table = null;

    public String[] getMethods() {
        if (table.isEmpty())
            return null;
        Enumeration e = table.keys();
        String[] keys = new String[table.size()];
        for (int i = 0; e.hasMoreElements(); i++) {
            keys[i] = (String) e.nextElement();
        }
        return keys;
    }

}