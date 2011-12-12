/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * a session of replica exchange method. this object will be managed by
 * REManager
 */
public interface RESession {

    public void step();

    public double getTemperature();

    public void setTemperature(double t);

    public void exchange(RESession session);

}