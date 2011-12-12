/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

/**
 * Manage a surface in the 3d-space type data. This class doesn't decide the way
 * to have a data. [concreate class : SurfaceXYData3D, SurfaceFunctionData3D]
 */
public abstract class SurfaceData3D extends PlotData3D {

    // ==========================
    // contructor
    // ==========================

    protected SurfaceData3D() {
        super();
    }

    protected SurfaceData3D(String dataName) {
        super(dataName);
    }

    // ==========================
    // operation
    // ==========================

    // following methods must be overrided.

    /** called by PlotModel to paint data */
    // abstract public MathVector [] getArray(RealRange r);
    /** make polygon data */
    public abstract RealPolygon[] getPolygons(MathVector[] vertexArray);

}