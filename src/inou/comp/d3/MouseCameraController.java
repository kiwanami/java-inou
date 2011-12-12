/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

/** camera controller interface */
public class MouseCameraController {

    public static int XYMode = 0;

    public static int XZMode = 1;

    private Camera camera;

    private double theta = 0, phi = 0;// current anguler

    private int mode = XYMode;// rotation mode

    private double scale = 2;// axis scale

    static double maxp = 2; // max position on real axis

    private double radius = scale * maxp;// camera position along r axis

    // (real translation) = (mouse translation)*(stdp)
    private static double stdp = 1. / 25.;

    // (real rotation) = (mouse translation)*(stdr)
    private static double stdr = 2. * Math.PI / 900.;

    public MouseCameraController(Camera camera) {
        this.camera = camera;
        cameraPosition(0, 0, 0);
    }

    /** get current camera */
    public Camera getCamera() {
        return camera;
    }

    /** set mouse move type XY or XZ */
    public void setMode(int a) {
        mode = a;
    }

    public void setupComponent(Component com) {
        com.addMouseListener(mouseListener);
        com.addMouseMotionListener(mouseMotionListener);
    }

    // =============================

    // pressed position
    private int sx, sy;

    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (e.isShiftDown()) {
                cameraPosition(y - sy, 0, 0);
            } else {
                cameraPosition(0, (x - sx) * stdr, (y - sy) * stdr);
            }
            sx = x;
            sy = y;
        }
    };

    private MouseListener mouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            sx = e.getX();
            sy = e.getY();
        }

        public void mouseClicked(MouseEvent e) {
            if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                if (timer != null) {
                    stopAutoPilot();
                } else {
                    startAutoPilot();
                }
            }
        }
    };

    /**
     * @param radius
     *            absolute value of distance between camera and origine (real
     *            length)
     * @param phi
     *            absolute value of rotation around x axis (rad)
     * @param theta
     *            absolute value of rotation around y(XYMODE) or z(XZMODE) axis
     *            (rad)
     */
    public void setCameraPosition(double radius, double phi, double theta) {
        this.theta = theta;
        this.phi = phi;
        this.radius = radius;
        moveCamera();
    }

    /**
     * @param dr
     *            abstract value of distance between camera and origine (real
     *            length)
     * @param dp
     *            abstract value of rotation around x axis (rad)
     * @param dt
     *            abstract value of rotation around y(XYMODE) or z(XZMODE) axis
     *            (rad)
     */
    protected void cameraPosition(double dr, double dt, double dp) {
        theta = theta - dt;
        phi = phi + dp;
        radius += dr * scale * stdp;
        if (radius < 0)
            radius = 0;
        moveCamera();
    }

    private void moveCamera() {
        cameraPosition.set(0, 0, -radius);
        MatrixQD rot = getRotMatrix();
        rot.qmults(cameraPosition);
        setCameraAngle(camera, phi, theta);
    }

    private MatrixQD rotationMatrix = new MatrixQD(0);

    private MatrixQD tmpMatrix = new MatrixQD(0);

    private VectorQD cameraPosition = new VectorQD();

    protected MatrixQD getRotMatrix() {
        if (mode == XYMode) {
            MatrixQD.getRotMatrix(phi, theta, 0, rotationMatrix, tmpMatrix);
        } else {
            MatrixQD.getRotMatrix(phi, 0, -theta, rotationMatrix, tmpMatrix);
        }
        return rotationMatrix;
    }

    protected void setCameraAngle(Camera c, double p, double t) {
        if (mode == XYMode) {
            c.setPositionAndAngle(cameraPosition, p, t, 0);
        } else {
            c.setPositionAndAngle(cameraPosition, p, 0, -t);
        }
    }

    // =================

    // timer object
    private Timer timer = new Timer();

    // auto pilot timestep
    private double pdt = 0.2;

    // Amplitude modulation size
    private double pr_amp = 0.1;

    private double pphy_amp = 0.02;

    private double ptheta_amp = 0.02;

    private boolean autoTranslationMode = true;

    // step evolution size
    private double pr_w = 0.04;

    private double pphy_w = 0.02;

    private double ptheta_w = 0;

    // current value
    private double range = scale * maxp;// camera position along r axis

    private double pcur_r = 0;

    private double pcur_theta = 0;

    private double pcur_phy = 0;

    public void startAutoPilot() {
        pcur_r = scale * maxp;
        pr_amp = range * 0.2;

        long delta = (long) (1000 * pdt);
        timer.schedule(timerTask, delta, delta);
    }

    public void setAutoPilotTranslation(boolean b) {
        autoTranslationMode = b;
    }

    public void setAutoPilotDt(double dt) {
        pdt = dt;
    }

    /**
     * d > 1 : fast, d < 1 slow.
     */
    public void setAutoPilotSpeed(double d) {
        pphy_w *= d;
        ptheta_w *= d;
    }

    public void stopAutoPilot() {
        timer.cancel();
    }

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            pcur_r += pr_w;
            pcur_phy += pphy_w;
            pcur_theta += ptheta_w;
            double r = pr_amp * Math.cos(pcur_r);
            double t = ptheta_amp * Math.cos(pcur_theta);
            double p = pphy_amp * Math.cos(pcur_phy) * 0.3;
            if (!autoTranslationMode) {
                r = 0;
            }
            cameraPosition(r, t, p);
        }
    };
}
