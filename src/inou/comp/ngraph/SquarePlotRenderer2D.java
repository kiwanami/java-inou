/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.RectPainter;
import inou.math.RealRange;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Default implementation of 2D PlotRenderer
 */
public class SquarePlotRenderer2D extends AbstractPlotRenderer {

    private SquarePlotRenderingParam plotParam;

    private Rectangle lastWholeArea = new Rectangle();

    public SquarePlotRenderer2D(PlotModel2D context,
            SquarePlotRenderingParam param) {
        super(context);
        if (param == null) {
            param = new SquarePlotRenderingParam();
        }
        this.plotParam = param;
    }

    public SquarePlotRenderer2D(PlotModel2D context) {
        this(context, null);
    }

    public SquarePlotRenderingParam getRenderingParam() {
        return plotParam;
    }

    public PlotModel2D getPlotContext2D() {
        return (PlotModel2D) getPlotContext();
    }

    private void urgePlotting(Plotter[] plotters, RenderingInfo info) {
        for (int i = 0; i < plotters.length; i++) {
            plotters[i].urgeDraw(info);
        }
    }

    private void urgeDrawingObjects(RenderingInfo info) {
        PlotObject[] objects = getPlotContext().getObjects();
        for (int i = 0; i < objects.length; i++) {
            objects[i].drawObject(info);
        }
    }

    private void urgePaintingBackground(Graphics g, RectPainter painter,
            Rectangle area) {
        if (painter != null) {
            painter.paint(g, area);
        }
    }

    public void paint(Graphics g, Rectangle wholeArea) {
        if (!wholeArea.equals(lastWholeArea)) {
            Plotter[] plotters = getPlotContext().getPlotter();
            for (int i = 0; i < plotters.length; i++) {
                plotters[i].updateData();
            }
        }
        lastWholeArea.setBounds(wholeArea);

        RenderingInfo2D renderingInfo = new RenderingInfo2D(g, wholeArea, this);

        g.setClip(renderingInfo.getBorderArea());
        urgePaintingBackground(g, plotParam.wholeBackgroundPainter,
                renderingInfo.getBorderArea());
        if (plotParam.outsideBorderColor != null) {
            g.setColor(plotParam.outsideBorderColor);
            Rectangle border = renderingInfo.getBorderArea();
            g.drawRect(border.x, border.y, border.width, border.height);
        }

        g.setClip(renderingInfo.getContentArea());
        urgePaintingBackground(g, plotParam.contentBackgroundPainter,
                renderingInfo.getContentArea());
        urgePlotting(getPlotContext2D().getBackgroundPlotter(), renderingInfo);

        g.setClip(renderingInfo.getBorderArea());
        drawAxis(renderingInfo);

        g.setClip(renderingInfo.getContentArea());
        urgePlotting(getPlotContext2D().getForegroundPlotter(), renderingInfo);

        urgeDrawingObjects(renderingInfo);

        urgeDrawingLegend(renderingInfo);
    }

    // ============================

    private void drawAxis(RenderingInfo2D info) {
        if (!isValidRange(info))
            return;

        try {
            drawXGrid(info);
            drawYGrid(info);
            drawBorderLine(info);
            drawAxisLabel(info);
        } catch (Exception e) {
            e.printStackTrace();
            message(info.getGraphics(),
                    "Arithmetric Exception (Bad number or operation).");
            message(info.getGraphics(), "(" + e.getClass().getName() + " : "
                    + e.getMessage() + ")");
            message(info.getGraphics(),
                    "Please change some parameters and try again.");
        }
    }

    private boolean isValidRange(RenderingInfo2D info) {
        Graphics g = info.getGraphics();
        RealRange activeRange = info.getPlotContext().getActiveRange();
        if (activeRange.x() <= 0 && info.getPlotContext2D().getAxisX().isLog()) {
            message(g, "Irregular area in x. Contains less than 0 .");
            message(g, "(x,y,w,h:" + activeRange.x() + " " + activeRange.y()
                    + " " + activeRange.width() + " " + activeRange.height());
            return false;
        }
        if (activeRange.y() <= 0 && info.getPlotContext2D().getAxisY().isLog()) {
            message(g, "Irregular data in y. Contains less than 0 .");
            message(g, "(x,y,w,h:" + activeRange.x() + " " + activeRange.y()
                    + " " + activeRange.width() + " " + activeRange.height());
            return false;
        }
        return true;
    }

	public static final boolean isNull(String s) {
		return (s == null || s.length() == 0);
	}

