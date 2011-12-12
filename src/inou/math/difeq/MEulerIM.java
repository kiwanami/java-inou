/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Easy Implementation of DifEqMethod by Euler method. This class solves dy/dx2 =
 * f(x,y) type. (default step interval is 0.05)
 */

public class MEulerIM extends MEuler {

    // ====== constructor

    public MEulerIM() {
        super();
    }

    public MEulerIM(double h) {
        super(h);
    }

    java.util.Hashtable table = new java.util.Hashtable();

    public void reset() {
        table.clear();
    }

    public void step(ScalarFunction df, VariableSet a) {
        VariableSet last = (VariableSet) table.get(df);
        if (last == null) {
            last = new VariableSet(a);
            table.put(df, last);
            // for the first time
            super.step(df, a);
            return;
        }
        double xx = a.x;
        double yy = a.y;
        a.y = last.y + 2. * df.f(a) * h;
        a.x = a.x + h;
        last.set(xx, yy);
    }
}