/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/** Interpolation interface */
public interface Interpolater {
    public double get(double x);

    public void update(double[] xx, double[] yy);
}