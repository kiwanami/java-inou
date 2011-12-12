/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.comp.RichString;
import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/** simple implementation of render class */
public class Virtual3DRenderer extends WireRenderer {

    private StringBuffer[] textBuffer;

    // colors
    public final static int WHITE_BACKGROUND = 0;

    public final static int BLACK_BACKGROUND = 1;

    private int colorSwitch = WHITE_BACKGROUND;

    private Color[] colorRight;

    private Color[] colorLeft;

    private Color[] backColor;

    private double diffDistance = 2;// distance of two camera

    private Camera centerCamera;

    public Virtual3DRenderer(SceneContext sceneContext) {
        this(sceneContext, null, null);
    }

    public Virtual3DRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        super(sceneContext, camera, rc);
        setHighSpeedMode();
        this.camera = new DefaultCamera();// dummy camera for just calculating
    }

    public void setBackground(Color backgroundColor) {
        int bright = backgroundColor.getRed();
        bright += backgroundColor.getGreen();
        bright += backgroundColor.getBlue();
        bright /= 3;
        if (bright > 128) {
            setBackground(WHITE_BACKGROUND);
        } else {
            setBackground(BLACK_BACKGROUND);
        }
    }

    public void setBackground(int c) {
        if (c == WHITE_BACKGROUND) {
            colorSwitch = WHITE_BACKGROUND;
        } else {
            colorSwitch = BLACK_BACKGROUND;
        }
    }

    public double getDifferencial() {
        return diffDistance;
    }

    public void setDifferencial(double d) {
        diffDistance = d;
    }

    public void setHighSpeedMode() {
        colorRight = new Color[] { new Color(240, 150, 150),
                new Color(0, 0, 128) };
        colorLeft = new Color[] { new Color(120, 240, 240), new Color(90, 0, 0) };
        backColor = new Color[] { Color.white, Color.black };
    }

    public void setPreciousMode() {
        colorRight = new Color[] { new Color(230, 50, 50, 128),
                new Color(0, 0, 70, 128) };
        colorLeft = new Color[] { new Color(60, 225, 225, 128),
                new Color(160, 0, 0, 128) };
        backColor = new Color[] { Color.white, Color.black };
    }

    public void setCamera(Camera camera) {
        this.centerCamera = camera;
        if (camera != null)
            centerCamera.setRenderer(this);
    }

    public Camera getCamera() {
        return centerCamera;
    }

    public void updateObjects() {
        prepareVertexBuffer();
        prepareWireBuffer();
        initializeWireBuffer();
    }

    protected void initializeWireBuffer() {
        int wireCounter = 0;
        ArrayList nonWireList = new ArrayList();
        ArrayList textList = new ArrayList();
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
                    StringBuffer sb = new StringBuffer();
                    sb.object = (StringObject) objects[i];
                    sb.copiedRichString = new RichString(sb.object.getText()
                            .getContent(), null, null, sb.object.getText()
                            .getFont());
                    sb.bufferId = vertexCounter;
                    textList.add(sb);
                } else if (objects[i] instanceof CircleObject) {
                    NonWire nw = new NonWire();
                    nw.typeId = TYPE_CIRCLE;
                    nw.object = objects[i];
                    nw.bufferId = vertexCounter;
                    nonWireList.add(nw);
                }
                vertexReference[vertexCounter] = objects[i].getVertices()[0];
                vertexCounter++;
            }
        }
        nonwireBuffer = new NonWire[nonWireList.size()];
        for (int i = 0; i < nonWireList.size(); i++) {
            nonwireBuffer[i] = (NonWire) nonWireList.get(i);
        }
        textBuffer = new StringBuffer[textList.size()];
        for (int i = 0; i < textList.size(); i++) {
            textBuffer[i] = (StringBuffer) textList.get(i);
        }
    }

    private MatrixQD camRotMatrix = new MatrixQD();

    private MatrixQD tmpMatrix = new MatrixQD();

    private VectorQD diffVector = new VectorQD();

    private void drawStrings(Graphics g) {
        g.setColor(colorLeft[colorSwitch]);
        for (int i = 0; i < textBuffer.length; i++) {
            drawStringObject(g, textBuffer[i], 0);
        }
        g.setColor(colorRight[colorSwitch]);
        for (int i = 0; i < textBuffer.length; i++) {
            drawStringObject(g, textBuffer[i], 1);
        }
    }

    private void drawStringObject(Graphics g, StringBuffer currentObject, int i) {
        StringObject sto = currentObject.object;
        if (currentObject.width == -1) {
            Dimension d = currentObject.copiedRichString.getSize(g);
            currentObject.width = d.width >> 1;
            currentObject.height = d.height >> 1;
        }
        currentObject.copiedRichString.drawContents(g, currentObject.xx[i]
                - currentObject.width, currentObject.yy[i]
                + currentObject.height);
    }

    final public void paint(Graphics g) {
        synchronized (camera) {
            if (wireBuffer == null || getRendererComponent() == null)
                return;

            Rectangle borderRect = rendererComponent.getPaintRegion();
            g.setColor(backColor[colorSwitch]);
            g.fillRect(borderRect.x, borderRect.y, borderRect.width,
                    borderRect.height);

            Vector3D cameraAngle = centerCamera.getAngle();
            MatrixQD.getInvRotMatrix(cameraAngle.x, cameraAngle.y,
                    cameraAngle.z, camRotMatrix, tmpMatrix);
            diffVector.x = diffDistance;
            diffVector.y = 0;
            diffVector.z = 0;
            diffVector.qmults(camRotMatrix);
            camera.setAngle(cameraAngle);
            VectorQD cameraPosition = camera.getPosition();
            cameraPosition.substitute(centerCamera.getPosition());
            cameraPosition.qadds(diffVector);

            g.setColor(colorRight[colorSwitch]);
            preparePainting();
            drawIWireObject(g);
            drawNonIWireObject(g);
            preDrawString(0);

            camera.setAngle(cameraAngle);
            cameraPosition.substitute(centerCamera.getPosition());
            cameraPosition.qsubs(diffVector);

            g.setColor(colorLeft[colorSwitch]);
            preparePainting();
            drawIWireObject(g);
            drawNonIWireObject(g);
            preDrawString(1);

            drawStrings(g);
        }
    }

    private void preDrawString(int leftRight) {
        for (int i = 0; i < textBuffer.length; i++) {
            StringBuffer currentObject = textBuffer[i];
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
            currentObject.xx[leftRight] = px + centerPosition.x;
            currentObject.yy[leftRight] = py + centerPosition.y;
        }
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
            drawCircleObject(g, currentObject);
        }
    }

    private void drawCircleObject(Graphics g, NonWire currentObject) {
        CircleObject co = (CircleObject) currentObject.object;
        int size = (int) (co.getDiameter() * currentObject.perse);
        int radious = size / 2;
        g.drawOval(currentObject.xx - radious, currentObject.yy - radious,
                size, size);
    }

    protected class StringBuffer {
        StringObject object;

        int bufferId;// id of buffer array

        int[] xx = new int[2];

        int[] yy = new int[2]; // screen position

        int width = -1, height = -1;// size of this object

        RichString copiedRichString;
    }
}
