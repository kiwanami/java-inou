/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

public class REMCSessionHolder implements REEnergySession {

    private MCSession session;

    public REMCSessionHolder(MCSession session) {
        if (!(session.getAcceptor() instanceof TemperatureAcceptor)) {
            throw new IllegalArgumentException(
                    "The Acceptor of the MCSession must be an implementation of TemperatureAcceptor.");
        }
        this.session = session;
    }

    public MCSession getSession() {
        return session;
    }

    public void step() {
        session.step();
    }

    public double getTemperature() {
        return ((TemperatureAcceptor) session.getAcceptor()).getTemperature();
    }

    public void setTemperature(double t) {
        ((TemperatureAcceptor) session.getAcceptor()).setTemperature(t);
    }

    public void exchange(RESession target) {
        if (target instanceof REMCSessionHolder) {
            REMCSessionHolder tholder = (REMCSessionHolder) target;
            RandomData d = session.getLastData();
            session.setData(tholder.getSession().getLastData());
            tholder.getSession().setData(d);
            tholder.getSession().getAcceptor().reset();
            getSession().getAcceptor().reset();
        } else {
            throw new InternalError("RESession must be "
                    + this.getClass().getName() + ".");
        }
    }

    public double getEnergy() {
        return session.getLastData().evaluate();
    }

    public String toString() {
        return "MCSession : E=" + getEnergy() + "  [Data:"
                + session.getLastData() + "]";
    }

    public static void initSessionData(REManager manager, RandomData data) {
        RESession[] sessions = manager.getSessions();
        for (int i = 0; i < sessions.length; i++) {
            REMCSessionHolder holder = (REMCSessionHolder) sessions[i];
            holder.getSession().init(data.getCopy());
        }
    }
}