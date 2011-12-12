/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.DefaultRectPainter;
import inou.comp.RectPainter;
import inou.math.vector.Vector3D;

import java.awt.Color;
import java.awt.Font;

public class PolygonPlotRenderingParam {

    // parameter area

    public Vector3D sceneSizeRatio = new Vector3D(1, 1, 1);

    public Vector3D getSceneSizeRatio() {
        return sceneSizeRatio;
    }

    public double sceneSize = 10.0;

    // real sideMargin = (contentSize)*(sideMarginRatio)
    public double legendSideMarginRatio = 0.01;

    // real innerMargin = (contentSize)*(innerMarginRatio)
    public double legendInnerMarginRatio = 0.01;

    // real symbolSize = (contentSize)*(symbolSizeRatio)
    public double legendSymbolSizeRatio = 0.025;

    // real separatorSize = (symbolSize)*(separatorRatio)
    public double legendSeparatorRatio = 0.6;

    public static final int TOP = -1;

    public static final int CENTER = 0;

    public static final int BOTTOM = 1;

    public static final int LEFT = -1;

    public static final int RIGHT = 1;

    public int legendVerticalPosition = TOP;

    public int legendHorizontalPosition = RIGHT;

    public boolean drawLegend = true;

    public int axisDivisionNumber = 10;

    // real ticsSize = (sceneSize)*(ticsRatio)
    public double mainGridTicsRatio = 0.02;

    public double subGridTicsRatio = 0.01;

    public double gridLabelAwayRatio = 0.02;

    public double axisLabelAwayRatio = 0.05;

    public RectPainter legendBackgroundPainter = new DefaultRectPainter(
            new Color(0xfff8f8));

    public Font legendFont = new Font("Serif", Font.PLAIN, 12);

    public Font labelFont = new Font("Serif", Font.PLAIN, 14);

    public Font gridFont = new Font("Serif", Font.PLAIN, 12);

    public Color outsideBorderColor = null;

    public Color legendBorderColor = Color.black;

    public static final int NONE = 0;

    public static final int BASE_THREE = 1;

    public static final int CENTER_THREE = 2;

    public static final int BOX = 3;

    public static final int XYPLANE = 4;

    public static final int YZPLANE = 5;

    public static final int ZXPLANE = 6;

    public int drawAxisType = BOX;

    public void setDrawAxisType(int a) {
        drawAxisType = a;
    }

    public Color backgroundColor = Color.white;

    public Color axisColor = Color.gray;

    public Color mainGridColor = Color.gray;

    public Color subGridColor = Color.lightGray;

    public Color axisLabelColor = Color.orange.darker();

    public Color legendLabelColor = Color.gray;

    public Color gridLabelColor = Color.blue;

}