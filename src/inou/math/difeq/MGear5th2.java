/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.MathVector;
import inou.math.Matrix;
import inou.math.MatrixUtil;
import inou.math.ScalarFunction;
import inou.math.vector.MatrixGD;
import inou.math.vector.VectorGD;

/**
 * Implementation of DifEqMethod by Gear P/C method. This class solves ddy/dx2 =
 * f(x,y,y') type. (default step interval is 0.05)
 */

public class MGear5th2 implements DifEqMethod {

    private MathVector ccalc = new VectorGD(5);

    private MathVector column = new VectorGD(5);

    private final static VectorGD CV_N = new VectorGD(new double[] {
            19. / 120., 3. / 4., 1, 1.0 / 2.0, 1.0 / 12.0 });

    private final static Matrix AM_N = new MatrixGD(new double[][] {
            { 1, 1, 1, 1, 1 }, { 0, 1, 2, 3, 4 }, { 0, 0, 1, 3, 6 },
            { 0, 0, 0, 1, 4 }, { 0, 0, 0, 0, 1 } });

    private MathVector initColumn = null;

    private double h;// step interval

    private double hh2;

    private int initStep = 3;

    // ====== constructor

    public MGear5th2() {
        this(0.05);
    }

    public MGear5th2(double h) {
        setDiscreteSize(h);
    }

    public void setInitColumn(MathVector ic) {
        initColumn = ic;
    }

    public void copyColumnVector(MGear5th2 from) {
        column.substitute(from.column);
    }

    public void reset() {
        if (initColumn != null) {
            initStep = 0;
            column.substitute(initColumn);
            double t = 1;
            for (int i = 0; i < column.getDimension(); i++) {
                column.v(i, column.v(i) * t);
                t = t * h / (i + 1);
            }
        } else {
            initStep = 2;
            column.zero();
        }
    }

    // ===== operation

    public double getDiscreteSize() {
        return h;
    }

    public void setDiscreteSize(double h) {
        this.h = h;
        hh2 = h * h * 0.5;
    }

    /** a = (x,y,y') */
    public void step(ScalarFunction df, VariableSet a) {
        if (initStep != 0) {
            initStep--;
            double p = df.f(a);
            a.x = a.x + h;
            a.y = a.y + a.dy * h;
            a.dy = a.dy + p * h;

            double curForce = p * h * h * 0.5;
            double lastC3 = column.v(3);
            column.v(3, (curForce - column.v(2)) / 3);
            column.v(4, (column.v(3) - lastC3) / 4);
            column.v(2, curForce);
            column.v(1, a.dy * h);
            column.v(0, a.y);
        } else {
            // predict
            MatrixUtil.multmv(ccalc, AM_N, column);
            column.substitute(ccalc);
            a.x = a.x + h;
            a.y = column.v(0);
            a.dy = column.v(1) / h;

            // correct
            double cor = hh2 * df.f(a) - column.v(2);
            ccalc.substitute(CV_N);
            ccalc.mults(cor);
            column.adds(ccalc);
            a.y = column.v(0);
            a.dy = column.v(1) / h;
        }
    }
}
