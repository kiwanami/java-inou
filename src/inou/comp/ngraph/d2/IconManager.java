/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d2;

import inou.comp.ngraph.d2.icon.CrossIcon;
import inou.comp.ngraph.d2.icon.CrossIcon2;
import inou.comp.ngraph.d2.icon.OvalIcon;
import inou.comp.ngraph.d2.icon.SquareIcon;
import inou.comp.ngraph.d2.icon.TriangleIcon;

import java.util.ArrayList;

/** automatically choose the icons */
public class IconManager {

    static ArrayList icons = new ArrayList();
    static {
        icons.add(new CrossIcon());
        icons.add(new SquareIcon());
        icons.add(new OvalIcon());
        icons.add(new TriangleIcon());
        icons.add(new CrossIcon2());
    }

    static int count = 0;

    public static void addIcon(IconPainter p) {
        icons.add(0, p);
    }

    public static IconPainter getPainter() {
        if (count >= icons.size())
            count = 0;
        IconPainter ip = (IconPainter) icons.get(count);
        count++;
        return ip;
    }

    public static void reset() {
        count = 0;
    }
}
