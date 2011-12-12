/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseDynamicRendererChanger extends MouseAdapter {

    private DynamicMultiRenderer multiRenderer;

    public MouseDynamicRendererChanger(Renderer steadyRenderer,
            Renderer movingRenderer, Camera targetCamera,
            RendererComponent targetComponent) {
        multiRenderer = new DynamicMultiRenderer(steadyRenderer, movingRenderer);
        multiRenderer.setCamera(targetCamera);
        multiRenderer.setRendererComponent(targetComponent);
    }

    public Renderer getCustomRenderer() {
        return multiRenderer;
    }

    public void setSteadyRenderer(Renderer r) {
        multiRenderer.setRenderer(0, r);
    }

    public void setMovingRenderer(Renderer r) {
        multiRenderer.setRenderer(1, r);
    }

    public Renderer getSteadyRenderer() {
        return multiRenderer.getRenderer(0);
    }

    public Renderer getMovingRenderer() {
        return multiRenderer.getRenderer(1);
    }

    public void mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) > 0
                || e.isControlDown()) {
            multiRenderer.changeRenderer(1);
        }
    }

    public void mouseReleased(MouseEvent e) {
        multiRenderer.changeRenderer(0);
    }
}