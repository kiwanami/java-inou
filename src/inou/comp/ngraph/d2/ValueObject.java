/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.UPlotData;
import inou.math.MathVector;

import java.awt.Graphics;
import java.text.MessageFormat;

public class ValueObject extends TextObject {

    private String format = "({0}, {1})";

    /**
     * set number format in the way of java.text.MessageFormat. (default : ({0},
     * {1}) : show (x,y) form)
     */
    public void setFormat(String format) {
        if (format == null)
            return;
        this.format = format;
    }

    public ValueObject() {
        super(null, null);
    }

    public ValueObject(MathVector pos) {
        super(null, pos);
    }

    public void drawObject2D(RenderingInfo2D info) {
        Graphics g = info.getGraphics();
        MathVector pos = getPosition();
        if (pos == null) {
            setText(null);
            return;
        }
        String[] args = { UPlotData.defaultNumberFormat(pos.v(0)),
                UPlotData.defaultNumberFormat(pos.v(1)) };
        setText(MessageFormat.format(format, args));
        super.drawObject2D(info);
    }
}