/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;

/** line pair */
public class LinePair {

    public int start, end;

    public Color color;

    public LinePair(int s, int e, Color color) {
        start = s;
        end = e;
        this.color = color;
    }

    public void getLineVector(VectorQD[] vertices, VectorQD lineVector) {
        lineVector.substitute(vertices[end]);
        lineVector.subs(vertices[start]);
    }

    public LinePair getCopy() {
        return new LinePair(start, end, color);
    }

    public String toString() {
        String ret = "LinePair: <" + start + "> to <" + end + ">";
        return ret + "\n";
    }
}