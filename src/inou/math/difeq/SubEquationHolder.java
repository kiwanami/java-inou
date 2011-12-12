/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/** Subequation of simultaneous equation */
public class SubEquationHolder {

    ScalarFunction equation;

    // ==== Constructor

    /**
     * later, you must set equation object.
     */
    public SubEquationHolder() {
        this(null);
    }

    /**
     * @param sf
     *            differential equation object
     */
    public SubEquationHolder(ScalarFunction sf) {
        setEquation(sf);
    }

    // ==== public operation

    /** you can know which this is in the equations. */
    final public int which() {
        return parent.which(this);
    }

    /** you can know how many there are. */
    final public int getNumber() {
        return parent.getNumber();
    }

    /** call equation holder by number. */
    final public ScalarFunction getMemberEquation(int i) {
        return parent.getMemberEquation(i);
    }

    /** get member's VariableSet. */
    final public VariableSet get(int i) {
        return parent.getMemberVariable(i);
    }

    /** set equation */
    final public void setEquation(ScalarFunction sc) {
        if (sc == null)
            return;
        equation = sc;
        if (equation instanceof SubEquation)
            ((SubEquation) equation).setHolder(this);
    }

    /** get equation */
    final public ScalarFunction getEquation() {
        return equation;
    }

    // ==== frame work (private area)

    SimEquation parent;

    /** called by SimEquation to register to equation-union. */
    protected void setParent(SimEquation p) {
        parent = p;
    }

}