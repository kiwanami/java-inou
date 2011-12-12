/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class AbstractRenderer implements Renderer {

    protected SceneContext sceneContext;

    protected Camera camera;// camera

    protected RendererComponent rendererComponent;

    protected VectorQD[] vertexBuffer = new VectorQD[0]; // calculation
                                                            // buffer

    protected VectorQD[] vertexReference = new VectorQD[0];// reference to
                                                            // original vertices

    private double perspectiveParam = 1.0;

    private double sizeCorrection = 1.0;

    private double sizeRatio = 1.0;

    protected AbstractRenderer(SceneContext sceneContext) {
        setSceneContext(sceneContext);
    }

    protected AbstractRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        setSceneContext(sceneContext);
        setCamera(camera);
        setRendererComponent(rc);
    }

    final public void setPerspectiveStrength(double p) {
        if (p <= 1e-6) {
            p = 1e-6;
        }
        perspectiveParam = 1.0 / p;
        updateSizeCorrection();
    }

    final protected double persepectiveRatio(double z) {
        return sizeRatio * sizeCorrection * perspectiveParam
                / (z + perspectiveParam);
    }

    abstract public void setBackground(Color backgroundColor);

    final public void setSceneContext(SceneContext sceneContext) {
        this.sceneContext = sceneContext;
        if (sceneContext != null) {
            sceneContext.addRenderer(this);
            updateObjects();
        }
    }

    final public void setRendererComponent(RendererComponent rc) {
        rendererComponent = rc;
        if (rendererComponent != null) {
            rendererComponent.setRenderer(this);
            updateSizeCorrection();
        }
    }

    final public RendererComponent getRendererComponent() {
        return rendererComponent;
    }

    public void updatePaintRegion() {
        paintRegion = null;
        preparePainting();
        updateSizeCorrection();
    }

    public void updateConfigration() {
        if (rendererComponent != null
                && rendererComponent.getPaintRegion() != null) {
            rendererComponent.repaintOrder();
        }
    }

    final protected void updateSizeCorrection() {
        if (paintRegion != null) {
            int w = Math.max(paintRegion.width, paintRegion.height);
            setSizeScale(w);
        } else {
            setSizeScale(450);
        }
    }

    final public void setSizeRatio(double f) {
        sizeRatio = f;
    }

    final protected void setSizeScale(int crtSize) {
        sizeCorrection = crtSize / 2.0 / Math.log(perspectiveParam + 1.0);
    }

    private int calculateVertexNumber() {
        int number = 0;
        IGeometricObject[] objects = sceneContext.getObjects();
        if (objects == null)
            return 0;
        for (int i = 0; i < objects.length; i++) {
            number += objects[i].getVertices().length;
        }
        return number;
    }

    /**
     * subclass must call this method at updateObjects() to prepare vertex
     * buffer.
     */
    final protected void prepareVertexBuffer() {
        vertexReference = new VectorQD[calculateVertexNumber()];
        vertexBuffer = new VectorQD[vertexReference.length];
        for (int i = 0; i < vertexBuffer.length; i++) {
            vertexBuffer[i] = new VectorQD();
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        if (camera != null) {
            camera.setRenderer(this);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    private MatrixQD cameraMatrix = new MatrixQD();

    private MatrixQD tmp = new MatrixQD();

    private void calculateCameraMatrix() {
        if (camera == null)
            return;
        camera.getCameraMatrix(cameraMatrix, tmp);
        for (int i = 0; i < vertexBuffer.length; i++) {
            VectorQD vec = vertexBuffer[i];
            vec.substitute(vertexReference[i]);
            vec.qmults(cameraMatrix);
            double perse = persepectiveRatio(vec.z);
            vec.x *= perse;
            vec.y *= perse;
        }
    }

    protected Rectangle paintRegion;

    protected Point centerPosition;

    protected Point endPosition;

    private void prepareRegionProperties() {
        if (rendererComponent == null)
            return;
        paintRegion = rendererComponent.getPaintRegion();
        centerPosition = new Point((paintRegion.width >> 1) + paintRegion.x,
                (paintRegion.height >> 1) + paintRegion.y);
        endPosition = new Point(paintRegion.width + paintRegion.x,
                paintRegion.height + paintRegion.y);
    }

    /**
     * subclass must call thie method at paint(Graphics g)
     */
    protected void preparePainting() {
        if (paintRegion == null) {
            prepareRegionProperties();
            prepareOption();
        }

        calculateCameraMatrix();
    }

    protected void prepareOption() {
        // somthing
    }

    protected static final int TYPE_SURFACE = 0;

    protected static final int TYPE_WIRE = 1;

    protected static final int TYPE_CIRCLE = 2;

    protected static final int TYPE_STRING = 3;

}
