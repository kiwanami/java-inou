/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

public class XOROperator extends BinaryOperator {

    public XOROperator(Condition left, Condition right) {
        super(left, right);
    }

    protected boolean decides(boolean a, boolean b) {
        if (a == b)
            return false;
        return true;
    }

    public String toString() {
        return "(" + getLeft().toString() + " XOR " + getRight().toString()
                + ")";
    }
}