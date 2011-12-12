/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.util.TreeStructure;

/** Primitive function, which has a derived and integrated function. */
public class PrimitiveFunction extends Functional {

    private String name;

    private ScalarFunction derivedFunction, integratedFunction;

    /**
     * Create an instance with a derivedFunction and integratedFunction that
     * calculated numerically and defined within whole range.
     * 
     * @param name
     *            function name
     * @param function
     *            function object
     */
    public PrimitiveFunction(String name, AFunction function) {
        this(name, function, null, null, null);
    }

    public PrimitiveFunction(String name, AFunction function,
            ScalarFunction derivedFunction, ScalarFunction integratedFunction,
            RealRange range) {
        super(function, FunctionUtil.variable());
        setName(name);
        setDerivedFunction(derivedFunction);
        setIntegratedFunction(integratedFunction);
        setDefinedRange(range);
    }

    /** set function name */
    public void setName(String n) {
        name = n;
    }

    /** get function name */
    public String getName() {
        return name;
    }

    /**
     * Get the value of integratedFunction. (AIntegratableFunction stuff)
     * 
     * @return Value of integratedFunction.
     */
    public ScalarFunction getIntegratedFunction() {
        if (integratedFunction == null)
            throw new ArithmeticException("Cannot integrate " + getName());
        return integratedFunction;
    }

    /** redirect getIntegratedFunction() */
    public ScalarFunction getIntegratedFunction(int c) {
        if (c == 0)
            return getIntegratedFunction();
        throw new InternalError("Forbidden method.");
    }

    /**
     * Set the value of integratedFunction.
     * 
     * @param v
     *            Value to assign to integratedFunction.
     */
    public void setIntegratedFunction(ScalarFunction v) {
        this.integratedFunction = v;
    }

    /**
     * Get the value of derivedFunction. (ADifferentiatableFunction stuff)
     * 
     * @return Value of derivedFunction.
     */
    public ScalarFunction getDerivedFunction() {
        if (derivedFunction == null)
            throw new ArithmeticException("Cannot derive " + getName());
        return derivedFunction;
    }

    /** redirect getDerivedFunction() */
    public ScalarFunction getDerivedFunction(int c) {
        if (c == 0)
            return getDerivedFunction();
        throw new InternalError("Forbidden method.");
    }

    public String toString() {
        return name;
    }

    /**
     * Set the value of derivedFunction.
     * 
     * @param v
     *            Value to assign to derivedFunction.
     */
    public void setDerivedFunction(ScalarFunction v) {
        this.derivedFunction = v;
    }

    public String getTreeNodeExpression() {
        return name + "(x)";
    }

    public TreeStructure[] getTreeNodes() {
        return null;
    }

}