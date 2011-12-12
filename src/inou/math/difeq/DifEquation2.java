/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.MathVector;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;

/** Differential equation */
abstract public class DifEquation2 implements SubEquation {

    /** ScalarFunction's stuff (Need not be care) */
    final public int getDimension() {
        return 3;
    }

    /** ScalarFunction's stuff (Need not be care) */
    final public double f(MathVector v) {
        return ddf((VariableSet) v);
    }

    /**
     * Define the equation d^2y/dx^2 = f(x,y,dy)
     * 
     * @param v
     *            variable set (x,y,dy/dx)
     */
    public abstract double ddf(VariableSet v);

    /**
     * Dynamic equation bulding
     * 
     * @param equation
     *            differential equation (x, y, dy)
     * @return equation object (ScalarFunction)
     */
    public static ScalarFunction getEquation(String equation) {
        String[] vs = { "x", "y", "dy" };
        return ScalarFunctionClass.getFunction(equation, vs);
    }

    // #########################

    protected SubEquationHolder holder;

    /** called by SubEquationHolder */
    public void setHolder(SubEquationHolder sh) {
        holder = sh;
    }

    /** get member variable */
    final public VariableSet get(int i) {
        return holder.get(i);
    }

    /** get my index */
    final public int which() {
        return holder.which();
    }
}