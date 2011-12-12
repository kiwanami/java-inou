/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.exp;

import inou.math.MathVector;
import inou.math.Parameter;
import inou.math.ScalarFunctionClass;

public class ScalarParameter extends ScalarFunctionClass {

    private Parameter param;

    public ScalarParameter(Parameter param, int dim) {
        super(dim);
        this.param = param;
    }

    public double f(MathVector x) {
        return param.getValue();
    }

}