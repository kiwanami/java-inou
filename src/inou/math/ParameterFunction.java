/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.exp.Expression;

/** Parameter functon made from AExpression o Expression . */
public class ParameterFunction extends ScalarFunctionClass {

    ScalarFunction function;

    DefaultParameter[] vars;

    public ParameterFunction(Expression exp) {
        super(exp.getParameters().length);
        init(exp.getParameters(), exp.getInput());
    }

    protected void init(Parameter[] ps, String input) {
        String[] args = new String[ps.length];
        for (int i = 0; i < ps.length; i++)
            args[i] = ps[i].getName();
        Expression nexp = new Expression(input, args, null);
        function = nexp.getFunction();
        ps = nexp.getParameters();
        vars = new DefaultParameter[ps.length];
        for (int i = 0; i < ps.length; i++)
            vars[i] = (DefaultParameter) ps[i];
    }

    public double f(MathVector x) {
        return function.f(x);
    }

    public ScalarFunction getFunction() {
        return function;
    }

    public ScalarFunction getDerivedFuntion(int c) {
        return FunctionUtil.getDerivedFunction(function, c);
    }

    public DefaultParameter[] getVariables() {
        return vars;
    }
}