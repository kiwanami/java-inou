/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Vector1D;
import inou.math.vector.VectorGD;

import java.io.Serializable;

/** this class is a wrapper for ScalaFunction to use as AFunction */
public class AWrapperFunction extends AFunction implements Serializable,
        FiniteRange {

    protected ScalarFunction func;

    protected MathVector prm;//

    protected MathVector arg;// temp

    protected int cl;

    /**
     * Initialize function as 1D function.
     * 
     * @param in
     *            scalar function
     */
    public AWrapperFunction(ScalarFunction in) {
        func = in;
        setParameter(new Vector1D());
        setColumn(0);
    }

    /**
     * @param in
     *            scalar function
     * @param p
     *            parameter value (this class makes a copy). Argument variable
     *            except variable-column will by used this value.
     * @param c
     *            variable-column
     */
    public AWrapperFunction(ScalarFunction in, MathVector p, int c) {
        func = in;
        setParameter(p);
        setColumn(c);
    }

    public RealRange getDefinedRange() {
        if (func instanceof FiniteRange) {
            RealRange sourceRange = ((FiniteRange) func).getDefinedRange();
            if (sourceRange == null)
                return null;
            return new RealRange(sourceRange.pos(getColumn()), sourceRange
                    .size(getColumn()));
        }
        return null;
    }

    /**
     * @param p
     *            parameter value (this class makes a copy)
     */
    public void setParameter(MathVector m) {
        arg = new VectorGD(m.getDimension());
        prm = m.getCopy();
    }

    /**
     * @return parameter value
     */
    public MathVector getParameter() {
        return prm;
    }

    /**
     * @param m
     *            scalar function
     */
    public void setFunction(ScalarFunction m) {
        func = m;
    }

    public ScalarFunction getFunction() {
        return func;
    }

    /**
     * @param c
     *            variable column
     */
    public void setColumn(int c) {
        cl = c;
    }

    /**
     * @return variable column
     */
    public int getColumn() {
        return cl;
    }

    /**
     * function implementation.
     * 
     */
    public double f(double x) {
        if (arg == null) {
            throw new NullPointerException(
                    "AWrapperFunction : haven't parameter vector.");
        }
        arg.substitute(prm);
        arg.v(cl, x);
        return func.f(arg);
    }

    public String toString() {
        return "Wrap_" + cl + "[ " + func.toString() + " ]";
    }
}