    private void drawAxisLabel(RenderingInfo2D info) {
        Graphics g = info.getGraphics();
        g.setColor(plotParam.axisLabelColor);
        g.setFont(plotParam.labelFont);
        String xlabel = info.getPlotContext2D().getAxisX().getLabel();
        if (!isNull(xlabel)) {
            int x = Math
                    .min(
                            info.getBorderArea().x
                                    + (int) (info.getBorderArea().width * (1.0 - plotParam.rightMarginRatio))
                                    - RenderingInfo2D.getStringWidth(g, xlabel),
                            info.getContentArea().x
                                    + info.getContentArea().width
                                    + RenderingInfo2D.getStringWidth(g, xlabel)
                                    / 2);
            int y = Math
                    .min(
                            info.getBorderArea().y
                                    + (int) (info.getBorderArea().height * (1.0 - plotParam.bottomMarginRatio))
                                    - RenderingInfo2D.getFontDescent(g), info
                                    .getContentArea().y
                                    + info.getContentArea().height
                                    + RenderingInfo2D.getFontHeight(g)
                                    + RenderingInfo2D.getFontAscent(g));
            g.drawString(xlabel, x, y);
        }
        String ylabel = info.getPlotContext2D().getAxisY().getLabel();
        if (!isNull(ylabel)) {
            int x = Math
                    .max(
                            info.getContentArea().x
                                    - (RenderingInfo2D
                                            .getStringWidth(g, ylabel) >> 1),
                            info.getBorderArea().x
                                    + (int) (info.getBorderArea().height * plotParam.leftMarginRatio));
            int y = Math
                    .max(
                            info.getBorderArea().y
                                    + (int) (info.getBorderArea().height * plotParam.topMarginRatio)
                                    + RenderingInfo2D.getFontAscent(g), info
                                    .getContentArea().y
                                    - RenderingInfo2D.getFontHeight(g));
            g.drawString(ylabel, x, y);
        }
    }

    private void drawBorderLine(RenderingInfo2D info) {
        drawVerticalOrgLine(info);
        drawHorizontalOrgLine(info);
        if (plotParam.contentBorderColor != null) {
            info.getGraphics().setColor(plotParam.contentBorderColor);
            info.getGraphics().drawRect(info.getContentArea().x,
                    info.getContentArea().y, info.getContentArea().width,
                    info.getContentArea().height);
        }
    }

    private void drawXGrid(RenderingInfo2D info) {
        RealRange range = info.getPlotContext().getActiveRange();
        GridInfo[] subTics = info.getPlotContext2D().getAxisX().getSubGrid(0,
                info.getPlotContext().getActiveRange());
        if (subTics != null) {
            for (int i = 0; i < subTics.length; i++) {
                if (range.x() > subTics[i].getValue()
                        || range.ex() < subTics[i].getValue()) {
                    continue;
                }
                drawXTic(info, subTics[i], false);
            }
        }
        GridInfo[] mainTics = info.getPlotContext2D().getAxisX().getMainGrid(0,
                info.getPlotContext().getActiveRange());
        if (mainTics != null) {
            for (int i = 0; i < mainTics.length; i++) {
                drawXTic(info, mainTics[i], true);
            }
        }
    }

    private void drawVerticalOrgLine(RenderingInfo2D info) {
        RealRange activeRange = info.getPlotContext().getActiveRange();
        if ((activeRange.x() * activeRange.ex()) <= 0) {
            Graphics g = info.getGraphics();
            g.setColor(plotParam.contentBorderColor);
            int gx = info.real2graphicsX(0);
            Rectangle contentArea = info.getContentArea();
            g.drawLine(gx, contentArea.y, gx, info.getContentBottom());
            g.setColor(plotParam.gridLabelColor);
            g.setFont(plotParam.gridFont);
            g.drawString("0", gx - RenderingInfo2D.getStringWidth(g, "0") / 2,
                    info.getContentBottom() + RenderingInfo2D.getFontAscent(g));
        }
    }

    private void drawYGrid(RenderingInfo2D info) {
        RealRange range = info.getPlotContext().getActiveRange();
        GridInfo[] subTics = info.getPlotContext2D().getAxisY().getSubGrid(1,
                range);
        if (subTics != null) {
            for (int i = 0; i < subTics.length; i++) {
                if (range.y() > subTics[i].getValue()
                        || range.ey() < subTics[i].getValue()) {
                    continue;
                }
                drawYTic(info, subTics[i], false);
            }
        }
        GridInfo[] mainTics = info.getPlotContext2D().getAxisY().getMainGrid(1,
                info.getPlotContext().getActiveRange());
        if (mainTics != null) {
            for (int i = 0; i < mainTics.length; i++) {
                drawYTic(info, mainTics[i], true);
            }
        }
    }

