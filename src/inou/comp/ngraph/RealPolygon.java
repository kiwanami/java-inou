/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

/** a set of polygons on a surface object */
public class RealPolygon {

    /** origin */
    public static final int gen = 0;

    /** placed in right-hand from top viewpoint */
    public static final int a = 1;

    /** placed in left-hand from top viewpoint */
    public static final int b = 2;

    // (b) (a)
    // 2 1
    // | /
    // | /
    // |/
    // 0 (gen)

    private int[] vertexIndex;

    private boolean reversible = true;

    public RealPolygon() {
    }

    /**
     * @param _vertexIndex
     *            array of vertex index that make this polygon. the array order
     *            is vertex-ID in this polygon. if this polygon has invisible
     *            side, the array order should be anticlockwise. this array
     *            object will be copied during initialize this object.
     * @param _reversible
     *            if this polygon object has two side, set true.
     */
    public RealPolygon(int[] _vertexIndex, boolean _reversible) {
        reversible = _reversible;
        vertexIndex = new int[_vertexIndex.length];
        for (int i = 0; i < vertexIndex.length; i++) {
            vertexIndex[i] = _vertexIndex[i];
        }
    }

    public RealPolygon(int oorg, int aa, int bb, boolean _reversible) {
        vertexIndex = new int[3];
        vertexIndex[0] = oorg;
        vertexIndex[1] = aa;
        vertexIndex[2] = bb;
        reversible = _reversible;
    }

    // =====================

    public int getVertexIndexById(int a) {
        return vertexIndex[a];
    }

    public int getVertexNumber() {
        return vertexIndex.length;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RealPolygon) {
            RealPolygon ext = (RealPolygon) obj;
            if (ext.vertexIndex.length != vertexIndex.length)
                return false;
            for (int i = 0; i < vertexIndex.length; i++) {
                if (vertexIndex[i] != ext.vertexIndex[i])
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean contains(int a) {
        for (int i = 0; i < vertexIndex.length; i++) {
            if (a == vertexIndex[i])
                return true;
        }
        return false;
    }
}