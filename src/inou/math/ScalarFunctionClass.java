/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.exp.Expression;

import java.io.Serializable;

/**
 * Scalar function sub-implementation class. You can get a scalar function with
 * implementation of only [double f(MathVector arg)] method. (You need not
 * implement [int getDimension()].
 */

public abstract class ScalarFunctionClass implements ScalarFunction,
        Differentiatable, Integratable, Serializable {

    private int dimension = 1;

    /**
     * Subclass must give a dimension parameter.
     */
    protected ScalarFunctionClass(int dimension) {
        if (dimension <= 0) {
            throw new ArithmeticException("Illegal dimension.");
        }
        this.dimension = dimension;
    }

    /** Scalar function. */
    public abstract double f(MathVector arg);

    /** Return the dimension. */
    public final int getDimension() {
        return dimension;
    }

    /** make a derived function by FunctionUtil.getDerivedFuntion */
    public ScalarFunction getDerivedFunction(int colm) {
        // return FunctionUtil.getDerivedFunction(this,colm);
        return new DerivedFunction(this, colm);
    }

    /** make a integrated function by FunctionUtil.getIntegratedFunction */
    public ScalarFunction getIntegratedFunction(int colm) {
        // return FunctionUtil.getIntegratedFunction(this,colm);
        return new IntegratedFunction(this, colm);
    }

    /** get a simple expression with plain-text */
    public String toString() {
        return "(" + getClass().getName() + ")";
    }

    // =====================================================

    /**
     * Build a scalar function from the given expression. See the detail of the
     * expression form of [Expression] class
     * 
     * @param expression
     *            expression of the variable x,y,z
     * @see inou.math.exp.Expression
     */
    public static ScalarFunction getFunction(String expression) {
        return getFunctionWithParameters(expression, null);
    }

    public static ScalarFunction getFunctionWithParameters(String expression,
            ParameterMaker pmaker) {
        String var = "x";
        if (expression.indexOf("y") != -1)
            var += " y";
        if (expression.indexOf("z") != -1)
            var += " z";
        String[] vars = var.split(" ");
        return getFunctionWithParameters(expression, vars, pmaker);
    }

    /**
     * Build a scalar function from the given expression. See the detail of the
     * expression form of [Expression] class
     * 
     * @param expression
     *            expression of the variable, which are defined in
     *            variableNames.
     * @param variableNames
     *            variable names using in the expression
     * @see inou.math.exp.Expression
     */
    public static ScalarFunction getFunction(String expression,
            String[] variableNames) {
        return getFunctionWithParameters(expression, variableNames, null);
    }

    public static ScalarFunction getFunctionWithParameters(String expression,
            String[] variableNames, ParameterMaker pmaker) {
        Expression exp = new Expression(expression, variableNames, pmaker);
        return exp.getFunction();
    }
}
