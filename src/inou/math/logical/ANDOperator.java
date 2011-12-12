/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

public class ANDOperator extends BinaryOperator {

    public ANDOperator(Condition left, Condition right) {
        super(left, right);
    }

    protected boolean decides(boolean a, boolean b) {
        return (a && b);
    }

    public String toString() {
        return "(" + getLeft().toString() + " AND " + getRight().toString()
                + ")";
    }
}