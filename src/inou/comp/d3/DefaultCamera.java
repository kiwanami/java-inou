/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

/** camera object */
public class DefaultCamera implements Camera {

    private VectorQD position;

    private Vector3D angle = new Vector3D(0, 0, 0);

    private Renderer renderer;

    public DefaultCamera() {
        this(0, 0, 0);
    }

    public DefaultCamera(double x, double y, double z) {
        this(x, y, z, 0, 0, 0);
    }

    /**
     * constructor with a position and a angle of looking at. As for the angle,
     * (0,0,0) is to look at the x-y surface along z axis.
     * 
     */
    public DefaultCamera(double x, double y, double z, double th, double ph,
            double ps) {
        position = new VectorQD(x, y, z);
        angle.x = th;
        angle.y = ph;
        angle.z = ps;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPosition(VectorQD m) {
        position = m;
        updateMessage();
    }

    public VectorQD getPosition() {
        return position;
    }

    public void setPositionAndAngle(VectorQD m, double theta, double phi,
            double psi) {
        position = m;
        angle.x = theta;
        angle.y = phi;
        angle.z = psi;
        updateMessage();
    }

    public void setPosition(double x, double y, double z) {
        position.set(x, y, z);
        updateMessage();
    }

    public void translate(double x, double y, double z) {
        position.x += x;
        position.y += y;
        position.z += z;
        updateMessage();
    }

    public Vector3D getAngle() {
        return angle;
    }

    public void setAngle(Vector3D angle) {
        this.angle.substitute(angle);
    }

    public void setAngle(double a, double b, double c) {
        angle.x = a;
        angle.y = b;
        angle.z = c;
        updateMessage();
    }

    public void rotate(double a, double b, double c) {
        angle.x += a;
        angle.y += b;
        angle.z += c;
        updateMessage();
    }

    public void getCameraMatrix(MatrixQD matrix, MatrixQD tmp) {
        MatrixQD.getRotMatrix(angle.x, angle.y, angle.z, tmp, matrix);
        MatrixQD.getTransMatrix(-position.x, -position.y, -position.z, matrix);
        matrix.qmults(tmp);
    }

    public void updateMessage() {
        if (renderer != null) {
            renderer.updateConfigration();
        }
    }
}