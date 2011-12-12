/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

public interface VectorDataModel {

    public int getVectorDimension();

    public int getArgumentDimension();

    public MathVector[] getVectors(MathVector[] vertexArray);

}