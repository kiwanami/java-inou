/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

import inou.comp.ColorSet;
import inou.comp.RichString;
import inou.comp.SwingUtils;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.CircleObject;
import inou.comp.d3.ColorRenderer;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.IWireObject;
import inou.comp.d3.LinePair;
import inou.comp.d3.MonotoneRenderer;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.MouseDynamicRendererChanger;
import inou.comp.d3.MouseRendererChanger;
import inou.comp.d3.ParallelLight;
import inou.comp.d3.PointSource;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.d3.Renderer;
import inou.comp.d3.SceneContext;
import inou.comp.d3.StringObject;
import inou.comp.d3.SubjectiveRenderer;
import inou.comp.d3.SwingRendererComponent;
import inou.comp.d3.Virtual3DRenderer;
import inou.comp.d3.WireObject;
import inou.comp.d3.WireRenderer;
import inou.math.vector.MatrixQD;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/** simple 3d test */
public class Demo {

    public static void main(String[] args) {
        JFrame f = SwingUtils.getTestFrame("graph test");
        f.getContentPane().add("Center", getDemo());
        f.setSize(600, 600);
        f.show();
    }

    public static JTabbedPane getDemo() {
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Renderers", new RendererTest().getComponent());
        tab.addTab("Dynamic", new MovingTest().getComponent());
        tab.addTab("Light", new LightTest().getComponent());
        return tab;
    }

    static SceneContext makeFullScene(double scale, int num) {
        IGeometricObject[] objs = makeCubicObjects(scale, num);
        // IGeometricObject [] objs = makeRandomObjects(scale,num);
        // IGeometricObject [] objs = makeAObjects(scale,num);
        // IGeometricObject [] objs = makeRingObjects(scale,num);
        SceneContext scene = new SceneContext();
        for (int i = 0; i < objs.length; i++) {
            scene.addObject(objs[i]);
        }
        scene.addLight(new ParallelLight(new Vector3D(1.2, 1, 1), 1));
        return scene;
    }

    static SceneContext makeRingScene(double scale, int num) {
        // IGeometricObject [] objs = makeCubicObjects(scale,num);
        // IGeometricObject [] objs = makeRandomObjects(scale,num);
        // IGeometricObject [] objs = makeAObjects(scale,num);
        IGeometricObject[] objs = makeRingObjects(scale, num);
        SceneContext scene = new SceneContext();
        for (int i = 0; i < objs.length; i++) {
            scene.addObject(objs[i]);
        }
        scene.addLight(new ParallelLight(new Vector3D(1, 1, 1), 1));
        return scene;
    }

    static SceneContext makeDebugScene(double scale, int num) {
        IGeometricObject[] objs = makeAObjects(scale, num);
        SceneContext scene = new SceneContext();
        for (int i = 0; i < objs.length; i++) {
            scene.addObject(objs[i]);
        }
        scene.addLight(new ParallelLight(new Vector3D(1, 1, 1), 1));
        return scene;
    }

    static Color background = Color.white;

    static IGeometricObject[] makeAObjects(double scale, int num) {
        // return new IGeometricObject[] {new CircleObject(new
        // VectorQD(5,0,0),2*scale,Color.black,getRandomColor())};
        return new IGeometricObject[] { PrimitiveObjectMaker.cube(1,
                Color.blue, Color.red) };
    }

    static IGeometricObject[] makeRingObjects(double scale, int num) {
        num = 20;
        IGeometricObject[] ret = new IGeometricObject[num];
        double radius = scale * num / 2;
        double rad = 3.14 * 2. / num;
        int counter = 0;
        for (int k = 0; k < num; k++) {
            double sz = stdrnd(2 * scale, 0.5 * scale);
            int rd = (int) (Math.random() * 3);
            IGeometricObject cur = null;
            // rd = 5;
            switch (rd) {
            case 0:
                cur = new CircleObject(new VectorQD(0, 0, -sz), sz,
                        Color.black, getRandomColor());
                break;
            case 1:
                cur = PrimitiveObjectMaker
                        .cube(sz, Color.red, getRandomColor());
                break;
            case 2:
                cur = PrimitiveObjectMaker.tetrahedron(sz, Color.gray,
                        getRandomColor());
                break;
            default:
                cur = PrimitiveObjectMaker.triangle(sz, Color.gray,
                        getRandomColor());
            }
            MatrixQD rot = MatrixQD.getRotMatrix(rndPos(0.1), rndPos(0.1),
                    rndPos(6.28));
            Vector3D pos = new VectorQD(radius * Math.cos(rad * k), 0, radius
                    * Math.sin(rad * k));
            cur.rotate(rot);
            cur.setPosition(pos);
            ret[counter++] = cur;
        }
        return ret;
    }

