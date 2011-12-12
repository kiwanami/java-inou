/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.AFunction;
import inou.math.AWrapperFunction;
import inou.math.FunctionUtil;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector2D;

/** Manage a funcion */
public class FunctionData2D extends PlotData2D implements FunctionDataModel {

    private AFunction function;

    private int div = 400;

    // if reverseAxis = true, the function will be 'x=f(y)'
    private boolean reverseAxis = false;

    // ==========================
    // contructor
    // ==========================

    /** Default constructor. Default function, y=x is set. */
    public FunctionData2D() {
        this(new AWrapperFunction(FunctionUtil.variable()));
    }

    /**
     * function data
     * 
     * @param f
     *            input function
     */
    public FunctionData2D(AFunction f) {
        super();
        setFunction(f);
    }

    // ==========================
    // access method
    // ==========================

    /**
     * @param rev
     *            if true, the function will be 'x=f(y)'
     */
    public void setReverseAxis(boolean rev) {
        reverseAxis = rev;
    }

    public RealRange getReferenceRange(RealRange referenceRange) {
        int functionDim = (reverseAxis) ? 0 : 1;
        RealRange partialRange = UPlotData.getPartialRange(functionDim,
                getArray(referenceRange));
        RealRange ret = new RealRange(referenceRange);
        ret.pos(functionDim, partialRange.x());
        ret.size(functionDim, partialRange.width());
        // System.out.println(ret);
        return ret;
    }

    public int[] getOutputDimensions() {
        if (reverseAxis)
            return new int[] { 0 };
        return new int[] { 1 };
    }

    public void setFunction(AFunction t) {
        function = t;
        if (function instanceof NamedFunction) {
            setDataName(((NamedFunction) function).getFunctionName());
        }
    }

    public AFunction getFunction() {
        return function;
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

    public MathVector[] getArray(RealRange r) {
        if (r == null || r.y() == Double.NaN || r.ey() == Double.NaN) {
            // error?
            return null;
        }

        Vector2D[] rets = new Vector2D[div];
        Exception exception = null;

        if (reverseAxis) {
            double start = r.y();
            double end = r.ey();
            double dy = (end - start) / div, y;

            for (int i = 0; i < div; i++) {
                y = start + dy * i;
                try {
                    rets[i] = new Vector2D(function.f(y), y);
                } catch (RuntimeException e) {
                    exception = e;
                    rets[i] = null;
                }
            }
        } else {
            double start = r.x();
            double end = r.ex();
            double dx = (end - start) / div, x;

            for (int i = 0; i < div; i++) {
                x = start + dx * i;
                try {
                    rets[i] = new Vector2D(x, function.f(x));
                } catch (RuntimeException e) {
                    exception = e;
                    rets[i] = null;
                }
            }
        }
        if (exception != null) {
            System.out.println(exception.getClass().getName() + " : "
                    + exception.getMessage()
                    + " : Maybe invalid range or calculation.");
        }
        return rets;
    }

}