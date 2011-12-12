/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.d3.IGeometricObject;
import inou.comp.ngraph.PlotData3D;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.RenderingInfo3D;
import inou.math.MathVector;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Graphics;
import java.awt.Rectangle;

public class ObjectPlotter3D extends Plotter3D {

    private IObjectHandler handler;

    public ObjectPlotter3D(PlotData3D data, IObjectHandler h) {
        super(data);
        handler = h;
    }

    private IGeometricObject[] objectCache;

    protected void draw3D(RenderingInfo3D info, MathVector[] vertices) {
        if (vertices == null)
            return;
        int num = vertices.length;
        if (objectCache == null || num != objectCache.length) {
            objectCache = new IGeometricObject[num];
        }

        VectorQD[] qdVertices = new VectorQD[vertices.length];
        for (int i = 0; i < qdVertices.length; i++) {
            qdVertices[i] = copyQD(vertices[i]);
            info.real2scene(qdVertices[i]);
        }

        for (int i = 0; i < num; i++) {
            IGeometricObject obj = handler.updateObject(info, objectCache[i],
                    vertices[i], i);
            if (obj == null && objectCache[i] == null) {
                continue;
            }
            if (obj == null) {
                objectCache[i] = null;
                continue;
            }
            if (objectCache[i] == null) {
                objectCache[i] = obj;
                obj.setPosition(qdVertices[i]);
                continue;
            }
            obj.setPosition(qdVertices[i]);
            info.addSceneObject(obj);
        }
    }

    protected VectorQD copyQD(MathVector vec) {
        if (vec instanceof Vector3D)
            return new VectorQD((Vector3D) vec);
        return new VectorQD(vec.v(0), vec.v(1), vec.v(2));
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        // do nothing?
    }

}