/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.Graphics;

// paint, updatePaintRegion, updateObject and updateConfigration methods
// should work without camera or rendererComponent
public interface Renderer {

    Camera getCamera();

    void setCamera(Camera camera);

    void paint(Graphics g);

    void updatePaintRegion();

    RendererComponent getRendererComponent();

    void setRendererComponent(RendererComponent rc);

    void updateObjects();

    void updateConfigration();

}