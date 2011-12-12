/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import java.util.Hashtable;

/** Selection utility */
public class SecondMethods extends MethodFactory {

    public SecondMethods() {
        table = new Hashtable();
        table.put("Euler method", new MEuler2());
        table.put("Runge-Kutta-Nistryeme method", new MRungeKutta2());
        table.put("Sympletic Integral method", new MSympletic2());
    }

    public DifEqMethod getInstance(String name) {
        return (DifEqMethod) table.get(name);
    }
}