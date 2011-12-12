/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

public class DefaultParameterMaker implements ParameterMaker {

    public Parameter getParameter(String name, double d) {
        return new DefaultParameter(name, d);
    }

}