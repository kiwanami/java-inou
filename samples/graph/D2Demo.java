/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

import inou.comp.ColorSet;
import inou.comp.SwingUtils;
import inou.comp.d3.AbstractRenderer;
import inou.comp.d3.Camera;
import inou.comp.d3.ColorRenderer;
import inou.comp.d3.DefaultCamera;
import inou.comp.ngraph.DefaultColorValueGenerator;
import inou.comp.ngraph.FunctionData2D;
import inou.comp.ngraph.ParametricData2D;
import inou.comp.ngraph.ParametricFunctionData2D;
import inou.comp.ngraph.PlotComponent;
import inou.comp.ngraph.PlotModel2D;
import inou.comp.ngraph.PlotModel3D;
import inou.comp.ngraph.PlotData;
import inou.comp.ngraph.Plotter;
import inou.comp.ngraph.PolygonPlotRenderer3D;
import inou.comp.ngraph.PolygonPlotRenderingParam;
import inou.comp.ngraph.RenderingInfo2D;
import inou.comp.ngraph.SingleColorValueGenerator;
import inou.comp.ngraph.SquarePlotRenderer2D;
import inou.comp.ngraph.SquarePlotRenderingParam;
import inou.comp.ngraph.StringGridGenerator;
import inou.comp.ngraph.StrutElement;
import inou.comp.ngraph.StrutsData;
import inou.comp.ngraph.SurfaceFunctionData3D;
import inou.comp.ngraph.SwingPlotComponent;
import inou.comp.ngraph.UGraph;
import inou.comp.ngraph.UPlotData;
import inou.comp.ngraph.VectorFunctionData;
import inou.comp.ngraph.XYData2D;
import inou.comp.ngraph.XYErrorData2D;
import inou.comp.ngraph.d2.ArrowPlotter;
import inou.comp.ngraph.d2.BarChartPlotter;
import inou.comp.ngraph.d2.ContourPainter;
import inou.comp.ngraph.d2.ErrorBarPlotter;
import inou.comp.ngraph.d2.GradationPainter;
import inou.comp.ngraph.d2.GradationVectorPlotter2D;
import inou.comp.ngraph.d2.IconManager;
import inou.comp.ngraph.d2.IconPlotter;
import inou.comp.ngraph.d2.LinePlotter;
import inou.comp.ngraph.d2.MaxPointPlotter;
import inou.comp.ngraph.d2.MinPointPlotter;
import inou.comp.ngraph.d2.ShadedSurfacePainter;
import inou.comp.ngraph.d2.TextObject;
import inou.comp.ngraph.d2.ValueObject;
import inou.comp.ngraph.d2.VerticalStrutsPlotter;
import inou.comp.ngraph.d2.icon.StarIcon;
import inou.comp.ngraph.d3.SurfacePainter3D;
import inou.math.AArrayFunction;
import inou.math.AFunction;
import inou.math.AFunctionClass;
import inou.math.AWrapperFunction;
import inou.math.FunctionUtil;
import inou.math.Gradient;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;
import inou.math.SplineInterpolater;
import inou.math.VectorFunction;
import inou.math.logical.Condition;
import inou.math.logical.ULogical;
import inou.math.util.LinearFit;
import inou.math.util.MarquardtFit;
import inou.math.vector.Vector2D;
import inou.math.vector.VectorGD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/** Simple test of PlotComponent */
public class D2Demo {

    public static void main(String[] arg) {
        JFrame f = SwingUtils.getTestFrame("graph test");
        f.getContentPane().add("Center", getDemo());
        f.getContentPane().add("South", positionLabel);
        f.setSize(600, 480);
        f.show();
    }

    public static JTabbedPane getDemo() {
        IconManager.addIcon(new StarIcon());// custom icon
        PlotComponent[] comps = { getXYDataTest(), getBarChartTest(),
                getStrutsTest(), getDoubleGraphTest(), getFunctionTest(),
                getReverseFunctionTest(), getParametricDataTest(),
                getParametricFunctionTest(), getImplicitFunctionTest(),
                get2DSurfaceTest(), get2DSurfaceTest2(), get2DSurfaceTest3(),
                get2DSurfaceTest4(), getXYErrorTest(), getXYErrorTest2(),
                getRegionTest(), };
        /* for debug */
        /*
         * PlotComponent [] comps = { getStrutsTest(), };
         */
        JTabbedPane tab = new JTabbedPane();
        for (int i = 0; i < comps.length; i++) {
            tab.addTab("" + (i + 1), comps[i].getComponent());
        }
        return tab;
    }

