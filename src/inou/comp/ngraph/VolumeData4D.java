/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

/**
 * Manage a volume in the 4d-space type data. This class doesn't decide the way
 * to have a data. [concreate class : VolumeXYData4D, VolumeFunctionData4D]
 */
public abstract class VolumeData4D extends PlotData4D {

    protected VolumeData4D() {
    }

    protected VolumeData4D(String title) {
        super(title);
    }

    /** make solid data */
    public abstract RealSolid[] getSolids(MathVector[] vertexArray);

}