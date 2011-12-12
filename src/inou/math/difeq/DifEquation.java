/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.MathVector;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;

/** Differential equation */
abstract public class DifEquation implements SubEquation {

    /** ScalarFunction's stuff (Need not be care) */
    final public int getDimension() {
        return 2;
    }

    /** ScalarFunction's stuff (Need not be care) */
    final public double f(MathVector v) {
        return df((VariableSet) v);
    }

    /**
     * Define the equation dy/dx = f(x,y)
     * 
     * @param v
     *            variable set (x,y)
     */
    public abstract double df(VariableSet v);

    /**
     * Dynamic equation bulding
     * 
     * @param equation
     *            differential equation (x, y)
     * @return equation object (ScalarFunction)
     */
    public static ScalarFunction getEquation(String equation) {
        String[] vs = { "x", "y" };
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