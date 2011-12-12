/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/** Simple implementation of Acceptor */
public class StepAcceptor implements TemperatureAcceptor {

    private double current = 1e60;

    /**
     * construct with 1e60 as initial value
     */
    public StepAcceptor() {
        this(1e60);
    }

    /** construct with initial value. */
    public StepAcceptor(double init) {
        current = init;
    }

    public double getTemperature() {
        return 0;
    }

    public void setTemperature(double d) {
    }

    /**
     * decide acceptannce
     */
    public boolean accept(double f) {
        if (current > f) {
            current = f;
            return true;
        }
        return false;
    }

    public double getCurrent() {
        return current;
    }

    public void reset() {
        current = 1e60;
    }

}