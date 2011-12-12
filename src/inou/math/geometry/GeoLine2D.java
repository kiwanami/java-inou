/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;

public class GeoLine2D extends GeoLine {

    public MathVector st, ed;

    /** 
		@param a start vertex
		@param b end vertex
	 */
    public GeoLine2D(MathVector a, MathVector b) {
        super(length2D(a, b));
        st = a;
        ed = b;
    }

    private static double length2D(MathVector aa, MathVector bb) {
        double x = aa.v(0) - bb.v(0);
        double y = aa.v(1) - bb.v(1);
        return Math.sqrt(x * x + y * y);
    }

    final public boolean contains(MathVector p) {
        return (st == p || ed == p);
    }

    final public boolean match(MathVector x, MathVector y) {
        return (x == st && y == ed) || (x == ed && y == st);
    }

    final public MathVector getAlternative(MathVector a) {
        return (a == st) ? ed : st;
    }

    final public MathVector vec() {
        return ed.sub(st);
    }

    final public MathVector get(int i) {
        return (i == 0) ? st : ed;
    }

    final public String toString() {
        return "Line----\n" + st + "--" + ed;
    }
}
