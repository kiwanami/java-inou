/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

public class OROperator extends BinaryOperator {

    public OROperator(Condition left, Condition right) {
        super(left, right);
    }

    protected boolean decides(boolean a, boolean b) {
        return (a || b);
    }

    public String toString() {
        return "(" + getLeft().toString() + " OR " + getRight().toString()
                + ")";
    }
}