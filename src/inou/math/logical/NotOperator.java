/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.MathVector;

public class NotOperator implements Condition {

    private Condition condition;

    public NotOperator(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean accept(MathVector arg) {
        return !(condition.accept(arg));
    }

    public String toString() {
        return "( NOT " + condition.toString() + ")";
    }
}