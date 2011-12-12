/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.d3.GSurface;
import inou.comp.d3.LinePair;
import inou.comp.d3.PolygonObject;
import inou.comp.d3.Surface;
import inou.comp.ngraph.ColorValueGenerator;
import inou.comp.ngraph.ColorValueInfo;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.RealPolygon;
import inou.comp.ngraph.RenderingInfo3D;
import inou.comp.ngraph.SurfaceData3D;
import inou.comp.ngraph.UGraph;
import inou.comp.ngraph.UPlotData;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/** plot gradation on 3D graph */

public class GradationSurfacePainter3D extends Plotter3D {

    private ColorValueGenerator colorValueGenerator = null;

    private double limit = 1e-20;

    public GradationSurfacePainter3D(SurfaceData3D p) {
        super(p);
    }

    public GradationSurfacePainter3D(SurfaceData3D p, ColorValueGenerator cvg) {
        super(p);
        setColorValueGenerator(cvg);
    }

    private SurfaceData3D getSurfaceData() {
        return (SurfaceData3D) getData();
    }

    public void setColorValueGenerator(ColorValueGenerator cvg) {
        colorValueGenerator = cvg;
    }

    /**
     * Drawing contour limit. If the limit is larger than height between
     * contours to draw, this painter gives up drawing. (Default : 1e-20)
     */
    public void setLimit(double s) {
        limit = s;
    }

    protected void draw3D(RenderingInfo3D info, MathVector[] vertices) {
        initScene(info, vertices);
    }

    protected void drawLegend(Graphics g, Rectangle r) {
        ColorValueInfo[] colors = getColorValueInfo(0, 1);
        double delta = ((double) colors.length) / r.height;
        for (int i = 0; i < r.height; i++) {
            g
                    .setColor(colors[colors.length - 1 - (int) (delta * i)]
                            .getColor());
            g.drawLine(r.x, r.y + i, r.x + r.width, r.y + i);
        }
    }

    private ColorValueInfo[] getColorValueInfo(double start, double width) {
        return UGraph.getColorValueInfo(colorValueGenerator, 16, start, width);
    }

    // =================================
    // prepare polygons
    // =================================

    private void initScene(RenderingInfo3D info, MathVector[] dataArray) {
        ColorValueInfo[] colorValues = makeColorIndex(dataArray);
        if (colorValues == null)
            return;

        makePolygonParts(info, dataArray, colorValues);
        assembleParts(info);
    }

    private ColorValueInfo[] makeColorIndex(MathVector[] dataArray) {
        RealRange range = UPlotData.getPartialRange(2, dataArray);
        if (range.width() < limit) {
            System.err.println("too small height : " + getClass().getName());
            return null;
        }
        ColorValueInfo[] colorValues = getColorValueInfo(range.x(), range
                .width());
        if (colorValues.length < 2) {
            System.err.println("too few colors : " + getClass().getName());
            return null;
        }
        return colorValues;
    }

    private void makePolygonParts(RenderingInfo3D info, MathVector[] dataArray,
            ColorValueInfo[] colorValues) {
        initVertexList(info, dataArray.length, colorValues.length);
        surfaceList.clear();
        lineList.clear();

        RealPolygon[] polygons = getSurfaceData().getPolygons(dataArray);
        for (int i = 0; i < polygons.length; i++) {
            RealPolygon polygon = polygons[i];
            addContourPolygon(info, colorValues, (Vector3D) dataArray[polygon
                    .getVertexIndexById(0)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(1)], (Vector3D) dataArray[polygon
                    .getVertexIndexById(2)]);
        }
        for (int i = 0; i < vertexList.size(); i++) {
            VectorQD vec = (VectorQD) vertexList.get(i);
            info.real2scene(vec);
        }
    }

