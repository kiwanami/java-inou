/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class SwingRendererComponent extends JComponent implements
        RendererComponent {

    private Rectangle paintRegion;

    private Renderer renderer;

    public SwingRendererComponent(int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
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
    }

    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        if (d == null)
            return;
        if (d.width != paintRegion.width || d.height != paintRegion.height) {
            paintRegion = new Rectangle(0, 0, d.width, d.height);
            if (renderer == null)
                return;
            renderer.updatePaintRegion();
        }
        renderer.paint(g);
    }

    public Rectangle getPaintRegion() {
        return paintRegion;
    }

    public Component getComponent() {
        return this;
    }

}
