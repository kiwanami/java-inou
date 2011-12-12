/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Color;

/** Color utility */
public class ColorUtil {

    public static double getBrightness(Color c) {
        return Math.sqrt(getNorm(c));
    }

    public static double getNorm(Color c) {
        return (c.getRed() * c.getRed() + c.getGreen() * c.getGreen() + c
                .getBlue()
                * c.getBlue());
    }

    public static boolean isBright(Color interestColor, Color comparedColor) {
        return getNorm(interestColor) > getNorm(comparedColor);
    }

    public static Color getBrighter(Color c1, Color c2) {
        return (getNorm(c1) > getNorm(c2)) ? c1 : c2;
    }

    public static Color getMixed(Color c1, Color c2) {
        int r1, r2, g1, g2, b1, b2;
        r1 = c1.getRed();
        g1 = c1.getGreen();
        b1 = c1.getBlue();
        r2 = c2.getRed();
        g2 = c2.getGreen();
        b2 = c2.getBlue();
        return new Color((r1 + r2) >> 1, (g1 + g2) >> 1, (b1 + b2) >> 1);
    }

}