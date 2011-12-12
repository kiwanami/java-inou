/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.Vector3D;

/** implementation of point source */
public class PointSource implements Light {

    private Vector3D position;

    private float lightStrength;

    private Vector3D lengthCalc = new Vector3D();

    /**
     * @param position
     *            light position
     * @param lightStrength
     *            light strength (0 - inf)
     */
    public PointSource(Vector3D position, float lightStrength) {
        this.position = position;
        if (lightStrength < 0f) {
            lightStrength = 10.0f;
        }
        this.lightStrength = lightStrength;
    }

    public float getBrightness(Vector3D surfaceVector, Vector3D surfacePosition) {
        if (surfaceVector == null)
            return 0f;
        lengthCalc.substitute(position);
        lengthCalc.subs(surfacePosition);
        float length2 = (float) lengthCalc.getSquare();
        // System.out.println("SV:"+surfaceVector);
        // System.out.println("SP:"+surfacePosition);
        // System.out.println("LV:"+lengthCalc);
        lengthCalc.normalize();
        if (length2 < 1)
            length2 = 1;
        float ret = ((float) surfaceVector.innerProduct(lengthCalc) + 1f)
                * lightStrength / length2;
        // System.out.println("LN:"+length2);
        // System.out.println("BR:"+ret);
        ret = Math.min(1f, Math.max(-1f, ret - 1f));
        return ret;
    }

}