/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.vector.Vector1D;

/**
 * functional class BASE-function( VARIABLE-function ( arg ) )
 */

public class Functional extends ScalarFunctionClass implements
        CalculationOrder, FiniteRange {

    private ScalarFunction in;

    private ScalarFunction base;

    private Vector1D var = new Vector1D();

    private RealRange definedRange;

    public Functional(ScalarFunction base, ScalarFunction in) {
        this(base, in, null);
    }

    public Functional(ScalarFunction base, ScalarFunction in,
            RealRange definedRange) {
        super(in.getDimension());
        this.in = in;
        this.base = base;
        this.definedRange = definedRange;
    }

    public double f(MathVector x) {
        var.x = in.f(x);
        return base.f(var);
    }

    public int getLevel() {
        return FUNCTION;
    }

    /** get function object */
    public ScalarFunction getBaseFunction() {
        return base;
    }

    /** get variable object */
    public ScalarFunction getVariableFunction() {
        return in;
    }

    public ScalarFunction getDerivedFunction(int c) {
        ScalarFunction dbase = FunctionUtil.getDerivedFunction(base, 0);
        ScalarFunction din = FunctionUtil.getDerivedFunction(in, c);
        return FunctionUtil.multiple(din, new Functional(dbase, in));
    }

    public void setDefinedRange(RealRange definedRange) {
        this.definedRange = definedRange;
    }

    public RealRange getDefinedRange() {
        return definedRange;
    }

    public String toString() {
        if (base instanceof PrimitiveFunction) {
            return base.toString() + "(" + in.toString() + ")";
        }
        return base.toString() + "[" + in.toString() + "]";
    }

}