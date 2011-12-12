/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * utilities of making primitive meshes
 */
public class PrimitiveObjectMaker {

    static double eps = 1e-10;

    /**
     * make triangle mesh
     * 
     * @param r
     *            side legth
     * @param wireColor
     *            border color
     * @param surfaceColor
     *            surface color
     */
    public static IPolygonObject triangle(double r, Color wireColor,
            Color surfaceColor) {
        VectorQD[] vecs = new VectorQD[3];
        VectorQD av = new VectorQD(r / 2, 0, 0);
        vecs[0] = av;
        av = new VectorQD(-r / 2, 0, 0);
        vecs[1] = av;
        av = new VectorQD(0, 1.732 / 2 * r, 0);
        vecs[2] = av;
        IPolygonObject m = commonTriangle(vecs, wireColor, surfaceColor);
        m.translate(m.getCenter().qmult(-1));
        return m;
    }

    private static IPolygonObject commonTriangle(VectorQD[] vecs,
            Color wireColor, Color surfaceColor) {
        // make point pair
        LinePair[] ps = new LinePair[3];
        ps[0] = new LinePair(0, 1, wireColor);
        ps[1] = new LinePair(1, 2, wireColor);
        ps[2] = new LinePair(2, 0, wireColor);
        // surface
        Surface[] sf = new Surface[1];
        sf[0] = new Surface(0, 1, 2, surfaceColor);
        sf[0].setReversible(true);
        // make a mesh
        IPolygonObject m = new PolygonObject(vecs, ps, sf);
        return m;
    }

    /**
     * make arrow object
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param color
     * 
     * <pre>
     *  org			  end
     *  v			   v			 
     *  ---------------&gt;				 
     *  origine		   destinasion	 
     * </pre>
     */
    public static IPolygonObject arrow(VectorQD org, VectorQD end, Color color) {
        // 7
        // 5 3
        // 0 - |\
        // | ----------------2
        // 1 - |/
        // 6 4 8
        // make translate matrix
        VectorQD vec = end.qsub(org);
        double r = vec.getLength();
        MatrixQD rot = translateMatrix(vec);
        //
        double dr = r * 0.1;
        double sr = r * 0.2;
        double ssr = sr * 1.732;
        VectorQD[] vecs = new VectorQD[9];
        VectorQD av = new VectorQD(0, dr, 0);
        vecs[0] = move(av, rot, org);
        av = new VectorQD(0, -dr, 0);
        vecs[1] = move(av, rot, org);
        av = new VectorQD(r, 0, 0);
        vecs[2] = move(av, rot, org);
        av = new VectorQD(r - ssr, sr, 0);
        vecs[3] = move(av, rot, org);
        av = new VectorQD(r - ssr, -sr, 0);
        vecs[4] = move(av, rot, org);
        //
        av = new VectorQD(0, 0, dr);
        vecs[5] = move(av, rot, org);
        av = new VectorQD(0, 0, -dr);
        vecs[6] = move(av, rot, org);
        av = new VectorQD(r - ssr, 0, -sr);
        vecs[7] = move(av, rot, org);
        av = new VectorQD(r - ssr, 0, sr);
        vecs[8] = move(av, rot, org);
        //
        // make point pair
        LinePair[] ps = new LinePair[12];
        ps[0] = new LinePair(0, 1, color);
        ps[1] = new LinePair(1, 2, color);
        ps[2] = new LinePair(2, 0, color);
        ps[3] = new LinePair(3, 4, color);
        ps[4] = new LinePair(4, 2, color);
        ps[5] = new LinePair(2, 3, color);
        //
        ps[6] = new LinePair(5, 6, color);
        ps[7] = new LinePair(6, 2, color);
        ps[8] = new LinePair(2, 5, color);
        ps[9] = new LinePair(7, 8, color);
        ps[10] = new LinePair(8, 2, color);
        ps[11] = new LinePair(2, 7, color);
        // surface
        Surface[] sf = new Surface[4];
        sf[0] = new Surface(0, 1, 2, color);
        sf[0].setReversible(true);
        sf[1] = new Surface(3, 4, 2, color);
        sf[1].setReversible(true);
        sf[2] = new Surface(5, 6, 2, color);
        sf[2].setReversible(true);
        sf[3] = new Surface(7, 8, 2, color);
        sf[3].setReversible(true);
        // make a mesh
        IPolygonObject m = new PolygonObject(vecs, ps, sf);
        return m;
    }