    private void drawHorizontalOrgLine(RenderingInfo2D info) {
        RealRange activeRange = info.getPlotContext().getActiveRange();
        // ** zero
        if ((activeRange.y() * activeRange.ey()) <= 0) {
            Graphics g = info.getGraphics();
            g.setColor(plotParam.contentBorderColor);
            int gy = info.real2graphicsY(0);
            Rectangle contentArea = info.getContentArea();
            g.drawLine(contentArea.x, gy, info.getContentRight(), gy);
            g.setColor(plotParam.gridLabelColor);
            g.setFont(plotParam.gridFont);
            g.drawString("0", contentArea.x
                    - RenderingInfo2D.getStringWidth(g, "0"), gy
                    + RenderingInfo2D.getFontAscent(g)
                    - RenderingInfo2D.getFontHeight(g) / 2);
        }
    }

    private void drawXTic(RenderingInfo2D info, GridInfo tic, boolean mainFlag) {
        Graphics g = info.getGraphics();
        int centerPos = info.real2graphicsX(tic.getValue());
        int bottom = info.getContentBottom();
        if (mainFlag) {
            g.setColor(plotParam.mainGridColor);
            if (plotParam.mainXGridWholeLine) {
                g.drawLine(centerPos, info.getContentArea().y, centerPos,
                        bottom);
            } else {
                g.drawLine(centerPos, bottom - info.getXMainGridSize(),
                        centerPos, bottom);
            }
            g.setColor(plotParam.gridLabelColor);
            g.setFont(plotParam.gridFont);
            g.drawString(tic.getTitle(), centerPos
                    - (RenderingInfo2D.getStringWidth(g, tic.getTitle()) >> 1),
                    bottom + RenderingInfo2D.getFontAscent(g));
        } else {
            g.setColor(plotParam.subGridColor);
            if (plotParam.subXGridWholeLine) {
                g.drawLine(centerPos, info.getContentArea().y, centerPos,
                        bottom);
            } else {
                g.drawLine(centerPos, bottom - info.getXSubGridSize(),
                        centerPos, bottom);
            }
        }
    }

    private void drawYTic(RenderingInfo2D info, GridInfo tic, boolean mainFlag) {
        Graphics g = info.getGraphics();
        int centerPos = info.real2graphicsY(tic.getValue());
        int left = info.getContentArea().x;
        int right = info.getContentArea().x + info.getContentArea().width;
        if (mainFlag) {
            g.setColor(plotParam.mainGridColor);
            if (plotParam.mainYGridWholeLine) {
                g.drawLine(right, centerPos, left, centerPos);
            } else {
                g.drawLine(left + info.getYMainGridSize(), centerPos, left,
                        centerPos);
            }
            g.setColor(plotParam.gridLabelColor);
            g.setFont(plotParam.gridFont);
            g.drawString(tic.getTitle(), left
                    - RenderingInfo2D.getStringWidth(g, tic.getTitle()),
                    centerPos + RenderingInfo2D.getFontAscent(g)
                            - RenderingInfo2D.getFontHeight(g) / 2);
        } else {
            g.setColor(plotParam.subGridColor);
            if (plotParam.subYGridWholeLine) {
                g.drawLine(right, centerPos, left, centerPos);
            } else {
                g.drawLine(left + info.getYSubGridSize(), centerPos, left,
                        centerPos);
            }
        }
    }

    private int mey = 0;

    private void message(Graphics g, String s) {
        g.setColor(plotParam.axisLabelColor);
        g.drawString(s, 10, 30 + mey * g.getFontMetrics().getHeight());
        mey++;
    }

    // ==============================================

    private void urgeDrawingLegend(RenderingInfo2D info) {
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
        int fontAscent = fm.getAscent();
        Rectangle currentRect = new Rectangle();
        currentRect.x = border.x + innerMargin.width;
        currentRect.y = border.y + innerMargin.height;
        currentRect.width = info.getLegendSymbolWidth();
        for (int i = 0; i < plotters.length; i++) {
            if (plotters[i].getData().getDataName() != null) {
                currentRect.height = Math.max(
                        plotters[i].getLegendHeightGen(g), RenderingInfo2D
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
