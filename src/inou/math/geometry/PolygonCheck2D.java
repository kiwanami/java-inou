/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.comp.SwingUtils;
import inou.math.MathVector;
import inou.math.vector.Vector2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class PolygonCheck2D {

    // test method
    public static void main(String[] args) {
        JFrame f = SwingUtils.getTestFrame("make polygon");
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("random", testPoints());
        tab.addTab("triangle", testTriLattice());
        tab.addTab("lattice", testLattice());
        tab.addTab("polar", testPolar());
        f.getContentPane().add(tab);
        f.setSize(700, 700);
        f.show();
    }

    private static JComponent testPolar() {
        return makePolygonPanel(makePolar(5));
    }

    private static JComponent testLattice() {
        return makePolygonPanel(makeLattice(5));
    }

    private static JComponent testTriLattice() {
        return makePolygonPanel(makeTriLattice(7));
    }

    private static JComponent testPoints() {
        return makePolygonPanel(makePoints(80));
    }

    private static JComponent makePolygonPanel(Vector2D[] ps) {
        MakePolygon2D mkp = new MakePolygon2D(ps);
        return new TestCanvas(ps, mkp.getLines(), mkp.getPolygons());
    }

    private static Vector2D[] makePoints(int num) {
        Vector2D[] vs = new Vector2D[num];
        for (int i = 0; i < num; i++) {
            vs[i] = new Vector2D(Math.random(), Math.random());
        }
        return vs;
    }

    private static Vector2D[] makeTriLattice(int num) {
        TriangleLatticeMaker sm = new TriangleLatticeMaker(1. / num, num);
        MathVector[] vs = sm.getLattice();
        Vector2D[] ds = new Vector2D[vs.length];
        for (int i = 0; i < ds.length; i++) {
            MathVector v = vs[i];
            ds[i] = new Vector2D(v.v(0) + .5, v.v(1) + .5);
        }
        return ds;
    }

    private static Vector2D[] makeLattice(int num) {
        SimpleLatticeMaker sm = new SimpleLatticeMaker(2, 1. / num, num);
        MathVector[] vs = sm.getLattice();
        Vector2D[] ds = new Vector2D[vs.length];
        for (int i = 0; i < ds.length; i++) {
            MathVector v = vs[i];
            ds[i] = new Vector2D(v.v(0) + .5, v.v(1) + .5);
        }
        return ds;
    }

    private static Vector2D[] makePolar(int num) {
        PolarLatticeMaker sm = new PolarLatticeMaker(2, 0.5 / num, num);
        MathVector[] vs = sm.getLattice();
        Vector2D[] ds = new Vector2D[vs.length];
        for (int i = 0; i < ds.length; i++) {
            MathVector v = vs[i];
            ds[i] = new Vector2D(v.v(0) + .5, v.v(1) + .5);
        }
        return ds;
    }
}

class TestCanvas extends JComponent {
    GeoLine2D[] lines;

    Vector2D[] points;

    GeoPolygon[] polygons;

    TestCanvas(Vector2D[] ps, GeoLine2D[] lines, GeoPolygon[] pgs) {
        this.lines = lines;
        this.points = ps;
        this.polygons = pgs;
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        for (int i = 0; i < polygons.length; i++) {
            GeoPolygon p = polygons[i];
            Vector2D a = (Vector2D) p.getVertex(0);
            Vector2D b = (Vector2D) p.getVertex(1);
            Vector2D c = (Vector2D) p.getVertex(2);
            int[] xs = { (int) (a.x * d.width), (int) (b.x * d.width),
                    (int) (c.x * d.width) };
            int[] ys = { (int) (a.y * d.height), (int) (b.y * d.height),
                    (int) (c.y * d.height) };
            g.setColor(inou.comp.ngraph.ColorManager.getColor());
            g.fillPolygon(xs, ys, 3);
        }
        g.setColor(Color.black);
        for (int i = 0; i < points.length; i++) {
            Vector2D a = points[i];
            g.drawOval((int) (a.x * d.width) - 4, (int) (a.y * d.height) - 4,
                    8, 8);
        }
        for (int i = 0; i < lines.length; i++) {
            Vector2D a = (Vector2D) lines[i].st;
            Vector2D b = (Vector2D) lines[i].ed;
            g.drawLine((int) (a.x * d.width), (int) (a.y * d.height),
                    (int) (b.x * d.width), (int) (b.y * d.height));
        }
    }
}