    /**
     * make a thick line (negligence)
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param color
     */
    public static IPolygonObject thickLine_lite(VectorQD org, VectorQD end,
            Color color) {
        // make translate matrix
        VectorQD vec = end.qsub(org);
        double r = vec.getLength();
        MatrixQD rot = translateMatrix(vec);
        //
        double dr = r * 0.1;
        VectorQD[] vecs = new VectorQD[4];
        VectorQD av = new VectorQD(0, dr, 0);
        vecs[0] = move(av, rot, org);
        av = new VectorQD(0, -dr, 0);
        vecs[1] = move(av, rot, org);
        av = new VectorQD(r, 0, -dr);
        vecs[2] = move(av, rot, org);
        av = new VectorQD(r, 0, dr);
        vecs[3] = move(av, rot, org);
        IPolygonObject mesh = commonSquare(vecs, color, color);
        mesh.getSurfaceById(0).setReversible(true);
        return mesh;
    }

    /**
     * make a line
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param div
     *            division of line
     * @param radius
     *            rod radius
     * @param color
     * @return an <code>IPolygonObject</code> value
     */
    public static IGeometricObject thickLine(VectorQD org, VectorQD end,
            int div, double radius, Color color) {
        VectorQD vec = end.qsub(org).qmults(1.0 / div);
        ObjectGroup object = new ObjectGroup();
        VectorQD sp = (VectorQD) org.getCopy();
        VectorQD ep = org.qadd(vec);
        for (int i = 0; i < div; i++) {
            object.addObject(rod(sp, ep, radius, color));
            sp.qadds(vec);
            ep.qadds(vec);
        }
        return object;
    }

    /**
     * make a line
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param radius
     *            a <code>double</code> value
     * @param color
     * @return an <code>IPolygonObject</code> value
     */
    public static IPolygonObject rod(VectorQD org, VectorQD end, double radius,
            Color color) {
        // make translate matrix
        VectorQD vec = end.qsub(org);
        double r = vec.getLength();
        MatrixQD rot = translateMatrix(vec);
        //
        double dr = radius;

        double dh = dr / 2 * 3 / 1.73205;
        VectorQD[] vecs = new VectorQD[6];
        VectorQD av = null;
        av = new VectorQD(0, 0, dr); // 0
        vecs[0] = move(av, rot, org);
        av = new VectorQD(0, -dh, -dr / 2);// 1
        vecs[1] = move(av, rot, org);
        av = new VectorQD(0, dh, -dr / 2); // 2
        vecs[2] = move(av, rot, org);

        av = new VectorQD(r, 0, dr); // 3
        vecs[3] = move(av, rot, org);
        av = new VectorQD(r, -dh, -dr / 2);// 4
        vecs[4] = move(av, rot, org);
        av = new VectorQD(r, dh, -dr / 2); // 5
        vecs[5] = move(av, rot, org);

        QSurface[] surfaces = new QSurface[3];
        surfaces[0] = new QSurface(0, 3, 1, 4, color);
        surfaces[1] = new QSurface(1, 4, 2, 5, color);
        surfaces[2] = new QSurface(2, 5, 0, 3, color);

        LinePair[] lines = { 
			new LinePair(0, 1, color),
			new LinePair(1, 2, color), 
			new LinePair(2, 0, color),

			new LinePair(3, 4, color), 
			new LinePair(4, 5, color),
			new LinePair(5, 3, color),

			new LinePair(0, 3, color), 
			new LinePair(1, 4, color),
			new LinePair(2, 5, color), 
		};

        IPolygonObject mesh = new PolygonObject(vecs, lines, surfaces);
        return mesh;
    }

