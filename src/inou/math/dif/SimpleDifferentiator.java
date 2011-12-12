/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.dif;

import inou.math.ADifferentiator;
import inou.math.AFunction;
import inou.math.AFunctionClass;
import inou.math.AOperator;
import inou.math.Differentiator;
import inou.math.Discrete;
import inou.math.MathVector;
import inou.math.Operator;
import inou.math.ScalarFunction;
import inou.math.vector.Vector1D;

import java.io.Serializable;

/** first differential operator */
public class SimpleDifferentiator implements ADifferentiator, Differentiator,
        Serializable, Discrete {

    private MathVector pos, calc;

    private int column;

    private double h = 1e-5;

    public SimpleDifferentiator() {
    }

    public SimpleDifferentiator(double h) {
        setDiscreteSize(h);
    }

    public void setDiscreteSize(double h) {
        if (h > 0) {
            this.h = h;
        }
    }

    public double getDiscreteSize() {
        return h;
    }

    public final Differentiator point(MathVector pos, int col) {
        this.pos = pos;
        if (calc == null || calc.getDimension() != pos.getDimension()) {
            calc = pos.getCopy();
        }
        this.column = col;
        return this;
    }

    public final AOperator point(double x) {
        if (this.pos == null || this.pos.getDimension() != 1) {
            this.pos = new Vector1D(x);
        } else {
            this.pos.v(0, x);
        }
        if (calc == null || calc.getDimension() != pos.getDimension()) {
            calc = pos.getCopy();
        }
        this.column = 0;
        return this;
    }

    public Operator copy() {
        return new SimpleDifferentiator(h);
    }

    public final double operate(AFunction func) {
        return operate((ScalarFunction) func);
    }

    public final double operate(ScalarFunction func) {
        calc.substitute(pos);
        double org = calc.v(column);
        calc.v(column, org - h);
        double left = func.f(calc);
        calc.v(column, org + h);
        double right = func.f(calc);
        return 0.5 * (right - left) / h;
    }

    public static void main(String[] args) {
        AFunction func = AFunctionClass.getFunction("sin(x)");
        AFunction ans = AFunctionClass.getFunction("cos(x)");
        double h = 0.1;
        SimpleDifferentiator dif = new SimpleDifferentiator();
        dif.setDiscreteSize(h);
        double result = 0;
        Vector1D pos = new Vector1D();
        double dx = 0.1;
        for (int j = 0; j < 10; j++) {
            double vdif = dif.point(pos, 0).operate(func);
            double vans = ans.f(pos.x);
            System.out.println("h:" + h + "  dif:" + vdif + "  ans:" + vans
                    + "  (" + Math.abs(vdif - vans) + ")");
            pos.x += dx;
        }
    }
}