    static IGeometricObject[] makeRandomObjects(double scale, int num) {
        int systemSize = (int) Math.pow(num, 0.3333) * 3;
        IGeometricObject[] ret = new IGeometricObject[num];
        for (int i = 0; i < num; i++) {
            double sz = stdrnd(2 * scale, 0.5 * scale);
            int rd = (int) (Math.random() * 8);
            // rd = 5;
            switch (rd) {
            case 1:
                ret[i] = PrimitiveObjectMaker.arrow(new VectorQD(0, 0, -sz),
                        new VectorQD(0, 0, sz), getRandomColor());
                break;
            case 2:
                ret[i] = PrimitiveObjectMaker.cube(sz, Color.red,
                        getRandomColor());
                break;
            case 3:
                ret[i] = PrimitiveObjectMaker.tetrahedron(sz, Color.gray,
                        getRandomColor());
                break;
            case 4:
                ret[i] = new StringObject(new VectorQD(0, 0, -sz),
                        new RichString("Text", getRandomColor(), null,
                                new Font("SansSerif", Font.PLAIN, 12)));
                break;
            case 5:
                ret[i] = new CircleObject(new VectorQD(0, 0, -sz), sz,
                        Color.black, getRandomColor());
                break;
            case 6:
                ret[i] = makeRandomPath();
                break;
            default:
                ret[i] = PrimitiveObjectMaker.triangle(sz, Color.gray,
                        getRandomColor());
            }
            MatrixQD rot = MatrixQD.getRotMatrix(rndPos(6.28), rndPos(6.28),
                    rndPos(6.28));
            Vector3D pos = randomPosition(systemSize * scale);
            ret[i].rotate(rot);
            ret[i].setPosition(pos);
        }
        return ret;
    }

    static IGeometricObject[] makeCubicObjects(double scale, int num) {
        IGeometricObject[] ret = new IGeometricObject[num];
        int rim = (int) Math.pow(num, 0.3333);
        int counter = 0;
        for (int i = 0; i < rim; i++) {
            for (int j = 0; j < rim; j++) {
                for (int k = 0; k < rim; k++) {
                    double sz = stdrnd(2 * scale, 0.5 * scale);
                    int rd = (int) (Math.random() * 3);
                    IGeometricObject cur = null;
                    // rd = 5;
                    switch (rd) {
                    case 0:
                        cur = new CircleObject(new VectorQD(0, 0, -sz), sz,
                                Color.black, getRandomColor());
                        break;
                    case 1:
                        cur = PrimitiveObjectMaker.cube(sz, Color.red,
                                getRandomColor());
                        break;
                    case 2:
                        cur = PrimitiveObjectMaker.tetrahedron(sz, Color.gray,
                                getRandomColor());
                        break;
                    default:
                        cur = PrimitiveObjectMaker.triangle(sz, Color.gray,
                                getRandomColor());
                    }
                    MatrixQD rot = MatrixQD.getRotMatrix(rndPos(0.1),
                            rndPos(0.1), rndPos(6.28));
                    Vector3D pos = new VectorQD(4 * scale * (i - rim / 2), 4
                            * scale * (j - rim / 2), 4 * scale * (k - rim / 2));
                    cur.rotate(rot);
                    cur.setPosition(pos);
                    ret[counter++] = cur;
                }
            }
        }
        return ret;
    }

    static IWireObject makeRandomPath() {
        int num = 16;
        VectorQD[] vecs = new VectorQD[num];
        for (int i = 0; i < num; i++) {
            vecs[i] = randomPositionQd(3);
        }
        ColorSet set = new ColorSet(ColorSet.rainbowIndex, vecs.length - 1);
        LinePair[] pairs = new LinePair[vecs.length - 1];
        for (int i = 0; i < (vecs.length - 1); i++) {
            pairs[i] = new LinePair(i, i + 1, set.getColor(i));
        }
        return new WireObject(vecs, pairs);
    }

    static Color getRandomColor() {
        return new Color(rnd(255), rnd(255), rnd(255));
    }

    static VectorQD randomPositionQd(double width) {
        return new VectorQD(rndPos(width), rndPos(width), rndPos(width));
    }

    static Vector3D randomPosition(double width) {
        return new Vector3D(rndPos(width), rndPos(width), rndPos(width));
    }

    static double stdrnd(double base, double s) {
        return base + rndPos(s) + rndPos(s) + rndPos(s) + rndPos(s) + rndPos(s);
    }

    static int rnd(int i) {
        return (int) (Math.random() * i);
    }

