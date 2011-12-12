/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

public interface TemperatureAcceptor extends Acceptor {

    public void setTemperature(double d);

    public double getTemperature();

}