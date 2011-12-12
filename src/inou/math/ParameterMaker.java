/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * Parameter maker.
 * 
 * @see inou.math.Parameter
 * @see inou.math.exp.AExpression
 * @see inou.math.exp.Expression
 */
public interface ParameterMaker {

    /**
     * called by AExpression or Expression to make a external parameter.
     * 
     * @param name
     *            parameter name
     * @param d
     *            default value
     * @return Parameter object
     */
    public Parameter getParameter(String name, double d);

}