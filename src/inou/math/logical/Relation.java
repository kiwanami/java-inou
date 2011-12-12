/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.MathVector;
import inou.math.ScalarFunction;

public abstract class Relation implements Condition {

    private ScalarFunction left, right;

    protected Relation(ScalarFunction left, ScalarFunction right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(ScalarFunction left) {
        this.left = left;
    }

    public void setRight(ScalarFunction right) {
        this.right = right;
    }

    public ScalarFunction getLeft() {
        return left;
    }

    public ScalarFunction getRight() {
        return right;
    }

    protected abstract boolean decides(double left, double right);

    public boolean accept(MathVector arg) {
        double leftValue = 0;
        double rightValue = 0;
        if (left != null) {
            leftValue = left.f(arg);
        }
        if (right != null) {
            rightValue = right.f(arg);
        }
        return decides(leftValue, rightValue);
    }
}