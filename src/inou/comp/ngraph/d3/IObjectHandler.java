/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.d3.IGeometricObject;
import inou.comp.ngraph.RenderingInfo3D;
import inou.math.MathVector;

public interface IObjectHandler {

    /**
     * Making an object state. After execution of this method, the scene
     * position will be set.
     * 
     * @param info
     *            renderering info
     * @param org
     *            the original instance. org can be null.
     * @param pos
     *            new position
     * @param index
     *            The index number in the position array.
     * @return If null, the object is removed from the scene. If org !=
     *         (returned object), the object is replaced. If org == (returned
     *         object), the scene remains this org object.
     */
    public IGeometricObject updateObject(RenderingInfo3D info,
            IGeometricObject org, MathVector pos, int index);
}