/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DynamicMultiRenderer implements Renderer, RendererComponent {

    private Renderer[] renderers;

    private Camera camera;

    private RendererComponent rendererComponent;

    private int currentRendererIndex = 0;

    public DynamicMultiRenderer(Renderer r1, Renderer r2) {
        this(new Renderer[] { r1, r2 });
    }

    public DynamicMultiRenderer(Renderer[] rs) {
        renderers = rs;
        for (int i = 0; i < renderers.length; i++) {
            if (renderers[i] != null) {
                renderers[i].setRendererComponent(this);
            }
        }
    }

    public void setRenderer(int index, Renderer r) {
        synchronized (camera) {
            if (renderers[index] != null) {
                renderers[index].setCamera(null);
                renderers[index].setRendererComponent(null);
            }
            renderers[index] = r;
            renderers[index].setRendererComponent(this);
            if (index == currentRendererIndex) {
                renderers[index].setCamera(camera);
                camera.updateMessage();
            }
        }
    }

    public void changeRenderer(int nextIndex) {
        synchronized (camera) {
            if (currentRendererIndex == nextIndex)
                return;
            renderers[currentRendererIndex].setCamera(null);
            renderers[nextIndex].setCamera(camera);
            currentRendererIndex = nextIndex;
            camera.updateMessage();
        }
    }

    public void nextRenderer() {
        int nextIndex = currentRendererIndex + 1;
        if (nextIndex >= renderers.length)
            nextIndex = 0;
        changeRenderer(nextIndex);
    }

    public Renderer getRenderer(int index) {
        return renderers[index];
    }

    public Renderer getCurrentRenderer() {
        return renderers[currentRendererIndex];
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        synchronized (camera) {
            getCurrentRenderer().setCamera(camera);
            if (rendererComponent != null) {
                camera.updateMessage();
            }
        }
    }

    public void paint(Graphics g) {
        getCurrentRenderer().paint(g);
    }

    public void updatePaintRegion() {
        for (int i = 0; i < renderers.length; i++) {
            renderers[i].updatePaintRegion();
        }
    }

    public RendererComponent getRendererComponent() {
        return rendererComponent;
    }

    public void setRendererComponent(RendererComponent rc) {
        rendererComponent = rc;
        for (int i = 0; i < renderers.length; i++) {
            renderers[i].setRendererComponent(this);
        }
        if (rendererComponent != null) {
            rendererComponent.setRenderer(this);
        }
    }

    public void updateObjects() {
        for (int i = 0; i < renderers.length; i++) {
            renderers[i].updateObjects();
        }
    }

    public void updateConfigration() {
        for (int i = 0; i < renderers.length; i++) {
            renderers[i].updateConfigration();
        }
    }

    // ================================

    public void setRenderer(Renderer renderer) {
        // do nothing
    }

    public void repaintOrder() {
        rendererComponent.repaintOrder();
    }

    public Rectangle getPaintRegion() {
        return rendererComponent.getPaintRegion();
    }

    public Component getComponent() {
        return rendererComponent.getComponent();
    }

}