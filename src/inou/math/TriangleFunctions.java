/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * This class suports fixed point calculation. Following functions return the
 * value multiplied 1024(2^10) times.
 */
public class TriangleFunctions {

    static int data[] = new int[320];

    static double pi = 3.141592653589;

    public static int retx;

    public static int rety;

    // This method returns an x,y data by retx,rety(long)
    // Check it.

    public void roll2d(int x, int y, int rad) {
        long xx;
        long yy;
        xx = x * cos(rad) - y * sin(rad);
        yy = x * sin(rad) + y * cos(rad);
        xx = xx >> 10;
        yy = yy >> 10;
        retx = (int) xx;
        rety = (int) yy;
    }

    static {
        int a;
        for (int i = 0; i < 320; i++) {
            data[i] = (int) (Math.sin((i & 255) * pi / 128) * 1024);
        }
    }

    public static int sin(int deg) {
        deg = deg & 255;
        return data[deg];
    }

    public static int cos(int deg) {
        deg = (deg & 255) + 64;
        return data[deg];
    }

    public static int sin(int deg, int r) {
        deg = deg & 255;
        return (data[deg] * r >> 10);
    }

    public static int cos(int deg, int r) {
        deg = (deg & 255) + 64;
        return (data[deg] * r >> 10);
    }

}