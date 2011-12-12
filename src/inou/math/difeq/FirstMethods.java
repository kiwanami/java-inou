/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import java.util.Hashtable;

/** Selection utility */
public class FirstMethods extends MethodFactory {

    public FirstMethods() {
        table = new Hashtable();
        table.put("Euler method", new MEuler());
        // table.put("Improved Euler method",new MEulerIM());
        table.put("Runge-Kutta method", new MRungeKutta());
        table.put("Adams Predictor and Corrector method", new MAdamsPC());
    }

    public DifEqMethod getInstance(String name) {
        return (DifEqMethod) table.get(name);
    }
}