    static SwingPlotComponent getPlotComponent(PlotModel2D context) {
        SquarePlotRenderingParam param = new SquarePlotRenderingParam();
        param.squareRect = true;
        SquarePlotRenderer2D renderer = new SquarePlotRenderer2D(context, param);
        SwingPlotComponent pc = new SwingPlotComponent(600, 500);
        pc.addRenderer(renderer);
        pc.addMouseMotionListener(new PositionShowHandler(renderer, pc));
        return pc;
    }

    private static JLabel positionLabel = new JLabel();

    static class PositionShowHandler extends MouseMotionAdapter {
        private SquarePlotRenderer2D renderer;

        private Component component;

        private RenderingInfo2D renderingInfo;

        private Dimension size = new Dimension();

        private RenderingInfo2D getInfo() {
            if (renderingInfo == null || !size.equals(component.getSize())) {
                size.width = component.getSize().width;
                size.height = component.getSize().height;
                Rectangle rect = new Rectangle(0, 0, size.width, size.height);
                renderingInfo = new RenderingInfo2D(component.getGraphics(),
                        rect, renderer);
            }
            return renderingInfo;
        }

        PositionShowHandler(SquarePlotRenderer2D r, Component c) {
            renderer = r;
            component = c;
        }

        public void mouseMoved(MouseEvent e) {
            RenderingInfo2D info = getInfo();
            double x = info.graphics2realX(e.getX());
            double y = info.graphics2realY(e.getY());
            positionLabel.setText("Position ("
                    + UPlotData.defaultNumberFormat(x) + ", "
                    + UPlotData.defaultNumberFormat(y) + ")");
        }
    };

