/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public class StringGridInfo implements GridInfo {

    private double value;

    private String title;

    public StringGridInfo(double value, String title) {
        this.value = value;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }

}