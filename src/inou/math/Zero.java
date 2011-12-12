/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

public class Zero extends Constant {

    public Zero(int dim) {
        super(0, dim);
    }

    public String getExpression() {
        return "";
    }
}