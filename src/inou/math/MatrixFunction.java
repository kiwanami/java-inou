/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.awt.Dimension;

/**
 * This is a mathematical matrix class. It provides foundamental operations.
 */
public interface MatrixFunction {

    // ===========================
    // Matrix value
    // ===========================

    /**
     * return a dimension size of output matrix. (class "Dimension" is belong to
     * "java.awt")
     */
    public Dimension getDimension();

    /**
     * return the dimension size of intput vector.
     */
    public int getArgDimension();

    // ===========================
    // operation
    // ===========================

    /** return a matrix, obtained given functions and parameter. */
    public Matrix f(MathVector x);

}