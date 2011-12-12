/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.MathVector;
import inou.math.ScalarFunction;

public class ConditionFunction implements ScalarFunction {

    private int dimension = 1;

    private Condition condition;

    public ConditionFunction(Condition condition, int dimension) {
        if (dimension <= 0) {
            throw new ArithmeticException("Illegal dimension.");
        }
        this.dimension = dimension;
        this.condition = condition;
    }

    public void setCondition(Condition c) {
        condition = c;
    }

    public Condition getCondition() {
        return condition;
    }

    /** Return the dimension. */
    public final int getDimension() {
        return dimension;
    }

    public double f(MathVector arg) {
        if (condition.accept(arg))
            return 1;
        return 0;
    }
}