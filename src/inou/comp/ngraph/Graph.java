/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.comp.ColorSet;
import inou.comp.SwingUtils;
import inou.comp.ngraph.d2.ContourPainter;
import inou.comp.ngraph.d2.GradationVectorPlotter2D;
import inou.comp.ngraph.d2.IconPlotter;
import inou.comp.ngraph.d2.LinePlotter;
import inou.comp.ngraph.d2.ShadedSurfacePainter;
import inou.comp.ngraph.d2.VectorPlotter2D;
import inou.comp.ngraph.d3.GradationVectorPlotter3D;
import inou.comp.ngraph.d3.IsovalueSurfacePainter3D;
import inou.comp.ngraph.d3.SurfacePainter3D;
import inou.math.AArrayFunction;
import inou.math.AFunction;
import inou.math.AWrapperFunction;
import inou.math.FunctionUtil;
import inou.math.Gradient;
import inou.math.RealRange;
import inou.math.ScalarFunction;
import inou.math.ScalarFunctionClass;
import inou.math.VectorFunction;
import inou.math.vector.Vector1D;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * This class provides the service to show a graph easily.
 */

public class Graph {

    // ================================
    // ####( AFunction )###############
    // 2D graph
    // ================================

    /**
     * plot a function object.
     * 
     * @param in
     *            function object
     */
    public static PlotModel show(AFunction in) {
        AFunction[] arg = { in };
        return show("func", arg, "X axis", "Y axis");
    }

    /**
     * plot function objects.
     * 
     * @param in
     *            funtion objects
     */
    public static PlotModel show(AFunction[] in) {
        return show("function", in, "X axis", "Y axis");
    }

    /**
     * plot a function object.
     * 
     * @param title
     *            graph title
     * @param in
     *            function object
     * @param xlabel
     *            label of x-axis
     * @param ylabel
     *            label of y-axis
     */
    public static PlotModel show(String title, AFunction in, String xlabel,
            String ylabel) {
        AFunction[] arg = { in };
        return show(title, arg, xlabel, ylabel);
    }

    /**
     * plot a function object.
     * 
     * @param title
     *            graph title
     * @param in
     *            function objects
     * @param xlabel
     *            label of x-axis
     * @param ylabel
     *            label of y-axis
     */
    public static PlotModel show(String title, AFunction[] in, String xlabel,
            String ylabel) {
        PlotModel2D plotContext = new PlotModel2D();
        plotContext.getAxisX().setLabel(xlabel);
        plotContext.getAxisY().setLabel(ylabel);

        for (int i = 0; i < in.length; i++) {
            PlotData d = null;
            Plotter p = null;
            if (in[i] instanceof AArrayFunction) {
                double[][] ar = ((AArrayFunction) in[i]).getArrays();
                d = new XYData2D(ar[0], ar[1]);
                d.setDataName("sample" + (i + 1));
                p = new LinePlotter(new IconPlotter(d));
            } else {
                d = new FunctionData2D(in[i]);
                d.setDataName("sample" + (i + 1));
                p = new LinePlotter(d);
            }
            plotContext.addPlotter(p);
        }

        setPlotContext2D(plotContext, title);

        return plotContext;
    }

    // ================================
    // ####( ScalarFunciton )##########
    //
    // ================================

    /**
     * show a scalar function. (2d and 3d)
     * 
     * @param in
     *            function object
     */
    public static PlotModel show(ScalarFunction in) {
        return show(in, getDefaultRange(in.getDimension()));
    }

    /**
     * show a scalar function. (2d and 3d)
     * 
     * @param in
     *            function objects
     */
    public static PlotModel show(ScalarFunction[] in) {
        outer: while (true) {
            AFunction[] nin = new AFunction[in.length];
            for (int i = 0; i < in.length; i++) {
                if (in[i].getDimension() > 1)
                    break outer;
                nin[i] = new AWrapperFunction(in[i]);
            }
            return show(nin);
        }
        for (int i = 0; i < in.length; i++) {
            return show(in[i], getDefaultRange(in[i].getDimension()));
        }
        return null;
    }

