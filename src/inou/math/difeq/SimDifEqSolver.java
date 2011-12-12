/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.AArrayFunction;
import inou.math.util.ArrayFileOut;

/** simultaneous differential equation solver */
public class SimDifEqSolver {

    DifEqMethod method;

    public SimDifEqSolver() {
        method = new MRungeKutta();
    }

    public SimDifEqSolver(DifEqMethod dfm) {
        method = dfm;
    }

    /**
     * solve the given equation.
     * 
     * @param mt
     *            given differential equations
     * @param as
     *            initial conditions (x0,y0) [x0 value will be used as[0]'s
     *            one.]
     * @param endx
     *            end position in x (must be x0<endx)
     * @return AArrayalFunction objects
     */
    public AArrayFunction[] solve(SimEquation mt, VariableSet[] as, double endx) {
        // initialize 1
        int dim = mt.getNumber();
        if (dim != as.length)
            // error
            System.out
                    .println("not match the number between equations and inital conditions");
        double x0 = as[0].x;
        for (int i = 0; i < dim; i++)
            as[i].x = x0;
        // initialize 2
        int num = (int) ((endx - x0) / method.getDiscreteSize());
        if (num > 1000000)
            System.out.println("warning : big region (DFSolver2)");
        double[][] y = new double[dim][num + 1];

        method.reset();
        // main loop
        for (int i = 0; i < num; i++) {
            mt.update(as);
            for (int j = 0; j < dim; j++) {
                y[j][i] = as[j].y;
                method.step(mt.getMemberEquation(j), as[j]);
            }
        }
        for (int j = 0; j < dim; j++) {
            y[j][num] = as[j].y;
        }

        // make function objects
        AArrayFunction[] rets = new AArrayFunction[dim];
        for (int j = 0; j < dim; j++)
            rets[j] = new AArrayFunction(x0, method.getDiscreteSize(), y[j]);

        return rets;
    }

    public static void main(String[] ar) throws Exception {
        // throw ball in the real air
        final SubEquationHolder[] equations = new SubEquationHolder[2];
        final double k = 0.02;
        equations[0] = new SubEquationHolder();
        equations[0].setEquation(new DifEquation() {
            double u, v;

            public double df(VariableSet a) {
                u = a.y;
                v = equations[0].get(1).y;
                return -k * u * Math.sqrt(v * v + u * u);
            }
        });
        equations[1] = new SubEquationHolder();
        equations[1].setEquation(new DifEquation() {
            double u, v;

            public double df(VariableSet a) {
                v = a.y;
                u = equations[1].get(0).y;
                return -0.98 - k * v * Math.sqrt(v * v + u * u);
            }
        });
        // equation set
        SimEquation mt = new SimEquation(equations);
        SimDifEqSolver eng = new SimDifEqSolver();

        // initial condition
        double V0 = 10;// (m/sec)
        double rad = 45. / 180. * Math.PI;
        double u0 = V0 * Math.cos(rad);
        double v0 = V0 * Math.sin(rad);
        VariableSet[] inits = new VariableSet[2];
        inits[0] = new VariableSet(0, u0);
        inits[1] = new VariableSet(0, v0);
        AArrayFunction[] s = eng.solve(mt, inits, 10);

        double[][] line = new double[2][];
        double[][] temp;
        temp = s[0].getArrays();
        line[0] = temp[1];
        temp = s[1].getArrays();
        line[1] = temp[1];

        String[] coms = { "x", "y" };
        ArrayFileOut.write("rep", "differential equation test", line, coms);
        System.out.println("wrote into the file \"rep\"...");
    }
}