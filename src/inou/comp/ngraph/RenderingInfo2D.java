/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class RenderingInfo2D implements RenderingInfo {

    private Graphics graphics;

    private SquarePlotRenderer2D renderer;

    private PlotModel2D context;

    private SquarePlotRenderingParam plotParam;

    private Rectangle borderArea = new Rectangle();

    private Rectangle contentArea = new Rectangle();

    private int xMainGridSize, xSubGridSize;

    private int yMainGridSize, ySubGridSize;

    private Dimension legendInnerMargin, legendInnerSize;

    private Rectangle legendBorder;

    private int legendSymbolOffset, legendSymbolWidth;

    public RenderingInfo2D(Graphics g, Rectangle wholeArea,
            SquarePlotRenderer2D renderer) {
        this.graphics = g;
        this.context = renderer.getPlotContext2D();
        this.plotParam = renderer.getRenderingParam();
        this.renderer = renderer;
        calculateParameter(wholeArea);
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public PlotModel getPlotContext() {
        return context;
    }

    public PlotModel2D getPlotContext2D() {
        return context;
    }

    public SquarePlotRenderer2D getPlotRenderer() {
        return renderer;
    }

    public int real2graphicsX(double x) {
        double lx = context.real2logicalX(x);
        return contentArea.x + (int) (contentArea.width * lx);
    }

    public int real2graphicsY(double y) {
        double ly = context.real2logicalY(y);
        return contentArea.y + (int) (contentArea.height * (1.0 - ly));
    }

    public double graphics2realX(double x) {
        double lx = (x - contentArea.x) / contentArea.width;
        return context.logical2realX(lx);
    }

    public double graphics2realY(double y) {
        double ly = 1.0 - (y - contentArea.y) / contentArea.height;
        return context.logical2realY(ly);
    }

    public Rectangle getContentArea() {
        return contentArea;
    }

    public int getContentRight() {
        return contentArea.x + contentArea.width;
    }

    public int getContentBottom() {
        return contentArea.y + contentArea.height;
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

    public int getXMainGridSize() {
        return xMainGridSize;
    }

    public int getYMainGridSize() {
        return yMainGridSize;
    }

    public int getXSubGridSize() {
        return xSubGridSize;
    }

    public int getYSubGridSize() {
        return ySubGridSize;
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

    private void calculateParameter(Rectangle wholeArea) {
        copyRectangle(wholeArea, borderArea);
        calculateContentArea();
        calculateGridSize();
        calculateLegendArea();
    }

    private void calculateContentArea() {
        graphics.setFont(plotParam.labelFont);
        int yAxisLabelHeight = getFontHeight(graphics);
        graphics.setFont(plotParam.gridFont);
        int yAxisWidth = getStringWidth(graphics, plotParam.referenceLabel);
        int yAxisGridHeight = getFontHeight(graphics);

        int x = (int) (borderArea.x + borderArea.width
                * plotParam.leftMarginRatio + yAxisWidth);
        int y = (int) (borderArea.y + borderArea.height
                * plotParam.topMarginRatio + yAxisLabelHeight);
        int width = (int) (borderArea.width
                * (1.0 - plotParam.leftMarginRatio - plotParam.rightMarginRatio) - yAxisWidth);
        int height = (int) (borderArea.height
                * (1.0 - plotParam.topMarginRatio - plotParam.bottomMarginRatio)
                - yAxisLabelHeight * 2 - yAxisGridHeight);

        if (!plotParam.squareRect) {
            contentArea.x = x;
            contentArea.y = y;
            contentArea.width = width;
            contentArea.height = height;
        } else {
            if (width < height) {
                contentArea.x = x;
                contentArea.width = width;
                contentArea.height = width;
                contentArea.y = (height - width) / 2 + y;
            } else {
                contentArea.y = y;
                contentArea.width = height;
                contentArea.height = height;
                contentArea.x = (width - height) / 2 + x;
            }
        }
    }

    private void copyRectangle(Rectangle from, Rectangle to) {
        to.x = from.x;
        to.y = from.y;
        to.width = from.width;
        to.height = from.height;
    }

    private void calculateGridSize() {
        xMainGridSize = (int) (contentArea.height * plotParam.mainGridTicsRatio);
        yMainGridSize = (int) (contentArea.width * plotParam.mainGridTicsRatio);
        xSubGridSize = (int) (contentArea.height * plotParam.subGridTicsRatio);
        ySubGridSize = (int) (contentArea.width * plotParam.subGridTicsRatio);
    }

    private void calculateLegendArea() {
        calculateLegendBoxInnerSize();
        legendInnerMargin = new Dimension(
                (int) (contentArea.width * plotParam.legendInnerMarginRatio),
                (int) (contentArea.height * plotParam.legendInnerMarginRatio));
        calculateLegendBoxBorder();
    }

    private void calculateLegendBoxBorder() {
        Dimension sideMargin = new Dimension(
                (int) (contentArea.width * plotParam.legendSideMarginRatio),
                (int) (contentArea.height * plotParam.legendSideMarginRatio));
        legendBorder = new Rectangle();
        legendBorder.width = legendInnerSize.width + legendInnerMargin.width
                * 2;
        legendBorder.height = legendInnerSize.height + legendInnerMargin.height
                * 2;
        switch (plotParam.legendVerticalPosition) {
        case SquarePlotRenderingParam.BOTTOM:
            legendBorder.y = contentArea.y + contentArea.height
                    - legendBorder.height - sideMargin.height;
            break;
        case SquarePlotRenderingParam.CENTER:
            legendBorder.y = contentArea.y
                    + (contentArea.height - legendBorder.height) / 2;
            break;
        case SquarePlotRenderingParam.TOP:
        default:
            legendBorder.y = contentArea.y + sideMargin.height;
        }

        switch (plotParam.legendHorizontalPosition) {
        case SquarePlotRenderingParam.LEFT:
            legendBorder.x = contentArea.x + sideMargin.width;
            break;
        case SquarePlotRenderingParam.CENTER:
            legendBorder.x = contentArea.x
                    + (contentArea.width - legendBorder.width) / 2;
            break;
        case SquarePlotRenderingParam.RIGHT:
        default:
            legendBorder.x = contentArea.x + contentArea.width
                    - legendBorder.width - sideMargin.width;
        }
    }

    private void calculateLegendBoxInnerSize() {
        Plotter[] plotters = context.getPlotter();
        int innerHeight = 0;
        int labelWidth = 0;
        int symbolWidth = (int) (contentArea.width * plotParam.legendSymbolSizeRatio);
        graphics.setFont(plotParam.legendFont);
        for (int i = 0; i < plotters.length; i++) {
            if (plotters[i].getData().getDataName() != null) {
                innerHeight += Math.max(plotters[i]
                        .getLegendHeightGen(graphics), getFontHeight(graphics));
                labelWidth = Math.max(getStringWidth(graphics, plotters[i]
                        .getData().getDataName()), labelWidth);
                symbolWidth = Math.max(plotters[i].getLegendWidthGen(graphics),
                        symbolWidth);
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
