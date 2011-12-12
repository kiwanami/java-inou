/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.math.MathVector;
import inou.math.vector.VectorGD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Make triangular polygons from given vertex array. Dimension of vertex should
 * be more than 2. If more than 3D vertex given, this object uses 0 and 1
 * dimensions as X and Y dimension data, respectively. You should consider
 * number of vertices, because calculation time is proportional to the 3rd power
 * of the number. For example, in case of about 100 vertices, it is few second
 * to calculate.
 */
public class MakePolygon2D {

    private MathVector[] points;

    private GeoPolygon[] polygons;

    private GeoLine2D[] lines;

    public MakePolygon2D(MathVector[] ps) {
        points = ps;
        createPolygons();
    }

    public MathVector[] getPoints() {
        return points;
    }

    public GeoPolygon[] getPolygons() {
        return polygons;
    }

    public GeoLine2D[] getLines() {
        return lines;
    }

    private void createPolygons() {
        createTriangulation();
        ArrayList tmpPgs = new ArrayList();

        for (int i = 0; i < lines.length; i++) {
            GeoLine2D aline = lines[i];
            for (int j = 0; j < lines.length; j++) {
                GeoLine2D bline = lines[j];
                if (bline == aline)
                    continue;
                if (bline.contains(aline.st)) {
                    MathVector key = bline.getAlternative(aline.st);
                    for (int k = 0; k < lines.length; k++) {
                        GeoLine2D cline = lines[k];
                        if (cline == aline || cline == bline)
                            continue;
                        if (cline.contains(key) && cline.contains(aline.ed)) {
                            // triangle!
                            addPolygon(aline.st, aline.ed, key, tmpPgs);
                        }
                    }
                }
            }
        }
        // exclusive check
        exclusiveCheck(tmpPgs);
        // comit
        polygons = new GeoPolygon[tmpPgs.size()];
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = (GeoPolygon) tmpPgs.get(i);
        }
    }

    private void exclusiveCheck(ArrayList pgList) {
        boolean finish = false;
        int cnt = 0;
        outer: while (!finish) {
            for (Iterator e = pgList.iterator(); e.hasNext();) {
                GeoPolygon pg = (GeoPolygon) e.next();
                for (int i = 0; i < points.length; i++) {
                    if (contains(points[i], pg)) {
                        pgList.remove(pg);
                        cnt++;
                        continue outer;
                    }
                }
            }
            finish = true;
        }
    }

    public static boolean contains(MathVector t, GeoPolygon p) {
        MathVector a = p.getVertex(0);
        MathVector b = p.getVertex(1);
        MathVector c = p.getVertex(2);
        MathVector g = a.add(b).adds(c).mults(1. / 3.);
        if (!insideTest(a, b, t, g))
            return false;
        if (!insideTest(b, c, t, g))
            return false;
        if (!insideTest(c, a, t, g))
            return false;
        return true;
    }

    private static boolean insideTest(MathVector a, MathVector b, MathVector t,
            MathVector g) {
        // vartical?
        if (eq(a.v(0), b.v(0))) {
            double gx = g.v(0) - a.v(0);
            double tx = t.v(0) - a.v(0);
            if ((gx * tx) > 0)
                return true;
        }
        // horizontal?
        if (eq(a.v(1), b.v(1))) {
            double gy = g.v(1) - a.v(1);
            double ty = t.v(1) - a.v(1);
            if ((gy * ty) > 0)
                return true;
        }
        double w = (b.v(1) - a.v(1)) / (b.v(0) - a.v(0)); // gradient
        double ty = w * (t.v(0) - b.v(0)) + b.v(1) - t.v(1);
        double gy = w * (g.v(0) - b.v(0)) + b.v(1) - g.v(1);
        if ((ty * gy) > 0)
            return true;
        return false;
    }

    private void addPolygon(MathVector a, MathVector b, MathVector c,
            ArrayList pgList) {
        for (int i = 0; i < pgList.size(); i++) {
            GeoPolygon pg = (GeoPolygon) pgList.get(i);
            if (pg.isMatch(a, b, c))
                return;
        }
        pgList.add(new GeoPolygon(a, b, c));
    }

    private void createTriangulation() {
        int num = points.length;
        ArrayList vpairs = new ArrayList();

        int count = 0;
        for (int i = 0; i < num; i++)
            for (int j = i + 1; j < num; j++)
                vpairs.add(new GeoLine2D(points[i], points[j]));

        int num2 = vpairs.size();// pair number
        GeoLine2D[] pairs = new GeoLine2D[num2];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = (GeoLine2D) vpairs.get(i);
        }
        Arrays.sort(pairs);

        ArrayList tmpLines = new ArrayList();
        outer: for (int i = 0; i < pairs.length; i++) {
            for (int j = 0; j < tmpLines.size(); j++)
                if (intersect((GeoLine2D) tmpLines.get(j), pairs[i]))
                    continue outer;
            tmpLines.add(pairs[i]);
        }

        lines = new GeoLine2D[tmpLines.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = (GeoLine2D) tmpLines.get(i);
        }
    }

    private boolean intersect(GeoLine2D a, GeoLine2D b) {
        // First check for zero or vertical vectors.
        if (eq(a.length(), 0) || eq(b.length(), 0))
            return true;

        // Now check that there are not points on top of other points.
        if (((a.st != b.st) && eq(a.st, b.st))
                || ((a.st != b.ed) && eq(a.st, b.ed))
                || ((a.ed != b.st) && eq(a.ed, b.st))
                || ((a.ed != b.ed) && eq(a.ed, b.ed)))
            return true;

        // Next check if the two vectors are the same.
        if ((eq(a.st, b.st) && eq(a.ed, b.ed))
                || (eq(a.st, b.ed) && eq(a.ed, b.st)))
            return true;

        int test1 = sign(innerProduct2D(perp(a.vec()), b.st.sub(a.st))
                * innerProduct2D(perp(a.vec()), b.ed.sub(a.st)));

        if (test1 > 0)
            return false;

        int test2 = sign(innerProduct2D(perp(b.vec()), a.st.sub(b.st))
                * innerProduct2D(perp(b.vec()), a.ed.sub(b.st)));

        if (test2 > 0)
            return false;
        if (test1 < 0 || test2 < 0)
            return true;

        // if execution gets here, there are colinear points.
        // Check to see if the two vectors are parallel.
        // If they are not parallel then they share only one end point and
        // are considered not to intersect.
        if (sign(innerProduct2D(perp(a.vec()), b.vec())) != 0)
            return false;

        // if execution reaches here then the segments lie on the same line.
        // All that is left is to determine if the two segments overlap at all.
        if (between(b.st, a.st, a.ed) || between(b.ed, a.st, a.ed))
            return true;
        return between(a.st, b.st, b.ed);

    }

    private static double eps = 1e-14;

    private static boolean nearlyEqual(double a, double b) {
        if (Math.abs(a - b) > eps)
            return false;
        return true;
    }

    private int sign(double x) {
        if (nearlyEqual(x, 0))
            return 0;
        return (x < 0.) ? -1 : 1;
    }

    private double innerProduct2D(MathVector v1, MathVector v2) {
        return v1.v(0) * v2.v(0) + v1.v(1) * v2.v(1);
    }

    // return perpendicular vector
    private static MathVector perp(MathVector v) {
        return new VectorGD(new double[] { -v.v(1), v.v(0) });
    }

    // vector equation
    private static boolean eq(MathVector a, MathVector b) {
        return nearlyEqual(a.sub(b).getLength(), 0);
    }

    // scalar equation
    private static boolean eq(double a, double b) {
        return nearlyEqual(a, b);
    }

    // check the relation of the given 3 position.
    private boolean between(MathVector point, MathVector low, MathVector high) {
        if (low.v(0) == high.v(0)) {
            if (low.v(1) < high.v(1)) {
                if (point.v(1) <= low.v(1))
                    return false;
                if (point.v(1) >= high.v(1))
                    return false;
                return true;
            } else {
                if (point.v(1) >= low.v(1))
                    return false;
                if (point.v(1) <= high.v(1))
                    return false;
                return true;
            }
        }
        if (low.v(0) < high.v(0)) {
            if (point.v(0) <= low.v(0))
                return false;
            if (point.v(0) >= high.v(0))
                return false;
            return true;
        } else {
            if (point.v(0) >= low.v(0))
                return false;
            if (point.v(0) <= high.v(0))
                return false;
            return true;
        }
    }

}
