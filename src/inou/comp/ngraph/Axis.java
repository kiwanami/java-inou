/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.RealRange;

public class Axis {

    private String label;

    private boolean logSwitch = false;

    private GridGenerator mainGridGenerator, subgridGenerator;

    public Axis(String label) {
        this.label = label;
        mainGridGenerator = new AutoGridGenerator(true);
        subgridGenerator = new AutoGridGenerator(false);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isLog() {
        return logSwitch;
    }

    public void setLog(boolean b) {
        logSwitch = b;
    }

    public void setMainGridGenerator(GridGenerator gg) {
        this.mainGridGenerator = gg;
    }

    public GridGenerator getMainGridGenerator() {
        return mainGridGenerator;
    }

    public GridInfo[] getMainGrid(int dimension, RealRange activeRange) {
        if (mainGridGenerator == null)
            return null;
        return mainGridGenerator.getGridInfo(activeRange.pos(dimension),
                activeRange.end(dimension), logSwitch);
    }

    public void setSubGridGenerator(GridGenerator gg) {
        this.subgridGenerator = gg;
    }

    public GridGenerator getSubGridGenerator() {
        return subgridGenerator;
    }

    public GridInfo[] getSubGrid(int dimension, RealRange activeRange) {
        if (subgridGenerator == null)
            return null;
        return subgridGenerator.getGridInfo(activeRange.pos(dimension),
                activeRange.end(dimension), logSwitch);
    }
}