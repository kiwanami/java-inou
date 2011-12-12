/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseRendererChanger extends MouseAdapter {

    private DynamicMultiRenderer multiRenderer;

    public MouseRendererChanger(Renderer[] renderers, Camera targetCamera,
            RendererComponent targetComponent) {
        multiRenderer = new DynamicMultiRenderer(renderers);
        multiRenderer.setCamera(targetCamera);
        multiRenderer.setRendererComponent(targetComponent);
    }

    public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) > 0
                || e.isControlDown()) {
            multiRenderer.nextRenderer();
        }
    }

}