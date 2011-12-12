/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

/**
 * This object presents a small part of volume data. 4D data (3D position and 1D
 * scalar data) are devided into small parts, because some plotters need the
 * devided parts to plot.
 */

public class RealSolid {

    int[] vertexIndex;

    public RealSolid(int[] vertexIndex) {
        this.vertexIndex = vertexIndex;
    }

    public int getVertexNumber() {
        return vertexIndex.length;
    }

    public int getVertexIndexById(int id) {
        return vertexIndex[id];
    }
}