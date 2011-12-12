/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.util;

import inou.math.AArrayFunction;
import inou.math.AFunction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** Array output utility. */
public class ArrayFileOut {

    /** column separator string (default : " ") */
    public static String columnSeparator = "  ";

    /** comment charactor (default : "#") */
    public static String commentChar = "# ";

    /** line separator char (default : depends OS ) */
    public static String n = System.getProperty("line.separator");

    /** file size limitter (default : true[ON]) */
    public static boolean opt = true;

    /** limitter line number (default : 2000) */
    public static int limit = 2000;

    public static void main(String[] a) throws Exception {
        double[] y = { 0, 1, 2, 3, 4, 5, 6 };
        write("arraytest.txt", "test array\nsecond line", 0, 2, "x-data", y,
                "y-data");
    }

    /** if set true, limit file size according to [limit] variable */
    public static void setLimitter(boolean b) {
        opt = b;
    }

    /**
     * write an array object into a file
     * 
     * @param filename
     *            filename to write data
     * @param comment
     *            comment to write into header of file
     * @param sx
     *            start position
     * @param dx
     *            fragment
     * @param xCom
     *            x comment
     * @param g
     *            data array [sampling number]
     * @param columnCom
     *            column comment
     */
    public static void write(String filename, String comment, double sx,
            double dx, String xCom, double[] g, String columnCom)
            throws IOException {
        int num = g.length;
        double x;
        double[][] sg = new double[2][];
        sg[0] = new double[num];
        for (int i = 0; i < num; i++) {
            x = dx * i + sx;
            sg[0][i] = x;
        }
        sg[1] = g;
        String[] cc = { xCom, columnCom };
        write(filename, comment, sg, cc);
    }

    /**
     * write a function object into a file.
     * 
     * @param filename
     *            filename to write data
     * @param comment
     *            comment to write into header of file
     * @param func
     *            data function
     */
    public static void write(String filename, String comment,
            AArrayFunction func) throws IOException {
        double[][] g;
        g = func.getArrays();
        String[] cc = { "x", "y" };
        write(filename, comment, g, cc);
    }

    /**
     * write a function object into a file.
     * 
     * @param filename
     *            filename to write data
     * @param comment
     *            comment to write into header of file
     * @param func
     *            data function
     * @param sx
     *            start position on x-axis
     * @param ex
     *            end position on x-axis
     * @param samplingNumber
     *            sampling number
     */
    public static void write(String filename, String comment, AFunction func,
            double sx, double ex, int samplingNumber) throws IOException {
        double[][] g;
        g = AArrayFunction.toArrays(sx, ex, samplingNumber, func);
        String[] cc = { "x", "y" };
        write(filename, comment, g, cc);
    }

    /**
     * write an array object into a file
     * 
     * @param filename
     *            filename to write data
     * @param g
     *            data array [2][sampling index] (x and y data)
     */
    public static void write(String filename, double[][] g) throws IOException {
        write(filename, null, g, null);
    }

    /**
     * write an array object into a file
     * 
     * @param filename
     *            filename to write data
     * @param comment
     *            comment to write into header of file, or null
     * @param g
     *            data array [2][sampling index] (x and y data)
     * @param columnCom
     *            column comment [dimension] or null
     * 
     * Note:[dimension] between g and columnCom must be the same. And in g,
     * [sampling number] of each [dimension] also must have the same number.
     */
    public static void write(String filename, String comment, double[][] g,
            String[] columnCom) throws IOException {
        int dim = g.length;
        int num = g[0].length;
        String mg = n + commentChar;

        PrintWriter numbers = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filename)));

        // file comment
        if (comment != null) {
            StringBuffer buf = new StringBuffer(comment);
            buf.insert(0, commentChar);
            int ps = buf.toString().indexOf("\n");
            try {
                while (ps > -1) {
                    buf.replace(ps, ps + 1, mg);
                    ps = buf.toString().indexOf("\n", ps + mg.length());
                }
            } catch (Exception e) {
            }
            ;
            numbers.println(buf.toString());
        }

        // column comments
        if (columnCom != null) {
            numbers.print(commentChar);
            for (int i = 0; i < dim; i++)
                numbers.print(columnCom[i] + columnSeparator);
            numbers.println("");
            numbers.flush();
        }

        // write
        int skip = 1;
        if (opt && (num > limit))
            skip = (int) (num / limit);

        for (int i = 0; i < num; i += skip) {
            for (int j = 0; j < dim; j++)
                numbers.print("" + g[j][i] + columnSeparator);
            numbers.println("");
        }
        numbers.flush();
        numbers.close();
    }
}
