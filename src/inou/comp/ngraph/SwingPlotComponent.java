/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.RealRange;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class SwingPlotComponent extends JComponent implements PlotComponent {

    private List rendererList = new ArrayList();

    private Dimension lastSize = null;

    public SwingPlotComponent() {
        super();
    }

    public SwingPlotComponent(int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
    }

    /**
     * @param renderer
     *            PlotRenderer object
     * @param configRatio
     *            configuration ratio that must be 2D object.
     * 
     * <pre>
     *      configRatio.x : 0.0(left) - 1.0(right)
     *      configRatio.y : 0.0(top) - 1.0(bottom)
     *      configRatio.width : 0.0(zero) - 1.0(full width)
     *      configRatio.height : 0.0(zero) - 1.0(full height)
     * </pre>
     */
    public void addRenderer(PlotRenderer renderer, RealRange configRatio) {
        if (renderer == null)
            return;
        if (configRatio == null) {
            configRatio = new RealRange(0, 0, 1, 1);
        } else if (configRatio.getDimension() != 2) {
            System.err.println("Configration ratio is invalid. ["
                    + configRatio.getDimension() + "]");
            return;
        }
        RendererHolder holder = new RendererHolder();
        holder.renderer = renderer;
        holder.configRatio = configRatio;
        rendererList.add(holder);
        renderer.setParentComponent(this);
    }

    public void addRenderer(PlotRenderer renderer) {
        addRenderer(renderer, null);
    }

    public Component getComponent() {
        return this;
    }

    public void repaintOrder() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        Rectangle borderArea = new Rectangle();
        for (int i = 0; i < rendererList.size(); i++) {
            RendererHolder holder = (RendererHolder) rendererList.get(i);
            RealRange ratio = holder.configRatio;
            borderArea.x = (int) (size.width * ratio.x());
            borderArea.y = (int) (size.height * ratio.y());
            borderArea.width = (int) (size.width * ratio.width());
            borderArea.height = (int) (size.height * ratio.height());
            holder.renderer.paint(g, borderArea);
        }
    }

    class RendererHolder {
        PlotRenderer renderer;

        RealRange configRatio;
    }

}