    private void assembleParts(RenderingInfo3D info) {
        VectorQD[] vertices = new VectorQD[vertexList.size()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = (VectorQD) vertexList.get(i);
        }
        // System.out.println(vertexList.size());
        LinePair[] pairs = new LinePair[lineList.size()];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = (LinePair) lineList.get(i);
        }
        Surface[] surfaces = new Surface[surfaceList.size()];
        for (int i = 0; i < surfaces.length; i++) {
            surfaces[i] = (Surface) surfaceList.get(i);
            surfaces[i].setReversible(true);
            // System.out.println(surfaces[i]);
        }
        info.addSceneObject(new PolygonObject(vertices, pairs, surfaces));
    }

    // work area
    private Vector3D ct1 = new Vector3D();

    private Vector3D ct2 = new Vector3D();

    private Vector3D cc1 = new Vector3D();

    private Vector3D cc2 = new Vector3D();

    private Vector3D calc = new Vector3D();

    private ArrayList vertexStack = new ArrayList();

    private ArrayList lineList = new ArrayList();

    private ArrayList surfaceList = new ArrayList();

    private ArrayList vertexList = new ArrayList();

    private ArrayList[] vertexArrayCache;

    private double startx, iwidthx;// start of x (active range) and inverse
                                    // width

    private double isizex, isizey, isizez;// inverse size along each axis

    class VertexHolder {
        int id;

        Vector3D vertex;
    }

    private void addContourPolygon(RenderingInfo3D info,
            ColorValueInfo[] colorValues, Vector3D a1, Vector3D a2, Vector3D a3) {

        // sort ==> p1.z < p2.z < p3.z
        Vector3D p1 = null, p2 = null, p3 = null;
        if (a1.z < a2.z) {
            if (a2.z < a3.z) {
                p1 = a1;
                p2 = a2;
                p3 = a3;
            } else if (a3.z < a1.z) {
                p1 = a3;
                p2 = a1;
                p3 = a2;
            } else {
                p1 = a1;
                p2 = a3;
                p3 = a2;
            }
        } else {
            if (a1.z < a3.z) {
                p1 = a2;
                p2 = a1;
                p3 = a3;
            } else if (a3.z < a2.z) {
                p1 = a3;
                p2 = a2;
                p3 = a1;
            } else {
                p1 = a2;
                p2 = a3;
                p3 = a1;
            }
        }

        if (p3.z < colorValues[1].getValue()) {
            addPolygon(info, p1, p2, p3, colorValues[0].getColor());
            return;
        } else if (p1.z > colorValues[colorValues.length - 1].getValue()) {
            addPolygon(info, p1, p2, p3, colorValues[colorValues.length - 1]
                    .getColor());
            return;
        }

        vertexStack.clear();
        vertexStack.add(p1);
        int lastLevel = -1;
        boolean secondLine = false;

        for (int n = 1; n < colorValues.length; n++) {
            lastLevel = n;
            if (colorValues[n].getValue() < p1.z)
                continue;
            if (colorValues[n].getValue() > p3.z)
                break;

            double currentValue = colorValues[n].getValue();

            // search start position (1st time)
            if (search(currentValue, p1, p2, ct1)) {
                // search end position
                if (search(currentValue, p1, p3, ct2)) {
                    addPolygon(info, vertexStack, ct1, ct2, colorValues[n - 1]
                            .getColor());
                    // System.out.println("CL(f):"+colorValues[n-1].getColor());
                } else {
                    throw new InternalError("Wrong search : n=" + n + "\n" + p1
                            + p2 + p3);
                }
            } else {
                // search start position (2nd time)
                if (search(currentValue, p2, p3, ct1)) {
                    // search end position
                    if (search(currentValue, p1, p3, ct2)) {
                        if (!secondLine) {
                            vertexStack.add(0, p2);
                            secondLine = true;
                        }
                        addPolygon(info, vertexStack, ct1, ct2,
                                colorValues[n - 1].getColor());
                        // System.out.println("CL(a):"+colorValues[n-1].getColor());
                    } else {
                        throw new InternalError("Wrong search : n=" + n + "\n"
                                + p1 + p2 + p3);
                    }
                }
            }
        }
        if (lastLevel != -1) {
            if (!secondLine) {
                vertexStack.add(0, p2);
            }
            // System.out.println("LL:"+lastLevel);
            addPolygon(info, vertexStack, p3, colorValues[lastLevel - 1]
                    .getColor());
            // System.out.println("CL(last):"+colorValues[lastLevel-1].getColor());
        }
    }

    private void initVertexList(RenderingInfo3D info, int num, int gradationNum) {
        int numberOfCache = (int) (num * gradationNum / 300);
        // System.out.println("Cache:"+numberOfCache);
        if (numberOfCache < 1)
            numberOfCache = 1;
        vertexArrayCache = new ArrayList[numberOfCache];
        for (int i = 0; i < numberOfCache; i++) {
            vertexArrayCache[i] = new ArrayList();
        }
        vertexList.clear();
        RealRange range = info.getPlotContext().getActiveRange();
        iwidthx = 1. / range.width() / numberOfCache;
        startx = range.x();
        isizex = 1. / range.width();
        isizey = 1. / range.height();
        isizez = 1. / range.length();
    }

    /**
     * @return vertex ID
     */
    private int addVertex(RenderingInfo3D info, Vector3D realVertex) {
        int index = (int) ((realVertex.x - startx) * iwidthx);
        int id = getVertexFromCache(vertexArrayCache[index], realVertex);
        if (id != -1) {
            // System.out.println("Found: index:["+index+"] id["+id+"] :
            // "+realVertex);
            return id;
        }
        VertexHolder holder = new VertexHolder();
        holder.id = vertexList.size();
        holder.vertex = copyQD(realVertex);
        vertexArrayCache[index].add(holder);
        vertexList.add(holder.vertex);
        // System.out.println("index:["+index+"] id:["+holder.id+"]
        // "+realVertex+"");
        return holder.id;
    }

    private int getVertexFromCache(ArrayList vertexCache, Vector3D realVertex) {
        for (int i = 0; i < vertexCache.size(); i++) {
            VertexHolder holder = (VertexHolder) vertexCache.get(i);
            Vector3D ref = holder.vertex;
            calc.substitute(ref);
            calc.subs(realVertex);
            calc.x *= isizex;
            calc.y *= isizey;
            calc.z *= isizez;
            if (calc.getSquare() < 1e-10) {
                return holder.id;
            }
        }
        return -1;
    }

    private VectorQD copyQD(MathVector vec) {
        if (vec instanceof Vector3D)
            return new VectorQD((Vector3D) vec);
        return new VectorQD(vec.v(0), vec.v(1), vec.v(2));
    }

    private void addLine(int vertex1, int vertex2, Color color) {
        lineList.add(new LinePair(vertex1, vertex2, color));
    }

    private void addSurface(int vertex1, int vertex2, int vertex3, Color color) {
        Surface s = new Surface(vertex1, vertex2, vertex3, color);
        surfaceList.add(s);
    }

    private void addPolygon(RenderingInfo3D info, Vector3D p1, Vector3D p2,
            Vector3D p3, Color color) {
        int pid1 = addVertex(info, p1);
        int pid2 = addVertex(info, p2);
        int pid3 = addVertex(info, p3);
        addLine(pid1, pid2, color);
        addLine(pid2, pid3, color);
        addLine(pid3, pid1, color);
        addSurface(pid1, pid2, pid3, color);
    }

    private void addPolygon(RenderingInfo3D info, ArrayList vertexStack,
            Vector3D ct1, Color color) {
        // System.out.println("---------------");
        Vector3D[] vertices = new Vector3D[1 + vertexStack.size()];
        vertices[0] = ct1;
        for (int i = 0; i < vertexStack.size(); i++) {
            vertices[1 + i] = (Vector3D) vertexStack.get(i);
        }
        int[] ids = new int[vertices.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = addVertex(info, vertices[i]);
            // System.out.println("V:["+ids[i]+"] : "+ids[i]);
        }
        for (int i = 0; i < (ids.length - 1); i++) {
            addLine(ids[i], ids[i + 1], color);
        }
        addLine(ids[ids.length - 1], ids[0], color);
        if (ids.length == 3) {
            Surface s = new Surface(ids[0], ids[1], ids[2], color);
            surfaceList.add(s);
        } else {
            Surface s = new GSurface(ids, color);
            surfaceList.add(s);
        }
        vertexStack.clear();
        cc1.substitute(ct1);
        vertexStack.add(cc1);
    }

    private void addPolygon(RenderingInfo3D info, ArrayList vertexStack,
            Vector3D ct1, Vector3D ct2, Color color) {
        // System.out.println("---------------");
        Vector3D[] vertices = new Vector3D[2 + vertexStack.size()];
        vertices[0] = ct2;
        vertices[1] = ct1;
        for (int i = 0; i < vertexStack.size(); i++) {
            vertices[2 + i] = (Vector3D) vertexStack.get(i);
        }
        int[] ids = new int[vertices.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = addVertex(info, vertices[i]);
            // System.out.println("V:["+ids[i]+"] : "+ids[i]);
        }
        for (int i = 0; i < (ids.length - 1); i++) {
            addLine(ids[i], ids[i + 1], color);
        }
        addLine(ids[ids.length - 1], ids[0], color);
        if (ids.length == 3) {
            Surface s = new Surface(ids[0], ids[1], ids[2], color);
            surfaceList.add(s);
        } else {
            Surface s = new GSurface(ids, color);
            surfaceList.add(s);
        }
        vertexStack.clear();
        cc1.substitute(ct1);
        cc2.substitute(ct2);
        vertexStack.add(cc1);
        vertexStack.add(cc2);
    }

    private boolean search(double z, Vector3D p1, Vector3D p2, Vector3D output) {
        Vector3D lowVec, highVec;
        if (p1.z > p2.z) {
            highVec = p1;
            lowVec = p2;
        } else {
            highVec = p2;
            lowVec = p1;
        }
        if (z >= lowVec.z && z <= highVec.z) {
            // liner interpolation
            double length = highVec.z - lowVec.z;
            double highRatio = (z - lowVec.z) / length;
            double lowRatio = 1. - highRatio;
            output.set(lowVec.x * lowRatio + highVec.x * highRatio, lowVec.y
                    * lowRatio + highVec.y * highRatio, z);
            return true;
        }
        return false;
    }
}
