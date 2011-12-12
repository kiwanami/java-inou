/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

/** simple implementation of render class */
public class WireRenderer extends AbstractRenderer {

    protected Wire[] wireBuffer;// temporary collection

    protected NonWire[] nonwireBuffer;

    protected Color backgroundColor = Color.white;

    public WireRenderer(SceneContext sceneContext) {
        super(sceneContext);
    }

    public WireRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        super(sceneContext, camera, rc);
    }

    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void updateObjects() {
        prepareVertexBuffer();
        prepareWireBuffer();
        prepareNonwireBuffer();
        initializeWireBuffer();
    }

    private int calculateLineNumber() {
        int number = 0;
        IGeometricObject[] objects = sceneContext.getObjects();
        if (objects == null)
            return 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof IWireObject) {
                number += ((IWireObject) objects[i]).getLineNumber();
            }
        }
        return number;
    }

    protected void prepareWireBuffer() {
        wireBuffer = new Wire[calculateLineNumber()];
        for (int i = 0; i < wireBuffer.length; i++) {
            wireBuffer[i] = new Wire();
        }
    }

    private int calculateNonwireObjectNumber() {
        int number = 0;
        IGeometricObject[] objects = sceneContext.getObjects();
        if (objects == null)
            return 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof StringObject
                    || objects[i] instanceof CircleObject) {
                number++;
            }
        }
        return number;
    }

    private void prepareNonwireBuffer() {
        nonwireBuffer = new NonWire[calculateNonwireObjectNumber()];
        for (int i = 0; i < nonwireBuffer.length; i++) {
            nonwireBuffer[i] = new NonWire();
        }
    }

    protected void initializeWireBuffer() {
        int wireCounter = 0;
        int nonwireCounter = 0;
        int vertexCounter = 0;
        IGeometricObject[] objects = sceneContext.getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof IWireObject) {
                IWireObject wireObject = (IWireObject) objects[i];
                VectorQD[] localVertices = wireObject.getVertices();
                for (int j = 0; j < localVertices.length; j++) {
                    vertexReference[j + vertexCounter] = localVertices[j];
                }
                for (int j = 0; j < wireObject.getLineNumber(); j++) {
                    wireBuffer[wireCounter].setLine(wireObject, j,
                            vertexCounter);
                    wireCounter++;
                }
                vertexCounter += localVertices.length;
            } else {
                if (objects[i] instanceof StringObject) {
                    nonwireBuffer[nonwireCounter].typeId = TYPE_STRING;
                } else if (objects[i] instanceof CircleObject) {
                    nonwireBuffer[nonwireCounter].typeId = TYPE_CIRCLE;
                    nonwireBuffer[nonwireCounter].color = ((CircleObject) objects[i])
                            .getBorderColor();
                }
                nonwireBuffer[nonwireCounter].object = objects[i];
                vertexReference[vertexCounter] = objects[i].getVertices()[0];
                nonwireBuffer[nonwireCounter].bufferId = vertexCounter;
                nonwireCounter++;
                vertexCounter++;
            }
        }
    }

    public synchronized void paint(Graphics g) {
        if (wireBuffer == null || getRendererComponent() == null)
            return;

        Rectangle borderRect = rendererComponent.getPaintRegion();
        g.setColor(backgroundColor);
        g.fillRect(borderRect.x, borderRect.y, borderRect.width,
                borderRect.height);

        preparePainting();

        drawIWireObject(g);
        drawNonIWireObject(g);
    }

    private void drawIWireObject(Graphics g) {
        Wire currentWire;
        int sx, sy, ex, ey;// terminal position of a line
        int px, py, pz;// temporary calculation position
        for (int i = 0; i < wireBuffer.length; i++) {
            currentWire = wireBuffer[i];
            // Start ID
            VectorQD vec = vertexBuffer[currentWire.startPointId];
            if (vec.z < 0) {
                continue;
            }
            px = (int) vec.x;
            py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height) {
                continue;
            }
            sx = px + centerPosition.x;
            sy = py + centerPosition.y;
            // End ID
            vec = vertexBuffer[currentWire.endPointId];
            if (vec.z < 0) {
                continue;
            }
            px = (int) vec.x;
            py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height) {
                continue;
            }
            ex = px + centerPosition.x;
            ey = py + centerPosition.y;
            //
            g.setColor(currentWire.color);
            g.drawLine(sx, sy, ex, ey);
        }
    }

    private void drawNonIWireObject(Graphics g) {
        for (int i = 0; i < nonwireBuffer.length; i++) {
            NonWire currentObject = nonwireBuffer[i];
            VectorQD vec = vertexBuffer[currentObject.bufferId];
            if (vec.z < 0) {
                continue;
            }
            int px = (int) vec.x;
            int py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height) {
                continue;
            }
            currentObject.xx = px + centerPosition.x;
            currentObject.yy = py + centerPosition.y;
            currentObject.perse = persepectiveRatio(vec.z);
            switch (currentObject.typeId) {
            case TYPE_STRING:
                drawStringObject(g, currentObject);
                break;
            case TYPE_CIRCLE:
                drawCircleObject(g, currentObject);
            default:
            // not supported object
            }
        }
    }

    private void drawStringObject(Graphics g, NonWire currentObject) {
        StringObject sto = (StringObject) currentObject.object;
        if (currentObject.width == -1) {
            Dimension d = sto.getText().getSize(g);
            currentObject.width = d.width >> 1;
            currentObject.height = d.height >> 1;
        }
        sto.getText().drawContents(g, currentObject.xx - currentObject.width,
                currentObject.yy + currentObject.height);
    }

    private void drawCircleObject(Graphics g, NonWire currentObject) {
        CircleObject co = (CircleObject) currentObject.object;
        g.setColor(currentObject.color);
        int size = (int) (co.getDiameter() * currentObject.perse);
        int radious = size / 2;
        g.drawOval(currentObject.xx - radious, currentObject.yy - radious,
                size, size);
    }

    protected class Wire {

        IWireObject object;// belonging mesh

        int lineId; // line id in this object

        int startPointId, endPointId;// id of buffer array

        Color color;

        void setLine(IWireObject object, int lineId, int pointArrayOffset) {
            this.object = object;
            this.lineId = lineId;
            startPointId = pointArrayOffset
                    + object.getLinePareById(lineId).start;
            endPointId = pointArrayOffset + object.getLinePareById(lineId).end;
            color = object.getColorById(lineId);
        }

    }

    protected static final int TYPE_CIRCLE = 0;

    protected static final int TYPE_STRING = 1;

    protected class NonWire {
        IGeometricObject object;

        int bufferId;// id of buffer array

        int typeId; // type of this object

        int xx, yy; // screen position

        int width = -1, height = -1;// size of this object

        double perse;// size correction

        Color color;
    }
}