/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.util.ArrayList;

public class StringGridGenerator implements GridGenerator {

    private double[] values;

    private String[] titles;

    public StringGridGenerator(double[] values, String[] titles) {
        this.values = values;
        this.titles = titles;
    }

    public GridInfo[] getGridInfo(double start, double end, boolean log) {
        ArrayList infoList = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            if (start < values[i] && values[i] < end) {
                infoList.add(new StringGridInfo(values[i], titles[i]));
            }
        }
        GridInfo[] infos = new GridInfo[infoList.size()];
        for (int i = 0; i < infos.length; i++) {
            infos[i] = (GridInfo) infoList.get(i);
        }
        return infos;
    }

}
