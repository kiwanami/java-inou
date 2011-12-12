/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * this object decides differential value dF.
 */
public class DMetropolisAcceptor implements TemperatureAcceptor {

    private BoltzmannDistribution distributionFunction;

    public DMetropolisAcceptor(double temperature) {
        distributionFunction = new BoltzmannDistribution(1. / temperature);
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
        if (f < 0 || distributionFunction.f(f) > Math.random())
            return true;
        return false;
    }

    public void reset() {
    }

}
