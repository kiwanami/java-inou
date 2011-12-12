/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.text.DecimalFormat;

public class NumberGridInfo implements GridInfo {

    private DecimalFormat customFormat = null;

    private double value;

    public NumberGridInfo(double value) {
        this.value = value;
    }

    public NumberGridInfo(double value, DecimalFormat customFormat) {
        this(value);
        this.customFormat = customFormat;
    }

    public String getTitle() {
        if (customFormat != null) {
            return customFormat.format(value);
        }

        return UPlotData.defaultNumberFormat(value);
    }

    public double getValue() {
        return value;
    }

}