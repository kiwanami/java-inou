/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

public class Variable extends ScalarFunctionClass {

    private int col;

    private int pow = 1;

    private Variable() {
        super(0);
    }

    public Variable(int col, int dim) {
        this(col, dim, 1);
    }

    public Variable(int col, int dim, int pow) {
        super(dim);
        this.pow = pow;
        this.col = col;
    }

    public int getPower() {
        return pow;
    }

    public double f(MathVector x) {
        if (pow == 1) {
            return x.v(col);
        }
        return Math.pow(x.v(col), pow);
    }

    public int getColumn() {
        return col;
    }

    public ScalarFunction getDerivedFunction(int c) {
        if (c == col) {
            if (pow == 1) {
                return new Unit(getDimension());
            }
            return FunctionUtil.multiple(new Variable(col, getDimension(),
                    pow - 1), pow);
        }
        return new Zero(getDimension());
    }

    public String toString() {
        String ret = null;
        if (getDimension() == 1) {
            ret = "x";
        } else if (getDimension() < 4) {
            switch (col) {
            case 0:
                ret = "x";
                break;
            case 1:
                ret = "y";
                break;
            case 2:
                ret = "z";
                break;
            }
        } else {
            ret = "x[" + col + "]";
        }
        if (getPower() != 1) {
            ret += "**" + getPower();
        }
        return ret;
    }

}