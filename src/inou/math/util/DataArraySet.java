/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.util;

/** Array data set, that consists of double arrays indexing with column. */
public class DataArraySet {

    private int col, row;

    private double[][] array;

    /**
     * Make data set.
     * 
     * @param a
     *            double array set
     * @see inou.math.util.Loader
     */
    public DataArraySet(double[][] a) {
        array = a;
        col = array.length;
        row = array[0].length;
    }

    /** return the number of row */
    public int getRow() {
        return row;
    }

    /** return the number of column */
    public int getColumn() {
        return col;
    }

    /** return the whole data arrays */
    public double[][] getArray() {
        return array;
    }

    /** return the data array of the [i]th column */
    public double[] getColumn(int i) {
        if (i >= 0 && i < col) {
            return array[i];
        }
        return null;
    }
}