/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.Component;
import java.awt.Rectangle;

public interface RendererComponent {

    public void setRenderer(Renderer renderer);

    public void repaintOrder();

    public Rectangle getPaintRegion();

    public Component getComponent();
}