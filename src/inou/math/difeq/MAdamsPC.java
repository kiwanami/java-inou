/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * Implementation of DiffEqMethod by Adams Predictor and Corrector method. This
 * class solves dy/dx = f(x,y) type equation. (default step interval is 0.05 and
 * error size is 0.00001)
 */

public class MAdamsPC implements DifEqMethod {

    protected double h0;// step interval

    protected double e0;// error size

    protected ScalarFunction df;// differential equation

    protected static double ini_h = 0.05;

    protected static double ini_e = 0.00001;

    protected static int warloop = 5;

    protected static int maxloop = 20;

    // ====== constructor

    public MAdamsPC() {
        this(ini_h, ini_e);// default coeffictent
    }

    public MAdamsPC(double h, double e) {
        this.h0 = h;
        this.e0 = e;
    }

    // ===== operation

    public double getDiscreteSize() {
        return h0;
    }

    public void setDiscreteSize(double h) {
        this.h0 = h;
    }

    public double getError() {
        return e0;
    }

    public void setError(double e) {
        this.e0 = e;
    }

    protected int lpnum = 0;

    public void reset() {
        table.clear();
    }

    double e1, h;

    double x0, x, xx, x9;

    double y0, y1, y2, y3, y4, y9, yy;

    EqData dat;

    class EqData {
        int lpnum;

        double[] f;

        VariableSet[] pool;

        EqData() {
            lpnum = 0;
            f = new double[5];
            pool = new VariableSet[4];
            for (int i = 0; i < 4; i++)
                pool[i] = new VariableSet(0, 0);
        }
    }

    java.util.Hashtable table = new java.util.Hashtable();

    VariableSet tp = new VariableSet(0, 0);

    public void step(ScalarFunction df, VariableSet a) {
        h = h0;
        tp.substitute(a);
        dat = (EqData) table.get(df);
        if (dat == null) {
            dat = new EqData();
            table.put(df, dat);
        }

        dat.lpnum++;
        if (dat.lpnum < 4) {
            if (dat.lpnum == 1) {
                dat.pool[0] = (VariableSet) a.getCopy();
                x = a.x;
                yy = a.y;
                dat.f[0] = df.f(tp);

                for (int i = 1; i <= 3; i++) {
                    y1 = dat.f[i - 1] * h;
                    tp.set(x + h / 2., yy + y1 / 2.);
                    y2 = df.f(tp) * h;
                    tp.set(x + h / 2., yy + y2 / 2.);
                    y3 = df.f(tp) * h;
                    tp.set(x + h, yy + y3);
                    y4 = df.f(tp) * h;
                    x += h;
                    yy += (y1 + y2 * 2. + y3 * 2. + y4) / 6.;
                    dat.pool[i] = new VariableSet(0, 0);
                    dat.pool[i].set(x, yy);
                    tp.set(x, yy);
                    dat.f[i] = df.f(tp);
                }
            }
            a.substitute(dat.pool[dat.lpnum]);
            return;
        }

        x = a.x;
        y0 = a.y;
        // predict by 4th Adams-Bashfort method
        y2 = y0
                + (55. * dat.f[3] - 59. * dat.f[2] + 37. * dat.f[1] - 9. * dat.f[0])
                * h / 24.;

        // correct by 4th Adams-Moulton method
        judge: while (true) {// goto loop!!

            for (int i = 0; i < maxloop; i++) {
                y1 = y2;
                tp.set(x + h, y1);
                dat.f[4] = df.f(tp);
                y2 = y0
                        + (9. * dat.f[4] + 19. * dat.f[3] - 5. * dat.f[2] + dat.f[1])
                        * h / 24;
                e1 = Math.abs(y2 - y1) * 24. / h / y2;
                if (e1 < e0)
                    break judge;
            }

            // more than [maxloop] times, warning
            System.out.println("error not vanished...");
            throw new ArithmeticException("error not vanished...");
        }

        a.set(x + h, y2);

        for (int i = 0; i < 4; i++)
            dat.f[i] = dat.f[i + 1];
        dat.f[4] = df.f(a);
    }
}