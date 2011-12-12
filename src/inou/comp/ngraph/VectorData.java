/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

public class VectorData implements VectorDataModel {

    private MathVector[] vectors;

    public VectorData(MathVector[] vecs) {
        vectors = new MathVector[vecs.length];
        for (int i = 0; i < vecs.length; i++) {
            vectors[i] = vecs[i].getCopy();
        }
    }

    public void updateVector(MathVector[] vecs) {
        for (int i = 0; i < vecs.length; i++) {
            vectors[i].substitute(vecs[i]);
        }
    }

    public int getVectorDimension() {
        return vectors[0].getDimension();
    }

    public int getArgumentDimension() {
        return vectors[0].getDimension();
    }

    public MathVector[] getVectors(MathVector[] vertexArray) {
        return vectors;
    }

}