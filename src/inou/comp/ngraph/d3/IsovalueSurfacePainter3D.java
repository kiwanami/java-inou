/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph.d3;

import inou.comp.d3.GSurface;
import inou.comp.d3.IGeometricObject;
import inou.comp.d3.LinePair;
import inou.comp.d3.PolygonObject;
import inou.comp.d3.Surface;
import inou.comp.d3.WireObject;
import inou.comp.ngraph.ColorValueGenerator;
import inou.comp.ngraph.ColorValueInfo;
import inou.comp.ngraph.DefaultColorValueGenerator;
import inou.comp.ngraph.Plotter3D;
import inou.comp.ngraph.RealSolid;
import inou.comp.ngraph.RenderingInfo3D;
import inou.comp.ngraph.UPlotData;
import inou.comp.ngraph.VolumeData4D;
import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.vector.Vector3D;
import inou.math.vector.Vector4D;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

/** plot isovalue surface on 3D graph */

public class IsovalueSurfacePainter3D extends Plotter3D {

    private ColorValueGenerator colorValueGenerator = null;

    private double limit = 1e-20;

    private RealRange valueRange = null;

    private boolean drawSurface = true;

    public IsovalueSurfacePainter3D(VolumeData4D p) {
        super(p);
    }

    public IsovalueSurfacePainter3D(VolumeData4D p, ColorValueGenerator cvg) {
        super(p);
        setColorValueGenerator(cvg);
    }

    private VolumeData4D getVolumeData() {
        return (VolumeData4D) getData();
    }

    public void setColorValueGenerator(ColorValueGenerator cvg) {
        colorValueGenerator = cvg;
    }

    public RealRange getValueRange() {
        return valueRange;
    }

    public void setDrawSurface(boolean b) {
        drawSurface = b;
    }

    public boolean doesDrawSurface() {
        return drawSurface;
    }

    public void setValueRange(RealRange vr) {
        if (vr == null) {
            valueRange = null;
            return;
        }
        valueRange = new RealRange(vr.x(), vr.width());
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
        int num = 16;
        if (colorValueGenerator == null) {
            colorValueGenerator = new DefaultColorValueGenerator(num);
        }
        ColorValueInfo[] info = colorValueGenerator.getColorValueInfo(start,
                width * (((double) num) / (num + 1)) + start);
        Arrays.sort(info);
        return info;
    }

    // =================================
    // prepare polygons
    // =================================

    private void initScene(RenderingInfo3D info, MathVector[] dataArray) {
        ColorValueInfo[] colorValues = makeColorIndex(dataArray);
        if (colorValues == null)
            return;

        makePolygonParts(info, (Vector4D[]) dataArray, colorValues);
        assembleParts(info);
    }

    private ColorValueInfo[] makeColorIndex(MathVector[] dataArray) {
        RealRange range = null;
        if (valueRange == null) {
            range = UPlotData.getPartialRange(3, dataArray);
        } else {
            range = valueRange;
        }
        ColorValueInfo[] colorValues = getColorValueInfo(range.x(), range
                .width());
        // System.out.println("Range:"+range.toString());
        // System.out.println("Colol:"+colorValues.length);
        return colorValues;
    }

