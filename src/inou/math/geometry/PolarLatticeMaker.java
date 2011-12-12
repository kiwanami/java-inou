/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;
import inou.math.vector.Vector1D;
import inou.math.vector.Vector2D;
import inou.math.vector.Vector3D;

import java.util.ArrayList;

/** Make a lattice with the polar coordination. */
public class PolarLatticeMaker implements LatticeMaker {

    private int d;

    private double dr;

    private int num;

    /**
     * make a lattice with polar coordination around the origine.
     * 
     * @param dim
     *            dimension of lattice
     * @param dr
     *            distance of points
     * @param num
     *            number of points along radious
     */
    public PolarLatticeMaker(int dim, double dr, int num) {
        this.d = dim;
        this.dr = dr;
        this.num = num;
    }

    /**
     * @return lattice points.
     */
    public MathVector[] getLattice() {
        ArrayList points = new ArrayList();
        switch (d) {
        case 2:
            get2D(points);
            break;
        case 3:
            get3D(points);
        case 1:
        default:
            get1D(points);
        }
        MathVector[] ret = new MathVector[points.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (MathVector) points.get(i);
        }
        return ret;
    }

    private void get1D(ArrayList points) {
        points.add(new Vector1D(0));
        for (int i = 1; i < num; i++) {
            points.add(new Vector1D(dr * i));
            points.add(new Vector1D(-dr * i));
        }
    }

    private void get2D(ArrayList points) {
        points.add(new Vector2D(0, 0));
        for (int i = 1; i < num; i++) {
            double cr = dr * i;
            int rnum = (int) Math.round(cr * Math.PI * 2. / dr);
            double rad = 2. * Math.PI / rnum;
            double offset = 0;
            if ((rnum % 2) == 1)
                offset = rad / 2.;
            for (int j = 0; j < rnum; j++) {
                double cx = cr * Math.cos(rad * j + offset);
                double cy = cr * Math.sin(rad * j + offset);
                points.add(new Vector2D(cx, cy));
            }
        }
    }

    private void get3D(ArrayList points) {
        points.add(new Vector3D(0, 0, 0));
        for (int i = 1; i < num; i++) {
            double cr = dr * i;
            int znum = (int) Math.round(cr * Math.PI * 2. / dr);
            if ((znum % 2) == 1)
                znum++;
            znum /= 2;
            for (int k = 0; k <= znum; k++) {
                double zrad = Math.PI / znum;
                double cz = cr * Math.cos(zrad * k);
                double cg = cr * Math.sin(zrad * k);
                int rnum = (int) Math.round(cg * Math.PI * 2. / dr);
                if (rnum == 0) {
                    points.add(new Vector3D(0, 0, cz));
                    continue;
                }
                double rad = 2. * Math.PI / rnum;
                double offset = 0;
                if ((rnum % 2) == 1)
                    offset = rad / 2.;
                for (int j = 0; j < rnum; j++) {
                    double cx = cg * Math.cos(rad * j + offset);
                    double cy = cg * Math.sin(rad * j + offset);
                    points.add(new Vector3D(cx, cy, cz));
                }
            }
        }
    }
}
