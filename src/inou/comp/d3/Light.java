/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.Vector3D;

/** represent of light object */
public interface Light {

    /**
     * @param surfaceVector
     *            surface normal vector
     * @param position
     *            surface position
     * @return brightness as double value (-1.0 - 1.0)
     */
    public float getBrightness(Vector3D surfaceVector, Vector3D position);

}