    private void makePolygonParts(RenderingInfo3D info, Vector4D[] dataArray,
            ColorValueInfo[] colorValues) {
        initVertexList(info, dataArray.length, colorValues.length);
        surfaceList.clear();
        lineList.clear();

        RealSolid[] solids = getVolumeData().getSolids(dataArray);
        for (int i = 0; i < solids.length; i++) {
            addIsosurfaces(info, dataArray, solids[i], colorValues);
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
        IGeometricObject object = null;
        if (drawSurface) {
            Surface[] surfaces = new Surface[surfaceList.size()];
            for (int i = 0; i < surfaces.length; i++) {
                surfaces[i] = (Surface) surfaceList.get(i);
                surfaces[i].setReversible(true);
            }
            object = new PolygonObject(vertices, pairs, surfaces);
        } else {
            object = new WireObject(vertices, pairs);
        }
        // System.out.println("Vertex:"+vertices.length);
        // System.out.println("Line:"+pairs.length);
        // System.out.println("Surface:"+surfaces.length);
        info.addSceneObject(object);
    }

    private void addIsosurfaces(RenderingInfo3D info, Vector4D[] dataArray,
            RealSolid solid, ColorValueInfo[] colorValues) {
        for (int i = 0; i < colorValues.length; i++) {
            // try {
            addIsosurface(info, dataArray, solid, colorValues[i]);
            /*
             * } catch (RuntimeException e) { System.err.println("SOLID:"); for
             * (int j=0;j<solid.getVertexNumber();j++) {
             * System.err.println("["+solid.getVertexIndexById(j)+"]
             * "+dataArray[solid.getVertexIndexById(j)]); } e.printStackTrace();
             * System.exit(0); }
             */
        }
    }

    private void addIsosurface(RenderingInfo3D info, Vector4D[] dataArray,
            RealSolid solid, ColorValueInfo colorValue) {
        int number = 0;
        if (solid.getVertexNumber() < 4)
            throw new RuntimeException("invalid solid data : v=["
                    + solid.getVertexNumber() + "]");

        // System.out.println("#########");
        Vector3D midVtx = new Vector3D();
        outerloop: for (int i = 0; i < coupleVertex1.length; i++) {
            if (search(colorValue.getValue(), dataArray[solid
                    .getVertexIndexById(coupleVertex1[i])], dataArray[solid
                    .getVertexIndexById(coupleVertex2[i])], midVtx)) {
                // try {
                int midId = addVertex(info, midVtx);
                for (int j = 0; j < number; j++) {
                    if (midId == valueHolders[j].valueIndex) {
                        continue outerloop;
                    }
                }
                valueHolders[number++].setValue(solid
                        .getVertexIndexById(coupleVertex1[i]), solid
                        .getVertexIndexById(coupleVertex2[i]), midId);
                /*
                 * } catch (RuntimeException e) {
                 * //System.out.println("NUM:"+number);
                 * //System.out.println("i:"+i);
                 * //System.out.println("v1:"+solid.getVertexIndexById(coupleVertex1[i]));
                 * //System.out.println("v2:"+solid.getVertexIndexById(coupleVertex2[i]));
                 * //System.out.println("MIDV:"+midVtx);
                 * //System.out.println("CV:"+colorValue.getValue()); for (int
                 * j=0;j<4;j++) { //System.out.println("HD["+j+"] --------");
                 * System.err.println("V1 : "+valueHolders[j].vertex1);
                 * System.err.println("V2 : "+valueHolders[j].vertex2);
                 * System.err.println("US : "+valueHolders[j].used);
                 * System.err.println("value : "+valueHolders[j].valueIndex); }
                 * throw e; }
                 */
            }
        }
        if (number == 0)
            return;
        if (number == 3) {
            addPolygon(info, valueHolders[0].valueIndex,
                    valueHolders[1].valueIndex, valueHolders[2].valueIndex,
                    colorValue.getColor());
        } else if (number == 4) {
            valueHolders[0].used = true;
            int hid2 = getNeighberValue(0);
            int hid3 = getNeighberValue(hid2);
            int hid4 = getNeighberValue(hid3);
            addPolygon(info, valueHolders[0].valueIndex,
                    valueHolders[hid2].valueIndex,
                    valueHolders[hid3].valueIndex,
                    valueHolders[hid4].valueIndex, colorValue.getColor());
        } else {
            // System.err.println("Wrong iso-values : V="+number);
        }
    }

    private int getNeighberValue(int holderId) {
        int v1 = valueHolders[holderId].vertex1;
        int v2 = valueHolders[holderId].vertex2;
        for (int i = 1; i < valueHolders.length; i++) {
            if (!valueHolders[i].used
                    && (valueHolders[i].vertex1 == v1
                            || valueHolders[i].vertex2 == v1
                            || valueHolders[i].vertex1 == v2 || valueHolders[i].vertex2 == v2)) {
                valueHolders[i].used = true;
                return i;
            }
        }
        System.err.println("Wrong values! : " + v1 + ", " + v2);
        for (int i = 0; i < valueHolders.length; i++) {
            System.err.println("V1 : " + valueHolders[i].vertex1);
            System.err.println("V2 : " + valueHolders[i].vertex2);
            System.err.println("US : " + valueHolders[i].used);
            System.err.println("value : " + valueHolders[i].valueIndex);
        }
        throw new RuntimeException();
    }

    private int[] coupleVertex1 = { 0, 0, 0, 1, 1, 2 };

    private int[] coupleVertex2 = { 1, 2, 3, 2, 3, 3 };

    private ValueHolder[] valueHolders;

    class ValueHolder {
        int vertex1, vertex2;

        int valueIndex;

        boolean used = false;

        void setValue(int vertex1, int vertex2, int valueIndex) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.valueIndex = valueIndex;
            used = false;
        }
    }

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
        valueHolders = new ValueHolder[4];
        for (int i = 0; i < valueHolders.length; i++)
            valueHolders[i] = new ValueHolder();
    }

    /**
     * @return vertex ID
     */
    private int addVertex(RenderingInfo3D info, Vector3D realVertex) {
        int index = (int) ((realVertex.x - startx) * iwidthx);
        // System.out.println("Add:"+realVertex);
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

    private Vector3D calc = new Vector3D();

    private int getVertexFromCache(ArrayList vertexCache, Vector3D realVertex) {
        for (int i = 0; i < vertexCache.size(); i++) {
            VertexHolder holder = (VertexHolder) vertexCache.get(i);
            Vector3D ref = holder.vertex;
            calc.substitute(ref);
            calc.subs(realVertex);
            calc.x *= isizex;
            calc.y *= isizey;
            calc.z *= isizez;
            if (calc.getSquare() < 1e-26) {
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
        surfaceList.add(new Surface(vertex1, vertex2, vertex3, color));
    }

    private void addPolygon(RenderingInfo3D info, int pid1, int pid2, int pid3,
            Color color) {
        addLine(pid1, pid2, color);
        addLine(pid2, pid3, color);
        addLine(pid3, pid1, color);
        addSurface(pid1, pid2, pid3, color);
    }

    private void addPolygon(RenderingInfo3D info, int ct1, int ct2, int ct3,
            int ct4, Color color) {
        int[] ids = { ct1, ct2, ct3, ct4 };
        for (int i = 0; i < (ids.length - 1); i++) {
            addLine(ids[i], ids[i + 1], color);
        }
        addLine(ids[ids.length - 1], ids[0], color);
        surfaceList.add(new GSurface(ids, color));
    }

    private boolean search(double t, Vector4D p1, Vector4D p2, Vector3D output) {
        Vector4D lowVec, highVec;
        if (p1.t > p2.t) {
            highVec = p1;
            lowVec = p2;
        } else {
            highVec = p2;
            lowVec = p1;
        }
        // System.out.println("Search---("+t+")");
        // System.out.println("V1:"+p1);
        // System.out.println("V2:"+p2);
        if (t > lowVec.t && t <= highVec.t) {
            // liner interpolation
            double length = highVec.t - lowVec.t;
            double highRatio = (t - lowVec.t) / length;
            double lowRatio = 1. - highRatio;
            output.set(lowVec.x * lowRatio + highVec.x * highRatio, lowVec.y
                    * lowRatio + highVec.y * highRatio, lowVec.z * lowRatio
                    + highVec.z * highRatio);
            // System.out.println("Found:"+output);
            return true;
        }
        return false;
    }
}
