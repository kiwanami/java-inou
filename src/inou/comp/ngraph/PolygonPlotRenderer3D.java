/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.RichString;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.ParallelLight;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.d3.Renderer;
import inou.comp.d3.RendererComponent;
import inou.comp.d3.SceneContext;
import inou.comp.d3.StringObject;
import inou.math.RealRange;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Default implementation of 3D PlotRenderer
 */
public class PolygonPlotRenderer3D extends AbstractPlotRenderer implements
        RendererComponent {

    private PolygonPlotRenderingParam plotParam;

    private Renderer polygonRenderer;

    private Rectangle paintRegion = new Rectangle();

    private SceneContext sceneContext = new SceneContext(false);

    public PolygonPlotRenderer3D(PlotModel3D context,
            PolygonPlotRenderingParam param) {
        super(context);
        if (param == null) {
            param = new PolygonPlotRenderingParam();
        }
        this.plotParam = param;
    }

    // ==============================

    public void setRenderer(Renderer renderer) {
        polygonRenderer = renderer;
    }

    public Renderer getRenderer() {
        return polygonRenderer;
    }

    public void setCamera(Camera camera) {
        polygonRenderer.setCamera(camera);
    }

    public Camera getCamera() {
        return polygonRenderer.getCamera();
    }

    public void repaintOrder() {
        update();
    }

    public Rectangle getPaintRegion() {
        return paintRegion;
    }

    public Component getComponent() {
        if (getPlotComponent() == null)
            return null;
        return getPlotComponent().getComponent();
    }

    public SceneContext getSceneContext() {
        return sceneContext;
    }

    // ==============================

    public PolygonPlotRenderer3D(PlotModel3D context) {
        this(context, null);
    }

    public PolygonPlotRenderingParam getRenderingParam() {
        return plotParam;
    }

    public PlotModel3D getPlotContext3D() {
        return (PlotModel3D) getPlotContext();
    }

    public void paint(Graphics g, Rectangle wholeArea) {
        if (!wholeArea.equals(paintRegion)) {
            paintRegion.setBounds(wholeArea);
            polygonRenderer.updatePaintRegion();
        } else {
            paintRegion.setBounds(wholeArea);
        }
        RenderingInfo3D info = new RenderingInfo3D(g, wholeArea, this,
                sceneContext);
        if (polygonRenderer instanceof AbstractRenderer) {
            ((AbstractRenderer) polygonRenderer)
                    .setBackground(plotParam.backgroundColor);
        }
        if (getPlotContext3D().isModified()) {
            makeupScene(info);
            sceneContext.updateObjects();
            getPlotContext3D().clearModified();
        }

        g.setClip(info.getBorderArea());
        polygonRenderer.paint(g);

        urgeDrawingLegend(info);

        if (plotParam.outsideBorderColor != null) {
            g.setColor(plotParam.outsideBorderColor);
            g
                    .drawRect(info.getBorderArea().x, info.getBorderArea().y,
                            info.getBorderArea().width - 1, info
                                    .getBorderArea().height - 1);
        }
    }

    private void makeupScene(RenderingInfo3D info) {
        sceneContext.removeAllObjects();
        sceneContext.removeAllLights();
        makeupAxis(info);
        makeupPlot(info);
        info.getSceneContext().addLight(
                new ParallelLight(new Vector3D(1, 1, 1), 1));
    }

    private void makeupAxis(RenderingInfo3D info) {
        RealRange activeRange = info.getPlotContext().getActiveRange();
        switch (plotParam.drawAxisType) {
        case PolygonPlotRenderingParam.BASE_THREE:
            Vector3D org = new Vector3D(activeRange.x(), activeRange.y(),
                    activeRange.z());
            makeOneAxis(info, 0, org, true);
            makeOneAxis(info, 1, org, true);
            makeOneAxis(info, 2, org, true);
            break;
        case PolygonPlotRenderingParam.CENTER_THREE:
            Vector3D center = new Vector3D(activeRange.center(0), activeRange
                    .center(1), activeRange.center(2));
            makeOneAxis(info, 0, center, true, AXIS_LABEL_ALIGN_MAX);
            makeOneAxis(info, 1, center, true, AXIS_LABEL_ALIGN_MAX);
            makeOneAxis(info, 2, center, true, AXIS_LABEL_ALIGN_MAX);
            break;
        case PolygonPlotRenderingParam.BOX:
            org = new Vector3D(activeRange.x(), activeRange.y(), activeRange
                    .z());
            makeOneAxis(info, 0, org, true);
            makeOneAxis(info, 1, org, true);
            makeOneAxis(info, 2, org, true);
            org = new Vector3D(activeRange.ex(), activeRange.ey(), activeRange
                    .z());
            makeOneAxis(info, 0, org, false);
            makeOneAxis(info, 1, org, false);
            makeOneAxis(info, 2, org, false);
            org = new Vector3D(activeRange.ex(), activeRange.y(), activeRange
                    .ez());
            makeOneAxis(info, 0, org, false);
            makeOneAxis(info, 1, org, false);
            makeOneAxis(info, 2, org, false);
            org = new Vector3D(activeRange.x(), activeRange.ey(), activeRange
                    .ez());
            makeOneAxis(info, 0, org, false);
            makeOneAxis(info, 1, org, false);
            makeOneAxis(info, 2, org, false);
            break;
        case PolygonPlotRenderingParam.XYPLANE:
            org = new Vector3D(activeRange.x(), activeRange.y(), activeRange
                    .z());
            makeOneAxis(info, 0, org, true);
            makeOneAxis(info, 1, org, true);
            makeOneAxis(info, 2, org, true);
            org = new Vector3D(activeRange.ex(), activeRange.ey(), activeRange
                    .z());
            makeOneAxis(info, 0, org, false);
            makeOneAxis(info, 1, org, false);
            break;
        case PolygonPlotRenderingParam.YZPLANE:
            org = new Vector3D(activeRange.x(), activeRange.y(), activeRange
                    .z());
            makeOneAxis(info, 0, org, true);
            makeOneAxis(info, 1, org, true);
            makeOneAxis(info, 2, org, true);
            org = new Vector3D(activeRange.x(), activeRange.ey(), activeRange
                    .ez());
            makeOneAxis(info, 2, org, false);
            makeOneAxis(info, 1, org, false);
            break;
        case PolygonPlotRenderingParam.ZXPLANE:
            org = new Vector3D(activeRange.x(), activeRange.y(), activeRange
                    .z());
            makeOneAxis(info, 0, org, true);
            makeOneAxis(info, 1, org, true);
            makeOneAxis(info, 2, org, true);
            org = new Vector3D(activeRange.ex(), activeRange.y(), activeRange
                    .ez());
            makeOneAxis(info, 2, org, false);
            makeOneAxis(info, 0, org, false);
            break;
        case PolygonPlotRenderingParam.NONE:
        default:
        }
    }

    private static final int AXIS_LABEL_ALIGN_MIN = 0;

    private static final int AXIS_LABEL_ALIGN_CENTER = 1;

    private static final int AXIS_LABEL_ALIGN_MAX = 2;

    private void makeOneAxis(RenderingInfo3D info, int dimenstion,
            Vector3D startPosition, boolean showGridLabel) {
        makeOneAxis(info, dimenstion, startPosition, showGridLabel,
                AXIS_LABEL_ALIGN_CENTER);
    }

    private void makeOneAxis(RenderingInfo3D info, int dimenstion,
            Vector3D startPosition, boolean showGridLabel, int align) {
        Axis axis = info.getPlotContext().getAxis(dimenstion);
        GridInfo[] subTics = axis.getSubGrid(dimenstion, info.getPlotContext()
                .getActiveRange());
        if (subTics != null) {
            for (int i = 0; i < subTics.length; i++) {
                makeOneTics(info, dimenstion, subTics[i], startPosition, false,
                        false);
            }
        }
        GridInfo[] mainTics = axis.getMainGrid(dimenstion, info
                .getPlotContext().getActiveRange());
        if (mainTics != null) {
            for (int i = 0; i < mainTics.length; i++) {
                makeOneTics(info, dimenstion, mainTics[i], startPosition, true,
                        showGridLabel);
            }
        }
        makeAxisLine(info, dimenstion, startPosition);
        if (showGridLabel) {
            makeAxisLabel(info, dimenstion, startPosition, align);
        }
    }

    private void makeAxisLabel(RenderingInfo3D info, int dimension,
            Vector3D position, int align) {
        RealRange range = info.getPlotContext().getActiveRange();
        VectorQD av = new VectorQD(position);
        switch (align) {
        case AXIS_LABEL_ALIGN_MIN:
            av.v(dimension, range.pos(dimension));
            break;
        case AXIS_LABEL_ALIGN_MAX:
            av.v(dimension, range.end(dimension));
            break;
        case AXIS_LABEL_ALIGN_CENTER:
        default:
            av.v(dimension, range.center(dimension));
        }
        info.real2scene(av);
        RealRange borderRange = info.getSceneBorder();
        double ticsRatio = plotParam.axisLabelAwayRatio;
        av.v(nextDimension(dimension), av.v(nextDimension(dimension))
                - borderRange.size(prevDimension(dimension)) * ticsRatio);
        av.v(prevDimension(dimension), av.v(prevDimension(dimension))
                - borderRange.size(prevDimension(dimension)) * ticsRatio);
        info.addSceneObject(new StringObject(av, new RichString(info
                .getPlotContext().getAxis(dimension).getLabel(),
                plotParam.axisLabelColor, null, plotParam.labelFont)));

    }

    private void makeAxisLine(RenderingInfo3D info, int dimension,
            Vector3D position) {
        RealRange range = info.getPlotContext().getActiveRange();
        double alength = range.size(dimension) / plotParam.axisDivisionNumber;
        for (int i = 0; i < plotParam.axisDivisionNumber; i++) {
            VectorQD av = new VectorQD();
            av.v(dimension, range.pos(dimension) + alength * i);
            av
                    .v(nextDimension(dimension), position
                            .v(nextDimension(dimension)));
            av
                    .v(prevDimension(dimension), position
                            .v(prevDimension(dimension)));
            info.real2scene(av);
            VectorQD bv = new VectorQD();
            bv.v(dimension, range.pos(dimension) + alength * i + alength);
            bv
                    .v(nextDimension(dimension), position
                            .v(nextDimension(dimension)));
            bv
                    .v(prevDimension(dimension), position
                            .v(prevDimension(dimension)));
            info.real2scene(bv);
            info.addSceneObject(PrimitiveObjectMaker.thinLine(av, bv,
                    plotParam.axisColor));
        }
    }

    private void makeOneTics(RenderingInfo3D info, int dimension,
            GridInfo tics, Vector3D position, boolean mainTics,
            boolean showGridLabel) {
        RealRange borderRange = info.getSceneBorder();
        double ticsRatio = (mainTics) ? plotParam.mainGridTicsRatio
                : plotParam.subGridTicsRatio;
        Color ticsColor = (mainTics) ? plotParam.mainGridColor
                : plotParam.subGridColor;
        VectorQD org = new VectorQD();
        org.v(dimension, tics.getValue());
        org.v(nextDimension(dimension), position.v(nextDimension(dimension)));
        org.v(prevDimension(dimension), position.v(prevDimension(dimension)));

        VectorQD av = (VectorQD) org.getCopy();
        info.real2scene(av);
        av.v(nextDimension(dimension), av.v(nextDimension(dimension))
                + borderRange.size(nextDimension(dimension)) * ticsRatio);

        VectorQD bv = (VectorQD) org.getCopy();
        info.real2scene(bv);
        bv.v(nextDimension(dimension), bv.v(nextDimension(dimension))
                - borderRange.size(nextDimension(dimension)) * ticsRatio);

        info.addSceneObject(PrimitiveObjectMaker.thinLine(av, bv, ticsColor));

        av = (VectorQD) org.getCopy();
        info.real2scene(av);
        av.v(prevDimension(dimension), av.v(prevDimension(dimension))
                + borderRange.size(prevDimension(dimension)) * ticsRatio);

        bv = (VectorQD) org.getCopy();
        info.real2scene(bv);
        bv.v(prevDimension(dimension), bv.v(prevDimension(dimension))
                - borderRange.size(prevDimension(dimension)) * ticsRatio);

        info.addSceneObject(PrimitiveObjectMaker.thinLine(av, bv, ticsColor));

        if (!mainTics || !showGridLabel)
            return;

        av = (VectorQD) org.getCopy();
        info.real2scene(av);
        av.v(nextDimension(dimension), av.v(nextDimension(dimension))
                - borderRange.size(prevDimension(dimension))
                * plotParam.gridLabelAwayRatio);
        av.v(prevDimension(dimension), av.v(prevDimension(dimension))
                - borderRange.size(prevDimension(dimension))
                * plotParam.gridLabelAwayRatio);
        info.addSceneObject(new StringObject(av, new RichString(
                tics.getTitle(), plotParam.gridLabelColor, null,
                plotParam.gridFont)));
    }

    private int nextDimension(int dim) {
        if (dim == 2)
            return dim = 0;
        return dim + 1;
    }

    private int prevDimension(int dim) {
        if (dim == 0)
            return dim = 2;
        return dim - 1;
    }

    private void makeupPlot(RenderingInfo3D info) {
        Plotter[] plotter = info.getPlotContext().getPlotter();
        for (int i = 0; i < plotter.length; i++) {
            plotter[i].urgeDraw(info);
        }
    }

    // almost the same as SquarePlotRenderer
    // ### should be refactored
    private void urgeDrawingLegend(RenderingInfo3D info) {
        Plotter[] plotters = info.getPlotContext().getPlotter();
        if (plotters.length == 0 || (!plotParam.drawLegend))
            return;

        Graphics g = info.getGraphics();
        Rectangle border = info.getLegendBorder();
        Dimension innerMargin = info.getLegendInnerMargin();

        if (plotParam.legendBackgroundPainter != null) {
            plotParam.legendBackgroundPainter.paint(g, border);
        }
        if (plotParam.legendBorderColor != null) {
            g.setColor(plotParam.legendBorderColor);
            g.drawRect(border.x, border.y, border.width, border.height);
        }

        g.setFont(plotParam.legendFont);
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();
        int fontAscent = fm.getAscent();
        Rectangle currentRect = new Rectangle();
        currentRect.x = border.x + innerMargin.width;
        currentRect.y = border.y + innerMargin.height;
        currentRect.width = info.getLegendSymbolWidth();
        for (int i = 0; i < plotters.length; i++) {
            if (plotters[i].getData().getDataName() != null) {
                currentRect.height = Math.max(
                        plotters[i].getLegendHeightGen(g), RenderingInfo3D
                                .getFontHeight(g));
                plotters[i].urgeDrawLegend(g, currentRect);
                g.setColor(plotParam.legendLabelColor);
                g.drawString(plotters[i].getData().getDataName(), currentRect.x
                        + info.getLegendSymbolOffset(), currentRect.y
                        + fontAscent);
                currentRect.y += currentRect.height;
            }
        }
    }

}