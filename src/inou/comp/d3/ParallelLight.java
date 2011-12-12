/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.Vector3D;

/** implementation of parallel light */
public class ParallelLight implements Light {

    private Vector3D way;

    private float lightStrength;

    /**
     * @param way
     *            light way (need not normalizing)
     * @param lightStrength
     *            light strength (0-1.0)
     */
    public ParallelLight(Vector3D way, float lightStrength) {
        this.way = (Vector3D) way.getUnit().mults(-1);
        if (lightStrength < 0f || lightStrength > 1f) {
            lightStrength = 1.0f;
        }
        this.lightStrength = lightStrength;
    }

    public float getBrightness(Vector3D surfaceVector, Vector3D position) {
        if (surfaceVector == null)
            return 0f;
        return (float) surfaceVector.innerProduct(way) * lightStrength;
    }
}