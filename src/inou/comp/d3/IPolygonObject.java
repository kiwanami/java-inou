/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

public interface IPolygonObject extends IWireObject {

    public VectorQD getVertexBySurfaceId(int surfaceId, int vertexId);

    public Surface getSurfaceById(int surfaceId);

    public int getSurfaceNumber();

    public VectorQD getSurfaceCenterBySurfaceId(int surfaceId);

}