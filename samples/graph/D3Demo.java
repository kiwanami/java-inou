/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

import inou.comp.ColorSet;
import inou.comp.SwingUtils;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.DefaultCamera;
import inou.comp.d3.MouseCameraController;
import inou.comp.d3.MouseDynamicRendererChanger;
import inou.comp.d3.SubjectiveRenderer;
import inou.comp.d3.WireRenderer;
import inou.comp.ngraph.DefaultColorValueGenerator;
import inou.comp.ngraph.PlotModel3D;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.PolygonPlotRenderer3D;
import inou.comp.ngraph.SingleColorValueGenerator;
import inou.comp.ngraph.SurfaceFunctionData3D;
import inou.comp.ngraph.SurfaceXYData3D;
import inou.comp.ngraph.SwingPlotComponent;
import inou.comp.ngraph.VectorFunctionData;
import inou.comp.ngraph.VolumeFunctionData4D;
import inou.comp.ngraph.d3.GradationSurfacePainter3D;
import inou.comp.ngraph.d3.GradationVectorPlotter3D;
import inou.comp.ngraph.d3.IsovalueSurfacePainter3D;
import inou.comp.ngraph.d3.SurfacePainter3D;
import inou.math.FunctionUtil;
import inou.math.Gradient;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;
import inou.math.VectorFunction;
import inou.math.vector.Vector2D;
import inou.math.vector.Vector3D;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class D3Demo {

    public static void main(String[] arg) {
        JFrame f = SwingUtils.getTestFrame("graph test");
        f.getContentPane().add("Center", getDemo());
        f.setSize(600, 600);
        f.show();
    }

    public static JTabbedPane getDemo() {
        SwingPlotComponent[] comps = { getSurfaceFunctionTest(),
                getDoubleSurfaceFunctionTest(), getSurfaceXYTest(),
                getGradationSurfaceTest(), getIsovalueSurfaceTest(),
                getContourSurfaceTest(), getVectorTest(),
        // getDebugTest()
        };
        JTabbedPane tab = new JTabbedPane();
        for (int i = 0; i < comps.length; i++) {
            tab.addTab("demo" + (i + 1), comps[i]);
        }
        return tab;
    }

    static SwingPlotComponent getPlotComponent(PlotModel3D plotContext) {
        PolygonPlotRenderer3D plotRenderer = new PolygonPlotRenderer3D(
                plotContext);
        SwingPlotComponent pc = new SwingPlotComponent(600, 500);
        pc.addRenderer(plotRenderer);

        // AbstractRenderer polygonRenderer = new
        // ColorRenderer(plotRenderer.getSceneContext());
        // AbstractRenderer polygonRenderer = new
        // MonotoneRenderer(plotRenderer.getSceneContext());
        AbstractRenderer polygonRenderer = new SubjectiveRenderer(plotRenderer
                .getSceneContext());
        Camera camera = new DefaultCamera();
        MouseCameraController mcCtrl = new MouseCameraController(camera);
        mcCtrl.setMode(MouseCameraController.XZMode);
        mcCtrl.setCameraPosition(9, 3.14 * 0.5, 3.14 * 0.8);
        mcCtrl.setupComponent(plotRenderer.getComponent());
        plotRenderer.getComponent().addMouseListener(
                new MouseDynamicRendererChanger(polygonRenderer,
                        new WireRenderer(plotRenderer.getSceneContext()),
                        camera, plotRenderer));
        plotRenderer.setCamera(camera);

        return pc;
    }

    static SwingPlotComponent getSurfaceFunctionTest() {
        PlotModel3D context = new PlotModel3D();

        String form2 = "exp(-(x*x+y*y)*0.2)*cos( (x*x+y*y)*0.5)*3";
        ScalarFunction func2 = ScalarFunctionClass.getFunction(form2);
        SurfaceFunctionData3D data2 = new SurfaceFunctionData3D(func2);
        data2.setDataName(form2);
        data2.setDivision(40);
        Plotter3D plotter2 = new SurfacePainter3D(data2);
        context.addPlotter(plotter2);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getDoubleSurfaceFunctionTest() {
        PlotModel3D context = new PlotModel3D();

        String form = "(x*x-y*y)/4+10";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDataName(form);
        data.setDivision(25);
        Plotter3D plotter = new SurfacePainter3D(data);
        context.addPlotter(plotter);

        String form2 = "x*x+y*y";
        ScalarFunction func2 = ScalarFunctionClass.getFunction(form2);
        SurfaceFunctionData3D data2 = new SurfaceFunctionData3D(func2);
        data2.setDataName(form2);
        data2.setDivision(25);
        Plotter3D plotter2 = new SurfacePainter3D(data2);
        context.addPlotter(plotter2);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getSurfaceXYTest() {
        PlotModel3D context = new PlotModel3D();

        String form = "1/(x+3)+1/(y+3)+random(0.1)";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);

        int num = 80;
        Vector2D arg = new Vector2D();
        MathVector[] array = new MathVector[num];
        for (int i = 0; i < num; i++) {
            arg.x = random(0, 5);
            arg.y = random(0, 5);
            array[i] = new Vector3D(arg.x, arg.y, func.f(arg));
        }

        SurfaceXYData3D data = new SurfaceXYData3D(array);
        data.setDataName(form);
        SurfacePainter3D plotter = new SurfacePainter3D(data);
        plotter.setSurfaceColor(null);
        context.addPlotter(plotter);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getDebugTest() {
        PlotModel3D context = new PlotModel3D();
        MathVector[] array = { new Vector3D(0, 0, 0), new Vector3D(2, 1, 2),
                new Vector3D(1, 2, 1), };
        SurfaceXYData3D data = new SurfaceXYData3D(array);
        data.setDataName("debug");

        // Plotter3D plotter = new SurfacePainter3D(data);
        // context.addPlotter(plotter);
        Plotter3D plotter2 = new GradationSurfacePainter3D(data,
                new DefaultColorValueGenerator(3, ColorSet.rainbowIndex));
        context.addPlotter(plotter2);
        return getPlotComponent(context);
    }

    static SwingPlotComponent getGradationSurfaceTest() {
        PlotModel3D context = new PlotModel3D();
        String form2 = "exp(-(x*x+y*y)*0.2)+cos(x*2)*cos(y*2)*0.05";
        ScalarFunction func2 = ScalarFunctionClass.getFunction(form2);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func2);
        data.setDataName(form2);
        data.setDivision(40);

        Plotter3D plotter2 = new GradationSurfacePainter3D(data,
                new DefaultColorValueGenerator(8, ColorSet.rainbowIndex));
        context.addPlotter(plotter2);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getIsovalueSurfaceTest() {
        PlotModel3D context = new PlotModel3D();
        // String form =
        // "exp(-0.15*((x+2.5)*(x+2.5)+y*y+z*z))-exp(-0.15*((x-2.5)*(x-2.5)+y*y+z*z))";
        String form = "x*x+y*y+z*z";
        // String form = "cos(x/2.5)*cos(y/2.5)*(2+cos(z))";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        VolumeFunctionData4D data = new VolumeFunctionData4D(func);
        data.setDataName(form);
        data.setDivision(12);

        Plotter3D plotter = new IsovalueSurfacePainter3D(data,
                new DefaultColorValueGenerator(5, ColorSet.rainbowIndex));
        context.addPlotter(plotter);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getContourSurfaceTest() {
        PlotModel3D context = new PlotModel3D();

        // sphere
        String form = "x*x+y*y+z*z-16";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        VolumeFunctionData4D data = new VolumeFunctionData4D(func);
        data.setDataName(form);
        data.setDivision(10);
        Plotter3D plotter = new IsovalueSurfacePainter3D(data,
                new SingleColorValueGenerator(new Color(0x4455ff), 0));
        context.addPlotter(plotter);

        // plane
        String form2 = "x-4";
        ScalarFunction func2 = ScalarFunctionClass.getFunction(form2);
        VolumeFunctionData4D data2 = new VolumeFunctionData4D(func2);
        data2.setDataName(form2);
        data2.setDivision(10);

        Plotter3D plotter2 = new IsovalueSurfacePainter3D(data2,
                new SingleColorValueGenerator(Color.pink, 0));
        context.addPlotter(plotter2);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getVectorTest() {
        PlotModel3D context = new PlotModel3D();
        final double d = 0.001;
        /* dipole */
        final ScalarFunction func = new ScalarFunctionClass(3) {
            Vector3D[] pos = { new Vector3D(d, d, d), new Vector3D(d, d, -d), };

            double[] sign = { 1, -1 };

            public double f(MathVector arg) {
                double ret = 0;
                for (int i = 0; i < pos.length; i++) {
                    ret += 1.0 / (arg.sub(pos[i]).getLength()) * sign[i];
                }
                return ret;
            }
        };
        /*
         * octapole ScalarFunction func = new ScalarFunctionClass(3) { Vector3D []
         * pos = { new Vector3D( d, d,d), new Vector3D(-d, d,d), new Vector3D(
         * d,-d,d), new Vector3D(-d,-d,d), new Vector3D( d, d,-d), new
         * Vector3D(-d, d,-d), new Vector3D( d,-d,-d), new Vector3D(-d,-d,-d), };
         * double [] sign = {1,-1,-1,1,-1,1,1,-1}; public double f(MathVector
         * arg) { double ret = 0; for (int i=0;i<pos.length;i++) { ret +=
         * 1.0/(arg.sub(pos[i]).getLength())*sign[i]; } return ret; } };
         */
        VolumeFunctionData4D data = new VolumeFunctionData4D(func);
        data.setDataName("Dipole");
        data.setDivision(5);
        VectorFunction vf = new Gradient(FunctionUtil.multiple(func, -1));
        VectorFunctionData vfd = new VectorFunctionData(vf);
        GradationVectorPlotter3D plotter = new GradationVectorPlotter3D(data,
                vfd);
        plotter.setMaxLimit(1.0e-4);
        plotter.setMinLimit(1.0e-9);
        context.addPlotter(plotter);

        VolumeFunctionData4D data2 = new VolumeFunctionData4D(func);
        // data2.setDivision(12);
        data2.setDataName("iso-value");
        IsovalueSurfacePainter3D plotter2 = new IsovalueSurfacePainter3D(data2,
                new DefaultColorValueGenerator(5, ColorSet.rainbowIndex));
        // plotter2.setDrawSurface(false);
        plotter2.setValueRange(new RealRange(-1.0e-4, 2.0e-4));
        context.addPlotter(plotter2);

        return getPlotComponent(context);
    }

    static private Random randomMaker = new Random();

    static private double stdrnd(double center, double width) {
        return randomMaker.nextGaussian() * width + center;
    }

    static private double random(double center, double width) {
        return (Math.random() - 0.5) * width + center;
    }

}