    /**
     * make a line
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param color
     */
    public static IWireObject thinLine(VectorQD org, VectorQD end, Color color) {
        // make translate matrix
        VectorQD[] vecs = { org, end };
        LinePair[] pare = { new LinePair(0, 1, color) };
        IWireObject mesh = new WireObject(vecs, pare);
        return mesh;
    }

    /**
     * make a line
     * 
     * @param org
     *            original position with 3d vector
     * @param end
     *            distination position with 3d vector
     * @param div
     *            number of division
     * @param color
     */
    public static IWireObject thinLine(VectorQD org, VectorQD end, int div,
            Color color) {
        // make translate matrix
        VectorQD calc = new VectorQD(end.sub(org).mults(1. / (div + 1)));
        VectorQD[] vecs = new VectorQD[div + 2];
        for (int i = 0; i <= div; i++) {
            vecs[i] = new VectorQD(org.add(calc.mult(i)));
        }
        vecs[div + 1] = end;
        LinePair[] pare = new LinePair[div + 1];
        for (int i = 0; i <= div; i++) {
            pare[i] = new LinePair(i, i + 1, color);
        }
        IWireObject mesh = new WireObject(vecs, pare);
        return mesh;
    }

	/**
	 * make a flat surface with single color.
	 * 
	 *               org                          
	 *                *                           
	 *               / \                          
	 *              /   \                         
	 *             /     \                        
	 *            /       \                       
	 *       c1  *         * c2                   
	 *            \       /                       
	 *             \     /                        
	 *              \   /                         
	 *               \ /                          
	 *                *                           
	 *
	 * @param org origin position
	 * @param corner1 corner position 1
	 * @param corner2 corner position 2
	 * @param div the number of division
	 * @param color surface color
	 * @return an <code>IPolygonObject</code> value
	 */
    public static IPolygonObject flatSquareSurface(VectorQD org,
            VectorQD corner1, VectorQD corner2, int div, Color color) {
        return flatSquareSurface(org, corner1, corner2, div, color, color);
    }