    /**
     * show a scalar function. (2d and 3d)
     * 
     * @param in
     *            function object
     */
    public static PlotModel show(ScalarFunction in, RealRange range) {
        // single parameter?
        if (in.getDimension() == 1) {
            Vector1D arg = new Vector1D();
            return show(new AWrapperFunction(in, arg, 0));
        }
        // plain field
        if (in.getDimension() == 2) {
            return show2D(in, range, true, true, false);
        }
        // solid model
        if (in.getDimension() == 3) {
            show3D(in, range);
        }
        System.err.println("Not supported function (Dim:" + in.getDimension()
                + ")");
        return null;
    }

    /**
     * show a scalar function. (2d scalar function)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     * @param cont
     *            draw contour line
     * @param surf
     *            draw surface
     * @param grad
     *            draw gradient allow
     */
    protected static PlotModel show2D(ScalarFunction in, RealRange range,
            boolean cont, boolean surf, boolean grad) {
        show2DOn3D(in, range);

        PlotModel2D plotContext = new PlotModel2D();
        plotContext.getAxisX().setLabel("x");
        plotContext.getAxisY().setLabel("y");
        if (range != null) {
            plotContext.setAutoScale(0, false);
            plotContext.setAutoScale(1, false);
            plotContext.setActiveRange(range);
        }

        // SurfaceData (render);
        SurfaceFunctionData3D sdata = new SurfaceFunctionData3D(in);
        sdata.setDivision(40);
        sdata.setDataName("Surface");
        ShadedSurfacePainter surfacePainter = new ShadedSurfacePainter(sdata);
        // GradPlotter (grad)
        VectorFunction vf = new Gradient(FunctionUtil.multiple(in, -1));
        VectorFunctionData vdata = new VectorFunctionData(vf);
        VectorPlotter2D vectorPlotter = new VectorPlotter2D(sdata, vdata);
        // SurfaceData (contour)
        ContourPainter contourPlotter = new ContourPainter(sdata);

        if (surf)
            plotContext.addPlotter(surfacePainter);
        if (grad)
            plotContext.addPlotter(vectorPlotter);
        if (cont)
            plotContext.addPlotter(contourPlotter);

        setPlotContext2D(plotContext, "2D Scalar");
        return plotContext;
    }

    /**
     * show scalar function. (2d scalar function)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     */
    public static PlotModel3D show2DOn3D(ScalarFunction in, RealRange range) {
        if (in.getDimension() != 2)
            return null;

        PlotModel3D context = new PlotModel3D();

        SurfaceFunctionData3D sdata = new SurfaceFunctionData3D(in);
        sdata.setDivision(discreteNum);
        context.addPlotter(new SurfacePainter3D(sdata));
        if (range != null) {
            context.setAutoScale(0, false);
            context.setAutoScale(1, false);
            context.setActiveRange(range);
        }
        setPlotContext3D(context, "Surface");

        return context;
    }

    /**
     * show scalar function. (3d scalar function)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     */
    public static PlotModel3D show3D(ScalarFunction in, RealRange range) {
        if (in.getDimension() != 3)
            return null;

        PlotModel3D context = new PlotModel3D();

        VolumeFunctionData4D vdata = new VolumeFunctionData4D(in);
        vdata.setDivision(discreteNum / 2);

        PlotModel3D plotContext = new PlotModel3D();
        plotContext.setActiveRange(range);
        Plotter3D plotter = new IsovalueSurfacePainter3D(vdata,
                new DefaultColorValueGenerator(5, ColorSet.rainbowIndex));
        plotContext.addPlotter(plotter);

        setPlotContext3D(plotContext, "Iso-value");

        return plotContext;
    }

    // ================================
    // ####( VectorFunciton )##########
    //
    // ================================

