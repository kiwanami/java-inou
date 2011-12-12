/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.ngraph.d2.LinePlotter;
import inou.math.RealRange;
import inou.math.vector.VectorGD;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlotModel {

    protected ArrayList plotters = new ArrayList();
    protected ArrayList objects = new ArrayList();
    protected ArrayList renderers = new ArrayList();

    protected Axis[] axises;

    protected RealRange defaultRangeAs1D = new RealRange(-5, 10);
    protected RealRange defaultLogRangeAs1D = new RealRange(1, 9);

    protected RealRange activeRange;

    protected boolean[] autoScales;

	public static final int AXIS_X = 0;
	public static final int AXIS_Y = 1;
	public static final int AXIS_Z = 2;

    protected double autoScaleMarginRatio = 0.02;

    protected PlotModel() {
        axises = initAxis();
        autoScales = new boolean[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            autoScales[i] = true;
        }
        activeRange = getDefaultRange();
    }

    // ==========================
    // access
    // ==========================

    public double getAutoScaleMarginRatio() {
        return autoScaleMarginRatio;
    }

    public void setAutoScaleMarginRatio(double ratio) {
        autoScaleMarginRatio = ratio;
    }

    public void setDefaultDataRange(double startPosition, double endPosition) {
        defaultRangeAs1D.x(startPosition);
        defaultRangeAs1D.length(Math.abs(endPosition - startPosition));
    }

    public void setDefaultLogDataRange(double startPosition, double endPosition) {
        if (startPosition <= 0 || endPosition <= 0) {
            System.err
                    .println("Negative range set : PlotModel.setDefaultLogDataRange()");
            return;
        }
        defaultLogRangeAs1D.x(startPosition);
        defaultLogRangeAs1D.length(Math.abs(endPosition - startPosition));
    }

    /**
     * subclass can add some task after modification of data, such as adding and
     * removing data.
     */
    protected void modifiedHook() {
    }

    // object
    public void addObject(PlotObject p) {
        objects.add(p);
        onModified();
    }

    public void removeObject(PlotObject p) {
        objects.remove(p);
        onModified();
    }

    public void removeAllObject() {
        objects.clear();
        onModified();
    }

    public PlotObject[] getObjects() {
        PlotObject[] ret = new PlotObject[objects.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (PlotObject) objects.get(i);
        }
        return ret;
    }

    // plotter
    public void addPlotter(Plotter p) {
        plotters.add(p);
        onModified();
    }

    public void removePlotter(Plotter p) {
        plotters.remove(p);
        onModified();
    }

    public void removeAllPlotter() {
        plotters.clear();
        onModified();
    }

    public Plotter[] getPlotter() {
        Plotter[] ret = new Plotter[plotters.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (Plotter) plotters.get(i);
        }
        return ret;
    }

    public int getDimension() {
        return axises.length;
    }

    public Axis getAxis(int dimension) {
        return axises[dimension];
    }

    public void setAxis(int dimension, Axis argAxis) {
        if (dimension >= 0 && dimension < getDimension())
            axises[dimension] = argAxis;
    }

    public void setActiveRange(RealRange range) {
        for (int i = 0; i < getDimension(); i++) {
            activeRange.pos(i, range.pos(i));
            activeRange.size(i, range.size(i));
        }
        onModified();
    }

    public RealRange getActiveRange() {
        return activeRange;
    }

    // renderers
    public void addRenderer(PlotRenderer p) {
        renderers.add(p);
    }

    public void removeRenderer(PlotRenderer p) {
        renderers.remove(p);
    }

    public void removeAllRenderer() {
        renderers.clear();
    }

    public boolean[] getAutoScales() {
        return autoScales;
    }

    public void setAutoScale(int dimension, boolean doAutoScale) {
        autoScales[dimension] = doAutoScale;
    }

    private void onModified() {
        scaleActiveRangeAutomatically();
        modifiedHook();
    }

    // ==========================
    // Auto scaling
    // ==========================

    private void scaleActiveRangeAutomatically() {
        activeRange = getAutoScaledRange(autoScales);
    }

    /**
     * translate a position in real space into logical space (0.0 - 1.0),
     * emplying active range information.
     */
    public double real2logical(int dimension, double x) {
        if (axises[dimension].isLog()) {
            if (!UPlotData.isValidLogValue(x)) {
                return 0;
            }
            return (Math.log(x / activeRange.pos(dimension)))
                    / Math.log((activeRange.end(dimension))
                            / activeRange.pos(dimension));
        }
        return (x - activeRange.pos(dimension)) / activeRange.size(dimension);
    }

    /**
     * translate a position in logical space into real space, emplying active
     * range information.
     */
    public double logical2real(int dimension, double x) {
        if (axises[dimension].isLog()) {
            return Math.exp(x
                    * Math.log((activeRange.end(dimension))
                            / activeRange.pos(dimension)))
                    * activeRange.pos(dimension);
        }
        return x * activeRange.size(dimension) + activeRange.pos(dimension);
    }

    public RealRange getAutoScaledRange(boolean[] appliedAxises) {
        // argument correction
        if (appliedAxises == null) {
            appliedAxises = new boolean[getDimension()];
            for (int i = 0; i < getDimension(); i++) {
                appliedAxises[i] = true;
            }
        }
        if (appliedAxises.length != getDimension()) {
            boolean[] corrects = new boolean[getDimension()];
            for (int i = 0; i < getDimension(); i++) {
                corrects[i] = true;
            }
            for (int i = 0; i < appliedAxises.length; i++) {
                corrects[i] = appliedAxises[i];
            }
            appliedAxises = corrects;
        }

        int dataNum = plotters.size();
        PlotData[] data = new PlotData[dataNum];
        boolean existFixedData = false;
        for (int i = 0; i < plotters.size(); i++) {
            data[i] = ((Plotter) plotters.get(i)).getData();
            if (data[i] instanceof FixedDataModel) {
                existFixedData = true;
            }
        }
        RealRange scaledRange = null;
        if (existFixedData) {
            scaledRange = getScaledRangeForFixedData(data, appliedAxises);
        }
        if (scaledRange == null) {
            scaledRange = getScaledRangeForNonfixedData(data, appliedAxises);
        }
        RealRange cr = getCanonicalRange(scaledRange, appliedAxises);
        return cr;
    }

    private RealRange getScaledRangeForFixedData(PlotData[] data,
            boolean[] appliedAxises) {
        RealRange scaledRange = null;
        for (int i = 0; i < data.length; i++) {
            if (data[i] instanceof FixedDataModel) {
                FixedDataModel fdm = (FixedDataModel) data[i];
                RealRange dataRange = fdm.getDataRange();
                if (dataRange == null)
                    continue;
                if (scaledRange == null) {
                    scaledRange = dataRange;
                } else {
                    scaledRange = scaledRange.getUnion(dataRange);
                }
            }
        }

        if (scaledRange == null) {
            scaledRange = getDefaultRange(appliedAxises);
        }

        // recover active range for non-application axis
        if (activeRange != null) {
            for (int i = 0; i < getDimension(); i++) {
                if (appliedAxises[i] == false) {
                    scaledRange.pos(i, activeRange.pos(i));
                    scaledRange.size(i, activeRange.size(i));
                }
            }
        }

        return scaledRange;
    }

    private RealRange getDefaultRange() {
        return getDefaultRange(null);
    }

    private RealRange getDefaultRange(boolean[] appliedAxises) {
        VectorGD pos = new VectorGD(getDimension());
        VectorGD size = new VectorGD(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            if (appliedAxises != null && appliedAxises[i] == false
                    && activeRange != null) {
                pos.v(i, activeRange.pos(i));
                size.v(i, activeRange.size(i));
            } else if (getAxis(i).isLog()) {
                pos.v(i, defaultLogRangeAs1D.x());
                size.v(i, defaultLogRangeAs1D.length());
            } else {
                pos.v(i, defaultRangeAs1D.x());
                size.v(i, defaultRangeAs1D.length());
            }
        }
        return new RealRange(pos, size);
    }

    private RealRange getScaledRangeForNonfixedData(PlotData[] data,
            boolean[] appliedAxises) {
        RealRange referenceRange = getDefaultRange(appliedAxises);
        RealRange scaledRange = null;
        for (int i = 0; i < data.length; i++) {
            if (data[i] instanceof FunctionDataModel) {
                FunctionDataModel fdm = (FunctionDataModel) data[i];
                RealRange dataRange = fdm.getReferenceRange(referenceRange);
                if (dataRange == null)
                    continue;
                scaledRange = getUnionRangeForNonFixedData(scaledRange,
                        dataRange, fdm.getOutputDimensions(), appliedAxises);
            }
        }
        if (scaledRange == null) {
            scaledRange = getDefaultRange(appliedAxises);
        }
        return scaledRange;
    }

    private RealRange getUnionRangeForNonFixedData(RealRange mainRange,
            RealRange subrange, int[] outputDim, boolean[] appliedAxises) {
        if (mainRange == null) {
            mainRange = getDefaultRange(appliedAxises);
            for (int i = 0; i < getDimension(); i++) {
                if (appliedAxises[i] == false || !doesInclude(outputDim, i))
                    continue;
                mainRange.pos(i, subrange.pos(i));
                mainRange.size(i, subrange.size(i));
            }
        } else {
            for (int i = 0; i < outputDim.length; i++) {
                int dim = outputDim[i];
                if (dim >= appliedAxises.length)
                    continue;// out of dimension
                if (appliedAxises[dim] == false)
                    continue;
                unitePartialRange(mainRange, subrange, dim);
            }
        }
        return mainRange;
    }

    private void unitePartialRange(RealRange mainRange, RealRange subrange,
            int dim) {
        double start = Math.min(mainRange.pos(dim), subrange.pos(dim));
        double end = Math.max(mainRange.end(dim), subrange.end(dim));
        mainRange.pos(dim, start);
        mainRange.size(dim, end - start);
    }

    private boolean doesInclude(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (target == array[i])
                return true;
        }
        return false;
    }

    private RealRange getCanonicalRange(RealRange rawRange,
            boolean[] appliedAxises) {
        RealRange canonicalRange = new RealRange(rawRange);
        for (int i = 0; i < getDimension(); i++) {
            if (appliedAxises[i] == false)
                continue;
            if (getAxis(i).isLog()) {
                if (UPlotData.isValidLogValue(rawRange.pos(i))) {
                    canonicalRange.pos(i, base(rawRange.pos(i)));
                    canonicalRange.size(i, base(rawRange.end(i)) * 10);
                } else if (UPlotData.isValidLogValue(rawRange.end(i))) {
                    double ts = Math.min(rawRange.end(i) / 1000, 0.001);
                    canonicalRange.pos(i, base(ts));
                    canonicalRange.size(i, base(rawRange.end(i)) * 10);
                } else {
                    canonicalRange.pos(i, base(0.001));
                    canonicalRange.size(i, base(0.001) * 10);
                }
            } else {
                double move = rawRange.size(i) * autoScaleMarginRatio / 2;
                canonicalRange.pos(i, rawRange.pos(i) - move);
                canonicalRange.size(i, rawRange.size(i)
                        * (1.0 + autoScaleMarginRatio));
            }
        }
        correctZeroRange(canonicalRange);
        return canonicalRange;
    }

    private void correctZeroRange(RealRange r) {
        for (int i = 0; i < r.getDimension(); i++) {
            if (Math.abs(r.size(i)) < 1e-30) {
                System.out.println("CorrectZero:[" + i + "] "
                        + Math.abs(r.size(i)));
                if (Math.abs(r.pos(i)) < 1e-30) {
                    r.pos(i, -1);
                    r.size(i, 2);
                    System.out.println("Around Zero:");
                } else {
                    double s = Math.abs(r.pos(i)) * 0.1;
                    r.pos(i, r.pos(i) - s);
                    r.size(i, s * 2);
                    System.out.println("Around position:" + r.pos(i) + ":"
                            + r.end(i));
                }
            }
        }
    }

    private static final double e10(double x) {
        return Math.pow(10, x);
    }

    private static final double log10(double x) {
        return Math.log(Math.abs(x)) / log10;
    }

    private static double log10 = Math.log(10);

    /**
     * return the largest 10^n number that is smaller than the input number. if
     * 0 is given, return 0.
     */
    private static final double base(double x) {
        if (x == 0)
            return 0;
        return e10(Math.floor(log10(x)));
    }

    // ==========================
    // frame work
    // ==========================

    abstract protected Axis[] initAxis();

    /**
     * Force all plotters refresh data and paint. If you change PlotData or
     * PlotModel object, call this method. Some plotter objects hold internal
     * data as cache because of calculation performance. This method calls
     * updateRenderer().
     */
    public void updatePlotter() {
        Plotter[] plotters = getPlotter();
        for (int i = 0; i < plotters.length; i++) {
            plotters[i].updateData();
        }
        onModified();
        updateRenderer();
    }

    /**
     * Force all renderer repaint. If you modify PlotData contents and intent to
     * update the graphics, you should call updatePlotter() method.
     */
    public void updateRenderer() {
        if (!renderers.isEmpty()) {
            for (Iterator e = renderers.iterator(); e.hasNext();) {
                PlotRenderer renderer = (PlotRenderer) e.next();
                renderer.update();
            }
        }
    }

    public static void main(String[] args) {
        PlotModel2D context = new PlotModel2D();
        AdditiveData2D d = new AdditiveData2D();
        d.setDataName("append data");
        context.addPlotter(new LinePlotter(d));
        d.add(0, 5);
        d.add(0.1, 5);
        d.add(0.2, 5.1);
        context.updatePlotter();
        SquarePlotRenderer2D renderer = new SquarePlotRenderer2D(context);
        AWTPlotComponent pc = new AWTPlotComponent(600, 500);
        pc.addRenderer(renderer);

        Frame t = new Frame("graph test");
        t.add("Center", pc);
        t.pack();
        t.show();
    }

}
