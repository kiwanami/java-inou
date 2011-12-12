/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.AArrayFunction;
import inou.math.util.ArrayFileOut;

/** simultaneous differential equation solver */
public class SimDifEqSolver2 {

    private DifEqMethod method;

    public SimDifEqSolver2() {
        method = new MRungeKutta2();
    }

    public SimDifEqSolver2(DifEqMethod dfm) {
        method = dfm;
    }

    public DifEqMethod getMethod() {
        return method;
    }

    public void setMethod(DifEqMethod m) {
        method = m;
    }

    /**
     * solve the given equation.
     * 
     * @param mt
     *            given differential equations
     * @param as
     *            initial conditions (x0,y0,y'0) [x0 value will be used as[0]'s
     *            one.]
     * @param endx
     *            end position in x (must be x0<endx)
     * @return AArrayalFunction objects [dimension][0:y 1:(dy/dx)]
     */
    public AArrayFunction[][] solve(SimEquation mt, VariableSet[] as,
            double endx) {
        final int dim = mt.getNumber();
        double x0 = as[0].x;
        final int num = (int) ((endx - x0) / method.getDiscreteSize());
        double frg = (endx - x0) / num;
        double old = method.getDiscreteSize();
        method.setDiscreteSize(frg);
        if (num > 1000000)
            System.err.println("warning : big region (MDFSolver2)");
        final double[][] y = new double[dim][num + 1];
        final double[][] dy = new double[dim][num + 1];

        SimStepListener listener = new SimStepListener() {
            int counter = 0;

            public boolean step(VariableSet[] current) {
                for (int j = 0; j < dim; j++) {
                    y[j][counter] = current[j].y;
                    dy[j][counter] = current[j].dy;
                }
                // stop check
                if (counter >= num)
                    return false;
                counter++;
                return true;
            }
        };
        solve(mt, as, listener);

        // make function objects
        AArrayFunction[][] rets = new AArrayFunction[dim][];
        for (int j = 0; j < dim; j++) {
            AArrayFunction[] funcs = new AArrayFunction[2];
            funcs[0] = new AArrayFunction(x0, method.getDiscreteSize(), y[j]);
            funcs[1] = new AArrayFunction(x0, method.getDiscreteSize(), dy[j]);
            rets[j] = funcs;
        }

        method.setDiscreteSize(old);

        return rets;
    }

    /**
     * solve the given equation.
     * 
     * @param mt
     *            given differential equations
     * @param as
     *            initial conditions (x0,y0,y'0) [x0 value will be used as[0]'s
     *            one.]
     * @param endx
     *            end position in x (must be x0<endx)
     * @param listener
     *            called to let you know the values at each steps.
     */
    public void solve(SimEquation mt, VariableSet[] as, SimStepListener listener) {
        // **initialize 1
        // prepare a working area
        int dim = mt.getNumber();
        if (dim != as.length)
            // error
            System.err
                    .println("not match the number between equations and inital conditions");
        double x0 = as[0].x;
        for (int i = 0; i < dim; i++)
            as[i].x = x0;
        // **initialize 2

        method.reset();
        // main loop
        while (true) {
            mt.update(as);
            if (!listener.step(as))
                break;
            for (int j = 0; j < dim; j++) {
                method.step(mt.getMemberEquation(j), as[j]);
            }
        }
    }

    // ====== test method

    public static void main(String[] ar) throws Exception {
        // throw ball in the real air
        final SubEquationHolder[] equations = new SubEquationHolder[2];
        final double k = 0;// PARAMETER
        equations[0] = new SubEquationHolder();
        equations[0].setEquation(new DifEquation2() {
            double u, v;

            public double ddf(VariableSet a) {
                u = a.dy;
                v = equations[0].get(1).dy;
                return -k * u * Math.sqrt(v * v + u * u);
            }
        });
        equations[1] = new SubEquationHolder();
        equations[1].setEquation(new DifEquation2() {
            double u, v;

            public double ddf(VariableSet r) {
                v = r.dy;
                u = equations[1].get(0).dy;
                return -0.98 - k * v * Math.sqrt(v * v + u * u);
            }
        });
        // equation set
        SimEquation mt = new SimEquation(equations);
        SimDifEqSolver2 eng = new SimDifEqSolver2();

        // initial condition
        double V0 = 10;// (m/sec)
        double rad = 45. / 180. * Math.PI;
        double u0 = V0 * Math.cos(rad);
        double v0 = V0 * Math.sin(rad);
        VariableSet[] inits = new VariableSet[2];
        inits[0] = new VariableSet(0, 0, u0);
        inits[1] = new VariableSet(0, 0, v0);
        AArrayFunction[][] s = eng.solve(mt, inits, 30);

        double[][] line = new double[2][];
        double[][] temp;
        temp = s[0][0].getArrays();
        line[0] = temp[1];
        temp = s[1][0].getArrays();
        line[1] = temp[1];

        String[] coms = { "x", "y" };
        ArrayFileOut.write("repx", "differential equation test", s[0][0]
                .getArrays(), coms);
        ArrayFileOut.write("repy", "differential equation test", s[1][0]
                .getArrays(), coms);
        ArrayFileOut.write("rep", "differential equation test", line, coms);
        System.out.println("wrote into the file \"rep\"...");
    }
}
