/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;

/**
 * This class presents triangular polygon.
 */
public class GeoPolygon {

    private MathVector[] points = new MathVector[3];

    public GeoPolygon(MathVector a, MathVector b, MathVector c) {
        points[0] = a;
        points[1] = b;
        points[2] = c;
    }

    public boolean contains(MathVector p) {
        return points[0] == p || points[1] == p || points[2] == p;
    }

    public boolean isMatch(MathVector a, MathVector b, MathVector c) {
        return contains(a) && contains(b) && contains(c);
    }

    public MathVector getVertex(int id) {
        return points[id];
    }
}
