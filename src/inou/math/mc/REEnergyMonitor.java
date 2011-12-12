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
import inou.comp.ngraph.UPlotData;
import inou.comp.ngraph.d2.LinePlotter;

import java.awt.Component;

import javax.swing.JFrame;

public class REEnergyMonitor extends REEvent {

    private Component component;

    private PlotModel2D plotContext;

    private AdditiveData2D[] plotDatas;

    private REEnergySession[] sessions;

    private long currentStep = 0;

    private long skipCount = 1;

    public REEnergyMonitor(REManager manager) {
        this(manager, new AWTPlotComponent(400, 300));
    }

    public REEnergyMonitor(REManager manager, PlotComponent pc) {
        initSessions(manager);
        component = initComponent(pc);
    }

    public void showFrame() {
        JFrame f = SwingUtils.getTestFrame("Replica Energy Monitor");
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

    private void initSessions(REManager manager) {
        RESession[] ss = manager.getSessions();
        sessions = new REEnergySession[ss.length];
        for (int i = 0; i < ss.length; i++) {
            sessions[i] = (REEnergySession) ss[i];
        }
    }

    private Component initComponent(PlotComponent pc) {
        plotContext = new PlotModel2D();
        plotDatas = new AdditiveData2D[sessions.length];
        for (int i = 0; i < sessions.length; i++) {
            plotDatas[i] = new AdditiveData2D();
            plotDatas[i].setDataName("T="
                    + UPlotData.defaultNumberFormat(sessions[i]
                            .getTemperature()));
            plotContext.addPlotter(new LinePlotter(plotDatas[i]));
        }

        SquarePlotRenderingParam param = new SquarePlotRenderingParam();
        param.legendVerticalPosition = SquarePlotRenderingParam.TOP;
        param.legendHorizontalPosition = SquarePlotRenderingParam.LEFT;
        SquarePlotRenderer2D renderer = new SquarePlotRenderer2D(plotContext,
                param);
        pc.addRenderer(renderer);
        return pc.getComponent();
    }

    public void event() {
        if ((currentStep % skipCount) == 0) {
            for (int i = 0; i < sessions.length; i++) {
                plotDatas[i].add(currentStep, sessions[i].getEnergy());
            }
            plotContext.updatePlotter();
        }
        currentStep++;
    }
}
