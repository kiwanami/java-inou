/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;

public interface IWireObject extends IGeometricObject {

    /**
     * return line number
     */
    public int getLineNumber();

    /**
     * return line object specified by line id.
     * 
     * @param id
     *            line id
     */
    public LinePair getLinePareById(int id);

    /**
     * return start vertex object specified by line id.
     * 
     * @param lineId
     *            line id
     */
    public VectorQD getStartVertex(int lineId);

    /**
     * return end vertex object specified by line id.
     * 
     * @param lineId
     *            line id
     */
    public VectorQD getEndVertex(int lineId);

    /**
     * return line color specified by line id.
     */
    public Color getColorById(int lineId);

}