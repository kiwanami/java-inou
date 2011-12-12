/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.ColorUtil;

import java.awt.Color;
import java.util.ArrayList;

/** Automatically provide suitable colors. */

public class ColorManager {

    private static ArrayList colors = new ArrayList();

    static {
        addColor(Color.blue);
        addColor(Color.darkGray);
        addColor(Color.red.darker());
        addColor(Color.green.darker().darker());
        addColor(Color.black);
        addColor(ColorUtil.getMixed(Color.orange.darker(), Color.blue));
        addColor(Color.pink.darker());
        addColor(Color.magenta.darker().darker());
    }

    private static int count = 0;

    /** you can add your color into color bank at the tail. */
    public static void addColor(Color p) {
        colors.add(p);
    }

    /** any one can get a suitable color. */
    public static Color getColor() {
        if (count >= colors.size())
            count = 0;
        Color ip = (Color) colors.get(count);
        count++;
        return ip;
    }

    /** reset color counter */
    public static void reset() {
        count = 0;
    }
}