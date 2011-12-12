/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.solve;

import inou.math.FunctionUtil;
import inou.math.MathVector;
import inou.math.Matrix;
import inou.math.MatrixFunction;
import inou.math.MatrixFunctionClass;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;
import inou.math.VectorFunction;
import inou.math.VectorGradient;
import inou.math.VectorSolver;
import inou.math.linear.Decomp;
import inou.math.vector.Vector1D;
import inou.math.vector.VectorGD;

/**
 * The implementation of Solver for the non-linear equation, using Marquardt
 * method.
 */
public class Marquardt implements VectorSolver {

    private MatrixFunction hFunc;

    private MathVector initPosition;

    private ScalarFunction evalutionFunc;

    /**
     * @param h
     *            hessian matrix
     */
    public Marquardt hesian(MatrixFunction h) {
        hFunc = h;
        return this;
    }

    /**
     * @param vec
     *            initial value
     */
    public Marquardt initial(MathVector vec) {
        initPosition = vec;
        return this;
    }

    public Marquardt evalution(ScalarFunction sf) {
        evalutionFunc = sf;
        return this;
    }

    /**
     * @param vf
     *            vector function to solve
     */
    public MathVector operate(VectorFunction vf) {
        if (hFunc == null) {
            hFunc = new VectorGradient(vf);
        }
        if (initPosition == null) {
            initPosition = new VectorGD(vf.getDimension());
        }
        if (evalutionFunc == null) {
            evalutionFunc = new SimpleEvalutionFunction(vf);
        }

        MathVector vec0 = initPosition.getCopy();
        MathVector vec = vec0.getCopy();
        double oldY = evalutionFunc.f(vec0);
        Vector1D lambda = new Vector1D(1.0);
        // Lambda matrix
        MatrixFunction scaleMt = lambdaMatrix(vf.getDimension());
        Decomp decomp = new Decomp();
        // main loop
        int count = 0;
        Matrix h = hFunc.f(vec);
        System.out.println("> " + oldY);
        while (true) {
            MathVector y = vf.f(vec).mults(-1);
            h.mults(scaleMt.f(lambda));
            MathVector delta = decomp.solves(h, y);
            MathVector newVec = vec.add(delta);
            double newY = evalutionFunc.f(newVec);
            if (newY < oldY) {
                System.out.println("> " + newY);
                lambda.x *= 2;
                vec.substitute(newVec);
                oldY = newY;
                h = hFunc.f(vec);
            }
            lambda.x *= 2;
            count++;
            if (count > 10)
                break;
        }
        return vec;
    }

    MatrixFunction lambdaMatrix(int dim) {
        ScalarFunction e1 = FunctionUtil.add(FunctionUtil.constant(1),
                FunctionUtil.variable());
        ScalarFunction e0 = FunctionUtil.constant(0.0);
        ScalarFunction[][] ae = new ScalarFunction[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    ae[i][j] = e1;
                } else {
                    ae[i][j] = e0;
                }
            }
        }
        return new MatrixFunctionClass(ae);
    }

    class SimpleEvalutionFunction extends ScalarFunctionClass {
        VectorFunction vf;

        SimpleEvalutionFunction(VectorFunction in) {
            super(in.getArgDimension());
            vf = in;
        }

        public double f(MathVector x) {
            return vf.f(x).getLength();
        }
    }
}