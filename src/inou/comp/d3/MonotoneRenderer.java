/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.comp.ColorSet;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

/** simple implementation of render class */
public class MonotoneRenderer extends AbstractRenderer {

    private ObjectBuffer[] objectBuffer;// temporary collection

    private ColorSet colorSet = new ColorSet(new Color(0xff222222), new Color(
            0xffe0e0e0), 64);

    private Color backgroundColor = Color.white;

    // (output brightness) = (input brightness)*contrast + brightness
    private float brightness = 0.05f;

    private float contrast = 0.9f;

    public MonotoneRenderer(SceneContext sceneContext) {
        super(sceneContext);
    }

    public MonotoneRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        super(sceneContext, camera, rc);
    }

    /**
     * (output brightness) = (input brightness)*contrast + brightness : Default =
     * 0.05
     */
    final public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    /**
     * (output brightness) = (input brightness)*contrast + brightness : Default =
     * 0.9
     */
    final public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void updateObjects() {
        prepareVertexBuffer();
        initializeObjectBuffer();
    }

    private void initializeObjectBuffer() {
        ArrayList objectList = new ArrayList();
        int vertexCounter = 0;
        IGeometricObject[] objects = sceneContext.getObjects();
        for (int i = 0; i < objects.length; i++) {
            VectorQD[] localVertices = objects[i].getVertices();
            for (int j = 0; j < localVertices.length; j++) {
                vertexReference[j + vertexCounter] = localVertices[j];
            }
            if (objects[i] instanceof IPolygonObject) {
                IPolygonObject polygonObject = (IPolygonObject) objects[i];
                for (int j = 0; j < polygonObject.getSurfaceNumber(); j++) {
                    SurfaceBuffer sb = new SurfaceBuffer(polygonObject, j,
                            vertexCounter);
                    objectList.add(sb);
                }
            } else if (objects[i] instanceof IWireObject) {
                IWireObject wireObject = (IWireObject) objects[i];
                for (int j = 0; j < wireObject.getLineNumber(); j++) {
                    WireBuffer wo = new WireBuffer(wireObject, j, vertexCounter);
                    objectList.add(wo);
                }
            } else if (objects[i] instanceof StringObject) {
                StringObject stringObject = (StringObject) objects[i];
                NonSurfaceBuffer nb = new NonSurfaceBuffer(stringObject,
                        TYPE_STRING);
                nb.vertexId = vertexCounter;
                objectList.add(nb);
            } else if (objects[i] instanceof CircleObject) {
                CircleObject circleObject = (CircleObject) objects[i];
                NonSurfaceBuffer nb = new NonSurfaceBuffer(circleObject,
                        TYPE_CIRCLE);
                nb.vertexId = vertexCounter;
                objectList.add(nb);
            } else {
                // not supported object
            }
            vertexCounter += localVertices.length;
        }
        objectBuffer = new ObjectBuffer[objectList.size()];
        for (int i = 0; i < objectBuffer.length; i++) {
            objectBuffer[i] = (ObjectBuffer) objectList.get(i);
        }
    }

    public void paint(Graphics g) {
        synchronized (camera) {
            if (objectBuffer == null || getRendererComponent() == null)
                return;

            Rectangle borderRect = rendererComponent.getPaintRegion();
            g.setColor(backgroundColor);
            g.fillRect(borderRect.x, borderRect.y, borderRect.width,
                    borderRect.height);

            preparePainting();

            caluculateZposition();
            sortBuffer();
            drawObjects(g);
        }
    }

    private void caluculateZposition() {
        for (int i = 0; i < objectBuffer.length; i++) {
            objectBuffer[i].adjustCenter();
        }
    }

    private void sortBuffer() {
        Arrays.sort(objectBuffer);
    }

    private void drawObjects(Graphics g) {
        for (int i = 0; i < objectBuffer.length; i++) {
            switch (objectBuffer[i].typeId) {
            case TYPE_SURFACE:
                drawSurface(g, (SurfaceBuffer) objectBuffer[i]);
                break;
            case TYPE_WIRE:
                drawWire(g, (WireBuffer) objectBuffer[i]);
                break;
            case TYPE_STRING:
                drawString(g, (NonSurfaceBuffer) objectBuffer[i]);
                break;
            case TYPE_CIRCLE:
                drawCircle(g, (NonSurfaceBuffer) objectBuffer[i]);
                break;
            default:
            }
        }
    }

    private VectorQD tmp1Vec = new VectorQD();

    private VectorQD tmp2Vec = new VectorQD();

    private VectorQD resultVec = new VectorQD();

    protected void drawSurface(Graphics g, SurfaceBuffer surfaceBuffer) {
        surfaceBuffer.surface.getNormalVector(surfaceBuffer.object
                .getVertices(), tmp1Vec, tmp2Vec, resultVec);
        if (!surfaceBuffer.surface.isReversible()
                && !isVisible(resultVec,
                        vertexReference[surfaceBuffer.bufferIndex[0]]))
            return;
        for (int i = 0; i < surfaceBuffer.bufferIndex.length; i++) {
            VectorQD vec = vertexBuffer[surfaceBuffer.bufferIndex[i]];
            if (vec.z < 0)
                return;
            int px = (int) vec.x;
            int py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height)
                return;
            surfaceBuffer.polygonX[i] = px + centerPosition.x;
            surfaceBuffer.polygonY[i] = py + centerPosition.y;
        }
        g.setColor(colorSet.getColor(getBrightness(resultVec,
                vertexReference[surfaceBuffer.bufferIndex[0]],
                surfaceBuffer.surface.isReversible())));
        g.fillPolygon(surfaceBuffer.polygonX, surfaceBuffer.polygonY,
                surfaceBuffer.bufferIndex.length);
    }

    private boolean isVisible(VectorQD normalVector, VectorQD position) {
        tmp1Vec.substitute(camera.getPosition());
        tmp1Vec.subs(position);
        return (tmp1Vec.innerProduct(normalVector) >= 0);
    }

    private double getBrightness(Vector3D surfaceVector,
            Vector3D surfacePosition, boolean reversible) {
        Light[] lights = sceneContext.getLights();
        if (lights.length == 0)
            return 0.5;
        double ret = 0;
        double lightsNumber = lights.length;
        for (int i = 0; i < lights.length; i++) {
            ret += (lights[i].getBrightness(surfaceVector, surfacePosition));
        }
        ret /= lightsNumber;
        if (reversible) {
            ret = Math.abs(ret) / 2 + 0.5;
        } else {
            ret = (ret + 1.) / 2.;
        }
        if (ret < 0)
            ret = 0;
        if (ret > 1)
            ret = 1;
        ret = ret * contrast + brightness;
        return ret;
    }

    protected void drawWire(Graphics g, WireBuffer wireBuffer) {
        VectorQD vec = vertexBuffer[wireBuffer.startId];
        if (vec.z < 0)
            return;
        int sx = (int) vec.x;
        int sy = (int) vec.y;
        if (sx < (-paintRegion.width) || sx > paintRegion.width
                || sy < (-paintRegion.height) || sy > paintRegion.height)
            return;
        sx = sx + centerPosition.x;
        sy = sy + centerPosition.y;

        vec = vertexBuffer[wireBuffer.endId];
        if (vec.z < 0)
            return;
        int ex = (int) vec.x;
        int ey = (int) vec.y;
        if (ex < (-paintRegion.width) || ex > paintRegion.width
                || ey < (-paintRegion.height) || ey > paintRegion.height)
            return;
        ex = ex + centerPosition.x;
        ey = ey + centerPosition.y;

        g.setColor(colorSet.getColor(1.0 - getBrightness(wireBuffer
                .getLineVector(), vertexReference[wireBuffer.startId], true)));
        g.drawLine(sx, sy, ex, ey);
    }

    private void drawString(Graphics g, NonSurfaceBuffer nonBuffer) {
        VectorQD vec = vertexBuffer[nonBuffer.vertexId];
        if (vec.z < 0)
            return;
        int px = (int) vec.x;
        int py = (int) vec.y;
        if (px < (-paintRegion.width) || px > paintRegion.width
                || py < (-paintRegion.height) || py > paintRegion.height)
            return;
        StringObject sto = (StringObject) nonBuffer.object;
        if (nonBuffer.width == -1) {
            Dimension d = sto.getText().getSize(g);
            nonBuffer.width = d.width >> 1;
            nonBuffer.height = d.height >> 1;
        }
        px = px + centerPosition.x;
        py = py + centerPosition.y;
        sto.getText().drawContents(g, px - nonBuffer.width,
                py + nonBuffer.height);
    }

    protected void drawCircle(Graphics g, NonSurfaceBuffer nonBuffer) {
        VectorQD vec = vertexBuffer[nonBuffer.vertexId];
        if (vec.z < 0)
            return;
        int px = (int) vec.x;
        int py = (int) vec.y;
        if (px < (-paintRegion.width) || px > paintRegion.width
                || py < (-paintRegion.height) || py > paintRegion.height)
            return;
        CircleObject co = (CircleObject) nonBuffer.object;
        tmp1Vec.substitute(camera.getPosition());
        tmp1Vec.subs(vertexReference[nonBuffer.vertexId]).normalize();
        g.setColor(colorSet.getColor(getBrightness(tmp1Vec,
                vertexReference[nonBuffer.vertexId], false)));
        double perse = persepectiveRatio(vec.z);
        int size = (int) (co.getDiameter() * perse);
        if (size > paintRegion.width)
            return;
        int radious = size / 2;
        px = px + centerPosition.x;
        py = py + centerPosition.y;
        g.fillOval(px - radious, py - radious, size, size);
    }

    protected abstract class ObjectBuffer implements Comparable {

        protected ObjectBuffer(IGeometricObject obj) {
            object = obj;
        }

        abstract void adjustCenter();

        protected IGeometricObject object;

        protected int typeId;

        protected double z;

        final public double getValue() {
            return z;
        }

        final public int compareTo(Object obj) {
            double ext = ((ObjectBuffer) obj).getValue();
            if (ext == z)
                return 0;
            return ((z - ext) > 0) ? -1 : 1;
        }
    }

    protected class SurfaceBuffer extends ObjectBuffer {
        Surface surface;

        int[] bufferIndex;// id of buffer array

        int[] polygonX;

        int[] polygonY;

        SurfaceBuffer(IPolygonObject object, int surfaceId, int vertexCounter) {
            super(object);
            surface = object.getSurfaceById(surfaceId);
            bufferIndex = new int[surface.index.length];
            polygonX = new int[surface.index.length];
            polygonY = new int[surface.index.length];
            typeId = TYPE_SURFACE;
            for (int k = 0; k < bufferIndex.length; k++) {
                bufferIndex[k] = vertexCounter + surface.index[k];
            }
        }

        void adjustCenter() {
            z = 0;
            for (int i = 0; i < bufferIndex.length; i++) {
                z += vertexBuffer[bufferIndex[i]].z;
            }
            z /= bufferIndex.length;
        }
    }

    protected class WireBuffer extends ObjectBuffer {
        int lineId;

        int startId, endId;

        WireBuffer(IWireObject object, int j, int vertexCounter) {
            super(object);
            lineId = j;
            startId = vertexCounter + object.getLinePareById(j).start;
            endId = vertexCounter + object.getLinePareById(j).end;
            typeId = TYPE_WIRE;
        }

        void adjustCenter() {
            z = (vertexBuffer[startId].z + vertexBuffer[endId].z) / 2;
        }

        Color getColor() {
            return ((IWireObject) object).getColorById(lineId);
        }

        VectorQD getLineVector() {
            ((IWireObject) object).getLinePareById(lineId).getLineVector(
                    object.getVertices(), tmpVec);
            tmpVec.normalize();
            return tmpVec;
        }
    }

    private VectorQD tmpVec = new VectorQD();

    protected class NonSurfaceBuffer extends ObjectBuffer {
        int vertexId;

        int width = -1, height = -1;

        NonSurfaceBuffer(IGeometricObject object, int type) {
            super(object);
            typeId = type;
        }

        void adjustCenter() {
            z = vertexBuffer[vertexId].z;
        }
    }

}
