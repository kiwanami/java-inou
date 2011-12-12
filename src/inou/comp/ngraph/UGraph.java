/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.MouseDynamicRendererChanger;
import inou.comp.d3.SubjectiveRenderer;
import inou.comp.d3.WireRenderer;
import inou.math.RealRange;

import java.util.Arrays;

public class UGraph {

    public static PlotComponent setup3DRenderer(PlotModel3D context) {
        PlotComponent pc = new AWTPlotComponent(500, 400);
        setup3DRenderer(context, pc);
        return pc;
    }

    public static void setup3DRenderer(PlotModel3D context,
            PlotComponent component) {
        setup3DRenderer(context, component, null);
    }

    public static PolygonPlotRenderer3D setup3DRenderer(PlotModel3D context,
            PlotComponent component, RealRange configRatio) {
        return setup3DRenderer(context, null, null, component, configRatio);
    }

    public static PolygonPlotRenderer3D setup3DRenderer(PlotModel3D context,
            PolygonPlotRenderingParam param, AbstractRenderer polygonRenderer,
            PlotComponent component, RealRange configRatio) {
        PolygonPlotRenderer3D plotRenderer = new PolygonPlotRenderer3D(context,
                param);
        component.addRenderer(plotRenderer, configRatio);

        // AbstractRenderer polygonRenderer = new
        // ColorRenderer(plotRenderer.getSceneContext());
        if (polygonRenderer == null) {
            polygonRenderer = new SubjectiveRenderer(plotRenderer
                    .getSceneContext());
        } else {
            polygonRenderer.setSceneContext(plotRenderer.getSceneContext());
        }
        polygonRenderer.setRendererComponent(plotRenderer);
        Camera camera = new DefaultCamera();
        plotRenderer.setCamera(camera);
        if (plotRenderer.getComponent() != null) {
            setupCameraController(plotRenderer);
        }
        return plotRenderer;
    }

    public static void setupCameraController(PolygonPlotRenderer3D plotRenderer) {
        if (plotRenderer.getComponent() == null) {
            throw new NullPointerException(
                    "The component is null. A renderer should have the real component to set up mouse controller.");
        }
        Camera camera = plotRenderer.getCamera();
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setMode(MouseCameraController.XZMode);
        mcCtrl.setCameraPosition(9, 3.14 * 0.5, 3.14 * 0.8);
        mcCtrl.setupComponent(plotRenderer.getComponent());
        plotRenderer.getComponent().addMouseListener(
                new MouseDynamicRendererChanger(plotRenderer.getRenderer(),// normal
                                                                            // renderer
                        new WireRenderer(plotRenderer.getSceneContext()),
                        camera, plotRenderer));
    }

    // default access
    public static ColorValueInfo[] getColorValueInfo(
            ColorValueGenerator colorValueGenerator, int num, double start,
            double width) {
        if (colorValueGenerator == null) {
            colorValueGenerator = new DefaultColorValueGenerator(num);
        }
        ColorValueInfo[] info = colorValueGenerator.getColorValueInfo(start,
                width * (num / (num - 1)) + start);
        Arrays.sort(info);
        return info;
    }

}
