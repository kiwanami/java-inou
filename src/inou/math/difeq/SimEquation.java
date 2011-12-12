/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.difeq;

import inou.math.ScalarFunction;

/**
 * This class provides simultaneous equation. SimEquation class manages
 * SubEquationHolders.
 */

public class SimEquation {

    SubEquationHolder[] equations;

    VariableSet[] backup;

    int num;

    /**
     * construct with the array of equation holders
     */
    public SimEquation(SubEquationHolder[] es) {
        num = es.length;
        equations = es;
        int dim = es[0].getEquation().getDimension();
        init(dim);
    }

    /** construct with number of equations */
    public SimEquation(int n, int dim) {
        num = n;
        equations = new SubEquationHolder[num];
        for (int i = 0; i < num; i++) {
            equations[i] = new SubEquationHolder();
        }
        init(dim);
    }

    protected void init(int dim) {
        for (int i = 0; i < num; i++) {
            equations[i].setParent(this);
        }
        //
        VariableSet vv = null;
        if (dim == 2)
            vv = new VariableSet(0, 0);
        else
            vv = new VariableSet(0, 0, 0);
        backup = new VariableSet[num];
        for (int i = 0; i < num; i++)
            backup[i] = (VariableSet) vv.getCopy();
    }

    /** set equation */
    public void setMemberEquation(int pos, ScalarFunction scf) {
        equations[pos].setEquation(scf);
    }

    /** called by SubEquation */
    final protected int which(SubEquationHolder s) {
        for (int i = 0; i < num; i++) {
            if (equations[i] == s)
                return i;
        }
        return -1;// error
    }

    /** called by SimDiffSolver */
    final protected int getNumber() {
        return num;
    }

    /**
     * called by SimDiffEqSolver copy from currentValues to backup vector.
     */
    protected void update(VariableSet[] currentValues) {
        for (int i = 0; i < num; i++) {
            backup[i].substitute(currentValues[i]);
        }
    }

    /** called by SubEquationHolder to get member equation */
    final protected ScalarFunction getMemberEquation(int i) {
        return equations[i].getEquation();
    }

    /** called by SubEquationHolder to get meber value */
    final protected VariableSet getMemberVariable(int i) {
        return backup[i];
    }

}