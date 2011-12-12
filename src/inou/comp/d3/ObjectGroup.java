/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.util.ArrayList;

public class ObjectGroup implements IPolygonObject {

    private ArrayList objectList = new ArrayList();

    private VectorQD position = new VectorQD(0, 0, 0);

    private VectorQD[] verticesCache;

    private LinePair[] lineCache;

    private Surface[] surfaceCache;

    private IGeometricObject[] objectCache;

    public ObjectGroup() {
    }

    public ObjectGroup(IGeometricObject[] objects) {
        for (int i = 0; i < objects.length; i++) {
            objectList.add(objects[i]);
        }
    }

    public void addObject(IGeometricObject object) {
        objectList.add(object);
        verticesCache = null;
        lineCache = null;
        surfaceCache = null;
        objectCache = null;
    }

    public int getObjectNumber() {
        return objectList.size();
    }

    private void prepareObjectCache() {
        if (objectCache == null) {
            objectCache = new IGeometricObject[objectList.size()];
            for (int i = 0; i < objectCache.length; i++) {
                objectCache[i] = (IGeometricObject) objectList.get(i);
            }
        }
    }

    public IGeometricObject getObject(int i) {
        prepareObjectCache();
        return objectCache[i];
    }

    // ===(delegation of IGeometricObject)

    public VectorQD getCenter() {
        return position;
    }

    public IGeometricObject getCopy() {
        prepareObjectCache();
        ObjectGroup copy = new ObjectGroup();
        for (int i = 0; i < objectCache.length; i++) {
            copy.addObject(objectCache[i].getCopy());
        }
        return copy;
    }

    public VectorQD[] getVertices() {
        if (verticesCache == null) {
            prepareObjectCache();
            int num = 0;
            for (int i = 0; i < objectCache.length; i++) {
                num += objectCache[i].getVertices().length;
            }
            verticesCache = new VectorQD[num];
            int count = 0;
            for (int i = 0; i < objectCache.length; i++) {
                IGeometricObject obj = objectCache[i];
                for (int j = 0; j < obj.getVertices().length; j++) {
                    verticesCache[count++] = obj.getVertices()[j];
                }
            }
        }
        return verticesCache;
    }

    public VectorQD getVertex(int id) {
        return getVertices()[id];
    }

    public void translate(MatrixQD trans) {
        prepareObjectCache();
        trans.qmults(position);
        for (int i = 0; i < objectCache.length; i++) {
            objectCache[i].translate(trans);
        }
    }

    public void translate(VectorQD dm) {
        prepareObjectCache();
        position.adds(dm);
        for (int i = 0; i < objectCache.length; i++) {
            objectCache[i].translate(dm);
        }
    }

    /** rotate around mass-center */
    public void rotate(MatrixQD rot, VectorQD temp) {
        prepareObjectCache();
        for (int i = 0; i < objectCache.length; i++) {
            IGeometricObject obj = (IGeometricObject) objectList.get(i);
            obj.rotate(rot, temp);
            temp.substitute(obj.getCenter());
            temp.subs(position);
            rot.qmults(temp).qadds(position);
            obj.setPosition(temp);
        }
    }

    public void rotate(MatrixQD rot) {
        rotate(rot, new VectorQD());
    }

    public void expansion(double t) {
        prepareObjectCache();
        position.mults(t);
        for (int i = 0; i < objectCache.length; i++) {
            IGeometricObject obj = objectCache[i];
            VectorQD pos = (VectorQD) (obj.getCenter().getCopy());
            obj.expansion(t);
            pos.mults(t);
            obj.setPosition(pos);
        }
    }

    public void setPosition(MathVector ps) {
        prepareObjectCache();
        VectorQD dif = new VectorQD(ps.getCopy());
        dif.subs(position);
        position.substitute(ps);
        for (int i = 0; i < ps.getDimension(); i++) {
            objectCache[i].translate(dif);
        }
    }

    // ===(delegation of IWireObject)

    private int getVertexIndex(VectorQD v) {
        for (int i = 0; i < verticesCache.length; i++) {
            if (verticesCache[i] == v) {
                return i;
            }
        }
        throw new RuntimeException("Not found the vertex in the vertex array.");
    }

    private void prepareLineCache() {
        if (lineCache == null) {
            prepareObjectCache();
            int lineNum = 0;
            for (int i = 0; i < objectCache.length; i++) {
                if (objectCache[i] instanceof IWireObject) {
                    IWireObject iw = (IWireObject) objectCache[i];
                    lineNum += iw.getLineNumber();
                }
            }
            lineCache = new LinePair[lineNum];
            int count = 0;
            for (int i = 0; i < objectCache.length; i++) {
                if (objectCache[i] instanceof IWireObject) {
                    IWireObject iw = (IWireObject) objectCache[i];
                    for (int j = 0; j < iw.getLineNumber(); j++) {
                        int sp = getVertexIndex(iw.getStartVertex(j));
                        int ep = getVertexIndex(iw.getEndVertex(j));
                        lineCache[count++] = new LinePair(sp, ep, iw
                                .getColorById(j));
                    }
                }
            }
        }
    }

    public int getLineNumber() {
        prepareLineCache();
        return lineCache.length;
    }

    public LinePair getLinePareById(int id) {
        prepareLineCache();
        return lineCache[id];
    }

    public VectorQD getStartVertex(int lineId) {
        prepareLineCache();
        return verticesCache[lineCache[lineId].start];
    }

    public VectorQD getEndVertex(int lineId) {
        prepareLineCache();
        return verticesCache[lineCache[lineId].end];
    }

    public Color getColorById(int lineId) {
        prepareLineCache();
        return lineCache[lineId].color;
    }

    // ===(IPolygonObject implementation)

    private void prepareSurfaceCache() {
        if (surfaceCache == null) {
            prepareObjectCache();
            int surfaceNum = 0;
            for (int i = 0; i < objectCache.length; i++) {
                if (objectCache[i] instanceof IPolygonObject) {
                    IPolygonObject iw = (IPolygonObject) objectCache[i];
                    surfaceNum += iw.getSurfaceNumber();
                }
            }
            surfaceCache = new Surface[surfaceNum];
            int count = 0;
            for (int i = 0; i < objectCache.length; i++) {
                if (objectCache[i] instanceof IPolygonObject) {
                    IPolygonObject iw = (IPolygonObject) objectCache[i];
                    for (int j = 0; j < iw.getSurfaceNumber(); j++) {
                        Surface s = iw.getSurfaceById(j).getCopy();
                        for (int k = 0; k < s.getVertexNumber(); k++) {
                            s.index[k] = getVertexIndex(iw
                                    .getVertexBySurfaceId(j, k));
                        }
                        surfaceCache[count++] = s;
                    }
                }
            }
        }
    }

    public VectorQD getVertexBySurfaceId(int surfaceId, int vertexId) {
        prepareSurfaceCache();
        return verticesCache[surfaceCache[surfaceId].index[vertexId]];
    }

    public Surface getSurfaceById(int surfaceId) {
        prepareSurfaceCache();
        return surfaceCache[surfaceId];
    }

    public int getSurfaceNumber() {
        prepareSurfaceCache();
        return surfaceCache.length;
    }

    public VectorQD getSurfaceCenterBySurfaceId(int surfaceId) {
        prepareSurfaceCache();
        return surfaceCache[surfaceId].getCenterPosition(verticesCache);
    }

}
