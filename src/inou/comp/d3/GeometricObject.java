/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

public class GeometricObject implements IGeometricObject {

    protected VectorQD[] points;

    private VectorQD massCenter;// cache

    public GeometricObject(VectorQD[] ps) {
        points = ps;
        massCenter = new VectorQD();
        // calc mass-center
        adjust();

    }

    final public VectorQD[] getVertices() {
        return points;
    }

    final public VectorQD getVertex(int id) {
        return points[id];
    }

    final public String toString() {
        String ret = "====(Object:" + getClass().getName() + ")====\n";
        for (int i = 0; i < points.length; i++) {
            ret += points[i].toString();
        }
        return ret;
    }

    public IGeometricObject getCopy() {
        VectorQD[] copy = new VectorQD[points.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (VectorQD) points[i].getCopy();
        }
        return new GeometricObject(copy);
    }

    /** adjust mass-center */
    private void adjust() {
        massCenter.qmults(0);
        for (int i = 0; i < points.length; i++) {
            massCenter.qadds(points[i]);
        }
        massCenter.qmults(1. / points.length);
    }

    /** rotate around mass-center */
    final public void rotate(MatrixQD rot, VectorQD temp) {
        for (int i = 0; i < points.length; i++) {
            temp.substitute(points[i]);
            temp.qsubs(massCenter);
            rot.qmults(temp).qadds(massCenter);
            points[i].substitute(temp);
        }
        adjust();
    }

    final public void rotate(MatrixQD rot) {
        rotate(rot, new VectorQD());
    }

    final public void translate(MatrixQD trans) {
        for (int i = 0; i < points.length; i++) {
            trans.qmults(points[i]);
        }
        adjust();
    }

    final public void expansion(double t) {
        for (int i = 0; i < points.length; i++) {
            points[i].qsubs(massCenter);
            points[i].qmults(t).qadds(massCenter);
        }
        adjust();
    }

    final public VectorQD getCenter() {
        return massCenter;
    }

    final public void translate(VectorQD dm) {
        for (int i = 0; i < points.length; i++) {
            points[i].qadds(dm);
        }
        adjust();
    }

    final public void setPosition(MathVector ps) {
        VectorQD org = getCenter().qmult(-1.);
        translate(org);
        VectorQD vr = new VectorQD();
        for (int i = 0; i < ps.getDimension(); i++)
            vr.v(i, ps.v(i));
        for (int i = 0; i < points.length; i++) {
            points[i].qadds(vr);
        }
        adjust();
    }

}