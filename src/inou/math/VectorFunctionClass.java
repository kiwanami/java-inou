/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.exp.Expression;

/**
 * Vector function sub-implementation class. You can get a vector function with
 * implementation of only [MathVector f(MathVector arg)] method. (You need not
 * implement [int getDimension()] and [AFunction getFunction(int index)].
 */

public abstract class VectorFunctionClass implements VectorFunction {

    int dimension = 1;

    int argDimension = 1;

    /**
     * Subclass must give a dimension parameter.
     * 
     * @param dimension
     *            the dimension of the output variable.
     * @param argDimension
     *            the dimension of the argument variable.
     */
    protected VectorFunctionClass(int dimension, int argDimension) {
        if (dimension <= 0) {
            throw new ArithmeticException("Illegal dimension.");
        }
        this.dimension = dimension;

        if (argDimension <= 0) {
            throw new ArithmeticException("Illegal argument dimension.");
        }
        this.argDimension = argDimension;
    }

    /**
     * Subclass must give a dimension parameter.
     * 
     * @param dimension
     *            the dimension of the output and the argument variable.
     * 
     */
    protected VectorFunctionClass(int dimension) {
        this(dimension, dimension);
    }

    /** Vector function */
    public abstract MathVector f(MathVector arg);

    /** Return the partial function. */
    public ScalarFunction getFunction(int index) {
        if (index < 0 || index >= this.dimension) {
            throw new ArithmeticException("Illegal dimension.");
        }
        return new ElementFunction(this, index);
    }

    /** Return the dimension the argument and function value. */
    public final int getDimension() {
        return dimension;
    }

    /** Return the dimension the argument and function value. */
    public final int getArgDimension() {
        return argDimension;
    }

    /**
     * Build a vector function from the given expression. See the detail of the
     * expression form of [Expression] class
     * 
     * @param expressions
     *            expressions of the variable x,y,z
     * @see inou.math.exp.Expression
     */
    public static VectorFunction getFunction(String[] expressions) {
        String var = "x";
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].indexOf("y") != -1) {
                var += " y";
                break;
            }
        }
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].indexOf("z") != -1) {
                var += " z";
                break;
            }
        }
        String[] vars = var.split(",");
        return getFunction(expressions, vars);
    }

    /**
     * Build a vector function from the given expression. See the detail of the
     * expression form of [Expression] class
     * 
     * @param expression
     *            expression of the variable, which are defined in
     *            variableNames.
     * @param variableNames
     *            variable names using in the expression
     * @see inou.math.exp.Expression
     */
    public static VectorFunction getFunction(String[] expressions,
            String[] variableNames) {
        ScalarFunction[] s = new ScalarFunction[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            Expression exp = new Expression(expressions[i], variableNames, null);
            s[i] = exp.getFunction();
        }
        return makeVectorFunction(s);
    }

    /**
     * Build a vector function from the given the scalar functions.
     * 
     * @param functions
     *            scalar functions (this method gets the information of the
     *            argument dimension from s[0] object. So, the given functions
     *            should has the same dimension.)
     */
    public static VectorFunction makeVectorFunction(ScalarFunction[] s) {
        return new SimpleVectorFunction(s);
    }

    /**
     * Make a scalar function from the given the vector function.
     * 
     * @param v
     *            vector function
     * @param i
     *            index
     * @return scalar function
     */
    public static ScalarFunction makeScalarFunction(VectorFunction v, int i) {
        if (i < 0 || i >= v.getDimension()) {
            throw new ArithmeticException("Illegal dimension.");
        }
        if (v instanceof VectorFunctionClass)
            return ((VectorFunctionClass) v).getFunction(i);
        return new ElementFunction(v, i);
    }

}

class ElementFunction extends ScalarFunctionClass {

    VectorFunction function;

    int index;

    ElementFunction(VectorFunction v, int index) {
        super(v.getDimension());
        function = v;
        this.index = index;
    }

    public double f(MathVector arg) {
        return function.f(arg).v(index);
    }
}