    static double rndPos(double width) {
        return width * (Math.random() - 0.5);
    }
}

class RendererTest {

    RendererTest() {
    }

    int width = 500, height = 500;

    SceneContext context;

    Renderer[] renderers;

    MouseCameraController mcCtrl;

    SwingRendererComponent rendererComponent;

    Component getComponent() {
        context = Demo.makeFullScene(0.5, 12 * 12 * 12);
        makeRenderers();
        initRendererComponent();
        return rendererComponent;
    }

    void initRendererComponent() {
        rendererComponent = new SwingRendererComponent(width, height);
        mcCtrl = new MouseCameraController(camera);
        mcCtrl.setupComponent(rendererComponent);
        rendererComponent.addMouseListener(new MouseRendererChanger(renderers,
                camera, rendererComponent));
    }

    void makeRenderers() {
        renderers = new Renderer[] { makeWireRenderer(),
                makeMonotoneRenderer(), makeColorRenderer(),
                makeVirtualRenderer(), makeSubjectiveRenderer(), };
    }

    Renderer makeVirtualRenderer() {
        Virtual3DRenderer virtualRenderer = new Virtual3DRenderer(context);
        // virtualRenderer.setPreciousMode();
        return virtualRenderer;
    }

    Renderer makeSubjectiveRenderer() {
        AbstractRenderer subjectRenderer = new SubjectiveRenderer(context);
        return subjectRenderer;
    }

    Renderer makeMonotoneRenderer() {
        AbstractRenderer monotoneRenderer = new MonotoneRenderer(context);
        return monotoneRenderer;
    }

    Renderer makeColorRenderer() {
        AbstractRenderer colorRenderer = new ColorRenderer(context);
        return colorRenderer;
    }

    Renderer makeWireRenderer() {
        AbstractRenderer wireRenderer = new WireRenderer(context);
        return wireRenderer;
    }

    Camera camera = new DefaultCamera();

}

class MovingTest {

    MovingTest() {
    }

    int width = 500, height = 500;

    AbstractRenderer movingRenderer;

    SubjectiveRenderer steadyRenderer;

    SwingRendererComponent rendererComponent;

    Camera camera = new DefaultCamera();

    Component getComponent() {
        initRenderer();
        initRendererComponent();
        return rendererComponent;
    }

    void initRenderer() {
        SceneContext context = Demo.makeFullScene(0.5, 12 * 12 * 12);
        movingRenderer = new WireRenderer(context);
        movingRenderer.setBackground(Color.white);
        steadyRenderer = new SubjectiveRenderer(context);
        steadyRenderer.setBackground(Color.white);
        // steadyRenderer.setContrast(1f);
        // steadyRenderer.setBrightness(0f);
        steadyRenderer.setEliminateDistance(30);
        steadyRenderer.setFogDistance(12);
    }

    void initRendererComponent() {
        rendererComponent = new SwingRendererComponent(width, height);
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setupComponent(rendererComponent);
        MouseDynamicRendererChanger changer = new MouseDynamicRendererChanger(
                steadyRenderer, movingRenderer, camera, rendererComponent);
        rendererComponent.addMouseListener(changer);
    }

}

class LightTest {

    LightTest() {
    }

    int width = 500, height = 500;

    AbstractRenderer movingRenderer;

    SubjectiveRenderer steadyRenderer;

    // AbstractRenderer steadyRenderer;
    SwingRendererComponent rendererComponent;

    Camera camera = new DefaultCamera();

    Component getComponent() {
        initRenderer();
        initRendererComponent();
        return rendererComponent;
    }

    void initRenderer() {
        SceneContext context = Demo.makeFullScene(0.5, 12 * 12 * 12);
        context.removeAllLights();
        context.addLight(new PointSource(new Vector3D(0, 0, 0), 10));
        context.addLight(new PointSource(new Vector3D(8, 8, 8), 30));
        movingRenderer = new MonotoneRenderer(context);
        movingRenderer.setBackground(Color.black);
        steadyRenderer = new SubjectiveRenderer(context);
        // steadyRenderer = new MonotoneRenderer(context);
        steadyRenderer.setBackground(Color.black);
        steadyRenderer.setContrast(1f);
        steadyRenderer.setBrightness(0f);
        steadyRenderer.setEliminateDistance(50);
        steadyRenderer.setFogDistance(30);
    }

    void initRendererComponent() {
        rendererComponent = new SwingRendererComponent(width, height);
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setupComponent(rendererComponent);
        MouseDynamicRendererChanger changer = new MouseDynamicRendererChanger(
                steadyRenderer, movingRenderer, camera, rendererComponent);
        rendererComponent.addMouseListener(changer);
    }

}