	/**
	 * make a flat checked surface with two colors.
	 * 
	 *               org                          
	 *                *                           
	 *               / \                          
	 *              /   \                         
	 *             /     \                        
	 *            /       \                       
	 *       c1  *         * c2                   
	 *            \       /                       
	 *             \     /                        
	 *              \   /                         
	 *               \ /                          
	 *                *                           
	 *
	 * @param org origin position
	 * @param corner1 corner position 1
	 * @param corner2 corner position 2
	 * @param div the number of division
	 * @param color1 surface color1
	 * @param color2 surface color2
	 * @return an <code>IPolygonObject</code> value
	 */
    public static IPolygonObject flatSquareSurface(VectorQD org,
            VectorQD corner1, VectorQD corner2, int div, Color color1,
            Color color2) {
        MathVector xvec = corner1.sub(org).mults(1.0 / div);
        MathVector yvec = corner2.sub(org).mults(1.0 / div);
        int pdiv = div + 1;
        VectorQD[] points = new VectorQD[pdiv * pdiv];
        for (int i = 0; i < pdiv; i++) {
            MathVector xorg = xvec.mult(i);
            for (int j = 0; j < pdiv; j++) {
                MathVector yorg = yvec.mult(j);
                points[i * pdiv + j] = new VectorQD(org.add(xorg).add(yorg));
            }
        }
        QSurface[] surfaces = new QSurface[div * div];
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                Color c = color1;
                if (((i - j) % 2) == 0) {
                    c = color2;
                }
                surfaces[i * div + j] = new QSurface(i * pdiv + j, i * pdiv + j
                        + 1, (i + 1) * pdiv + j, (i + 1) * pdiv + j + 1, c);
                surfaces[i * div + j].setReversible(true);
            }
        }
        LinePair[] lines = new LinePair[div * div * 2];
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                lines[i * div + j] = new LinePair(i * pdiv + j, i * pdiv + j
                        + 1, color1);
                lines[(i + div) * div + j] = new LinePair(j * pdiv + i, (j + 1)
                        * pdiv + i, color1);
            }
        }
        return new PolygonObject(points, lines, surfaces);
    }

    /**
     * make a translation matrix that translate (1,0,0) vector into given
     * vector.
     */
    public static MatrixQD translateMatrix(VectorQD vec) {
        vec.normalize();
        VectorQD ref = new VectorQD(1, 0, 0);
        if (Math.abs(ref.innerProduct(vec) - 1.) < eps) {
            // not change
            MatrixQD rot = new MatrixQD(1);
            return rot;
        }
        if (Math.abs(ref.innerProduct(vec) + 1.) < eps) {
            // opposite
            MatrixQD rot = new MatrixQD(-1);
            return rot;
        }
        VectorQD nz = vec.qouterProduct(ref);
        VectorQD ny = vec.qouterProduct(nz);
        nz.normalize();
        ny.normalize();
        VectorQD non = new VectorQD();
        VectorQD[] bases = { vec, ny, nz, non };
        MatrixQD rot = new MatrixQD(bases);
        rot = (MatrixQD) rot.getTrans();
        return rot;
    }

    /**
     * @param r
     *            input vector
     * @param rot
     *            rotation matrix
     * @param org
     *            origine of vector
     * @return new Vector( r * rot + org )
     */
    private static VectorQD move(VectorQD r, MatrixQD rot, VectorQD org) {
        VectorQD vec = new VectorQD();
        vec.substitute(r);
        vec.qmults(rot);
        vec.qadds(org);
        return vec;
    }

    /** make triangle mesh from given positions */
    public static IPolygonObject customTriangle(VectorQD[] pos,
            Color wireColor, Color surfaceColor) {
        if (pos.length < 3)
            return null;
        VectorQD[] vecs = new VectorQD[3];
        VectorQD av;
        for (int i = 0; i < 3; i++) {
            av = new VectorQD();
            av.substitute(pos[i]);
            vecs[i] = av;
        }
        return commonTriangle(vecs, wireColor, surfaceColor);
    }

    /**
     * make square mesh
     */
    public static IPolygonObject square(double r, Color wireColor,
            Color surfceColor) {
        VectorQD[] vecs = new VectorQD[4];
        VectorQD av = new VectorQD(0, 0, 0);
        vecs[0] = av;
        av = new VectorQD(r, 0, 0);
        vecs[1] = av;
        av = new VectorQD(r, r, 0);
        vecs[2] = av;
        av = new VectorQD(0, r, 0);
        vecs[3] = av;
        // make point pair
        IPolygonObject m = commonSquare(vecs, wireColor, surfceColor);
        m.translate((VectorQD) m.getCenter().qmult(-1));
        return m;
    }

    private static IPolygonObject commonSquare(VectorQD[] vecs,
            Color wireColor, Color surfaceColor) {
        // make point pair
        LinePair[] ps = new LinePair[4];
        ps[0] = new LinePair(0, 1, wireColor);
        ps[1] = new LinePair(1, 2, wireColor);
        ps[2] = new LinePair(2, 3, wireColor);
        ps[3] = new LinePair(3, 0, wireColor);
        // surface
        Surface[] sf = new Surface[1];
        sf[0] = new QSurface(0, 3, 1, 2, surfaceColor);
        // make a mesh
        IPolygonObject m = new PolygonObject(vecs, ps, sf);
        return m;
    }

    /** make tetrahedron mesh */
    public static IPolygonObject tetrahedron(double r, Color wireColor,
            Color surfaceColor) {
		//          3 .        Z axis
		//        .. . .           
		//      ..   .  ..         
		//     .     .    ..   X   
		//    .       ...... 0     
		//   2.......  .    ..      
		// Y  ...      . ..        
		//       ..... 1     XY plane

        double a = 2 * r / 1.316;
        VectorQD[] vecs = new VectorQD[4];
        VectorQD av = new VectorQD(a / 2, 0, 0);
        vecs[0] = av;
        av = new VectorQD(-a / 2, 0, 0);
        vecs[1] = av;
        av = new VectorQD(0, 1.732 / 2 * a, 0);
        vecs[2] = av;
        av = new VectorQD(0, 1.732 / 6 * a, Math.sqrt(6) / 3 * a);
        vecs[3] = av;
        // make point pair
        LinePair[] ps = new LinePair[6];
        ps[0] = new LinePair(0, 1, wireColor);
        ps[1] = new LinePair(0, 2, wireColor);
        ps[2] = new LinePair(0, 3, wireColor);
        ps[3] = new LinePair(1, 2, wireColor);
        ps[4] = new LinePair(1, 3, wireColor);
        ps[5] = new LinePair(2, 3, wireColor);
        // surface
        Surface[] sf = new Surface[4];
        sf[0] = new Surface(2, 0, 1, surfaceColor);
        sf[1] = new Surface(2, 1, 3, surfaceColor);
        sf[2] = new Surface(2, 3, 0, surfaceColor);
        sf[3] = new Surface(3, 1, 0, surfaceColor);
        // make a mesh
        IPolygonObject m = new PolygonObject(vecs, ps, sf);
        m.translate((VectorQD) m.getCenter().qmult(-1));
        return m;
    }

    /** make cubic mesh */
    public static IPolygonObject cube(double r, Color wireColor,
            Color surfaceColor) {
		//
		// 
		//	  7------6
		// 4------5	 |
		// |  |	  |	 |
		// |  |	  |	 |
		// |  3---|--2
		// 0------1
		// 
        VectorQD[] vecs = new VectorQD[8];
        VectorQD av = new VectorQD(0, 0, 0);
        vecs[0] = av;
        av = new VectorQD(r, 0, 0);
        vecs[1] = av;
        av = new VectorQD(r, r, 0);
        vecs[2] = av;
        av = new VectorQD(0, r, 0);
        vecs[3] = av;
        av = new VectorQD(0, 0, r);
        vecs[4] = av;
        av = new VectorQD(r, 0, r);
        vecs[5] = av;
        av = new VectorQD(r, r, r);
        vecs[6] = av;
        av = new VectorQD(0, r, r);
        vecs[7] = av;
        // make point pair
        LinePair[] ps = new LinePair[12];
        ps[0] = new LinePair(0, 1, wireColor);
        ps[1] = new LinePair(1, 2, wireColor);
        ps[2] = new LinePair(2, 3, wireColor);
        ps[3] = new LinePair(3, 0, wireColor);
        ps[4] = new LinePair(4, 5, wireColor);
        ps[5] = new LinePair(5, 6, wireColor);
        ps[6] = new LinePair(6, 7, wireColor);
        ps[7] = new LinePair(7, 4, wireColor);
        ps[8] = new LinePair(0, 4, wireColor);
        ps[9] = new LinePair(1, 5, wireColor);
        ps[10] = new LinePair(2, 6, wireColor);
        ps[11] = new LinePair(3, 7, wireColor);
        // surface
        Surface[] sf = new Surface[6];
        sf[0] = new QSurface(0, 3, 1, 2, surfaceColor);
        sf[1] = new QSurface(0, 1, 4, 5, surfaceColor);
        sf[2] = new QSurface(0, 4, 3, 7, surfaceColor);
        sf[3] = new QSurface(1, 2, 5, 6, surfaceColor);
        sf[4] = new QSurface(2, 3, 6, 7, surfaceColor);
        sf[5] = new QSurface(4, 5, 7, 6, surfaceColor);
        // make a mesh
        IPolygonObject m = new PolygonObject(vecs, ps, sf);
        m.translate((VectorQD) m.getCenter().qmult(-1));
        return m;
    }

    public static CircleObject sphere_lite(VectorQD p, double r,
            Color wireColor, Color surfaceColor) {
        return new CircleObject(p, r, wireColor, surfaceColor);
    }

    public static CircleObject sphere_lite(double r, Color wireColor,
            Color surfaceColor) {
        return new CircleObject(new VectorQD(0, 0, 0), r, wireColor,
                surfaceColor);
    }

    private static HashMap sphereCache = new HashMap();

    private static IPolygonObject getSphereCache(int div) {
        Object obj = sphereCache.get(new Integer(div));
        if (obj != null) {
            return (IPolygonObject) obj;
        }
        double r = 1;
        int vnum = div / 2;
        int max = div * vnum + 2;
        VectorQD[] points = new VectorQD[max];
        points[0] = new VectorQD(0, 0, -r);
        points[max - 1] = new VectorQD(0, 0, r);
        for (int i = 0; i < vnum; i++) {
            double theta = Math.PI * (i + 1.0) / (vnum + 1.0);
            for (int j = 0; j < div; j++) {
                double phi = Math.PI * j / div * 2;
                double z = -r * Math.cos(theta);
                double x = r * Math.sin(theta) * Math.cos(phi);
                double y = r * Math.sin(theta) * Math.sin(phi);
                points[j + i * div + 1] = new VectorQD(x, y, z);
            }
        }

        ArrayList lineList = new ArrayList();
        for (int i = 0; i < div; i++) {
            int ii = (i == (div - 1)) ? 0 : (i + 1);
            lineList.add(new LinePair(0, i + 1, Color.gray));
            lineList.add(new LinePair(max - 1, i + 1 + div * (vnum - 1),
                    Color.gray));
            lineList.add(new LinePair(i + 1 + div * (vnum - 1), ii + 1 + div
                    * (vnum - 1), Color.gray));
        }
        for (int i = 0; i < (vnum - 1); i++) {
            for (int j = 0; j < div; j++) {
                int jj = (j == (div - 1)) ? 0 : (j + 1);
                lineList.add(new LinePair(1 + j + i * div, 1 + jj + i * div,
                        Color.gray));
                lineList.add(new LinePair(1 + j + i * div, 1 + j + (i + 1)
                        * div, Color.gray));
            }
        }
        LinePair[] lines = new LinePair[lineList.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = (LinePair) lineList.get(i);
        }

        ArrayList surfaceList = new ArrayList();
        for (int i = 0; i < div; i++) {
            int ii = (i == (div - 1)) ? 0 : (i + 1);
            surfaceList.add(new Surface(0, ii + 1, i + 1, Color.blue));
            surfaceList.add(new Surface(max - 1, i + 1 + div * (vnum - 1), ii
                    + 1 + div * (vnum - 1), Color.blue));
        }
        for (int i = 0; i < (vnum - 1); i++) {
            for (int j = 0; j < div; j++) {
                int jj = (j == (div - 1)) ? 0 : (j + 1);
                int i1 = 1 + j + i * div;
                int i2 = 1 + jj + i * div;
                surfaceList.add(new QSurface(i1, i2, i1 + div, i2 + div,
                        Color.gray));
            }
        }
        Surface[] surfaces = new Surface[surfaceList.size()];
        for (int i = 0; i < surfaces.length; i++) {
            surfaces[i] = (Surface) surfaceList.get(i);
        }

        PolygonObject ret = new PolygonObject(points, lines, surfaces);
        sphereCache.put(new Integer(div), ret);
        return ret;
    }

    public static IPolygonObject sphere(double r, int div, Color wireColor,
            Color surfaceColor) {
        if (div < 3) {
            throw new RuntimeException(
                    "Division parameter should be more than 3. [" + div + "]");
        }
        IPolygonObject obj = (IPolygonObject) getSphereCache(div).getCopy();
        obj.expansion(r);
        for (int i = 0; i < obj.getLineNumber(); i++) {
            obj.getLinePareById(i).color = wireColor;
        }
        for (int i = 0; i < obj.getSurfaceNumber(); i++) {
            obj.getSurfaceById(i).setColor(surfaceColor);
        }

        return obj;
    }

}
