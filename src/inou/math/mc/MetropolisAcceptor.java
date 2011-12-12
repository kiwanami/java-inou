/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * Simple implementation of Acceptor this object decides absolute value F.
 */
public class MetropolisAcceptor implements TemperatureAcceptor {

    private BoltzmannDistribution distributionFunction;

    private double current = 1e60;

    /**
     * construct with 1e60 as initial value
     */
    public MetropolisAcceptor(double temperature) {
        this(temperature, 1e60);
    }

    /** construct with initial value. */
    public MetropolisAcceptor(double temperature, double init) {
        distributionFunction = new BoltzmannDistribution(1. / temperature);
        this.current = init;
    }

    public double getTemperature() {
        return 1. / distributionFunction.getBeta();
    }

    public void setTemperature(double d) {
        distributionFunction.setBeta(1. / d);
    }

    /**
     * decide acceptannce
     */
    public boolean accept(double f) {
        if (current > f || distributionFunction.f(f - current) > Math.random()) {
            current = f;
            return true;
        }
        return false;
    }

    public void setCurrent(double c) {
        current = c;
    }

    public double getCurrent() {
        return current;
    }

    public void reset() {
        current = 1e60;
    }

}