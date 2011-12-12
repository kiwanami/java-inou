/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;

abstract public class GeoLine implements Comparable {

    private double length;

    /** start and end */
    protected GeoLine(double a) {
        length = a;
    }

    abstract public boolean contains(MathVector p);

    abstract public boolean match(MathVector x, MathVector y);

    private int ref = 0;

    final public void incRef() {
        ref++;
    }

    final public void setRef(int r) {
        ref = r;
    }

    final public int getRef() {
        return ref;
    }

    final public double length() {
        return length;
    }

    /**
     * implementation method for ComparableClass
     */
    final public int compareTo(Object obj) {
        double ext = ((GeoLine) obj).length();
        if (ext == length)
            return 0;
        return ((length - ext) < 0) ? -1 : 1;
    }
}
