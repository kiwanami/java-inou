/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

public class StrutElement {

    private String title;

    private double[] data;

    private int minIndex, maxIndex;

    public StrutElement(String title, double[] data) {
        this.title = title;
        if (data.length < 2)
            throw new IllegalArgumentException(
                    "Strut data should contain more than two elements. ["
                            + data.length + "]");
        this.data = data;
        boolean sorted1 = true, sorted2 = true;
        for (int i = 0; i < (data.length - 1); i++) {
            if (data[i] > data[i + 1]) {
                sorted1 = false;
                break;
            }
        }
        for (int i = 0; i < (data.length - 1); i++) {
            if (data[i] < data[i + 1]) {
                sorted2 = false;
                break;
            }
        }
        if (!(sorted1 || sorted2)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i]).append(" -> ");
            }
            String list = sb.toString().substring(0, sb.length() - 4);
            throw new IllegalArgumentException("Not sorted elements. [" + list
                    + "]");
        }
        if (sorted1) {
            minIndex = 0;
            maxIndex = data.length - 1;
        } else {
            maxIndex = 0;
            minIndex = data.length - 1;
        }
    }

    public String getTitle() {
        return title;
    }

    public int getRegionNum() {
        return data.length - 1;
    }

    public double[] getElements() {
        return data;
    }

    public double getMinElement() {
        return data[minIndex];
    }

    public double getMaxElement() {
        return data[maxIndex];
    }
}