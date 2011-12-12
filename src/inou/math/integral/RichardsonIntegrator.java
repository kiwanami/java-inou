/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.integral;

import inou.math.AFunction;
import inou.math.AFunctionClass;
import inou.math.AIntegrator;
import inou.math.AOperator;
import inou.math.Discrete;
import inou.math.HPSum;
import inou.math.Integrator;
import inou.math.MathVector;
import inou.math.Operator;
import inou.math.ScalarFunction;
import inou.math.vector.Vector1D;

import java.io.Serializable;

/** partial integral operator for ScalarFunction */
public class RichardsonIntegrator implements AIntegrator, Integrator,
        Serializable, Discrete {

    protected MathVector sp, ep;

    protected double width;

    protected double h = 0.01;

    protected int colm = 0, num;

    // ========================
    // constructor
    // ========================

    public RichardsonIntegrator() {
    }

    public RichardsonIntegrator(double h) {
        this.h = h;
    }

    // ========================
    // operate
    // ========================

    public void setDiscreteSize(double h) {
        this.h = h;
    }

    public double getDiscreteSize() {
        return h;
    }

    public Operator range(MathVector tsp, double w, int c) {
        if (sp == null) {
            sp = tsp.getCopy();
            ep = sp.getCopy();
        } else {
            sp.substitute(tsp);
            ep.substitute(tsp);
        }
        if (curPos == null) {
            curPos = sp.getCopy();
        }
        width = w;
        colm = c;

        ep.v(colm, ep.v(colm) + width);
        return this;
    }

    public AOperator range(double sx, double ex) {
        if (sp == null || ep == null || sp.getDimension() != 1) {
            sp = new Vector1D(sx);
            ep = new Vector1D(ex);
        } else {
            sp.v(0, sx);
            ep.v(0, ex);
        }
        if (curPos == null) {
            curPos = sp.getCopy();
        }
        width = ex - sx;
        colm = 0;
        return this;
    }

    public double operate(AFunction a) {
        return operate((ScalarFunction) a);
    }

    private MathVector curPos;

    private HPSum summation = new HPSum();

    private double calc(ScalarFunction a, double dh) {
        summation.reset();
        int num = (int) Math.abs(width / dh);
        double x = sp.v(colm);
        curPos.substitute(sp);

        summation.add((a.f(sp) + a.f(ep)) * 0.5);
        for (int i = 1; i < num; i++) {
            curPos.v(colm, x + i * dh);
            summation.add(a.f(curPos));
        }
        double w = summation.get();
        return w * dh;
    }

    public double operate(ScalarFunction a) {
        return (calc(a, h) - 0.25 * calc(a, h * 2)) * 1.3333333333333333333333333333333;
    }

    public static void main(String[] args) {
        check(AFunctionClass.getFunction("sin(x)"), 0, Math.PI);
        check(AFunctionClass.getFunction("exp(x)"), 0, 1);
        check(AFunctionClass.getFunction("2/(1+x*x)"), -1, 1);
        check(AFunctionClass.getFunction("exp(-x*x)"), 0, 1);
        check(AFunctionClass.getFunction("4*sqrt(1-x*x)"), 0, 1);
    }

    private static void check(AFunction f, double sx, double ex) {
        System.out.println("----------------");
        System.out.println("| " + f.toString());
        RichardsonIntegrator ri = new RichardsonIntegrator();
        int mini = 3;
        int maxi = 10;
        double[] ansr = new double[10 - 3 + 1];
        for (int i = mini; i <= maxi; i++) {
            int num = 1 << i;
            double dx = (ex - sx) / num;
            ri.setDiscreteSize(dx);
            double ans = ri.range(sx, ex).operate(f);
            System.out.println("N=" + num + "  : " + ans);
            ansr[i - mini] = ans;
        }
        for (int i = 2; i <= (maxi - mini); i++) {
            System.out.println((Math.pow(2, -4) - (ansr[i] - ansr[i - 1])
                    / (ansr[i - 1] - ansr[i - 2])));
        }
    }
}