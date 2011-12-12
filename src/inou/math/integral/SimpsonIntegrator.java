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

/** partial simpson integral operator for ScalarFunction */
public class SimpsonIntegrator implements AIntegrator, Integrator,
        Serializable, Discrete {

    protected MathVector sp, ep;

    protected double width;

    protected double h = 0.01;

    protected int colm = 0, num;

    // ========================
    // constructor
    // ========================

    public SimpsonIntegrator() {
    }

    public SimpsonIntegrator(double h) {
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
        width = w;
        colm = c;

        ep.v(colm, ep.v(colm) + width);
        frag();
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
        width = ex - sx;
        colm = 0;
        frag();
        return this;
    }

    private void frag() {
        num = Math.abs((int) (width / h)) / 2;
        sh = width / num / 2;
        if (curPos == null) {
            curPos = sp.getCopy();
        }
    }

    private double sh;

    private MathVector curPos;

    private HPSum summation = new HPSum();

    public double operate(AFunction a) {
        return operate((ScalarFunction) a);
    }

    public double operate(ScalarFunction a) {
        summation.reset();
        curPos.substitute(sp);

        summation.add(-a.f(sp) + a.f(ep));
        for (int i = 0; i < num; i++) {
            summation.add(2. * a.f(curPos));
            curPos.v(colm, curPos.v(colm) + sh);
            summation.add(4. * a.f(curPos));
            curPos.v(colm, curPos.v(colm) + sh);
        }
        double w = summation.get();
        /*
         * if ( sp.v(colm) > ep.v(colm) ) { w *= -1; }
         */
        return w * sh / 3.;
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
        SimpsonIntegrator ri = new SimpsonIntegrator();
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