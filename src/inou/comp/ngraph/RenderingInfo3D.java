/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.d3.IGeometricObject;
import inou.comp.d3.SceneContext;
import inou.math.RealRange;
import inou.math.vector.Vector3D;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class RenderingInfo3D implements RenderingInfo {

    private Graphics graphics;

    private PolygonPlotRenderer3D renderer;

    private PlotModel3D context;

    private PolygonPlotRenderingParam plotParam;

    private SceneContext sceneContext;

    private RealRange sceneBorder;

    private Rectangle borderArea = new Rectangle();

    private Dimension legendInnerMargin, legendInnerSize;

    private Rectangle legendBorder;

    private int legendSymbolOffset, legendSymbolWidth;

    public RenderingInfo3D(Graphics g, Rectangle wholeArea,
            PolygonPlotRenderer3D renderer, SceneContext sceneContext) {
        this.graphics = g;
        this.sceneContext = sceneContext;
        this.context = renderer.getPlotContext3D();
        this.plotParam = renderer.getRenderingParam();
        this.renderer = renderer;
        calculateParameter(wholeArea);
        calculateSceneParameter();
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public PlotModel getPlotContext() {
        return context;
    }

    public PlotModel3D getPlotContext2D() {
        return context;
    }

    public PolygonPlotRenderer3D getPlotRenderer() {
        return renderer;
    }

    public SceneContext getSceneContext() {
        return sceneContext;
    }

    public void addSceneObject(IGeometricObject polygon) {
        sceneContext.addObject(polygon);
    }

    public void removeSceneObject(IGeometricObject polygon) {
        sceneContext.removeObject(polygon);
    }

    public void addSceneObject(IGeometricObject[] polygons) {
        for (int i = 0; i < polygons.length; i++) {
            sceneContext.addObject(polygons[i]);
        }
    }

    public void real2scene(Vector3D position) {
        position.x = sceneBorder.x() + sceneBorder.width()
                * (context.real2logicalX(position.x));
        position.y = sceneBorder.y() + sceneBorder.height()
                * (context.real2logicalY(position.y));
        position.z = sceneBorder.z() + sceneBorder.length()
                * (context.real2logicalZ(position.z));
    }

    public RealRange getSceneBorder() {
        return sceneBorder;
    }

    public Rectangle getBorderArea() {
        return borderArea;
    }

    public int getBorderRight() {
        return borderArea.x + borderArea.width;
    }

    public int getBorderBottom() {
        return borderArea.y + borderArea.height;
    }

    public double getMainGridSize() {
        return plotParam.sceneSize * plotParam.mainGridTicsRatio;
    }

    public double getSubGridSize() {
        return plotParam.sceneSize * plotParam.subGridTicsRatio;
    }

    public Dimension getLegendInnerMargin() {
        return legendInnerMargin;
    }

    public Rectangle getLegendBorder() {
        return legendBorder;
    }

    public int getLegendBorderRight() {
        return legendBorder.x + legendBorder.width;
    }

    public int getLegendBorderBottom() {
        return legendBorder.y + legendBorder.height;
    }

    public int getLegendSymbolOffset() {
        return legendSymbolOffset;
    }

    public int getLegendSymbolWidth() {
        return legendSymbolWidth;
    }

    // ========================================

    private void calculateSceneParameter() {
        sceneBorder = new RealRange(-plotParam.sceneSize
                * plotParam.sceneSizeRatio.x / 2, -plotParam.sceneSize
                * plotParam.sceneSizeRatio.y / 2, -plotParam.sceneSize
                * plotParam.sceneSizeRatio.x / 2, plotParam.sceneSize
                * plotParam.sceneSizeRatio.x, plotParam.sceneSize
                * plotParam.sceneSizeRatio.y, plotParam.sceneSize
                * plotParam.sceneSizeRatio.z);
    }

    private void calculateParameter(Rectangle wholeArea) {
        borderArea = new Rectangle(wholeArea);
        calculateLegendArea();
    }

    private void calculateLegendArea() {
        calculateLegendBoxInnerSize();
        legendInnerMargin = new Dimension(
                (int) (borderArea.width * plotParam.legendInnerMarginRatio),
                (int) (borderArea.height * plotParam.legendInnerMarginRatio));
        calculateLegendBoxBorder();
    }

    private void calculateLegendBoxBorder() {
        Dimension sideMargin = new Dimension(
                (int) (borderArea.width * plotParam.legendSideMarginRatio),
                (int) (borderArea.height * plotParam.legendSideMarginRatio));
        legendBorder = new Rectangle();
        legendBorder.width = legendInnerSize.width + legendInnerMargin.width
                * 2;
        legendBorder.height = legendInnerSize.height + legendInnerMargin.height
                * 2;
        switch (plotParam.legendVerticalPosition) {
        case SquarePlotRenderingParam.BOTTOM:
            legendBorder.y = borderArea.y + borderArea.height
                    - legendBorder.height - sideMargin.height;
            break;
        case SquarePlotRenderingParam.CENTER:
            legendBorder.y = borderArea.y
                    + (borderArea.height - legendBorder.height) / 2;
            break;
        case SquarePlotRenderingParam.TOP:
        default:
            legendBorder.y = borderArea.y + sideMargin.height;
        }

        switch (plotParam.legendHorizontalPosition) {
        case SquarePlotRenderingParam.LEFT:
            legendBorder.x = borderArea.x + sideMargin.width;
            break;
        case SquarePlotRenderingParam.CENTER:
            legendBorder.x = borderArea.x
                    + (borderArea.width - legendBorder.width) / 2;
            break;
        case SquarePlotRenderingParam.RIGHT:
        default:
            legendBorder.x = borderArea.x + borderArea.width
                    - legendBorder.width - sideMargin.width;
        }
    }

    private void calculateLegendBoxInnerSize() {
        Plotter[] plotters = context.getPlotter();
        int innerHeight = 0;
        int labelWidth = 0;
        int symbolWidth = (int) (borderArea.width * plotParam.legendSymbolSizeRatio);
        graphics.setFont(plotParam.legendFont);
        for (int i = 0; i < plotters.length; i++) {
            if (plotters[i].getData().getDataName() != null) {
                innerHeight += Math.max(plotters[i]
                        .getLegendHeightGen(graphics), getFontHeight(graphics));
                labelWidth = Math.max(getStringWidth(graphics, plotters[i]
                        .getData().getDataName()), labelWidth);
                symbolWidth = Math.max(
                        plotters[i].getLegendHeightGen(graphics), symbolWidth);
            }
        }
        legendSymbolWidth = symbolWidth;
        legendSymbolOffset = symbolWidth
                + (int) (symbolWidth * plotParam.legendSeparatorRatio);
        legendInnerSize = new Dimension(labelWidth
                + (int) (symbolWidth * (1 + plotParam.legendSeparatorRatio)),
                innerHeight);
    }

    public static int getFontHeight(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();
        return height;
    }

    public static int getFontDescent(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getDescent();
        return height;
    }

    public static int getFontAscent(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getAscent();
        return height;
    }

    public static int getStringWidth(Graphics g, String message) {
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(message);
        return width;
    }

}