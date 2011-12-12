/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

import java.util.ArrayList;

/** manage the sessions with replica exchange method. */
public class REManager {

    private RESession[] sessions;

    private RESessionFunction evalFunction;

    private Acceptor[] acceptors;

    private ArrayList reEventList = new ArrayList();

    private REEvent[] reEvents;

    private ArrayList exEventList = new ArrayList();

    private REExchangeEvent[] exEvents;

    private long exchangeStep = 0;

    private long currentStep = 0;

    private boolean debug = false;

    public REManager(RESession[] sessions) {
        this(sessions, new REEnergySessionFunction(),
                new Acceptor[] { new DExpAcceptor() });
    }

    public REManager(RESession[] sessions, RESessionFunction evalFunction,
            Acceptor acceptor) {
        this(sessions, evalFunction, new Acceptor[] { acceptor });
    }

    /**
     * @param sessions
     *            session array (n)
     * @param evalFunctions
     *            evalution functions between sessions (n-1)
     * @param acceptors
     *            acceptors between sessions (n-1)
     */
    public REManager(RESession[] sessions, RESessionFunction evalFunction,
            Acceptor[] acceptors) {
        this.sessions = sessions;
        this.evalFunction = evalFunction;
        if (acceptors.length == 1 && sessions.length > 2) {
            this.acceptors = new Acceptor[sessions.length - 1];
            try {
                for (int i = 0; i < this.acceptors.length; i++) {
                    this.acceptors[i] = (Acceptor) acceptors[0].getClass()
                            .newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Wrong acceptor class");
            }
        } else {
            this.acceptors = acceptors;
        }
    }

    public void setAcceptor(int index, Acceptor a) {
        acceptors[index] = a;
    }

    /**
     * make sessions.
     * 
     * @param rndMaker
     *            random maker object
     * @param num
     *            number of sessions
     * @param maxTemp
     *            max temperature (scaled by k_B)
     * @param minTemp
     *            min temperature (scaled by k_B)
     */
    public static REManager makeMCSession(RandomMaker rndMaker, int num,
            double maxTemp, double minTemp) {
        RESession[] ses = new RESession[num];
        for (int i = 0; i < num; i++) {
            Acceptor ac = new MetropolisAcceptor(1.);
            ses[i] = new REMCSessionHolder(new MCSession(rndMaker, ac));
        }
        distributeInitialTemperature(ses, maxTemp, minTemp);
        REManager man = new REManager(ses);
        return man;
    }

    public static void distributeInitialTemperature(RESession[] sessions,
            double maxTemp, double minTemp) {
        int num = sessions.length;
        double maxBeta = 1. / maxTemp;
        double minBeta = 1. / minTemp;
        double dBeta = (minBeta - maxBeta) / (num - 1);
        for (int i = 0; i < num; i++) {
            double beta = minBeta - dBeta * i;
            sessions[i].setTemperature(1. / beta);
        }
    }

    public static void distributeLogInitialTemperature(RESession[] sessions,
            double maxTemp, double minTemp) {
        int num = sessions.length;
        double maxLT = Math.log(maxTemp);
        double minLT = Math.log(minTemp);
        double dBeta = (minLT - maxLT) / (num - 1);
        for (int i = 0; i < num; i++) {
            double beta = minLT - dBeta * i;
            sessions[i].setTemperature(Math.exp(beta));
        }
    }

    public static void distributeLinearInitialTemperature(RESession[] sessions,
            double maxTemp, double minTemp) {
        int num = sessions.length;
        double dt = (maxTemp - minTemp) / (num - 1);
        for (int i = 0; i < num; i++) {
            double t = minTemp + dt * i;
            sessions[i].setTemperature(t);
        }
    }

    /**
     * make sessions.
     * 
     * @param sessions
     *            some simulation sessions
     * @param maxTemp
     *            max temperature (scaled by k_B)
     * @param minTemp
     *            min temperature (scaled by k_B)
     */
    public static REManager makeSession(RESession[] sessions, double maxTemp,
            double minTemp) {
        double maxBeta = 1. / maxTemp;
        double minBeta = 1. / minTemp;
        double dBeta = (minBeta - maxBeta) / (sessions.length - 1);
        for (int i = 0; i < sessions.length; i++) {
            double beta = minBeta - dBeta * i;
            sessions[i].setTemperature(1. / beta);
        }
        REManager man = new REManager(sessions, new REEnergySessionFunction(),
                new DExpAcceptor());
        return man;
    }

    public void setDebug(boolean t) {
        debug = t;
    }

    public void simulationTempering(long averageTimes, long iterationTimes) {
        final long oneExamCount = averageTimes * getExchangeStep();
        final double maverageTimes = averageTimes;
        final int[] jumpCount = new int[sessions.length - 1];
        final double[] jumpRatio = new double[sessions.length - 1];
        REExchangeEvent jumpEvent = new REExchangeEvent() {
            public void event(int lowerIndex) {
                jumpCount[lowerIndex]++;
            }
        };
        addExchangeEvent(jumpEvent);
        REEvent tempCorrector = new REEvent() {
            int count = 0;

            double[] oldBeta = new double[sessions.length];

            double[] newBeta = new double[sessions.length];

            public void event() {
                count++;
                if (count == oneExamCount) {
                    if (debug) {
                        System.out.println("==== Jump Ratio");
                    }
                    count = 0;
                    double ave = 0;
                    for (int i = 0; i < sessions.length; i++) {
                        newBeta[sessions.length - 1 - i] = 1. / sessions[i]
                                .getTemperature();
                        oldBeta[sessions.length - 1 - i] = 1. / sessions[i]
                                .getTemperature();
                    }
                    for (int i = 0; i < jumpRatio.length; i++) {
                        int idx = jumpRatio.length - 1 - i;
                        jumpRatio[i] = jumpCount[idx] / maverageTimes;
                        if (jumpRatio[i] == 1) {
                            jumpRatio[i] = 0.999;
                        } else if (jumpRatio[i] == 0) {
                            jumpRatio[i] = 0.001;
                        }
                        ave += jumpRatio[i];
                        if (debug) {
                            System.out
                                    .println("Ratio[" + i + "] : JR="
                                            + jumpRatio[i] + " ("
                                            + jumpCount[idx] + ") : T="
                                            + sessions[idx].getTemperature());
                        }
                        jumpCount[idx] = 0;
                    }
                    ave /= jumpRatio.length;
                    for (int i = 1; i < sessions.length; i++) {
                        newBeta[i] = newBeta[i - 1]
                                + (oldBeta[i] - oldBeta[i - 1])
                                * jumpRatio[i - 1] / ave;
                    }
                    for (int i = 0; i < sessions.length; i++) {
                        sessions[i].setTemperature(1. / newBeta[sessions.length
                                - 1 - i]);
                    }
                }
            }
        };
        addEvent(tempCorrector);
        long oldLimit = getLimit();
        setLimit(iterationTimes * oneExamCount);
        start();
        //
        if (debug) {
            System.out.println("=== Jump Ratio");
            for (int i = 0; i < jumpRatio.length; i++) {
                System.out.println("Ratio[" + i + "] : " + jumpRatio[i]
                        + "  (T=" + sessions[i].getTemperature() + ")");
            }
            System.out.println("Last (T="
                    + sessions[sessions.length - 1].getTemperature() + ")");
        }
        //
        removeEvent(tempCorrector);
        removeExchangeEvent(jumpEvent);
        setLimit(oldLimit);
    }

    public String getSessionInfo() {
        StringBuffer sb = new StringBuffer();
        RESession[] sessions = getSessions();
        for (int i = 0; i < sessions.length; i++) {
            sb.append("[" + i + "] : ");
            sb.append("temp=" + sessions[i].getTemperature() + " : ");
            sb.append(" == " + sessions[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private long limit = -1;

    /** set running time limit (default:-1, no limit) */
    public void setLimit(long l) {
        limit = l;
    }

    /** get running time limit */
    public long getLimit() {
        return limit;
    }

    /** set exchange step (default 4) */
    public void setExchangeStep(long l) {
        exchangeStep = l;
    }

    /** get exchange step */
    public long getExchangeStep() {
        return exchangeStep;
    }

    /** return session array */
    public RESession[] getSessions() {
        return sessions;
    }

    public void addEvent(REEvent e) {
        e.setManager(this);
        reEventList.add(e);
        eventModified = true;
    }

    public void removeEvent(REEvent e) {
        reEventList.remove(e);
        eventModified = true;
    }

    public void addExchangeEvent(REExchangeEvent e) {
        e.setManager(this);
        exEventList.add(e);
        eventModified = true;
    }

    public void removeExchangeEvent(REExchangeEvent e) {
        exEventList.remove(e);
        eventModified = true;
    }

    private boolean eventModified = false;

    private void prepareEvents() {
        reEvents = new REEvent[reEventList.size()];
        for (int i = 0; i < reEvents.length; i++) {
            reEvents[i] = (REEvent) reEventList.get(i);
        }
        exEvents = new REExchangeEvent[exEventList.size()];
        for (int i = 0; i < exEvents.length; i++) {
            exEvents[i] = (REExchangeEvent) exEventList.get(i);
        }
        eventModified = false;
    }

    private boolean runningSignal = true;

    /** start system */
    public void start() {
        runningSignal = true;
        currentStep = 0;
        resume();
    }

    public void resume() {
        runningSignal = true;
        while (runningSignal) {
            step();
        }
    }

    protected void step() {
        currentStep++;
        if (eventModified) {
            prepareEvents();
        }
        for (int i = 0; i < sessions.length; i++) {
            sessions[i].step();
        }
        for (int i = 0; i < reEvents.length; i++) {
            reEvents[i].event();
        }

        if ((currentStep % exchangeStep) == 0) {
            exchange();
        }
        if (limit > 0 && limit <= currentStep) {
            stop();
        }
    }

    public long getCurrentStep() {
        return currentStep;
    }

    /** stop system */
    public void stop() {
        runningSignal = false;
    }

    protected void exchange() {
        for (int i = (sessions.length - 2); i >= 0; i--) {
            RESession low = sessions[i];
            RESession high = sessions[i + 1];
            double df = evalFunction.evaluate(low, high);
            if (acceptors[i].accept(df)) {
                if (debug) {
                    System.out.println("Exchange (df= " + df + "): " + i
                            + "(EL(" + sessions[i].getTemperature() + ")= "
                            + ((REEnergySession) low).getEnergy() + ") - "
                            + (i + 1) + "(EH("
                            + sessions[i + 1].getTemperature() + ")= "
                            + ((REEnergySession) high).getEnergy() + ")");
                }
                low.exchange(high);

                for (int j = 0; j < exEvents.length; j++) {
                    exEvents[j].event(i);
                }
            }
        }
    }

    // ===========================
    // test mathod area
    // ===========================

    public static void main(String[] args) {
        System.out.println("Exact f(x,y)=0: x=0, y=0");
        System.out.println("  f(x,y)=0.001*(x*x+y*y)-cos(2*x)-cos(2*y)+2");
        System.out.println("  x0=8, y0=8");
        /*
         * make sessions. @param rndMaker random maker object @param num number
         * of sessions @param maxTemp max temperature (scaled by k_B) @param
         * minTemp min temperature (scaled by k_B)
         */
        int unit = 16;
        REManager ses = REManager
                .makeMCSession(new TestMaker(), unit, 5, 0.001);
        long exchange = 40;
        long average = 40;
        long iter = 80;
        ses.setExchangeStep(exchange);
        REMCSessionHolder.initSessionData(ses, new TestData(9, 9));
        ses.simulationTempering(average, iter);
        System.out.println(ses.getSessionInfo());

        MinEvent mine = new MinEvent();
        ses.addEvent(mine);
        ses.setLimit(50000);
        REMCSessionHolder.initSessionData(ses, new TestData(9, 9));
        REEnergyMonitor em = new REEnergyMonitor(ses);
        ses.addEvent(em);
        em.setSkipCount(exchange / 2);
        em.showFrame();
        ses.start();
        System.out.println(ses.getSessionInfo());
        System.out.println(mine.min);
    }

    // test method and class
    static class TestData extends RandomDataClass {
        double x, y;

        TestData(double ix, double iy) {
            x = ix;
            y = iy;
        }

        public RandomData getCopy() {
            return new TestData(x, y);
        }

        public double evaluate() {
            return (0.001 * (x * x + y * y) + (-Math.cos(x * 2)
                    - Math.cos(y * 2) + 2) * 1);
        }

        public String toString() {
            return x + "  " + y + "  " + evaluate();
        }
    };

    static class TestMaker implements RandomMaker {
        double width = 5;

        public RandomData makeRandom(RandomData lastData) {
            TestData td = (TestData) lastData;
            double nx = td.x + (0.5 - Math.random())
                    * Math.sqrt(lastData.evaluate()) * width;
            double ny = td.y + (0.5 - Math.random())
                    * Math.sqrt(lastData.evaluate()) * width;
            if (nx > 10)
                nx = nx - 20;
            if (nx < -10)
                nx = nx + 20;
            if (ny > 10)
                ny = ny - 20;
            if (ny < -10)
                ny = ny + 20;
            return new TestData(nx, ny);
        }
    };

    static class MinEvent extends REEvent {
        TestData min;

        public void event() {
            REMCSessionHolder holder = (REMCSessionHolder) getManager()
                    .getSessions()[0];
            MCSession ses = holder.getSession();
            TestData td = (TestData) ses.getLastData();
            if (min == null) {
                min = (TestData) td.getCopy();
            } else {
                if (min.evaluate() > td.evaluate()) {
                    min = (TestData) td.getCopy();
                }
            }
        }
    };
}
