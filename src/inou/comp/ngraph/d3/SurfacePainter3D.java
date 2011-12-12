/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.d3.IGeometricObject;
import inou.comp.d3.LinePair;
import inou.comp.d3.PolygonObject;
import inou.comp.d3.QSurface;
import inou.comp.d3.Surface;
import inou.comp.d3.WireObject;
import inou.comp.ngraph.ColorManager;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.RealPolygon;
import inou.comp.ngraph.RenderingInfo3D;
import inou.comp.ngraph.SurfaceData3D;
import inou.math.MathVector;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** make 3d mesh from 2d scalar function data */
public class SurfacePainter3D extends Plotter3D {

    private Color surfaceColor = ColorManager.getColor();

    private Color wireColor = Color.gray;

    public SurfacePainter3D(SurfaceData3D surfaceData) {
        super(surfaceData);
    }

    /**
     * if null is given, this painter does not paint suface.
     */
    public void setSurfaceColor(Color color) {
        this.surfaceColor = color;
    }

    public Color getSurfaceColor() {
        return surfaceColor;
    }

    /**
     * wire color can not be null. if you want to eliminate the edge, you set
     * the same color as surface.
     */
    public void setWireColor(Color color) {
        if (color == null)
            return;
        this.wireColor = color;
    }

    public Color getWireColor() {
        return wireColor;
    }

    protected SurfaceData3D getSurfaceData() {
        return (SurfaceData3D) getData();
    }

    protected void draw3D(RenderingInfo3D info, MathVector[] vertices) {
        info.addSceneObject(makeD3Polygon(info, vertices));
    }

    protected IGeometricObject[] makeD3Polygon(RenderingInfo3D info,
            MathVector[] vertices) {
        VectorQD[] qdVertices = new VectorQD[vertices.length];
        for (int i = 0; i < qdVertices.length; i++) {
            qdVertices[i] = copyQD(vertices[i]);
            info.real2scene(qdVertices[i]);
        }

        RealPolygon[] polygons = getSurfaceData().getPolygons(qdVertices);

        LinePair[] linePairs = makeLinePairs(qdVertices, polygons);

        IGeometricObject object = null;
        if (surfaceColor != null) {
            Surface[] surfaces = makeSurfaces(qdVertices, polygons);
            object = new PolygonObject(qdVertices, linePairs, surfaces);
        } else {
            object = new WireObject(qdVertices, linePairs);
        }
        return new IGeometricObject[] { object };
    }

    protected VectorQD copyQD(MathVector vec) {
        if (vec instanceof Vector3D)
            return new VectorQD((Vector3D) vec);
        return new VectorQD(vec.v(0), vec.v(1), vec.v(2));
    }

    protected LinePair[] makeLinePairs(VectorQD[] vertices,
            RealPolygon[] polygons) {
        int lineNum = 0;
        for (int i = 0; i < polygons.length; i++) {
            int num = polygons[i].getVertexNumber();
            if (num == 2) {
                lineNum += 1;
            } else {
                lineNum += num;
            }
        }
        int lineCount = 0;
        LinePair[] linePairs = new LinePair[lineNum];
        for (int i = 0; i < polygons.length; i++) {
            RealPolygon polygon = polygons[i];
            if (polygon.getVertexNumber() == 2) {
                linePairs[lineCount] = new LinePair(polygon
                        .getVertexIndexById(0), polygon.getVertexIndexById(1),
                        wireColor);
            } else {
                for (int j = 0; j < polygon.getVertexNumber(); j++) {
                    int next = j + 1;
                    if (next >= polygon.getVertexNumber()) {
                        next = 0;
                    }
                    linePairs[lineCount] = new LinePair(polygon
                            .getVertexIndexById(j), polygon
                            .getVertexIndexById(next), wireColor);
                    lineCount++;
                }
            }
        }
        return linePairs;
    }

    protected Surface[] makeSurfaces(VectorQD[] vertices, RealPolygon[] polygons) {
        Surface[] surfaces = new Surface[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            RealPolygon pg = polygons[i];
            if (pg.getVertexNumber() == 3) {
                surfaces[i] = new Surface(pg.getVertexIndexById(0), pg
                        .getVertexIndexById(1), pg.getVertexIndexById(2),
                        surfaceColor);
            } else if (pg.getVertexNumber() == 3) {
                surfaces[i] = new QSurface(pg.getVertexIndexById(0), pg
                        .getVertexIndexById(1), pg.getVertexIndexById(2), pg
                        .getVertexIndexById(3), surfaceColor);
            }
            surfaces[i].setReversible(true);
        }
        return surfaces;
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        if (surfaceColor != null) {
            g.setColor(surfaceColor);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
        g.setColor(wireColor);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

}