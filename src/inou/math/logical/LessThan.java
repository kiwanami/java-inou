/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.ScalarFunction;

public class LessThan extends Relation {

    public LessThan(ScalarFunction left, ScalarFunction right) {
        super(left, right);
    }

    public LessThan(ScalarFunction left) {
        super(left, null);
    }

    protected boolean decides(double left, double right) {
        return (left < right);
    }

    public String toString() {
        return "(" + getLeft().toString() + " < " + getRight().toString() + ")";
    }
}