/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.integral.TrapezoidalIntegrator;
import inou.math.vector.Vector1D;
import inou.math.vector.VectorGD;

/** Numerically Partial Derived Function */

public class IntegratedFunction extends ScalarFunctionClass {

    private ScalarFunction function;

    private Integrator integrator = new TrapezoidalIntegrator(0.01);

    // private Integrator integrator = new DEIntegrator(0.1);
    private int colm = 0;

    private MathVector startPosition;

    public IntegratedFunction(ScalarFunction f) {
        this(f, 0);
    }

    public IntegratedFunction(ScalarFunction f, int colm) {
        this(f, null, colm);
    }

    public IntegratedFunction(ScalarFunction f, MathVector startPosition,
            int colm) {
        super(f.getDimension());
        function = f;
        this.colm = colm;
        setStartPosition(startPosition);
    }

    public void setFunction(ScalarFunction f) {
        function = f;
    }

    public ScalarFunction getFunction() {
        return function;
    }

    public ScalarFunction getDerivedFunction(int c) {
        if (c == colm) {
            return function;
        }
        return FunctionUtil.getDerivedFunction(this, c);
    }

    public void setStartPosition(MathVector vec) {
        if (vec != null) {
            startPosition = vec.getCopy();
        } else {
            if (function.getDimension() == 1) {
                startPosition = new Vector1D(0);
            } else {
                startPosition = new VectorGD(function.getDimension());
            }
        }
    }

    public String toString() {
        return "(int_" + colm + ")(" + function.toString() + ")";
    }

    public Integrator getMethod() {
        return integrator;
    }

    public void setMethod(Integrator d) {
        integrator = d;
    }

    public double f(MathVector x) {
        double length = x.v(colm) - startPosition.v(colm);
        return integrator.range(startPosition, length, colm).operate(function);
    }

}