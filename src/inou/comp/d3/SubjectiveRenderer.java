/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.comp.MemoryImage;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class SubjectiveRenderer extends AbstractRenderer {

    private RendererComponent lastRenderer = null;

    private MemoryImage memory;

    private Image image;

    // (output brightness) = (input brightness)*contrast + brightness
    private float brightness = 0.4f;

    private float contrast = 0.4f;

    private Color bgColor;

    private int bgRed, bgGreen, bgBlue;

    private double alphaDistance = 6;// alpha rendering distance

    private double fogDistance = 12;// fog start distance

    private double eliminateDistance = 20;// visible limit distance

    private double fogLength = eliminateDistance - fogDistance;

    private ObjectBuffer[] objectBuffer;// temporary collection

    private StringBuffer[] textBuffer;

    public SubjectiveRenderer(SceneContext sceneContext) {
        this(sceneContext, null, null);
    }

    public SubjectiveRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        super(sceneContext, camera, rc);
        setBackground(Color.white);
    }

    final public void setBackground(Color color) {
        bgColor = color;
        bgRed = bgColor.getRed();
        bgGreen = bgColor.getGreen();
        bgBlue = bgColor.getBlue();
    }

    /**
     * @param eliminateDistance
     *            When the distance between camera and objects is more than
     *            "Eliminate Distance", this renderer eliminates objects.
     *            (Default:20)
     */
    final public void setEliminateDistance(double eliminateDistance) {
        this.eliminateDistance = eliminateDistance;
        fogLength = eliminateDistance - fogDistance;
    }

    /**
     * @param fogDistance
     *            When the distance between camera and objects is more than "Fog
     *            Distance", this renderer fogs objects. (Default:12)
     */
    final public void setFogDistance(double fogDistance) {
        this.fogDistance = fogDistance;
        this.fogLength = eliminateDistance - fogDistance;
    }

    /**
     * @param alphaDistance
     *            When the distance between camera and objects is less than
     *            "Alpha Distance", this renderer makes objects transparent.
     *            (Default:6)
     */
    final public void setAlphaDistance(double alphaDistance) {
        this.alphaDistance = alphaDistance;
    }

    final public double getAlphaDistance() {
        return alphaDistance;
    }

    /**
     * (output brightness) = (input brightness)*contrast + brightness : Default =
     * 0.4
     */
    final public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    /**
     * (output brightness) = (input brightness)*contrast + brightness : Default =
     * 0.4
     */
    final public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    final public void updateObjects() {
        prepareVertexBuffer();
        initializeObjectBuffer();
        if (memory != null) {
            updateConfigration();
        }
    }

    private void initializeObjectBuffer() {
        ArrayList objectList = new ArrayList();
        ArrayList textList = new ArrayList();
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
                StringBuffer nb = new StringBuffer(stringObject);
                nb.vertexId = vertexCounter;
                textList.add(nb);
            } else if (objects[i] instanceof CircleObject) {
                CircleObject circleObject = (CircleObject) objects[i];
                CircleBuffer nb = new CircleBuffer(circleObject);
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
        textBuffer = new StringBuffer[textList.size()];
        for (int i = 0; i < textBuffer.length; i++) {
            textBuffer[i] = (StringBuffer) textList.get(i);
        }
    }

    final public void paint(Graphics g) {
        if (getRendererComponent() == null)
            return;
        if (memory == null) {
            updateConfigration();
            return;
        }
        g.drawImage(image, paintRegion.x, paintRegion.y, rendererComponent
                .getComponent());
        drawStrings(g);
        lastRenderer = rendererComponent;
    }

    private void drawStrings(Graphics g) {
        for (int i = 0; i < textBuffer.length; i++) {
            drawString(g, textBuffer[i]);
        }
    }

    private void paintMemory() {
        if (rendererComponent.getPaintRegion() == null)
            return;
        if (objectBuffer == null || camera == null)
            return;

        synchronized (camera) {
            preparePainting();
            memory.clear(bgColor.getRGB());
            caluculateZposition();
            sortBuffer();
            drawObjects();
        }
        memory.update();
    }

    public void updatePaintRegion() {
        if (paintRegion == null)
            return;
        lastRenderer = null;
        super.updatePaintRegion();
        if (camera != null) {
            camera.updateMessage();
        }
    }

    protected void prepareOption() {
        memory = new MemoryImage(paintRegion.width, paintRegion.height);
        image = memory.createImage(rendererComponent.getComponent(), true);
        if (lastRenderer != rendererComponent) {
            updateSizeCorrection();
            rendererComponent.repaintOrder();
        }
    }

    public void updateConfigration() {
        paintMemory();
    }

    private void caluculateZposition() {
        for (int i = 0; i < objectBuffer.length; i++) {
            objectBuffer[i].adjustCenter();
        }
    }

    private void sortBuffer() {
        Arrays.sort(objectBuffer);
    }

    private void drawObjects() {
        for (int i = 0; i < objectBuffer.length; i++) {
            switch (objectBuffer[i].typeId) {
            case TYPE_SURFACE:
                drawSurface((SurfaceBuffer) objectBuffer[i]);
                break;
            case TYPE_WIRE:
                drawWire((WireBuffer) objectBuffer[i]);
                break;
            case TYPE_CIRCLE:
                drawCircle((CircleBuffer) objectBuffer[i]);
                break;
            default:
            }
        }
    }

    private VectorQD tmp1Vec = new VectorQD();

    private VectorQD tmp2Vec = new VectorQD();

    private VectorQD resultVec = new VectorQD();

    final protected void drawSurface(SurfaceBuffer surfaceBuffer) {
        surfaceBuffer.surface.getNormalVector(surfaceBuffer.object
                .getVertices(), tmp1Vec, tmp2Vec, resultVec);
        if (!surfaceBuffer.surface.isReversible()
                && !isVisible(resultVec,
                        vertexReference[surfaceBuffer.bufferIndex[0]]))
            return;
        for (int i = 0; i < surfaceBuffer.bufferIndex.length; i++) {
            VectorQD vec = vertexBuffer[surfaceBuffer.bufferIndex[i]];
            if (vec.z < 0 || vec.z > eliminateDistance)
                return;
            int px = (int) vec.x;
            int py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height)
                return;
            surfaceBuffer.polygonX[i] = px + centerPosition.x;
            surfaceBuffer.polygonY[i] = py + centerPosition.y;
        }
        try {
            memory
                    .fillPolygon(
                            surfaceBuffer.polygonX,
                            surfaceBuffer.polygonY,
                            getRGB(
                                    surfaceBuffer.surface.getColor(),
                                    translateBrightness(getBrightness(
                                            resultVec,
                                            vertexReference[surfaceBuffer.bufferIndex[0]],
                                            surfaceBuffer.surface
                                                    .isReversible())),
                                    surfaceBuffer.z));
        } catch (RuntimeException e) {
            System.err.println("-----");
            for (int i = 0; i < surfaceBuffer.bufferIndex.length; i++) {
                System.err.println(surfaceBuffer.polygonX[i] + ", "
                        + surfaceBuffer.polygonY[i]);
            }
            e.printStackTrace();
        }
    }

    private boolean isVisible(VectorQD normalVector, VectorQD position) {
        tmp1Vec.substitute(camera.getPosition());
        tmp1Vec.subs(position);
        return (tmp1Vec.innerProduct(normalVector) >= 0);
    }

    private int getRGB(Color color, float brightness, double z) {
        int sr = color.getRed();
        int sg = color.getGreen();
        int sb = color.getBlue();
        int foreRatio = 0;
        int backRatio = 0;
        if (brightness > 0.5) {
            foreRatio = Math.min((int) (256 * (brightness - 0.5)), 255);
            backRatio = 255 - foreRatio;
            foreRatio = 255 * foreRatio;
            sr = (foreRatio + sr * backRatio) >> 8;
            sg = (foreRatio + sg * backRatio) >> 8;
            sb = (foreRatio + sb * backRatio) >> 8;
        } else {
            foreRatio = Math.min((int) (512 * brightness), 255);
            sr = (sr * foreRatio) >> 8;
            sg = (sg * foreRatio) >> 8;
            sb = (sb * foreRatio) >> 8;
        }
        if (z < alphaDistance) {
            int alpha = (int) Math.max((z * 255) / alphaDistance, 50);
            return (alpha << 24) + (sr << 16) + (sg << 8) + sb;
        } else if (z > fogDistance) {
            foreRatio = Math.min(
                    (int) (255 * (eliminateDistance - z) / fogLength), 255);
            backRatio = 255 - foreRatio;
            sr = (sr * foreRatio + bgRed * backRatio) >> 8;
            sg = (sg * foreRatio + bgGreen * backRatio) >> 8;
            sb = (sb * foreRatio + bgBlue * backRatio) >> 8;
        }
        return 0xff000000 + (sr << 16) + (sg << 8) + sb;
    }

    private float translateBrightness(float in) {
        float ret = Math.max(Math.min(contrast * in + brightness, 1), 0);
        return ret;

    }

    private float getBrightness(Vector3D surfaceVector,
            Vector3D surfacePosition, boolean reversible) {
        Light[] lights = sceneContext.getLights();
        if (lights.length == 0)
            return 0.5f;
        float ret = 0;
        float lightsNumber = (float) lights.length;
        for (int i = 0; i < lights.length; i++) {
            ret += (lights[i].getBrightness(surfaceVector, surfacePosition));
        }
        ret /= lightsNumber;
        if (reversible) {
            ret = Math.abs(ret) / 2.f + 0.5f;
        } else {
            ret = (ret + 1.0f) / 2.f;
        }
        if (ret < 0f)
            ret = 0f;
        if (ret > 1f)
            ret = 1f;
        return ret;
    }

    private void drawWire(WireBuffer wireBuffer) {
        VectorQD vec = vertexBuffer[wireBuffer.startId];
        if (vec.z < 0 || vec.z > eliminateDistance)
            return;
        int sx = (int) vec.x;
        int sy = (int) vec.y;
        if (sx < (-paintRegion.width) || sx > paintRegion.width
                || sy < (-paintRegion.height) || sy > paintRegion.height)
            return;
        sx = sx + centerPosition.x;
        sy = sy + centerPosition.y;

        vec = vertexBuffer[wireBuffer.endId];
        if (vec.z < 0 || vec.z > eliminateDistance)
            return;
        int ex = (int) vec.x;
        int ey = (int) vec.y;
        if (ex < (-paintRegion.width) || ex > paintRegion.width
                || ey < (-paintRegion.height) || ey > paintRegion.height)
            return;
        ex = ex + centerPosition.x;
        ey = ey + centerPosition.y;

        memory.drawLine(sx, sy, ex, ey, getRGB(wireBuffer.getColor(),
                translateBrightness(1.0f - (float) getBrightness(wireBuffer
                        .getLineVector(), vertexReference[wireBuffer.startId],
                        true)), wireBuffer.z));
    }

    protected void drawCircle(CircleBuffer nonBuffer) {
        VectorQD vec = vertexBuffer[nonBuffer.vertexId];
        if (vec.z < 0 || vec.z > eliminateDistance)
            return;
        int px = (int) vec.x;
        int py = (int) vec.y;
        if (px < (-paintRegion.width) || px > paintRegion.width
                || py < (-paintRegion.height) || py > paintRegion.height)
            return;
        CircleObject co = (CircleObject) nonBuffer.object;
        tmp1Vec.substitute(camera.getPosition());
        tmp1Vec.subs(vertexReference[nonBuffer.vertexId]).normalize();
        double perse = persepectiveRatio(vec.z);
        int size = (int) (co.getDiameter() * perse);
        int radious = size / 2;
        if (radious > paintRegion.width)
            return;
        px = px + centerPosition.x;
        py = py + centerPosition.y;
        memory.fillCircle(px, py, radious, getRGB(co.getSurfaceColor(),
                translateBrightness(getBrightness(tmp1Vec,
                        vertexReference[nonBuffer.vertexId], false)),
                nonBuffer.z));
    }

    private void drawString(Graphics g, StringBuffer nonBuffer) {
        VectorQD vec = vertexBuffer[nonBuffer.vertexId];
        if (vec.z < 0 || vec.z > eliminateDistance)
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

    abstract class ObjectBuffer implements Comparable {
        ObjectBuffer(IGeometricObject obj) {
            object = obj;
        }

        abstract void adjustCenter();

        IGeometricObject object;

        int typeId;

        double z;

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

    class SurfaceBuffer extends ObjectBuffer {
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

    class WireBuffer extends ObjectBuffer {
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

    class StringBuffer extends ObjectBuffer {
        int vertexId;

        int width = -1, height = -1;

        StringBuffer(StringObject object) {
            super(object);
            typeId = TYPE_STRING;
        }

        void adjustCenter() {
            z = vertexBuffer[vertexId].z;
        }
    }

    class CircleBuffer extends ObjectBuffer {
        int vertexId;

        CircleBuffer(CircleObject object) {
            super(object);
            typeId = TYPE_CIRCLE;
        }

        void adjustCenter() {
            z = vertexBuffer[vertexId].z;
        }
    }

}
