/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Expressible something */
public interface CalculationOrder {

    /**
     * The calculation strength of this expression. Just retun a level, not a
     * type of expression. You can detect a type with using "instanceof"
     * operator and inou.math classes. <br/> 100: terminal expression, such as
     * constant, variable and parameter. <br/> 110: function <br/> more than 2:
     * binary function <br/> (ADD == SUB, MULTIPLE == DIVIDE)
     */
    public int getLevel();

    public final int TERMINAL = 10;

    public final int FUNCTION = 11;

    public final int ADD = 3;

    public final int SUB = 3;

    public final int MULTIPLE = 4;

    public final int DIVIDE = 4;

    public final int POWER = 5;
}