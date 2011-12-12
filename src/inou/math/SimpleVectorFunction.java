/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.VectorGD;

/**
 * Vector function sub-implementation class. You can get a vector function by a
 * construction with some scalar functions. (You need not implement [MathVector
 * f(MathVector x], [int getDimension()] and [AFunction getFunction(int index)].
 */

class SimpleVectorFunction extends VectorFunctionClass {
    ScalarFunction[] functions;

    public SimpleVectorFunction(ScalarFunction[] sc) {
        super(sc.length, sc[0].getDimension());
        functions = sc;
        temp = new VectorGD(sc.length);
    }

    public ScalarFunction getFunction(int d) {
        return functions[d];
    }

    MathVector temp;

    public MathVector f(MathVector arg) {
        for (int i = 0; i < functions.length; i++)
            temp.v(i, functions[i].f(arg));
        return temp;
    }
}