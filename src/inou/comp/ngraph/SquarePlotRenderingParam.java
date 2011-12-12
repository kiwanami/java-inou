/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.DefaultRectPainter;
import inou.comp.RectPainter;

import java.awt.Color;
import java.awt.Font;

public class SquarePlotRenderingParam {

    // parameter area

    // real margin = (wholeSize)*(marginRatio)
    public double topMarginRatio = 0.04;

    public double bottomMarginRatio = 0.04;

    public double leftMarginRatio = 0.04;

    public double rightMarginRatio = 0.04;

    // real sideMargin = (contentSize)*(sideMarginRatio)
    public double legendSideMarginRatio = 0.01;

    // real innerMargin = (contentSize)*(innerMarginRatio)
    public double legendInnerMarginRatio = 0.01;

    // real symbolSize = (contentSize)*(symbolSizeRatio)
    public double legendSymbolSizeRatio = 0.05;

    // real separatorSize = (symbolSize)*(separatorRatio)
    public double legendSeparatorRatio = 0.6;

    public static final int TOP = -1;

    public static final int CENTER = 0;

    public static final int BOTTOM = 1;

    public static final int LEFT = -1;

    public static final int RIGHT = 1;

    public int legendVerticalPosition = TOP;

    public int legendHorizontalPosition = RIGHT;

    public boolean squareRect = false;

    public boolean drawLegend = true;

    public boolean mainXGridWholeLine = false;

    public boolean mainYGridWholeLine = false;

    public boolean subXGridWholeLine = false;

    public boolean subYGridWholeLine = false;

    // real ticsSize = (contentSize)*(ticsRatio)
    public double mainGridTicsRatio = 0.02;

    public double subGridTicsRatio = 0.015;

    public RectPainter wholeBackgroundPainter = new DefaultRectPainter(
            Color.white);

    public RectPainter contentBackgroundPainter = new DefaultRectPainter(
            new Color(0xffffe0));

    public RectPainter legendBackgroundPainter = new DefaultRectPainter(
            new Color(0xfff0f0));

    public Font legendFont = new Font("Serif", Font.PLAIN, 12);

    public Font labelFont = new Font("Serif", Font.PLAIN, 14);

    public Font gridFont = new Font("Serif", Font.PLAIN, 12);

    public Color outsideBorderColor = null;

    public Color contentBorderColor = Color.black;

    public Color legendBorderColor = Color.black;

    public Color mainGridColor = Color.gray;

    public Color subGridColor = Color.lightGray;

    public Color axisLabelColor = Color.orange.darker();

    public Color legendLabelColor = Color.gray;

    public Color gridLabelColor = Color.blue;

    public String referenceLabel = "WWW";

    public SquarePlotRenderingParam() {
    }

    public SquarePlotRenderingParam(double fontScale) {
        legendFont = new Font("Serif", Font.PLAIN, (int) (13 * fontScale));
        labelFont = new Font("Serif", Font.PLAIN, (int) (14 * fontScale));
        gridFont = new Font("Serif", Font.PLAIN, (int) (12 * fontScale));
    }
}
