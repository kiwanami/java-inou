/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.AArrayFunction;
import inou.math.AFunction;
import inou.math.ScalarFunction;
import inou.math.vector.VectorGD;

/**
 * This class is a solver of Differential equation. The usage is written in main
 * method.
 */

public class DifEqSolver {

    DifEqMethod method;

    public DifEqSolver() {
        method = new MRungeKutta();
    }

    public DifEqSolver(DifEqMethod dfm) {
        method = dfm;
    }

    /**
     * solve the given equation.
     * 
     * @param df
     *            given differential equation
     * @param a
     *            initial condition (x0,y0)
     * @param endx
     *            end position in x (must be x0<endx)
     * @return AArrayFunction object
     */
    public AArrayFunction solve(ScalarFunction df, VariableSet a, double endx) {
        double x0 = a.x;
        int num = (int) ((endx - x0) / method.getDiscreteSize());
        if (num > 1000000)
            System.out.println("warning : big region (DFSolver2)");
        double[] y = new double[num + 1];
        method.reset();
        for (int i = 0; i < num; i++) {
            y[i] = a.y;
            method.step(df, a);
        }
        y[num] = a.y;
        return new AArrayFunction(x0, method.getDiscreteSize(), y);
    }

    /**
     * solve the given equation.
     * 
     * @param df
     *            given differential equation
     * @param a
     *            initial condition (x0,y0)
     * @param endx
     *            end position in x (must be x0<endx)
     * @param listener
     *            called to let you know the values at each steps.
     * @return AArrayFunction object
     */
    public void solve(ScalarFunction df, VariableSet a, double endx,
            StepListener listener) {
        double x0 = a.x;
        long num = (int) ((endx - x0) / method.getDiscreteSize());
        method.reset();
        for (long i = 0; i < num; i++) {
            if (!listener.step(a))
                return;
            method.step(df, a);
        }
        listener.step(a);// last step
    }

    /** test implementation */
    public static void main(String[] ar) {
        ScalarFunction equation = new DifEquation() {
            public double df(VariableSet a) {
                return -a.y;
            }
        };
        AFunction exact = new AFunction() {
            public double f(double t) {
                return Math.exp(-t);
            }
        };
        MGear5th mg = new MGear5th();
        mg.setInitColumn(new VectorGD(new double[] { 1, -1, 1, -1, 1 }));
        DifEqMethod[] methods = { new MEuler(), new MAdamsPC(),
                new MRungeKutta(), mg, };
        double h = 0.2;
        double ratio = 0.6;
        int num = 12;
        double[] dh = new double[num];
        double[][] errors = new double[methods.length][num];
        for (int i = 0; i < num; i++) {
            dh[i] = h;
            System.out.print("h = " + h);
            for (int j = 0; j < methods.length; j++) {
                methods[j].setDiscreteSize(h);
                errors[j][i] = testMethod(methods[j], equation, exact);
                System.out.print("  " + errors[j][i]);
            }
            h *= ratio;
            System.out.println();
        }
        AFunction[] fs = new AFunction[methods.length];
        for (int j = 0; j < methods.length; j++) {
            fs[j] = new AArrayFunction(dh, errors[j]);
        }
        inou.comp.ngraph.PlotModel ct = inou.comp.ngraph.Graph.show(fs);
        ct.getAxis(0).setLog(true);
        ct.getAxis(1).setLog(true);
        ct.updatePlotter();
    }

    private static double testMethod(DifEqMethod method,
            ScalarFunction equation, AFunction exact) {
        DifEqSolver eng = new DifEqSolver(method);
        VariableSet init = new VariableSet(0, 1);
        double end = 25;
        AArrayFunction s = eng.solve(equation, init, end);
        double[][] g = s.getArrays();
        double error = 0;
        for (int i = 0; i < g[0].length; i++) {
            error += Math.abs(g[1][i] - exact.f(g[0][i]));
        }
        return error;
    }
}