    /**
     * show vector function. (2d and 3d)
     * 
     * @param in
     *            function object
     */
    public static PlotModel show(VectorFunction in) {
        return show(in, getDefaultRange(in.getDimension()));
    }

    /**
     * show vector function. (2d and 3d)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     */
    public static PlotModel show(VectorFunction in, RealRange range) {
        if (in.getDimension() == 2 && in.getArgDimension() == 2) {
            return show2d(in, range);
        }
        if (in.getDimension() == 3 && in.getArgDimension() == 3) {
            show3d(in, range);
            return null;
        }
        System.err.println("Sorry, given function isn't supported.");
        return null;
    }

    /**
     * show vector function. (2d vector function)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     */
    private static PlotModel show2d(VectorFunction in, RealRange range) {
        PlotModel2D plotContext = new PlotModel2D();
        plotContext.setAutoScale(0, false);
        plotContext.setAutoScale(1, false);
        plotContext.setActiveRange(range);

        VectorFunctionData vdata = new VectorFunctionData(in);

        plotContext.addPlotter(new GradationVectorPlotter2D(vdata));

        setPlotContext2D(plotContext, "2D Vector");

        return plotContext;
    }

    /**
     * show scalar function. (3d scalar function)
     * 
     * @param in
     *            function object
     * @param range
     *            valid range
     */
    public static void show3d(VectorFunction in, RealRange range) {
        PlotModel3D plotContext = new PlotModel3D();
        plotContext.setAutoScale(0, false);
        plotContext.setAutoScale(1, false);
        plotContext.setAutoScale(2, false);
        plotContext.setActiveRange(range);

        VectorFunctionData vfd = new VectorFunctionData(in);

        GradationVectorPlotter3D plotter = new GradationVectorPlotter3D(vfd);
        if (plotter.getData() instanceof VolumeFunctionData4D) {
            ((VolumeFunctionData4D) plotter.getData())
                    .setDivision(discreteNum / 2);
        }

        plotContext.addPlotter(plotter);

        PlotComponent pc = UGraph.setup3DRenderer(plotContext);
        JTabbedPane panel = getTab();
        panel.addTab("3D Vector", pc.getComponent());
        frame.pack();
    }

    /** remove all pages */
    public static void clear() {
        if (tabs != null) {
            tabs.removeAll();
        }
    }

    public static int discreteNum = 25;

    // ===========================
    // private area
    // ===========================

    private static JFrame frame;

    private static JTabbedPane tabs = null;

    private static void setPlotContext3D(PlotModel3D plotContext, String title) {
        PlotComponent pc = UGraph.setup3DRenderer(plotContext);
        JTabbedPane panel = getTab();
        panel.addTab(title, pc.getComponent());
        frame.pack();
    }

    private static void setPlotContext2D(PlotModel2D plotContext, String title) {
        PlotRenderer renderer = new SquarePlotRenderer2D(plotContext);
        AWTPlotComponent comp = new AWTPlotComponent(500, 400);
        comp.addRenderer(renderer);

        JTabbedPane panel = getTab();
        panel.addTab(title, comp);
        frame.pack();
    }

    private static JTabbedPane getTab() {
        if (tabs == null) {
            tabs = new JTabbedPane();
            frame = SwingUtils.getTestFrame("Graph");
            frame.getContentPane().add(tabs);
            frame.setSize(500, 400);
            frame.show();
        }
        return tabs;
    }

    /** return default range with given dimension. */
    public static RealRange getDefaultRange(int d) {
        d++;
        RealRange r = new RealRange(d);
        for (int i = 0; i < d; i++) {
            r.pos(i, -5);
            r.size(i, 10);
        }
        return r;
    }

    public static void main(String[] args) {
        String func = null;
        if (args.length == 0) {
            func = "exp(-0.1*(x*x+y*y))*cos(x*x+y*y)";
        } else {
            func = args[0];
        }
        show(ScalarFunctionClass.getFunction(func));
    }

}
