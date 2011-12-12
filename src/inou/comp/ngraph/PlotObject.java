/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;

public interface PlotObject {

    public void drawObject(RenderingInfo info);

    public MathVector getPosition();

    public void setPosition(MathVector vector);

}