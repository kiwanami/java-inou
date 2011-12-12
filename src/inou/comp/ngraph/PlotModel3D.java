/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public class PlotModel3D extends PlotModel {

    private boolean modified = false;

    public PlotModel3D() {
    }

    protected Axis[] initAxis() {
        return new Axis[] { new Axis("X"), new Axis("Y"), new Axis("Z") };
    }

    protected void modifiedHook() {
        modified = true;
    }

    public boolean isModified() {
        return modified;
    }

    public void clearModified() {
        modified = false;
    }

	public void setAutoScaleX(boolean auto) {
		setAutoScale(AXIS_X,auto);
	}
	public void setAutoScaleY(boolean auto) {
		setAutoScale(AXIS_Y,auto);
	}
	public void setAutoScaleZ(boolean auto) {
		setAutoScale(AXIS_Z,auto);
	}

    public Axis getAxisX() {
        return axises[AXIS_X];
    }

    public Axis getAxisY() {
        return axises[AXIS_Y];
    }

    public Axis getAxisZ() {
        return axises[AXIS_Z];
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

    public double real2logicalZ(double z) {
        if (axises[AXIS_Z].isLog()) {
            if (!UPlotData.isValidLogValue(z)) {
                return 0;
            }
            return (Math.log(z / activeRange.z()))
                    / Math.log((activeRange.ez()) / activeRange.z());
        }
        return (z - activeRange.z()) / activeRange.length();
    }

}
