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

public class DifEqSolver2 {

    private DifEqMethod method;

    public DifEqSolver2() {
        method = new MRungeKutta2();
    }

    public DifEqSolver2(DifEqMethod dfm) {
        method = dfm;
    }

    /**
     * solve the given equation.
     * 
     * @param df
     *            given differential equation
     * @param a
     *            initial condition (x0,y0,y'0)
     * @param endx
     *            end position in x (must be x0<endx)
     * @return AArrayFunction [] object, [0]=y,[1]=dy/dx
     */
    public AArrayFunction[] solve(ScalarFunction df, VariableSet a, double endx) {
        double x0 = a.x;
        int num = (int) ((endx - x0) / method.getDiscreteSize());
        if (num > 1000000)
            System.err
                    .println("warning : big region, more than 1M steps (DFSolver2)");
        double[] y = new double[num + 1];
        double[] dy = new double[num + 1];
        method.reset();
        for (int i = 0; i < num; i++) {
            y[i] = a.y;
            dy[i] = a.dy;
            method.step(df, a);
        }
        y[num] = a.y;
        dy[num] = a.dy;
        AArrayFunction[] funcs = new AArrayFunction[2];
        funcs[0] = new AArrayFunction(x0, method.getDiscreteSize(), y);
        funcs[1] = new AArrayFunction(x0, method.getDiscreteSize(), dy);
        return funcs;
    }

    /**
     * solve the given equation.
     * 
     * @param df
     *            given differential equation
     * @param a
     *            initial condition (x0,y0,y'0)
     * @param endx
     *            end position in x (must be x0<endx)
     * @param listener
     *            called to let you know the values at each steps.
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
        ScalarFunction equation = new DifEquation2() {
            public double ddf(VariableSet a) {
                return -a.y;
            }
        };
        AFunction exact = new AFunction() {
            public double f(double t) {
                return Math.cos(t);
            }
        };
        MGear5th2 mg = new MGear5th2();
        mg.setInitColumn(new VectorGD(new double[] { 1, 0, -1, 0, 1 }));
        DifEqMethod[] methods = { new MEuler2(), new MSympletic2(),
                new MRungeKutta2(), mg };
        double h = 1;
        double ratio = 0.6;
        int num = 16;
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
        DifEqSolver2 eng = new DifEqSolver2(method);
        VariableSet init = new VariableSet(0, 1, 0);
        double end = Math.PI * 10;
        AArrayFunction[] s = eng.solve(equation, init, end);
        double[][] g = s[0].getArrays();
        double error = 0;
        for (int i = 0; i < g[0].length; i++) {
            error += Math.abs(g[1][i] - exact.f(g[0][i]));
        }
        return error;
    }
}