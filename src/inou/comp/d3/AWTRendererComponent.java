/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.comp.BufferedCanvas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class AWTRendererComponent extends BufferedCanvas implements
        RendererComponent {

    private Rectangle paintRegion;

    private Renderer renderer;

    public AWTRendererComponent(int width, int height) {
        super();
        setSize(width, height);
        paintRegion = new Rectangle(0, 0, width, height);
    }

    public void repaintOrder() {
        repaint();
    }

    public void onResizeCanvas() {
        Dimension d = getSize();
        if (d == null)
            return;
        paintRegion = new Rectangle(0, 0, d.width, d.height);
        if (renderer == null)
            return;
        renderer.updatePaintRegion();
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
        // System.out.println("# change render to :"+renderer);
    }

    public void bpaint(Graphics g) {
        renderer.paint(g);
    }

    public Rectangle getPaintRegion() {
        return paintRegion;
    }

    public Component getComponent() {
        return this;
    }

}
