/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/** Simultaneous differential equation */

public interface SubEquation extends ScalarFunction {

    /** called by SubEquationHolder */
    public void setHolder(SubEquationHolder sh);

    /** get member variable */
    public VariableSet get(int i);

    /** get my index */
    public int which();
}