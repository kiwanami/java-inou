/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Color;

/** manage the 3d object which made from points, lines, and surfaces */
public class PolygonObject implements IPolygonObject {

    private IWireObject geometricObject;

    protected Surface[] surfaces;

    public PolygonObject(VectorQD[] ps, LinePair[] ls, Surface[] sf) {
        this(new WireObject(ps, ls), sf);
    }

    public PolygonObject(IWireObject obj, Surface[] sf) {
        geometricObject = obj;
        surfaces = sf;
    }

    // ===(delegation of IGeometricObject)

    public IGeometricObject getCopy() {
        IWireObject cg = (IWireObject) geometricObject.getCopy();
        Surface[] cs = new Surface[surfaces.length];
        for (int i = 0; i < surfaces.length; i++) {
            cs[i] = surfaces[i].getCopy();
        }
        return new PolygonObject(cg, cs);
    }

    public VectorQD[] getVertices() {
        return geometricObject.getVertices();
    }

    public VectorQD getVertex(int id) {
        return geometricObject.getVertex(id);
    }

    public void rotate(MatrixQD rot, VectorQD temp) {
        geometricObject.rotate(rot, temp);
    }

    public void rotate(MatrixQD rot) {
        geometricObject.rotate(rot);
    }

    public void translate(MatrixQD trans) {
        geometricObject.translate(trans);
    }

    public void expansion(double t) {
        geometricObject.expansion(t);
    }

    public VectorQD getCenter() {
        return geometricObject.getCenter();
    }

    public void translate(VectorQD delta) {
        geometricObject.translate(delta);
    }

    public void setPosition(MathVector ps) {
        geometricObject.setPosition(ps);
    }

    // ===(delegation of IWireObject)

    public int getLineNumber() {
        return geometricObject.getLineNumber();
    }

    public LinePair getLinePareById(int id) {
        return geometricObject.getLinePareById(id);
    }

    public VectorQD getStartVertex(int lineId) {
        return geometricObject.getStartVertex(lineId);
    }

    public VectorQD getEndVertex(int lineId) {
        return geometricObject.getEndVertex(lineId);
    }

    public Color getColorById(int lineId) {
        return geometricObject.getColorById(lineId);
    }

    // ===(IPolygonObject implementation)

    public VectorQD getVertexBySurfaceId(int surfaceId, int vertexId) {
        return geometricObject.getVertex(surfaces[surfaceId].index[vertexId]);
    }

    public Surface getSurfaceById(int surfaceId) {
        return surfaces[surfaceId];
    }

    public int getSurfaceNumber() {
        return surfaces.length;
    }

    public VectorQD getSurfaceCenterBySurfaceId(int surfaceId) {
        return surfaces[surfaceId].getCenterPosition(geometricObject
                .getVertices());
    }

    public String toString() {
        String ret = geometricObject.toString();
        for (int i = 0; i < surfaces.length; i++) {
            ret += surfaces[i];
        }
        return ret;
    }

}