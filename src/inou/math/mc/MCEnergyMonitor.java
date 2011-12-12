/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

import inou.comp.SwingUtils;
import inou.comp.ngraph.AWTPlotComponent;
import inou.comp.ngraph.AdditiveData2D;
import inou.comp.ngraph.PlotComponent;
import inou.comp.ngraph.PlotModel2D;
import inou.comp.ngraph.SquarePlotRenderer2D;
import inou.comp.ngraph.SquarePlotRenderingParam;
import inou.comp.ngraph.d2.LinePlotter;

import java.awt.Component;

import javax.swing.JFrame;

public class MCEnergyMonitor extends MCEvent {

    private Component component;

    private PlotModel2D plotContext;

    private AdditiveData2D plotData;

    private long currentStep = 0;

    private long skipCount = 1;

    public MCEnergyMonitor() {
        this(new AWTPlotComponent(400, 300));
    }

    public MCEnergyMonitor(PlotComponent pc) {
        initComponent(pc);
    }

    public void showFrame() {
        JFrame f = SwingUtils.getTestFrame("Monte Carlo Energy Monitor");
        f.getContentPane().add(getComponent());
        f.setSize(500, 500);
        f.show();
    }

    public Component getComponent() {
        return component;
    }

    public long getSkipCount() {
        return skipCount;
    }

    public PlotModel2D getPlotContext() {
        return plotContext;
    }

    /**
     * if ( currentStep % skipCount ) == 0 ), observation is executed.
     */
    public void setSkipCount(long sk) {
        if (sk < 1)
            sk = 1;
        skipCount = sk;
    }

    private void initComponent(PlotComponent pc) {
        plotContext = new PlotModel2D();
        plotData = new AdditiveData2D(200);
        plotData.setDataName("Energy");
        plotContext.addPlotter(new LinePlotter(plotData));

        SquarePlotRenderingParam param = new SquarePlotRenderingParam();
        param.legendVerticalPosition = SquarePlotRenderingParam.TOP;
        param.legendHorizontalPosition = SquarePlotRenderingParam.LEFT;
        SquarePlotRenderer2D renderer = new SquarePlotRenderer2D(plotContext,
                param);
        pc.addRenderer(renderer);
        component = pc.getComponent();
    }

    public void event(RandomData d, boolean s) {
        if ((currentStep % skipCount) == 0) {
            plotData.add(currentStep, d.evaluate());
            plotContext.updatePlotter();
        }
        currentStep++;
    }
}
