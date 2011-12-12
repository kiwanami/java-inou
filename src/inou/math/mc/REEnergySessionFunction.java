/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

public class REEnergySessionFunction implements RESessionFunction {

    private double scale = 1;

    public REEnergySessionFunction() {
        this(1);
    }

    public REEnergySessionFunction(double energyScale) {
        scale = energyScale;
    }

    /**
     * @param low
     *            low temperature session
     * @param high
     *            high temperature session
     * @return If F(low) < F(high), return positive value. F(low) > F(high),
     *         return negative value.
     */
    public double evaluate(RESession low, RESession high) {
        return evaluate((REEnergySession) low, (REEnergySession) high);
    }

    private double evaluate(REEnergySession low, REEnergySession high) {
        if (low.getTemperature() <= Double.MIN_VALUE) {
            if (low.getTemperature() < 0) {
                throw new InternalError("Temperature became less than zero.");
            }
            return (-low.getEnergy() + high.getEnergy()) * 1e60 * scale;
        }
        return (1. / low.getTemperature() - 1. / high.getTemperature())
                * (-low.getEnergy() + high.getEnergy()) * scale;
    }
}