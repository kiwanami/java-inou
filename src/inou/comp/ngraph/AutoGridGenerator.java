/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AutoGridGenerator implements GridGenerator {

    private boolean mainFlag = true;

    private double eliminateRatio = 0.04;

    public AutoGridGenerator(boolean mainFlag) {
        this.mainFlag = mainFlag;
    }

    public GridInfo[] getGridInfo(double startValue, double endValue,
            boolean log) {
        if (log)
            return getGridInfoLog(startValue, endValue);
        return getGridInfoNormal(startValue, endValue);
    }

    private GridInfo[] getGridInfoNormal(double startValue, double endValue) {
        double width = endValue - startValue;

        double delta = base(width);

        if (!UPlotData.isValidLogValue(delta)
                || (log10(width) < log10(startValue) - 10)) {
            // under overflow
            return new NumberGridInfo[] { new NumberGridInfo(
                    (startValue + endValue) / 2.) };
        }

        double ratio = width / delta;

        if (ratio >= 10 || ratio < 1) {
            throw new InternalError("Can not detect tic metrix.");
        }

        if (ratio < 2.0) {
            delta /= 5.0;
        } else if (ratio < 3.0) {
            delta /= 4.0;
        } else if (ratio < 5.0) {
            delta /= 2.0;
        } else if (ratio > 8.0) {
            delta *= 2.;
        }

        if (!mainFlag)
            delta /= 2;

        double start = (int) (startValue / delta) * delta;
        // if (start < 0) start -= delta;
        int safeCount = 0;
        while (start < startValue) {
            start += delta;
            if (safeCount++ > 10) {
                throw new RuntimeException("Range : " + startValue + " - "
                        + endValue + "  d:" + delta + "  s:" + start);
            }
        }

        ArrayList infoList = new ArrayList();
        // small difference
        DecimalFormat customFormat = null;
        if (Math.abs((startValue - endValue) / startValue) < 1e-2) {
            customFormat = new DecimalFormat("0.######E0");
        }

        // avoid around zero
        if (Math.abs(startValue) / width > eliminateRatio) {
            infoList.add(new NumberGridInfo(startValue, customFormat));
        }
        while (start < endValue) {
            if (canAddValue(startValue, endValue, start)) {
                infoList.add(new NumberGridInfo(start, customFormat));
            }
            start += delta;
        }
        // avoid around zero
        if (Math.abs(endValue) / width > eliminateRatio) {
            infoList.add(new NumberGridInfo(endValue, customFormat));
        }

        NumberGridInfo[] info = new NumberGridInfo[infoList.size()];
        for (int i = 0; i < info.length; i++) {
            info[i] = (NumberGridInfo) infoList.get(i);
        }
        return info;
    }

    /** avoid around zero, edge of activeArea */
    private boolean canAddValue(double startValue, double endValue, double value) {
        double width = endValue - startValue;
        if (Math.abs(startValue - value) / width < eliminateRatio)
            return false;
        if (Math.abs(endValue - value) / width < eliminateRatio)
            return false;
        if (Math.abs(value) / width < eliminateRatio)
            return false;
        return true;
    }

    private GridInfo[] getGridInfoLog(double startValue, double endValue) {
        if (mainFlag) {
            return getGridInfoLogMain(startValue, endValue);
        }
        return getGridInfoLogSub(startValue, endValue);
    }

    private GridInfo[] getGridInfoLogMain(double startValue, double endValue) {
        int base = (int) log10(startValue) - 1;
        int count = 1;
        int delta = 1;
        while (endValue > e10(base + count)) {
            count++;
        }
        while (count > 6) {
            count /= 2;
            delta *= 2;
        }
        GridInfo[] info = new GridInfo[count + 1];
        for (int i = 0; i < info.length; i++) {
            info[i] = new NumberGridInfo(e10(base + i * delta));
        }
        return info;
    }

    private GridInfo[] getGridInfoLogSub(double startValue, double endValue) {
        if ((log10(endValue) - log10(startValue)) < 6) {
            return getGridInfoLogSubNormal(startValue, endValue);
        }
        return getGridInfoLogSubLarge(startValue, endValue);
    }

    private GridInfo[] getGridInfoLogSubLarge(double startValue, double endValue) {
        GridInfo[] gen = getGridInfoLogMain(startValue, endValue);
        GridInfo[] ret = new GridInfo[gen.length - 1];
        for (int i = 0; i < ret.length; i++) {
            double value = e10((log10(gen[i].getValue()) + log10(gen[i + 1]
                    .getValue())) / 2);
            ret[i] = new NumberGridInfo(value);
        }
        return ret;
    }

    private GridInfo[] getGridInfoLogSubNormal(double startValue,
            double endValue) {
        int base = (int) log10(startValue);
        int count = 1;
        double delta = e10(base);
        double current = startValue;

        while (endValue > current) {
            current += delta;
            count++;
            if (current >= e10(base + 1)) {
                base++;
                current = e10(base);
                delta = current;
                // current += delta;
            }
        }
        base = (int) log10(startValue);
        delta = e10(base);
        current = startValue;
        GridInfo[] info = new GridInfo[count + 1];
        for (int i = 0; i < info.length; i++) {
            info[i] = new NumberGridInfo(current);
            current += delta;
            if (current >= e10(base + 1)) {
                base++;
                current = e10(base);
                delta = current;
                // current += delta;
            }
        }
        return info;
    }

    private static double log10 = Math.log(10);

    private static final double log10(double x) {
        return Math.log(Math.abs(x)) / log10;
    }

    private static final double e10(double x) {
        return Math.pow(10, x);
    }

    // return the closest +/-infinit integer
    private static final double round(double x) {
        // return Math.ceil(x);
        if (x >= 0)
            return Math.floor(x);
        return Math.ceil(x);
    }

    // return the largest 10^n number that is smaller than the input number.
    // if 0 is given, return 0.
    private static final double base(double x) {
        if (x == 0)
            return 0;
        return e10(Math.floor(log10(x)));
    }

    private static final int sgn(double x) {
        if (x == 0)
            return 0;
        return (x > 0) ? 1 : -1;
    }

    public static void main(String[] args) {
        GridGenerator gg = new AutoGridGenerator(true);
        /*
         * printGridInfo(gg.getGridInfo(0,10,false));
         * printGridInfo(gg.getGridInfo(-12,14,false));
         * printGridInfo(gg.getGridInfo(0,100000,false));
         * printGridInfo(gg.getGridInfo(1,10,true));
         * printGridInfo(gg.getGridInfo(1e-6,100,true));
         * printGridInfo(gg.getGridInfo(10,1e5,true));
         * System.out.println("-----------------"); gg = new
         * AutoGridGenerator(false); printGridInfo(gg.getGridInfo(0,10,false));
         * printGridInfo(gg.getGridInfo(-12,14,false));
         * printGridInfo(gg.getGridInfo(0,100000,false));
         * printGridInfo(gg.getGridInfo(1,10,true));
         * printGridInfo(gg.getGridInfo(1e-6,100,true));
         * printGridInfo(gg.getGridInfo(10,1e5,true));
         * System.out.println("-----------------"); gg = new
         * AutoGridGenerator(true);
         */
        printGridInfo(gg.getGridInfo(0.953, 1.044, false));
        printGridInfo(gg.getGridInfo(-1.044, -0.953, false));
        printGridInfo(gg.getGridInfo(-5.1, 5.1, false));
        printGridInfo(gg.getGridInfo(85.752, 97.872, false));
    }

    private static void printGridInfo(GridInfo[] info) {
        System.out.print("| ");
        for (int i = 0; i < info.length; i++) {
            System.out.print(info[i].getTitle() + " | ");
        }
        System.out.println();
    }
}
