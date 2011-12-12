/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

public class DefaultParameter implements Parameter {

    private double d = 0;

    private String name;

    public DefaultParameter(String name) {
        this(name, 0);
    }

    public DefaultParameter(String name, double d) {
        this.name = name;
        this.d = d;
    }

    public void setValue(double d) {
        this.d = d;
    }

    public double getValue() {
        return d;
    }

    public String getName() {
        return name;
    }
}