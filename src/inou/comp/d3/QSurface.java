/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;

/** Square surface */
public class QSurface extends Surface {

    //                         
    // - b------a
    // / | /
    // c | /
    // \ | /
    // \| /
    // gen

    public static final int c = 3;

    protected QSurface() {
    }

    /**
     * 
     * -an------a / | / bi | / \ | / \| / gi
     */
    public QSurface(int gi, int ai, int bi, int an, Color color) {
        index = new int[4];
        index[gen] = gi;
        index[a] = ai;
        index[b] = an;
        index[c] = bi;
        this.color = color;
    }

    public QSurface(int[] ps, Color color) {
        index = new int[4];
        index[gen] = ps[gen];
        index[a] = ps[a];
        index[b] = ps[b];
        index[c] = ps[c];
        this.color = color;
    }

    public void getNormalVector(VectorQD[] points, VectorQD tmp1,
            VectorQD tmp2, VectorQD result) {
        tmp1.substitute(points[index[a]]);
        tmp1.qsubs(points[index[gen]]);
        tmp2.substitute(points[index[c]]);
        tmp2.qsubs(points[index[gen]]);
        tmp1.qouterProduct(tmp2, result);
        result.normalize();
    }

    public Surface getCopy() {
        return new QSurface(index[gen], index[a], index[c], index[b], color);
    }
}