/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.util;

import inou.math.FunctionUtil;
import inou.math.MathVector;
import inou.math.Matrix;
import inou.math.MatrixFunction;
import inou.math.MatrixFunctionClass;
import inou.math.ScalarFunction;
import inou.math.VectorFunction;
import inou.math.exp.Expression;
import inou.math.linear.Decomp;
import inou.math.vector.Vector1D;

/**
 * non-linear fitting by Marquardt method
 */

public class MarquardtFit {

    ScalarFunction function;

    double[] sx, sy;

    MathVector initParams;

    MathVector answers;

    MeritFunction evalFunc;

    VectorFunction yFunc;

    MatrixFunction hFunc;

    /**
     * @param f
     *            model function, having n+2 variables as [x,y] and n-parameters
     * @param sx
     *            data array
     * @param sy
     *            data array
     * @param params
     *            n-dimension parameter vector
     */
    public MarquardtFit(String exp, String[] vars, double[] sx, double[] sy,
            MathVector params) {
        Expression obj = new Expression(exp, vars, null);
        init(obj.getFunction(), sx, sy, params);
    }

    /**
     * @param f
     *            model function, having n+2 variables as [x,y] and n-parameters
     * @param sx
     *            data array
     * @param sy
     *            data array
     * @param params
     *            n-dimension parameter vector
     */
    public MarquardtFit(ScalarFunction f, double[] sx, double[] sy,
            MathVector params) {
        init(f, sx, sy, params);
    }

    public MathVector getAnswer() {
        return answers;
    }

    void init(ScalarFunction f, double[] sx, double[] sy, MathVector params) {
        this.function = f;
        this.sx = sx;
        this.sy = sy;
        this.initParams = params;
        nonlinearAnalyze();
    }

    void nonlinearAnalyze() {
        // evalution function Y
        evalFunc = new MeritFunction(function, sx, sy);
        // y : div Y
        yFunc = evalFunc.getGradientFunction();
        // H : div div Y
        hFunc = evalFunc.getHessianFunction();
        answers = iteration(initParams);
    }

    MathVector iteration(MathVector vec0) {
        vec0 = vec0.getCopy();
        MathVector vec = vec0.getCopy();
        double oldY = evalFunc.f(vec0);
        Vector1D lambda = new Vector1D(1.0);
        // Lambda matrix
        ScalarFunction e1 = FunctionUtil.add(FunctionUtil.constant(1.0),
                FunctionUtil.variable());
        ScalarFunction e0 = FunctionUtil.constant(0.0);
        ScalarFunction[][] ae = new ScalarFunction[hFunc.getArgDimension()][hFunc
                .getArgDimension()];
        for (int i = 0; i < hFunc.getArgDimension(); i++) {
            for (int j = 0; j < hFunc.getArgDimension(); j++) {
                if (i == j) {
                    ae[i][j] = e1;
                } else {
                    ae[i][j] = e0;
                }
            }
        }
        MatrixFunctionClass scaleMt = new MatrixFunctionClass(ae);
        Decomp decomp = new Decomp();
        // main loop
        int count = 0;
        Matrix h = hFunc.f(vec);
        System.out.println("> " + oldY);
        while (true) {
            MathVector y = yFunc.f(vec);
            h.mults(scaleMt.f(lambda));
            MathVector delta = decomp.solves(h, y);
            MathVector newVec = vec.add(delta);
            double newY = evalFunc.f(newVec);
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

}