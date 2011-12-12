/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

/** camera object */
public interface Camera {

    public void setRenderer(Renderer renderer);

    public void setPosition(VectorQD m);

    public VectorQD getPosition();

    public Vector3D getAngle();

    /**
     * @param m
     *            camera position (x,y,z)
     * @param theta
     *            camera angle around x axis (rad)
     * @param phi
     *            camera angle around y axis (rad)
     * @param psi
     *            camera angle around z axis (rad)
     */
    public void setPositionAndAngle(VectorQD m, double theta, double phi,
            double psi);

    public void setPosition(double x, double y, double z);

    public void translate(double x, double y, double z);

    /**
     * @param angle
     *            angle vector (a,b,c) a: camera angle around x axis (rad) b:
     *            camera angle around y axis (rad) c: camera angle around z axis
     *            (rad)
     */
    public void setAngle(Vector3D angle);

    /**
     * @param a
     *            rotation angle around x axis (rad)
     * @param b
     *            rotation angle around y axis (rad)
     * @param c
     *            rotation angle around z axis (rad)
     */
    public void setAngle(double a, double b, double c);

    /**
     * @param a
     *            rotation angle around x axis (rad)
     * @param b
     *            rotation angle around y axis (rad)
     * @param c
     *            rotation angle around z axis (rad)
     */
    public void rotate(double a, double b, double c);

    /**
     * Matrix : (rot x)(rot y)(rot z)(translate)
     * 
     * @param matrix
     *            return value
     * @param tmp
     *            temporary workarea
     */
    public void getCameraMatrix(MatrixQD matrix, MatrixQD tmp);

    /**
     * send update message to renderer.
     */
    public void updateMessage();

}