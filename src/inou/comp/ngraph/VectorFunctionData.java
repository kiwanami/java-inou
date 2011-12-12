/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.VectorFunction;
import inou.math.VectorUtil;

/**
 * makes vector data from a function.
 */
public class VectorFunctionData implements VectorDataModel {

    private VectorFunction function;

    public VectorFunctionData(VectorFunction vf) {
        this.function = vf;
    }

    public VectorFunction getFunction() {
        return function;
    }

    public int getVectorDimension() {
        return function.getDimension();
    }

    public int getArgumentDimension() {
        return function.getArgDimension();
    }

    public MathVector[] getVectors(MathVector[] vertexArray) {
        if (vertexArray == null)
            return null;
        MathVector[] vectors = new MathVector[vertexArray.length];
        MathVector arg = VectorUtil.createVector(getArgumentDimension());
        for (int i = 0; i < vertexArray.length; i++) {
            for (int j = 0; j < arg.getDimension(); j++) {
                arg.v(j, vertexArray[i].v(j));
            }
            vectors[i] = function.f(arg);
        }
        return vectors;
    }
}