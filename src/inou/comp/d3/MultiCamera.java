/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.util.ArrayList;

/** camera object */
public class MultiCamera implements Camera {

    private ArrayList cameraList = new ArrayList();

    public MultiCamera() {
    }

    public MultiCamera(Camera[] cams) {
        for (int i = 0; i < cams.length; i++) {
            cameraList.add(cams[i]);
        }
    }

    public void addCamera(Camera cam) {
        cameraList.add(cam);
    }

    /**
     * do nothing
     */
    public void setRenderer(Renderer renderer) {
    }

    /**
     * set given position to holding cameras
     */
    public void setPosition(VectorQD m) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).setPosition(m);
        }
    }

    /**
     * do nothing
     */
    public VectorQD getPosition() {
        return null;
    }

    public Vector3D getAngle() {
        return null;
    }

    public void setPositionAndAngle(VectorQD m, double theta, double phi,
            double psi) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i))
                    .setPositionAndAngle(m, theta, phi, psi);
        }
    }

    public void setPosition(double x, double y, double z) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).setPosition(x, y, z);
        }
    }

    public void translate(double x, double y, double z) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).translate(x, y, z);
        }
    }

    public void setAngle(Vector3D angle) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).setAngle(angle);
        }
    }

    public void setAngle(double a, double b, double c) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).setAngle(a, b, c);
        }
    }

    public void rotate(double a, double b, double c) {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).rotate(a, b, c);
        }
    }

    /**
     * do nothing
     * 
     * @param matrix
     *            return value
     * @param tmp
     *            temporary workarea
     */
    public void getCameraMatrix(MatrixQD matrix, MatrixQD tmp) {
    }

    /**
     * send update message to renderer. (Maybe need not update manually)
     */
    public void updateMessage() {
        for (int i = 0; i < cameraList.size(); i++) {
            ((Camera) cameraList.get(i)).updateMessage();
        }
    }

}
