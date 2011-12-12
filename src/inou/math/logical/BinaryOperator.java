/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.MathVector;

public abstract class BinaryOperator implements Condition {

    private Condition left, right;

    protected BinaryOperator(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(Condition left) {
        this.left = left;
    }

    public void setRight(Condition right) {
        this.right = right;
    }

    public Condition getLeft() {
        return left;
    }

    public Condition getRight() {
        return right;
    }

    protected abstract boolean decides(boolean a, boolean b);

    public boolean accept(MathVector arg) {
        return decides(left.accept(arg), right.accept(arg));
    }

}