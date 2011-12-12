/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

import inou.comp.RichString;
import inou.comp.SwingUtils;
import inou.comp.d3.StringObject;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.ColorRenderer;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.ParallelLight;
import inou.comp.d3.PrimitiveObjectMaker;
import inou.comp.d3.SceneContext;
import inou.comp.d3.SwingRendererComponent;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;

public class Sample {

    public static void main(String[] args) {
        JFrame f = SwingUtils.getTestFrame("graph test");
        f.getContentPane().add("Center", getDemo());
        f.setSize(600, 600);
        f.show();
    }

    private static Component getDemo() {
        Camera camera = new DefaultCamera();
        SwingRendererComponent rendererComponent = new SwingRendererComponent(
                500, 500);
        // AbstractRenderer renderer = new
        //          MonotoneRenderer(makeScene(),camera,rendererComponent);
        AbstractRenderer renderer = new ColorRenderer(makeScene(), camera,
                rendererComponent);
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setupComponent(rendererComponent);
        return rendererComponent;
    }

    private static RichString str2rich(String str) {
        return new RichString(str,Color.yellow,null,new Font("SansSerif",Font.BOLD,14));
    }

    private static void addObject(SceneContext scene, VectorQD p, IGeometricObject obj) {
        obj.setPosition(p);
        scene.addObject(obj);
        scene.addObject(new StringObject(p,str2rich(p.toString())));
    }
    
	private static SceneContext makeScene() {
		SceneContext scene = new SceneContext();

        addObject(scene,new VectorQD(-3,-3,0),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
		addObject(scene,new VectorQD( 3,-3,0),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
		addObject(scene,new VectorQD( 3, 3,0),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
		addObject(scene,new VectorQD(-3, 3,0),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));

        addObject(scene,new VectorQD(-3,-3, 3),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
        addObject(scene,new VectorQD( 3,-3, 3),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
        addObject(scene,new VectorQD( 3, 3, 3),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));
        addObject(scene,new VectorQD(-3, 3, 3),PrimitiveObjectMaker.cube(1,Color.black,Color.blue));

		addObject(scene,new VectorQD(0,0,0),PrimitiveObjectMaker.sphere(1,16,Color.red,Color.pink));
		
		scene.addLight( new ParallelLight(new Vector3D(1.2,1,1),1) );
		return scene;
	}

}
