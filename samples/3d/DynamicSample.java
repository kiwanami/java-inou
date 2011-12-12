/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

import inou.comp.SwingUtils;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.MonotoneRenderer;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.ObjectGroup;
import inou.comp.d3.ParallelLight;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.d3.SceneContext;
import inou.comp.d3.SwingRendererComponent;
import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;

public class DynamicSample {

    public static void main(String[] args) {
        JFrame f = SwingUtils.getTestFrame("graph test");
        f.getContentPane().add("Center", getDemo());
        f.setSize(600, 600);
        f.show();
        Thread thread = new Thread(runner);
        thread.start();
    }

    private static Component getDemo() {
        Camera camera = new DefaultCamera();
        SwingRendererComponent rendererComponent = new SwingRendererComponent(
                500, 500);
        AbstractRenderer monotoneRenderer = new MonotoneRenderer(makeScene(),
                camera, rendererComponent);
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setMode(MouseCameraController.XZMode);
        mcCtrl.setupComponent(rendererComponent);
        return rendererComponent;
    }

    private static ObjectGroup group;

    private static SceneContext scene;

    private static SceneContext makeScene() {
        scene = new SceneContext();
        group = new ObjectGroup();
        IGeometricObject cur = PrimitiveObjectMaker.sphere(2, 8, Color.black,
                Color.green);
        VectorQD lastPos = new VectorQD(5 * (-3), Math.random() * 4 - 2, Math
                .random() * 4 - 2);
        cur.setPosition(lastPos);
        scene.addObject(cur);
        group.addObject(cur);
        for (int i = 0; i < 5; i++) {
            cur = PrimitiveObjectMaker.sphere(2, 8, Color.black, Color.green);
            MatrixQD rot = MatrixQD.getRotMatrix(rndPos(0.1), rndPos(0.1),
                    rndPos(6.28));
            VectorQD pos = new VectorQD(5 * (i - 2), Math.random() * 4 - 2,
                    Math.random() * 4 - 2);
            cur.rotate(rot);
            cur.setPosition(pos);
            IGeometricObject line = PrimitiveObjectMaker.rod(lastPos, pos, 0.1,
                    Color.gray);
            lastPos = pos;
            scene.addObject(line);
            group.addObject(line);
            scene.addObject(cur);
            group.addObject(cur);
        }
        IGeometricObject obj = PrimitiveObjectMaker.tetrahedron(1, Color.black,
                Color.blue);
        obj.setPosition(new VectorQD(4, 0, 0));
        scene.addObject(obj);
        obj = PrimitiveObjectMaker.tetrahedron(1, Color.black, Color.blue);
        obj.setPosition(new VectorQD(0, 4, 0));
        scene.addObject(obj);
        obj = PrimitiveObjectMaker.tetrahedron(1, Color.black, Color.blue);
        obj.setPosition(new VectorQD(0, 0, 4));
        scene.addObject(obj);

        obj = PrimitiveObjectMaker.flatSquareSurface(
                new VectorQD(-15, -15, -15), new VectorQD(30, -15, -15),
                new VectorQD(-15, 30, -15), 8, Color.orange, Color.red);
        scene.addObject(obj);

        scene.addLight(new ParallelLight(new Vector3D(1.2, 1, 1), 1));
        return scene;
    }

    static double rndPos(double width) {
        return width * (Math.random() - 0.5);
    }

    static Runnable runner = new Runnable() {
        public void run() {
            while (true) {
                MatrixQD rot = MatrixQD.getRotMatrix(rndPos(0.1), rndPos(0.1),
                        rndPos(0.1));
                for (int i = 0; i < 100; i++) {
                    group.rotate(rot);
                    scene.updateConfigration();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    };
}
