/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;

/** triangle surface */
public class Surface {

    public int[] index;

    protected boolean rev = false;

    protected Color color;

    //                         
    // b------a
    // | /
    // | /
    // | /
    // | /
    // gen

    /** origin */
    public static final int gen = 0;

    /** placed in right-hand from top viewpoint */
    public static final int a = 1;

    /** placed in left-hand from top viewpoint */
    public static final int b = 2;

    protected Surface() {
    }

    public Surface(int gi, int ai, int bi, Color color) {
        index = new int[3];
        index[gen] = gi;
        index[a] = ai;
        index[b] = bi;
        this.color = color;
    }

    public int getVertexNumber() {
        return index.length;
    }

    void getNormalVector(VectorQD[] points, VectorQD tmp1, VectorQD tmp2,
            VectorQD result) {
        tmp1.substitute(points[index[a]]);
        tmp1.qsubs(points[index[gen]]);
        tmp2.substitute(points[index[b]]);
        tmp2.qsubs(points[index[gen]]);
        tmp1.qouterProduct(tmp2, result);
        result.normalize();
    }

    public VectorQD getCenterPosition(VectorQD[] points) {
        VectorQD av = (VectorQD) points[index[0]].getCopy();
        for (int i = 1; i < getVertexNumber(); i++) {
            av.qadds(points[index[i]]);
        }
        return (VectorQD) av.qmults(1. / 3.);
    }

    public void setReversible(boolean tb) {
        rev = tb;
    }

    public boolean isReversible() {
        return rev;
    }

    public Surface getCopy() {
        return new Surface(index[gen], index[a], index[b], color);
    }

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        String ret = "---(Surface:" + getClass().getName() + " : " + color
                + " )---\n";
        for (int i = 0; i < index.length; i++) {
            ret += " <" + index[i] + "> ";
        }
        return ret + "\n";
    }
}