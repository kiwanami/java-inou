/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Read a data file and build data columns that consists of the double array.
 */
public class Loader {

    /**
     * load a data from local file system and make an array object.
     * 
     * @param filename
     *            filename
     * @return array[line][column] object
     */
    public static double[][] load(String filename) throws IOException {
        return load(filename, null);
    }

    /**
     * load a data from local file system and make an array object.
     * 
     * @param filename
     *            filename
     * @param delim
     *            column separater (See java.util.StringTokenizer)
     * @return array[line][column] object
     */
    public static double[][] load(String filename, String delim)
            throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        try {
            return load(in, delim);
        } finally {
            in.close();
        }
    }

    /**
     * load a data from BufferedReader and make an array object.
     * 
     * @param filename
     *            filename
     * @param delim
     *            column separater (See java.util.StringTokenizer)
     * @return array[line][column] object
     */
    public static double[][] load(BufferedReader in, String delim)
            throws IOException {
        ArrayList list = new ArrayList();
        int columnNumber = -1;
        while (true) {
            String line = in.readLine();
            if (line == null)
                break;
            line = line.trim();
            if (line.length() == 0)
                continue;
            if (!line.substring(0, 1).equals("?")) {
                int col = line2cols(line, delim, list);
                if (col > columnNumber) {
                    columnNumber = col;
                }
            }
        }
        if (columnNumber < 1 || list.isEmpty()) {
            return null;
        }
        double[][] ret = new double[list.size()][];
        for (int i = 0; i < ret.length; i++) {
            double[] line = (double[]) list.get(i);
            if (line.length != columnNumber) {
                double[] copy = new double[columnNumber];
                for (int j = 0; j < line.length; j++) {
                    copy[j] = line[j];
                }
                line = copy;
            }
            ret[i] = line;
        }
        return ret;
    }

    private static int line2cols(String line, String delim, List list) {
        String[] cols = line.split(delim);
        double[] cold = new double[cols.length];
        for (int i = 0; i < cols.length; i++) {
            cold[i] = Double.parseDouble(cols[i]);
        }
        list.add(cold);
        return cold.length;
    }

    /** example of this class */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("usage : > java inou.math.util.Loader (file)");
            return;
        }
        // load test
        DataArraySet ds = new DataArraySet(Loader.load(args[0]));
        double[][] ar = ds.getArray();
        for (int i = 0; i < ar[0].length; i++) {
            for (int j = 0; j < ar.length; j++) {
                System.out.print(ar[j][i]);
                if (j < (ar.length - 1)) {
                    System.out.print(" : ");
                }
            }
            System.out.println();
        }
    }
}
