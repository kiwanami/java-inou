/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

import java.util.ArrayList;

/**
 * MCSession is a server to provide the numerical Monte Carlo method. Sample
 * usage is shown at "static main" method.
 * 
 */

public class MCSession {

    protected RandomMaker maker;

    protected Acceptor acceptor;

    protected ArrayList eventList = new ArrayList();

    protected MCEvent[] events = new MCEvent[0];

    protected boolean green = true;// true:go false:stop

    protected RandomData lastData;

    protected long finish = 1000000;

    protected long currentStep = 0;

    // ===========================
    // Constructor
    // ===========================

    public MCSession(RandomMaker maker) {
        this(maker, new StepAcceptor());
    }

    public MCSession(RandomMaker maker, Acceptor ac) {
        this.maker = maker;
        this.acceptor = ac;
    }

    // ===========================
    // accessor
    // ===========================

    public void setLimit(long k) {
        finish = k;
    }

    public long getLimit() {
        return finish;
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(Acceptor ac) {
        acceptor = ac;
    }

    public RandomData getLastData() {
        return lastData;
    }

    public RandomMaker getRandomMaker() {
        return maker;
    }

    // ===========================
    // operation
    // ===========================

    public void addEvent(MCEvent e) {
        e.setSession(this);
        eventList.add(e);
        updateEventList();
    }

    public MCEvent[] getEvents() {
        return events;
    }

    public void removeEvent(MCEvent e) {
        eventList.remove(e);
        updateEventList();
    }

    public void removeAllEvents() {
        eventList.clear();
        updateEventList();
    }

    public void stop() {
        green = false;
    }

    public void start() {
        if (lastData == null) {
            throw new InternalError("MCSession haven't been initialized.");
        }
        green = true;
        while (green) {
            step();
        }
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public void setData(RandomData data) {
        lastData = data;
    }

    public void init(RandomData init) {
        setData(init);
        stop();
    }

    public void step() {
        RandomData data = maker.makeRandom(lastData);
        double f = data.evaluate();
        boolean su = acceptor.accept(f);
        if (su) {
            data.onAccepted();
            lastData = data;
        } else {
            data.onFailed();
        }
        eventDrive(lastData, su);

        if (finish < 0)
            return;
        currentStep++;
        if (currentStep >= finish) {
            stop();
        }
    }

    // ===========================
    // private area
    // ===========================

    private void updateEventList() {
        events = new MCEvent[eventList.size()];
        for (int i = 0; i < events.length; i++) {
            events[i] = (MCEvent) eventList.get(i);
        }
    }

    private void eventDrive(RandomData d, boolean s) {
        for (int i = 0; i < events.length; i++) {
            events[i].event(d, s);
        }
    }

    // ===========================
    // test mathod area
    // ===========================

    public static void main(String[] args) {
        System.out.println("Exact ln(x)=10: x=" + Math.exp(10));
        // MCSession ses = new MCSession(new TestMaker());
        MCSession ses = new MCSession(new TestMaker(), new MetropolisAcceptor(
                0.0000001));
        ses.addEvent(new TestEvent());
        ses.setLimit(3000000);
        ses.init(new TestData(100));
        ses.start();
    }

    // test method and class
    static class TestData extends RandomDataClass {
        double x;

        TestData(double it) {
            x = it;
        }

        public RandomData getCopy() {
            return new TestData(x);
        }

        public double evaluate() {
            if (x <= 0)
                return 1e60;
            double t = Math.log(x) - 10;
            return t * t;
        }
    };

    static class TestMaker implements RandomMaker {
        double width = 10;

        public RandomData makeRandom(RandomData lastData) {
            double ker = ((TestData) lastData).x;
            return new TestData(ker + (0.5 - Math.random())
                    * Math.sqrt(lastData.evaluate()) * width);
        }
    };

    static class TestEvent extends MCEvent {
        long count = 0;

        long step = 10000;

        public void event(RandomData data, boolean su) {
            count++;
            if (count >= step) {
                System.out.println(((TestData) data).x
                        + "  |  current:"
                        + ((MetropolisAcceptor) getSession().getAcceptor())
                                .getCurrent());
                count = 0;
            }
        }
    };

}
