/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.util.ArrayList;

/**
 * This object manages objects, lights and renderers. The system size is assumed
 * to be about 10...100. You should scale your object size and position to fit
 * this 3D world.
 */
public class SceneContext {

    private ArrayList objectsList = new ArrayList();

    private IGeometricObject[] objects = new IGeometricObject[0];

    private ArrayList lightsList = new ArrayList();

    private Light[] lights = new Light[0];

    private ArrayList renderersList = new ArrayList();

    private Renderer[] renderers = new Renderer[0];

    private boolean autoUpdate = true;

    public SceneContext() {
        this(true);
    }

    /**
     * @param autoUpdate
     *            if true, this object notifies all renderers to update object
     *            cache each time when adding or removing an object in this
     *            scene object. Generally, the calculation cost of refresh the
     *            object array increases with number of objects linearly. If you
     *            have small number of objects, "true" is convenient. However
     *            you have large number of objects, such as more than 1000
     *            objects, you should turn autoUpdateswitch off and update
     *            caches of renderers manually calling updateObjects().
     */
    public SceneContext(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public void setAutoUpdate(boolean b) {
        autoUpdate = b;
    }

    public void addObject(IGeometricObject obj) {
        if (obj == null || objectsList.contains(obj))
            return;
        objectsList.add(obj);
        objects = null;
        onModified();
    }

    public void removeObject(IGeometricObject obj) {
        if (obj == null || objectsList.contains(obj))
            return;
        objectsList.remove(obj);
        objects = null;
        onModified();
    }

    public IGeometricObject[] getObjects() {
        if (objects == null) {
            objects = new IGeometricObject[objectsList.size()];
            for (int i = 0; i < objects.length; i++) {
                objects[i] = (IGeometricObject) objectsList.get(i);
            }
        }
        return objects;
    }

    public void removeAllObjects() {
        objectsList.clear();
        onModified();
    }

    public void addLight(Light light) {
        if (light == null || lightsList.contains(light))
            return;
        lightsList.add(light);
        lights = null;
        onModified();
    }

    public Light[] getLights() {
        if (lights == null) {
            lights = new Light[lightsList.size()];
            for (int i = 0; i < lights.length; i++) {
                lights[i] = (Light) lightsList.get(i);
            }
        }
        return lights;
    }

    public void removeAllLights() {
        lightsList.clear();
        onModified();
    }

    public void addRenderer(Renderer rd) {
        if (rd == null || renderersList.contains(rd))
            return;
        renderersList.add(rd);
        renderers = null;
        if (autoUpdate)
            updateObjects();
    }

    public void removeRenderer(Renderer r) {
        if (r == null)
            return;
        // System.out.println("# remove :"+r);
        renderersList.remove(r);
        // System.out.println("# renders :"+renderersList.size());
        renderers = null;
        if (autoUpdate)
            updateObjects();
    }

    public Renderer[] getRenderers() {
        if (renderers == null) {
            renderers = new Renderer[renderersList.size()];
            for (int i = 0; i < renderers.length; i++) {
                renderers[i] = (Renderer) renderersList.get(i);
            }
        }
        return renderers;
    }

    public void removeAllRenderers() {
        renderersList.clear();
    }

    private void onModified() {
        if (autoUpdate)
            updateObjects();
    }

    /**
     * If number of objects changed by adding or removing an object, this class
     * calls all renderers to notify the modified scene objects. If you turn
     * autoUpdate switch off, you must call this method manually after
     * modification of objects number.
     */
    public void updateObjects() {
        Renderer[] rds = getRenderers();
        for (int i = 0; i < rds.length; i++) {
            rds[i].updateObjects();
        }
    }

    /**
     * If configration of objects or some components modified, you call this
     * method to repaint rendering components.
     */
    public void updateConfigration() {
        Renderer[] rds = getRenderers();
        for (int i = 0; i < rds.length; i++) {
            rds[i].updateConfigration();
        }
    }
}
