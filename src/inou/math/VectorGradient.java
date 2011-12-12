/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/** gradient operator for VectorFunction */
public class VectorGradient extends MatrixFunctionClass implements Serializable {

    protected VectorFunction originalFunction;

    /** Construct divergence operator */
    public VectorGradient(VectorFunction vf) {
        originalFunction = vf;
        int dd = vf.getDimension();
        int ad = vf.getArgDimension();
        ScalarFunction[][] grads = new ScalarFunction[dd][ad];
        for (int i = 0; i < dd; i++) {
            for (int j = 0; j < ad; j++) {
                grads[i][j] = FunctionUtil.getDerivedFunction(
                        VectorFunctionClass.makeScalarFunction(vf, i), j);
            }
        }
        init(grads);
    }

}