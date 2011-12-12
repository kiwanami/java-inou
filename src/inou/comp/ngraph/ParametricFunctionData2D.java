/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.AFunction;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.VectorGD;

/**
 * Manage a funcion.
 */
public class ParametricFunctionData2D extends PlotData2D implements
        FixedDataModel {

    protected AFunction[] functions;

    protected int div = 400;

    protected double start_t, end_t;

    protected VectorGD[] data;

    protected RealRange dataRange;

    // ==========================
    // contructor
    // ==========================

    /**
     * function data
     * 
     * @param fs
     *            input functions (default parameter range [0:1] is used.)
     */
    public ParametricFunctionData2D(AFunction[] fs) {
        this(fs, 0, 1);
    }

    /**
     * function data
     * 
     * @param fs
     *            input functions
     * @param st
     *            start parameter
     * @param et
     *            end parameter
     */
    public ParametricFunctionData2D(AFunction[] fs, double st, double et) {
        if (fs.length < 2) {
            System.err
                    .println("bad function dimension : ParametricFunctionData ["
                            + fs.length + "]");
        }
        functions = fs;
        setParameter(st, et);
        makeArray();
    }

    // ==========================
    // access method
    // ==========================

    public RealRange getDataRange() {
        return dataRange;
    }

    public void setFunctions(AFunction[] fs) {
        functions = fs;
        makeArray();
    }

    public AFunction[] getFunctions() {
        return functions;
    }

    public void setParameter(double st, double et) {
        start_t = Math.min(st, et);
        end_t = Math.max(st, et);
        makeArray();
    }

    public boolean isFixed() {
        return true;
    }

    public void setDivision(int d) {
        div = d;
    }

    public int getDivision() {
        return div;
    }

    // ==========================
    // operation
    // ==========================

    /* (no check about y axis) */
    public MathVector[] getArray(RealRange r) {
        return data;
    }

    // ==========================
    // private area
    // ==========================

    void makeArray() {
        double dt = (end_t - start_t) / div, t;
        data = new VectorGD[div];
        int dim = functions.length;

        for (int i = 0; i < div; i++) {
            t = start_t + dt * i;
            data[i] = new VectorGD(dim);
            for (int j = 0; j < dim; j++) {
                data[i].v(j, functions[j].f(t));
            }
        }
        dataRange = UPlotData.getDataRange(data);
    }
}