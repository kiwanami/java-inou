/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * This class is used in Expression object.
 * 
 * @see inou.math.exp.ParameterMaker
 */
public interface Parameter {

    public double getValue();

    public String getName();

}