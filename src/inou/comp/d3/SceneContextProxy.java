/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

/**
 * This class delegates SceneContext object.
 */

public class SceneContextProxy extends SceneContext {

    private SceneContext context;

    public SceneContextProxy() {
    }

    public SceneContextProxy(SceneContext context) {
        this.context = context;
    }

    public void setSceneContext(SceneContext context) {
        this.context = context;
    }

    public SceneContext getSceneContext() {
        return context;
    }

    // =============================

    public void setAutoUpdate(boolean b) {
        context.setAutoUpdate(b);
    }

    public void addObject(IGeometricObject obj) {
        context.addObject(obj);
    }

    public IGeometricObject[] getObjects() {
        return context.getObjects();
    }

    public void addLight(Light light) {
        context.addLight(light);
    }

    public Light[] getLights() {
        return context.getLights();
    }

    public void addRenderer(Renderer rd) {
        context.addRenderer(rd);
    }

    public Renderer[] getRenderers() {
        return context.getRenderers();
    }

    public void updateObjects() {
        context.updateObjects();
    }

    public void updateConfigration() {
        context.updateConfigration();
    }
}