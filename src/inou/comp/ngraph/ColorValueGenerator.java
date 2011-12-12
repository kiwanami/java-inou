/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public interface ColorValueGenerator {

    public ColorValueInfo[] getColorValueInfo(double startValue, double endValue);

}