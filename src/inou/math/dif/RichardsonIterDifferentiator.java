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
public class RichardsonIterDifferentiator implements ADifferentiator,
        Differentiator, Serializable, Discrete {

    private MathVector position, calc;

    private int difColumn;

    private double h;

    private int iteration;

    private double[][] workarea;

    public RichardsonIterDifferentiator() {
        this(0.1, 1);
    }

    public RichardsonIterDifferentiator(double h, int iteration) {
        setDiscreteSize(h);
        setIteration(iteration);
    }

    public void setIteration(int iteration) {
        if (iteration > 0) {
            this.iteration = iteration;
            workarea = new double[iteration][];
            for (int i = 0; i < iteration; i++) {
                workarea[iteration - i - 1] = new double[i + 2];
                // System.out.println("MK area :
                // ["+(iteration-i-1)+"]["+(i+2)+"]");
            }
        } else {
            // System.err.println("Iteration num must be more than zero.
            // ["+iteration+"]");
            setIteration(1);
        }
    }

    public void setDiscreteSize(double h) {
        if (h != 0) {
            this.h = Math.abs(h);
        }
    }

    public double getDiscreteSize() {
        return h;
    }

    public final Differentiator point(MathVector pos, int col) {
        this.position = pos;
        if (calc == null || pos.getDimension() != calc.getDimension()) {
            calc = pos.getCopy();
        }
        this.difColumn = col;
        return this;
    }

    public final AOperator point(double x) {
        if (position == null || position.getDimension() != 1) {
            position = new Vector1D(x);
        } else {
            position.v(0, x);
        }
        if (calc == null || calc.getDimension() != position.getDimension()) {
            calc = position.getCopy();
        }
        this.difColumn = 0;
        return this;
    }

    public final double operate(AFunction func) {
        return operate((ScalarFunction) func);
    }

    public Operator copy() {
        return new RichardsonIterDifferentiator(h, iteration);
    }

    public final double operate(ScalarFunction func) {
        int num = 1 + iteration;
        // make first stage
        // System.out.println("#### prepare");
        for (int i = 0; i < num; i++) {
            workarea[0][i] = getD(func, h / Math.pow(2, i));
            // System.out.println("h="+(h/Math.pow(2,i))+" : "+workarea[0][i]);
        }
        // calculate upper stage
        // System.out.println("### iterate");
        for (int i = 0; i < iteration; i++) {
            num = iteration - i;
            if (num == 1) {
                return getNext(workarea[i][1], workarea[i][0], iteration);
            }
            for (int j = 0; j < num; j++) {
                workarea[i + 1][j] = getNext(workarea[i][j + 1],
                        workarea[i][j], i + 1);
            }
        }
        throw new InternalError("Bad iteration routine (BUG)");
    }

    private double getNext(double dhigh, double dlow, int m) {
        double four = Math.pow(4, m);
        double ret = dhigh + (dhigh - dlow) / (four - 1.);
        // System.out.println("m:"+m+" (k+1):"+dhigh + " (k):"+dlow + " =>
        // "+ret);
        return ret;
    }

    private double getD(ScalarFunction func, double dh) {
        calc.substitute(position);
        double org = calc.v(difColumn);
        calc.v(difColumn, org - dh);
        double left = func.f(calc);
        calc.v(difColumn, org + dh);
        double right = func.f(calc);
        return 0.5 * (right - left) / dh;
    }

    public static void main(String[] args) {
        AFunction func = AFunctionClass.getFunction("sin(x)");
        AFunction ans = AFunctionClass.getFunction("cos(x)");
        RichardsonIterDifferentiator dif = new RichardsonIterDifferentiator(
                0.05, 1);
        double result = 0;
        Vector1D pos = new Vector1D();
        double dx = 0.1;
        for (int j = 0; j < 10; j++) {
            double vdif = dif.point(pos, 0).operate(func);
            double vans = ans.f(pos.x);
            System.out.println("x:" + pos.x + "   dif:" + vdif + "  ans:"
                    + vans + "  (" + Math.abs(vdif - vans) + ")");
            pos.x += dx;
        }
    }

}