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

/**
 * partial double-exponential integral operator for ScalarFunction
 */
public class DEIntegrator implements AIntegrator, Integrator, Serializable,
        Discrete {

    protected MathVector sp, ep;

    protected double width, additive;

    protected double halfWidth, halfAdditive;

    protected double h = 0.25;

    protected double err;

    protected int colm = 0;

    // ========================
    // constructor
    // ========================

    public DEIntegrator() {
    }

    public DEIntegrator(double h) {
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
            curPos = sp.getCopy();
        } else {
            sp.substitute(tsp);
            ep.substitute(tsp);
        }
        colm = c;
        ep.v(colm, ep.v(colm) + w);

        adjustParam();

        return this;
    }

    private void adjustParam() {
        if (curPos == null) {
            curPos = sp.getCopy();
        }
        double sx = sp.v(colm);
        double ex = ep.v(colm);
        if (ex == Double.POSITIVE_INFINITY) {
            width = 0;
            halfAdditive = 0;
            additive = 0;
            halfWidth = 0;
            if (sx == 0) {
                transFunction = halfInfiniteFunction;
                transFunctionJacobian = halfInfiniteFunctionJacobian;
            } else if (sx == Double.NEGATIVE_INFINITY) {
                transFunction = infiniteFunction;
                transFunctionJacobian = infiniteFunctionJacobian;
            } else {
                throw new ArithmeticException(
                        "Not supported integral range. [ " + sx + " - " + ex
                                + " ]");
            }
        } else {
            transFunction = finiteFunction;
            transFunctionJacobian = finiteFunctionJacobian;
            width = ep.v(colm) - sp.v(colm);
            halfWidth = width * 0.5;
            additive = ep.v(colm) + sp.v(colm);
            halfAdditive = additive * 0.5;
        }
    }

    public AOperator range(double sx, double ex) {
        if (sp == null || ep == null || sp.getDimension() != 1) {
            sp = new Vector1D(sx);
            ep = new Vector1D(ex);
        } else {
            sp.v(0, sx);
            ep.v(0, ex);
        }
        colm = 0;
        adjustParam();
        return this;
    }

    private static double sinh(double x) {
        return 0.5 * (Math.exp(x) - Math.exp(-x));
    }

    private static double cosh(double x) {
        return 0.5 * (Math.exp(x) + Math.exp(-x));
    }

    private static double tanh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x));
    }

    private static double HPI = Math.PI * 0.5;

    private AFunctionClass finiteFunction = new AFunctionClass() {
        public double f(double x) {
            return halfWidth * tanh(HPI * sinh(x)) + halfAdditive;
        }
    };

    private AFunctionClass finiteFunctionJacobian = new AFunctionClass() {
        public double f(double x) {
            double in = cosh(HPI * sinh(x));
            return Math.PI * halfWidth * cosh(x) / (2 * in * in);
        }
    };

    private AFunctionClass halfInfiniteFunction = new AFunctionClass() {
        public double f(double x) {
            return Math.exp(HPI * sinh(x));
        }
    };

    private AFunctionClass halfInfiniteFunctionJacobian = new AFunctionClass() {
        public double f(double x) {
            return HPI * cosh(x) * Math.exp(HPI * sinh(x));
        }
    };

    private AFunctionClass infiniteFunction = new AFunctionClass() {
        public double f(double x) {
            return sinh(HPI * sinh(x));
        }
    };

    private AFunctionClass infiniteFunctionJacobian = new AFunctionClass() {
        public double f(double x) {
            return HPI * cosh(x) * cosh(HPI * sinh(x));
        }
    };

    private MathVector curPos;

    private HPSum summation = new HPSum();

    private AFunctionClass transFunction;

    private AFunctionClass transFunctionJacobian;

    public double operate(AFunction a) {
        return operate((ScalarFunction) a);
    }

    public double operate(ScalarFunction a) {
        summation.reset();
        curPos.substitute(sp);
        double limit = 4.0;
        int num = (int) (limit / h);
        for (int i = -num; i <= num; i++) {
            double t = h * i;
            curPos.v(colm, transFunction.f(t));
            summation.add(a.f(curPos) * transFunctionJacobian.f(t));
        }
        return summation.get() * h;
    }

    public static void main(String[] args) {
        check(AFunctionClass.getFunction("sin(x)"), 0, Math.PI);
        check(AFunctionClass.getFunction("exp(x)"), 0, 1);
        check(AFunctionClass.getFunction("2/(1+x*x)"), -1, 1);
        check(AFunctionClass.getFunction("exp(-x*x)"), 0, 1);
        check(AFunctionClass.getFunction("4*sqrt(1-x*x)"), 0, 1);
        System.out.println("---(0 .. INF)------");
        check(AFunctionClass.getFunction("1/(1+x)**2"), 0,
                Double.POSITIVE_INFINITY);
        check(AFunctionClass.getFunction("ln(x)/(1+x*x)"), 0,
                Double.POSITIVE_INFINITY);
        System.out.println("---(-INF .. INF)------");
        check(AFunctionClass.getFunction("1/(1+x*x)"),
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        check(AFunctionClass.getFunction("exp(-x*x)"),
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    private static void check(AFunction f, double sx, double ex) {
        System.out.println("----------------");
        System.out.println("| " + f.toString());
        DEIntegrator ri = new DEIntegrator();

        int mini = 2;
        int maxi = 9;
        double[] ansr = new double[maxi - mini + 1];
        for (int i = mini; i <= maxi; i++) {
            int num = 1 << i;
            double dx = 4. / num;
            ri.setDiscreteSize(dx);
            double ans = ri.range(sx, ex).operate(f);
            System.out.println("N=" + num + "  : " + ans);
            ansr[i - mini] = ans;
        }
    }

}