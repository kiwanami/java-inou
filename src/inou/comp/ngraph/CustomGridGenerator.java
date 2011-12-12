/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.util.ArrayList;

public class CustomGridGenerator implements GridGenerator {

    private double start, delta;

    /**
     * This class will generate a grid with the rule: (grid pos[n]) = (start) +
     * (delta)*n.
     * 
     * @param start
     * @param delta
     */
    public CustomGridGenerator(double start, double delta) {
        this.start = start;
        this.delta = delta;
    }

    /**
     * This class will generate a grid with the rule: (grid pos[n]) = (start) +
     * (delta)*n, (delta) = ( (end) - (start) )/ (number-1)
     * 
     * @param start
     * @param end
     * @param number
     */
    public CustomGridGenerator(double start, double end, int number) {
        this.start = start;
        this.delta = (end - start) / (number - 1);
    }

    public final static int GRID_LIMIT_NUMBER = 100;

    public GridInfo[] getGridInfo(double sx, double ex, boolean log) {
        ArrayList infoList = new ArrayList();
        for (int i = 0; i < GRID_LIMIT_NUMBER; i++) {
            double x = start + delta * i;
            if (sx < x && x < ex) {
                infoList.add(new StringGridInfo(x, UPlotData
                        .defaultNumberFormat(x)));
            }
        }
        GridInfo[] infos = new GridInfo[infoList.size()];
        for (int i = 0; i < infos.length; i++) {
            infos[i] = (GridInfo) infoList.get(i);
        }
        return infos;
    }

}
