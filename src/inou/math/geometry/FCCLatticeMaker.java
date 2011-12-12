/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.geometry;

import inou.comp.SwingUtils;
import inou.comp.d3.AWTRendererComponent;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.MonotoneRenderer;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.MouseDynamicRendererChanger;
import inou.comp.d3.ParallelLight;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.d3.SceneContext;
import inou.comp.d3.WireRenderer;
import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;

/** Make a FCC lattice for 2D. */
public class FCCLatticeMaker implements LatticeMaker {

    private double dx;

    private int num;

    /**
     * make FCC lattice around the origine. When dx = 1 and num = 7, make the
     * lattice as following:
     * 
     * <pre>
     *      O O O O  
     *      O O O O O  
     *      O O O O O O
     *      O O O * O O O
     *      O O O O O O
     *      O O O O O  
     *      O O O O  
     * </pre>
     * 
     * ("*" is the origine.)
     * 
     * @param dx
     *            distance of points
     * @param num
     *            number of the side
     */
    public FCCLatticeMaker(double dx, int num) {
        this.dx = dx;
        this.num = num;
    }

    /**
     * @return lattice points.
     */
    public MathVector[] getLattice() {
        ArrayList points = new ArrayList();
        Vector3D a = new Vector3D(0, 0, dx * Math.sqrt(2));
        Vector3D b = new Vector3D(dx / Math.sqrt(2), 0, dx / Math.sqrt(2));
        Vector3D c = new Vector3D(0, dx / Math.sqrt(2), dx / Math.sqrt(2));
        int halfNum = num / 2;
        for (int i = -halfNum; i <= halfNum; i++) {
            for (int j = Math.max(-halfNum, -halfNum - i); j <= Math.min(
                    halfNum, halfNum - i); j++) {
                int kmax = Math.min(Math.min(
                        Math.min(halfNum - i - j, halfNum), halfNum - j),
                        halfNum - i);
                int kmin = Math.max(Math.max(Math.max(-halfNum - i - j,
                        -halfNum), -halfNum - j), -halfNum - i);
                for (int k = kmin; k <= kmax; k++) {
                    MathVector val = a.mult(j);
                    val.adds(b.mult(i));
                    val.adds(c.mult(k));
                    points.add(val);
                }
            }
        }
        MathVector[] ret = new MathVector[points.size()];
        for (int i = 0; i < points.size(); i++) {
            ret[i] = (MathVector) points.get(i);
        }
        return ret;
    }

    public static void main(String[] args) {
        JFrame f = SwingUtils.getTestFrame("graph test");

        f.getContentPane().add("Center", getDemo());
        f.setSize(600, 600);
        f.show();
    }

    private static Component getDemo() {
        SceneContext context = makeScene();
        AbstractRenderer movingRenderer = new WireRenderer(context);
        AbstractRenderer steadyRenderer = new MonotoneRenderer(context);
        // AbstractRenderer steadyRenderer = new SubjectiveRenderer(context);
        AWTRendererComponent rendererComponent = new AWTRendererComponent(400,
                400);
        Camera camera = new DefaultCamera();
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setupComponent(rendererComponent);
        MouseDynamicRendererChanger changer = new MouseDynamicRendererChanger(
                steadyRenderer, movingRenderer, camera, rendererComponent);
        rendererComponent.addMouseListener(changer);
        return rendererComponent;
    }

    private static SceneContext makeScene() {
        SceneContext sc = new SceneContext();
        sc.addLight(new ParallelLight(new Vector3D(1.2, 1, 1), 1));
        double dx = 2;
        int num = 12;
        MathVector[] poss = new FCCLatticeMaker(dx, num).getLattice();
        for (int i = 0; i < poss.length; i++) {
            // VertexObject obj =
            // PrimitiveObjectMaker.tetrahedron(0.5,Color.red,Color.blue);
            IGeometricObject obj = PrimitiveObjectMaker.sphere(0.5, 8,
                    Color.red, Color.blue);
            obj.setPosition(poss[i]);
            obj.rotate(MatrixQD.getRotMatrix(rndPos(6.28), rndPos(6.28),
                    rndPos(6.28)));
            sc.addObject(obj);
        }

        double half = dx * num / 6;
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(-half, -half, half), 10, Color.black));
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(-half, half, -half), 10, Color.black));
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(half, -half, -half), 10, Color.black));

        half = -dx * num / 6;
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(-half, -half, half), 10, Color.black));
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(-half, half, -half), 10, Color.black));
        sc.addObject(PrimitiveObjectMaker.thinLine(new VectorQD(-half, -half,
                -half), new VectorQD(half, -half, -half), 10, Color.black));

        return sc;
    }

    private static double rndPos(double width) {
        return width * (Math.random() - 0.5);
    }

}
