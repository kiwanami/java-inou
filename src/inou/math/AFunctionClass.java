/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.io.Serializable;

/** 1D scalar function sub-implementation class */
public abstract class AFunctionClass extends AFunction implements
        Differentiatable, Integratable, FiniteRange, Serializable {

    // public double f(double x);

    /** make a derived function by AFunctionUtil.getDerivedFunction */
    public AFunction getDerivedFunction() {
        return getDerivedFunction(this);
    }

    /** make a derived function by FunctionUtil.getDerivedFunction */
    public ScalarFunction getDerivedFunction(int i) {
        return FunctionUtil.getDerivedFunction(this, 0);
    }

    public static AFunction getDerivedFunction(AFunction a) {
        return wrap(FunctionUtil.getDerivedFunction(a, 0));
    }

    /** make a integrated function by AFunctionUtil.getIntegratedFunction */
    public AFunction getIntegratedFunction() {
        return getIntegratedFunction(this);
    }

    /** make a integrated function by FunctionUtil.getIntegratedFunction */
    public ScalarFunction getIntegratedFunction(int i) {
        return FunctionUtil.getIntegratedFunction(this, 0);
    }

    public static AFunction getIntegratedFunction(AFunction a) {
        return wrap(FunctionUtil.getIntegratedFunction(a, 0));
    }

    /** get the defined range [default: null] */
    public RealRange getDefinedRange() {
        return null;
    }

    /** get a simple expression with plain-text */
    public String toString() {
        return "(" + getClass().getName() + ")";
    }

    public static ScalarFunction removeWrapper(ScalarFunction in) {
        if (in instanceof AWrapperFunction) {
            AWrapperFunction wrapper = (AWrapperFunction) in;
            return removeWrapper(wrapper.getFunction());
        }
        return in;
    }

    public static AWrapperFunction wrap(ScalarFunction in) {
        return new AWrapperFunction(in);
    }

    /**
     * Build a function from the given expression. See the detail of the
     * expression form of [AExpression] class
     * 
     * @param expression
     *            expression of the variable, which are defined at variableName.
     * @param variableName
     *            variable name using in the expression
     * @see inou.math.exp.AExpression
     */
    public static AFunction getFunction(String expression, String variableName) {
        return getFunctionWithParameters(expression, variableName, null);
    }

    public static AFunction getFunctionWithParameters(String expression,
            String variableName, ParameterMaker pmaker) {
        return wrap(ScalarFunctionClass.getFunctionWithParameters(expression,
                new String[] { variableName }, pmaker));
    }

    /**
     * Build a function from the given expression. See the detail of the
     * expression form of [AExpression] class
     * 
     * @param expression
     *            expression of the variable x
     * @see inou.math.exp.AExpression
     */
    public static AFunction getFunction(String expression) {
        return getFunctionWithParameters(expression, null);
    }

    public static AFunction getFunctionWithParameters(String expression,
            ParameterMaker pmaker) {
        return getFunctionWithParameters(expression, "x", pmaker);
    }
}