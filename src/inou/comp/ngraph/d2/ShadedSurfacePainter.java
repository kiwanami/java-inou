/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ColorSet;
import inou.comp.ngraph.Plotter2D;
import inou.comp.ngraph.RealPolygon;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SurfaceData3D;
import inou.math.MathVector;
import inou.math.vector.Vector3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** paint polygon surface on 2D graph */

public class ShadedSurfacePainter extends Plotter2D {

    private ColorSet index;

    private Vector3D way; // light angle

    private SurfaceData3D data;

    public ShadedSurfacePainter(SurfaceData3D data) {
        super(data, true);
        Vector3D w = new Vector3D(1, 1, 1);
        //
        this.way = (Vector3D) w.getUnit().mults(-1);
        index = new ColorSet(Color.white, Color.lightGray, 64);
    }

    private SurfaceData3D getSurfaceData() {
        return (SurfaceData3D) getData();
    }

    protected void draw2D(RenderingInfo2D info, MathVector[] dataArray) {
        // dataArray is nonsense for surface painting.

        RealPolygon[] polygons = getSurfaceData().getPolygons(dataArray);
        Graphics g = info.getGraphics();
        int[] sx = new int[3];
        int[] sy = new int[3];
        Vector3D p1, p2, p3;
        RealPolygon pg;
        int num = polygons.length;
        for (int i = 0; i < num; i++) {
            pg = polygons[i];
            p1 = new Vector3D(dataArray[pg.getVertexIndexById(0)]);
            p2 = new Vector3D(dataArray[pg.getVertexIndexById(1)]);
            p3 = new Vector3D(dataArray[pg.getVertexIndexById(2)]);
            sx[0] = info.real2graphicsX(p1.x);
            sy[0] = info.real2graphicsY(p1.y);
            sx[1] = info.real2graphicsX(p2.x);
            sy[1] = info.real2graphicsY(p2.y);
            sx[2] = info.real2graphicsX(p3.x);
            sy[2] = info.real2graphicsY(p3.y);
            g.setColor(index.getColor(way.innerProduct(getNormalVector(pg,
                    dataArray))));
            g.fillPolygon(sx, sy, 3);
        }
    }

    private MathVector getNormalVector(RealPolygon polygon,
            MathVector[] dataArray) {
        Vector3D tmp1 = new Vector3D();
        Vector3D tmp2 = new Vector3D();
        Vector3D result = new Vector3D();
        tmp1.substitute(dataArray[polygon.getVertexIndexById(1)]);
        tmp1.subs(dataArray[polygon.getVertexIndexById(0)]);
        tmp2.substitute(dataArray[polygon.getVertexIndexById(2)]);
        tmp2.subs(dataArray[polygon.getVertexIndexById(0)]);
        tmp1.outerProduct(tmp2, result);
        result.normalize();
        return result;
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        int[] sx = new int[3];
        int[] sy = new int[3];
        g.setColor(index.getColor(0.0));
        sx[0] = r.x;
        sy[0] = r.y + r.height;
        sx[1] = r.x;
        sy[1] = r.y;
        sx[2] = r.x + r.width / 2;
        sy[2] = r.y;
        g.fillPolygon(sx, sy, 3);

        g.setColor(index.getColor(0.5));
        sx[0] = r.x;
        sy[0] = r.y + r.height;
        sx[1] = r.x + r.width;
        sy[1] = r.y + r.height;
        sx[2] = r.x + r.width / 2;
        sy[2] = r.y;
        g.fillPolygon(sx, sy, 3);

        g.setColor(index.getColor(1.0));
        sx[0] = r.x + r.width;
        sy[0] = r.y + r.height;
        sx[1] = r.x + r.width;
        sy[1] = r.y;
        sx[2] = r.x + r.width / 2;
        sy[2] = r.y;
        g.fillPolygon(sx, sy, 3);

    }

}