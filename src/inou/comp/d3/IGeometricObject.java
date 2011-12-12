/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.MathVector;
import inou.math.vector.MatrixQD;
import inou.math.vector.VectorQD;

public interface IGeometricObject {

    public IGeometricObject getCopy();

    /**
     * returns all vertices that this object contains.
     * 
     * @return a <code>VectorQD[]</code> value
     */
    public VectorQD[] getVertices();

    /**
     * returns the vertex specified.
     * 
     * @return a <code>VectorQD[]</code> value
     */
    public VectorQD getVertex(int id);

    /**
     * applies rotation matrix around rotation center.
     * 
     * @param rot
     *            just rotation matrix. Not operate transition matrix.
     * @param temp
     *            working object.
     */
    public void rotate(MatrixQD rot, VectorQD temp);

    /**
     * applies rotation matrix around rotation center.
     * 
     * @param rot
     *            just rotation matrix. Not operate transition matrix.
     */
    public void rotate(MatrixQD rot);

    /**
     * applies translation matrix to vertices.
     * 
     * @param trans
     *            just transition matrix. Not operate rotation matrix.
     */
    public void translate(MatrixQD trans);

    /**
     * expands this object around rotation center.
     * 
     * @param t
     *            positive value. if t > 1, enlarges this object. if t < 1,
     *            reduces.
     */
    public void expansion(double t);

    /**
     * returns position of rotation center.
     */
    public VectorQD getCenter();

    /**
     * translate all vertices by given vector.
     */
    public void translate(VectorQD delta);

    /**
     * this object moves so that the center position is equal to given position.
     */
    public void setPosition(MathVector ps);

}