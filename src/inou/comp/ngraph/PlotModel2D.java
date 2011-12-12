/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public class PlotModel2D extends PlotModel {

    public PlotModel2D() {
    }

    protected Axis[] initAxis() {
        return new Axis[] { new Axis("X"), new Axis("Y") };
    }

    public Plotter2D[] getForegroundPlotter() {
        return getPlotter2DGen(false);
    }

    public Plotter2D[] getBackgroundPlotter() {
        return getPlotter2DGen(true);
    }

    private Plotter2D[] getPlotter2DGen(boolean background) {
        Plotter[] allPlotter = getPlotter();
        int num = 0;
        for (int i = 0; i < allPlotter.length; i++) {
            if (((Plotter2D) allPlotter[i]).isBackground() == background) {
                num++;
            }
        }
        Plotter2D[] ret = new Plotter2D[num];
        int count = 0;
        for (int i = 0; i < allPlotter.length; i++) {
            if (((Plotter2D) allPlotter[i]).isBackground() == background) {
                ret[count++] = (Plotter2D) allPlotter[i];
            }
        }
        return ret;
    }

	public void setAutoScaleX(boolean auto) {
		setAutoScale(AXIS_X,auto);
	}

	public void setAutoScaleY(boolean auto) {
		setAutoScale(AXIS_Y,auto);
	}

    public Axis getAxisX() {
        return axises[AXIS_X];
    }

    public Axis getAxisY() {
        return axises[AXIS_Y];
    }

    public double logical2realX(double x) {
        return logical2real(AXIS_X, x);
    }

    public double logical2realY(double y) {
        return logical2real(AXIS_Y, y);
    }

    public double real2logicalX(double x) {
        if (axises[AXIS_X].isLog()) {
            if (!UPlotData.isValidLogValue(x)) {
                return 0;
            }
            return (Math.log(x / activeRange.x()))
                    / Math.log((activeRange.ex()) / activeRange.x());
        }
        return (x - activeRange.x()) / activeRange.width();
    }

    public double real2logicalY(double y) {
        if (axises[AXIS_Y].isLog()) {
            if (!UPlotData.isValidLogValue(y)) {
                return 0;
            }
            return (Math.log(y / activeRange.y()))
                    / Math.log((activeRange.ey()) / activeRange.y());
        }
        return (y - activeRange.y()) / activeRange.height();
    }
}
