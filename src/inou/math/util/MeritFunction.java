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
import inou.math.ScalarFunctionClass;
import inou.math.VectorFunction;
import inou.math.VectorFunctionClass;
import inou.math.vector.MatrixGD;
import inou.math.vector.VectorGD;

import java.awt.Dimension;

/**
 * Parameter fitting adapter class. This class supports y = F(x; a,b,c..) type
 * non-linear function, then [x] and [y] are variables and [a,b,c...] are
 * fitting parameters. Since the input function has variable [x] and n
 * parameters, the merit-function, which is least square function, has n+2
 * dimensional function, [x,y, a,b,c,...]. So this class make a least square
 * merit-function from a n+2 dimensional function and data array, and pretends n
 * dimension parameter function, makes some utility functions.
 */
public class MeritFunction extends ScalarFunctionClass {

    int paramDim;

    int num;

    ScalarFunction in;

    double[] xx, yy;

    /**
     * @param in
     *            fitting function, having the variables as [x,y, parameters]
     * @param xx
     *            x value array
     * @param yy
     *            y value array
     */
    public MeritFunction(ScalarFunction in, double[] xx, double[] yy) {
        super(in.getDimension() - 2);
        paramDim = in.getDimension() - 2;
        // make squared dispersion function
        ScalarFunction inner = FunctionUtil.sub(FunctionUtil.variable(1, in
                .getDimension()), in);
        this.in = FunctionUtil.power(inner, FunctionUtil.constant(2, in
                .getDimension()));
        this.xx = xx;
        this.yy = yy;
        num = xx.length;
    }

    /**
     * return the n-dimension gradient function
     */
    public VectorFunction getGradientFunction() {
        ScalarFunction[] divs = new ScalarFunction[paramDim];
        for (int i = 0; i < paramDim; i++)
            divs[i] = FunctionUtil.getDerivedFunction(in, 2 + i);
        return new TrVectorFunction(VectorFunctionClass
                .makeVectorFunction(divs));
    }

    /**
     * return the n-dimension Hessian function
     */
    public MatrixFunction getHessianFunction() {
        ScalarFunction[][] divs = new ScalarFunction[paramDim][paramDim];
        for (int i = 0; i < paramDim; i++) {
            ScalarFunction a = FunctionUtil.getDerivedFunction(in, 2 + i);
            for (int j = 0; j < paramDim; j++) {
                divs[i][j] = FunctionUtil.getDerivedFunction(a, 2 + j);
            }
        }
        System.out.println("H(00)=" + divs[0][0]);
        return new TrHessianFunction(divs);
    }

    /**
     * calculate n-dimension merit-function.
     * 
     * @param vec
     *            parameter vector (p1,p2,...)
     * @return summation of square-deviation.
     */
    public double f(MathVector vec) {
        MathVector args = new VectorGD(in.getDimension());
        for (int i = 0; i < paramDim; i++) {
            args.v(2 + i, vec.v(i));
        }
        double ret = 0;
        for (int i = 0; i < num; i++) {
            args.v(0, xx[i]);
            args.v(1, yy[i]);
            ret += in.f(args);
        }
        return ret;
    }

    class TrVectorFunction extends VectorFunctionClass {
        VectorFunction vf;

        TrVectorFunction(VectorFunction vf) {
            super(vf.getDimension(), paramDim);
            this.vf = vf;
        }

        public MathVector f(MathVector vec) {
            MathVector args = new VectorGD(vf.getArgDimension());
            for (int i = 0; i < paramDim; i++) {
                args.v(2 + i, vec.v(i));
            }
            MathVector ret = new VectorGD(paramDim);
            for (int i = 0; i < num; i++) {
                args.v(0, xx[i]);
                args.v(1, yy[i]);
                ret.subs(vf.f(args));
            }
            return ret;
        }
    }

    class TrHessianFunction implements MatrixFunction {
        MatrixFunction mf;

        TrHessianFunction(ScalarFunction[][] in) {
            this.mf = new MatrixFunctionClass(in);
        }

        public Dimension getDimension() {
            return new Dimension(mf.getArgDimension(), mf.getArgDimension());
        }

        public int getArgDimension() {
            return paramDim;
        }

        public Matrix f(MathVector vec) {
            MathVector args = new VectorGD(mf.getArgDimension());
            for (int i = 0; i < paramDim; i++) {
                args.v(2 + i, vec.v(i));
            }
            Matrix ret = new MatrixGD(paramDim);
            for (int i = 0; i < num; i++) {
                args.v(0, xx[i]);
                args.v(1, yy[i]);
                ret.adds(mf.f(args));
            }
            return ret;
        }
    }
}