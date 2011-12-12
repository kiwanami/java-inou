/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Color;

public class WireObject implements IWireObject {

    private IGeometricObject geometricObject;

    protected LinePair[] lines;

    public WireObject(IGeometricObject obj, LinePair[] pares) {
        this.geometricObject = obj;
        this.lines = pares;
    }

    public WireObject(VectorQD[] ps, LinePair[] pares) {
        this(new GeometricObject(ps), pares);
    }

    // === delegation of IGeometricObject

    public IGeometricObject getCopy() {
        IGeometricObject cg = geometricObject.getCopy();
        LinePair[] cl = new LinePair[lines.length];
        for (int i = 0; i < lines.length; i++) {
            cl[i] = lines[i].getCopy();
        }
        IWireObject copy = new WireObject(cg, cl);
        return copy;
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

    // === wire object implementation

    public int getLineNumber() {
        return lines.length;
    }

    public LinePair getLinePareById(int id) {
        return lines[id];
    }

    public VectorQD getStartVertex(int lineId) {
        return geometricObject.getVertex(lines[lineId].start);
    }

    public VectorQD getEndVertex(int lineId) {
        return geometricObject.getVertex(lines[lineId].end);
    }

    public Color getColorById(int lineId) {
        return lines[lineId].color;
    }

    public String toString() {
        String ret = geometricObject.toString();
        for (int i = 0; i < lines.length; i++) {
            ret += lines[i].toString();
        }
        return ret;
    }
}