    static SwingPlotComponent getBarChartTest() {
        PlotModel2D context = new PlotModel2D();
        context.setAutoScaleMarginRatio(0);
        double[] px = { 1, 2, 3, 4, 5, 6 };
        String[] titles = { "January", "February", "March", "April", "May", "" };
        double[] py = { 10, 13, 44, 80, 65, 0 };
        context.getAxisX().setMainGridGenerator(
                new StringGridGenerator(px, titles));
        context.getAxisX().setSubGridGenerator(null);

        PlotData d = new XYData2D(px, py);
        d.setDataName("Sales");
        Plotter p = new BarChartPlotter(d);
        context.addPlotter(p);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getDoubleGraphTest() {
        PlotModel2D context1 = new PlotModel2D();
        PlotData d1 = new XYData2D(data(1, 20));
        d1.setDataName("XYData1");
        context1.addPlotter(new BarChartPlotter(d1));

        PlotModel2D context2 = new PlotModel2D();
        context2.addPlotter(new IconPlotter(new LinePlotter(d1)));

        SwingPlotComponent pc = new SwingPlotComponent(600, 500);
        pc.addRenderer(new SquarePlotRenderer2D(context1));
        pc.addRenderer(new SquarePlotRenderer2D(context2), new RealRange(0.15,
                0.1, 0.3, 0.3));
        return pc;
    }

    static SwingPlotComponent getXYDataTest() {
        PlotModel2D context = new PlotModel2D();

        PlotData d = new XYData2D(D2Demo.data(1));
        d.setDataName("sin1");
        Plotter p = new IconPlotter(new LinePlotter(d));
        context.addPlotter(p);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getFunctionTest() {
        PlotModel2D context = new PlotModel2D();

        AArrayFunction af = new AArrayFunction(D2Demo.data(1));
        af.setInterpolater(new SplineInterpolater());
        PlotData d = new FunctionData2D(af);
        d.setDataName("sin2");
        context.addPlotter(new LinePlotter(d));

        String form = "0.5*sin(x)*exp(-x*x*0.5)";
        AFunction func = AFunctionClass.getFunction(form);
        PlotData d2 = new FunctionData2D(func);
        d2.setDataName(form);
        context.addPlotter(new LinePlotter(d2));

        return getPlotComponent(context);
    }

    static SwingPlotComponent getReverseFunctionTest() {
        PlotModel2D context = new PlotModel2D();

        AArrayFunction af = new AArrayFunction(D2Demo.data(1));
        af.setInterpolater(new SplineInterpolater());
        FunctionData2D d = new FunctionData2D(af);
        d.setDataName("sin2");
        d.setReverseAxis(true);
        context.addPlotter(new LinePlotter(d));

        String form = "0.5*sin(x)*exp(-x*x*0.5)";
        AFunction func = AFunctionClass.getFunction(form);
        FunctionData2D d2 = new FunctionData2D(func);
        d2.setDataName(form);
        d2.setReverseAxis(true);
        context.addPlotter(new LinePlotter(d2));

        return getPlotComponent(context);
    }

    static SwingPlotComponent getParametricDataTest() {
        PlotModel2D context = new PlotModel2D();

        // ParametricData
        double[] px = { 1, -1, -1, 1, 1 };
        double[] py = { 1, 1, -1, -1, 1 };
        PlotData d = new ParametricData2D(px, py);
        d.setDataName("square");
        Plotter p = new ArrowPlotter(d);
        context.addPlotter(p);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getParametricFunctionTest() {
        PlotModel2D context = new PlotModel2D();

        // ParametricFunctionData
        AFunction[] fs = new AFunction[2];
        fs[0] = new AFunction() {
            public double f(double t) {
                return (1 + Math.sin(t * 10) * 0.5) * Math.cos(t);
            }
        };
        fs[1] = new AFunction() {
            public double f(double t) {
                return (1 + Math.sin(t * 10) * 0.5) * Math.sin(t);
            }
        };
        PlotData d = new ParametricFunctionData2D(fs, 0, Math.PI * 2);
        d.setDataName("circle");
        Plotter p = new LinePlotter(d);
        context.addPlotter(p);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getImplicitFunctionTest() {
        PlotModel2D context = new PlotModel2D();
        String form = "x*x -4*x*y - y*y ";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDivision(40);
        data.setDataName(form);
        ContourPainter cp = new ContourPainter(data,
                new SingleColorValueGenerator(Color.black, 10));
        context.addPlotter(cp);
        return getPlotComponent(context);
    }

    // shade and gradation contour
    static SwingPlotComponent get2DSurfaceTest() {
        PlotModel2D context = new PlotModel2D();
        String form = "cos(x)*cos(y)*exp(-(x*x+y*y)*0.1)+random(0.05)";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDivision(80);
        data.setDataName(form);
        context.addPlotter(new ContourPainter(new ShadedSurfacePainter(data)));
        return getPlotComponent(context);
    }

    // gradation
    static SwingPlotComponent get2DSurfaceTest2() {
        PlotModel2D context = new PlotModel2D();
        /*
         * debug MathVector [] array = { new Vector3D(0,0,0), new
         * Vector3D(2,0,1), new Vector3D(0,2,2), new Vector3D(2,2,2), new
         * Vector3D(1,1,-1), }; SurfaceXYData3D data = new
         * SurfaceXYData3D(array);
         */
        String form = "cos(x)*cos(y)*exp(-(x*x+y*y)*0.1)";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDivision(40);
        data.setDataName(form);
        GradationPainter gp = new GradationPainter(data,
                new DefaultColorValueGenerator(64, ColorSet.rainbowIndex));
        context.addPlotter(gp);
        return getPlotComponent(context);
    }

    // gradation and mono contour
    static SwingPlotComponent get2DSurfaceTest3() {
        PlotModel2D context = new PlotModel2D();
        String form = "exp(-(x-2)*(x-2)-y*y)-exp(-(x+2)*(x+2)-y*y)";
        ScalarFunction func = ScalarFunctionClass.getFunction(form);
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDivision(40);
        data.setDataName(form);
        GradationPainter gp = new GradationPainter(data,
                new DefaultColorValueGenerator(16, ColorSet.rainbowIndex));
        // context.addPlotter(gp);
        ContourPainter cp = new ContourPainter(gp,
                new DefaultColorValueGenerator(16, new Color[] { Color.black }));
        context.addPlotter(cp);
        return getPlotComponent(context);
    }

    // shade and arrow + 3D
    static PlotComponent get2DSurfaceTest4() {
        PlotModel2D context = new PlotModel2D();
        final double d = 1;
        ScalarFunction func = new ScalarFunctionClass(2) {
            Vector2D[] pos = { new Vector2D(d, d), new Vector2D(-d, d),
                    new Vector2D(d, -d), new Vector2D(-d, -d), };

            double[] sign = { 1, -1, -1, 1 };

            public double f(MathVector arg) {
                double ret = 0;
                for (int i = 0; i < pos.length; i++) {
                    ret += 1.0 / (arg.sub(pos[i]).getLength()) * sign[i];
                }
                return ret;
            }
        };
        SurfaceFunctionData3D data = new SurfaceFunctionData3D(func);
        data.setDivision(30);
        data.setDataName("Quadrupole");
        VectorFunction vf = new Gradient(FunctionUtil.multiple(func, -1));
        VectorFunctionData vfd = new VectorFunctionData(vf);
        context.addPlotter(new GradationVectorPlotter2D(new ContourPainter(
                data, new DefaultColorValueGenerator(16,
                        new Color[] { Color.black })), vfd));

        PlotComponent component = getPlotComponent(context);

        // Second plot on 3D
        PlotModel3D context3D = new PlotModel3D();
        context3D.addPlotter(new SurfacePainter3D(data));

        PolygonPlotRenderingParam param = new PolygonPlotRenderingParam();
        param.drawLegend = false;
        param.drawAxisType = PolygonPlotRenderingParam.XYPLANE;
        param.outsideBorderColor = Color.black;
        PolygonPlotRenderer3D plotRenderer = new PolygonPlotRenderer3D(
                context3D, param);
        component.addRenderer(plotRenderer, new RealRange(0.6, 0.6, 0.3, 0.25));
        AbstractRenderer polygonRenderer = new ColorRenderer(plotRenderer
                .getSceneContext());
        polygonRenderer.setRendererComponent(plotRenderer);
        Camera camera = new DefaultCamera();
        plotRenderer.setCamera(camera);
        UGraph.setupCameraController(plotRenderer);
        return component;
    }

    static SwingPlotComponent getXYErrorTest() {
        PlotModel2D context = new PlotModel2D();
        double[] xs = { 0, 1, 3, 4, 6, 8, 10 };
        double[] ys = { 5, 6, 8, 10, 15, 16, 21 };
        double[] es = { 1, 1.5, 0.5, 1, 1.2, 1, 0.8 };
        XYErrorData2D data = new XYErrorData2D(xs, ys, es);
        data.setDataName("exp");
        context.addPlotter(new ErrorBarPlotter(new IconPlotter(data)));
        // fit
        LinearFit fit = new LinearFit(xs, ys, es);
        FunctionData2D fdata = new FunctionData2D(fit.getFunction());
        fdata.setDataName("Mean square fit");
        context.addPlotter(new LinePlotter(fdata));
        context
                .addObject(new TextObject("Fitting example",
                        new Vector2D(1, 15)));
        return getPlotComponent(context);
    }

    static SwingPlotComponent getXYErrorTest2() {
        PlotModel2D context = new PlotModel2D();
        double[] xs = { 0, 1, 3, 4, 6, 8, 10 };
        double[] ys = { 1, 2, 8, 10, 4, 1, 1 };
        double[] es = { 1, 1.5, 0.5, 1, 1.2, 1, 0.8 };
        XYErrorData2D data = new XYErrorData2D(xs, ys, es);
        data.setDataName("exp");
        context.addPlotter(new ErrorBarPlotter(new IconPlotter(data)));
        // NL fit
        String func = "a*exp(-b*(x+c)*(x+c))+d";
        String[] vars = { "x", "y", "a", "b", "c", "d" };
        VectorGD init = new VectorGD(new double[] { 10, 1, -4, 1 });
        MarquardtFit fit = new MarquardtFit(func, vars, xs, ys, init);
        ScalarFunction sf = ScalarFunctionClass.getFunction(func, vars);
        MathVector ans = fit.getAnswer();
        System.out.println("ans:" + ans);
        VectorGD vec = new VectorGD(new double[] { 0, 0, ans.v(0), ans.v(1),
                ans.v(2), ans.v(3) });
        FunctionData2D fdata = new FunctionData2D(new AWrapperFunction(sf, vec,
                0));
        fdata.setDataName("Non-linear fit");
        context.addPlotter(new LinePlotter(fdata));
        context
                .addObject(new TextObject("Fitting example", new Vector2D(1, 8)));
        return getPlotComponent(context);
    }

    static SwingPlotComponent getRegionTest() {
        PlotModel2D context = new PlotModel2D();
        context.setAutoScale(0, false);
        context.setAutoScale(1, false);
        context.setActiveRange(new RealRange(-3, -4, 6, 8));

        String form = "x*(x-2)*(x+2)";
        AFunction func = AFunctionClass.getFunction(form);
        PlotData d2 = new FunctionData2D(func);
        d2.setDataName(form);
        MaxPointPlotter mpp = new MaxPointPlotter(new LinePlotter(d2));
        mpp.setRegion(new RealRange(-2, 4));
        ValueObject vo = new ValueObject();
        vo.setSpotPosition(TextObject.LEFT, TextObject.BOTTOM);
        mpp.addObject(vo);
        MinPointPlotter mpp2 = new MinPointPlotter(mpp);
        mpp2.setRegion(new RealRange(-2, 4));
        vo = new ValueObject();
        vo.setSpotPosition(TextObject.LEFT, TextObject.BOTTOM);
        mpp2.addObject(vo);
        context.addPlotter(mpp2);

        /*
         * Condition regionLogic = ULogical.and (ULogical.greaterEqual
         * (FunctionUtil.variable(),FunctionUtil.constant(-2)),
         * ULogical.lessEqual
         * (FunctionUtil.variable(),FunctionUtil.constant(2)));
         */
        Condition regionLogic = ULogical
                .string2logic("[[-2<=x] & [x<=2]] & [ (x*(x-2)*(x+2)) >= y]");
        ScalarFunction regionFunc = ULogical.logic2func(regionLogic, 1);

        SurfaceFunctionData3D data = new SurfaceFunctionData3D(regionFunc);
        data.setDivision(80);
        data.setDataName("-2<x<2");
        GradationPainter gp = new GradationPainter(data,
                new DefaultColorValueGenerator(8, new Color[] { Color.white,
                        Color.cyan }));
        context.addPlotter(gp);

        return getPlotComponent(context);
    }

    static SwingPlotComponent getStrutsTest() {
        double[] data1 = { 0, 1, 4, 6, 7, 9 };
        double[] data2 = { 0, 4, 5, 6, 8, 9 };
        double[] data3 = { 9, 5, 4, 3, 2, 0 };
        StrutElement e1 = new StrutElement("Data1", data1);
        StrutElement e2 = new StrutElement("Data2", data2);
        StrutElement e3 = new StrutElement("Data3", data3);
        StrutsData data = new StrutsData(new StrutElement[] { e1, e2, e3 });
        VerticalStrutsPlotter pl = new VerticalStrutsPlotter(data);
        PlotModel2D context = new PlotModel2D();
        context.addPlotter(pl);
        context.getAxisX().setMainGridGenerator(data.getGridGenerator());
        return getPlotComponent(context);
    }

    static Vector2D[] data(double p) {
        return data(p, 7);
    }

    static Vector2D[] data(double p, int num) {
        AFunction f = new AFunction() {
            public double f(double x) {
                return Math.exp(-x * x) * x;
            }
        };
        double sx = -3, ex = 3;
        double dx = (ex - sx) / (num - 1);
        Vector2D[] ret = new Vector2D[num];
        for (int i = 0; i < num; i++) {
            ret[i] = new Vector2D(dx * i * p + sx, f.f(dx * i * p + sx));
        }
        return ret;
    }

    static Vector2D[] flatData() {
        Vector2D[] ret = { new Vector2D(0, 0), new Vector2D(1, 0),
                new Vector2D(2, 0), new Vector2D(3, 0), new Vector2D(4, 0), };
        return ret;
    }

}
