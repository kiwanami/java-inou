/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import java.util.ArrayList;
import java.util.Random;

/** Small utilities */
public class MathUtil {

    private static Random randomServer = new Random();

    public static double eps = 1e-14;

    /**
     * if |a-b|<1^-14, return true.
     */
    public static boolean nearlyEqual(double a, double b) {
        if (Math.abs(a - b) > eps)
            return false;
        return true;
    }

    /**
     * return a uniform random number -d/2 < x < d/2
     */
    public static double random(double d) {
        return d * (0.5 - Math.random());
    }

    /**
     * return a regular random number <x>=0, <(<x>-x)^2>=1
     */
    public static double regRandom() {
        return randomServer.nextGaussian();
    }

    /**
     * @return if "a" is an integer, return true.
     */
    public static boolean integerTest(double a) {
        int b = (int) a;
        if (a == b)
            return true;
        return false;
    }

    /**
     * @return if "a" is an integer and 1, return true.
     */
    public static boolean unitTest(double a) {
        int b = (int) a;
        if (b == 1 && integerTest(a))
            return true;
        return false;
    }

    /**
     * @return if "a" is an integer and 0, return true.
     */
    public static boolean zeroTest(double a) {
        int b = (int) a;
        if (b == 0 && integerTest(a))
            return true;
        return false;
    }

    /**
     * search the minimum value in the given array.
     * 
     * @param d
     *            array
     * @return minimum index
     */
    public static int min(double[] d) {
        int num = d.length;
        int min = 0;
        for (int i = 1; i < num; i++) {
            if (d[min] > d[i]) {
                min = i;
            }
        }
        return min;
    }

    /**
     * search the minimum value in the given array.
     * 
     * @param d
     *            array
     * @param offset
     *            the offset to start to search
     * @return minimum index
     */
    public static int min(double[] d, int offset) {
        int num = d.length;
        if (num <= 0)
            return 0;
        int min = offset;
        for (int i = offset + 1; i < num; i++) {
            if (d[min] > d[i]) {
                min = i;
            }
        }
        return min;
    }

    /**
     * search the minimum value at the c-column in the given vector array.
     * 
     * @param d
     *            vector array
     * @param c
     *            column number in the vector
     * @return minimum index
     */
    public static int min(MathVector[] d, int c) {
        int num = d.length;
        int min = getFirstValidIndex(d, c);
        ;
        if (min == -1) {
            return -1;
        }
        for (int i = 1; i < num; i++) {
            if (isValidPoint(d[i], c) && d[min].v(c) > d[i].v(c)) {
                min = i;
            }
        }
        return min;
    }

    /**
     * search the maximum value in the given array.
     * 
     * @param d
     *            array
     * @return maximum index
     */
    public static int max(double[] d) {
        int num = d.length;
        int max = 0;
        for (int i = 1; i < num; i++) {
            if (d[max] < d[i]) {
                max = i;
            }
        }
        return max;
    }

    private static boolean isValidPoint(MathVector p, int col) {
        return (p != null) && (!Double.isNaN(p.v(col)));
    }

    private static int getFirstValidIndex(MathVector[] array, int col) {
        for (int i = 0; i < array.length; i++) {
            if (isValidPoint(array[i], col)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * search the maximum value at the c-column in the given vector array.
     * 
     * @param d
     *            vector array
     * @param c
     *            column number in the vector
     * @return maximum index
     */
    public static int max(MathVector[] d, int c) {
        int num = d.length;
        int max = getFirstValidIndex(d, c);
        if (max == -1) {
            return -1;
        }
        for (int i = max + 1; i < num; i++) {
            if (isValidPoint(d[i], c) && d[max].v(c) < d[i].v(c)) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Searches the specified array of doubles for the specified value using the
     * binary search algorithm. (this search method has compatibility to Java2
     * (java.util.Arrays)) The array <strong>must</strong> be sorted
     * 
     * @param a
     *            the array to be searched.
     * @param key
     *            the value to be searched for.
     * @return index of the search key, if it is contained in the list;
     *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
     *         <i>insertion point</i> is defined as the point at which the key
     *         would be inserted into the list: the index of the first element
     *         greater than the key, or <tt>list.size()</tt>, if all elements
     *         in the list are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if
     *         the key is found.
     */
    public static int binarySearch(double[] d, double key) {
        int low, high, middle;
        int num = d.length;

        low = 0;
        high = num - 1;

        if (d[num - 1] < key)
            return -1 - num;
        if (d[0] > key)
            return -1;
        try {
            while (low <= high) {
                middle = (low + high) / 2;
                if (key == d[middle])
                    return middle;
                if (key > d[middle] && key < d[middle + 1])
                    return -2 - middle;
                else if (key < d[middle])
                    high = middle - 1;
                else
                    low = middle + 1;
            }
        } catch (RuntimeException e) {
        }
        return -1;
    }

    /**
     * Searches the specified array of vectors for the specified value using the
     * binary search algorithm. (this search method has compatibility to Java2
     * (java.util.Arrays)) The array <strong>must</strong> be sorted
     * 
     * @param a
     *            the array to be searched.
     * @param colm
     *            column number in the vector.
     * @param key
     *            the value to be searched for.
     * @return index of the search key, if it is contained in the list;
     *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
     *         <i>insertion point</i> is defined as the point at which the key
     *         would be inserted into the list: the index of the first element
     *         greater than the key, or <tt>list.size()</tt>, if all elements
     *         in the list are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if
     *         the key is found.
     */
    public static int binarySearchN(MathVector[] d, double key, int colm) {
        int low, high, middle;
        int num = d.length;

        low = 0;
        high = num - 1;

        try {
            while (low <= high) {
                middle = (low + high) / 2;
                if (key >= d[middle].v(colm) && key < d[middle + 1].v(colm))
                    return middle - 1;
                else if (key < d[middle].v(colm))
                    high = middle - 1;
                else
                    low = middle + 1;
                if (middle >= (num - 1))
                    return num;
                if (middle < 0)
                    return -1;
            }
        } catch (RuntimeException e) {
        }
        return -1;
    }

    /**
     * make prime number array.
     */
    public static int[] getPrimeNumbers(int upperLimit) {
        if (upperLimit < 300) {
            return getPrimeNumbers_small(upperLimit);
        }
        return getPrimeNumbers_index(upperLimit);
    }

    private static int[] PRIME_NUMBERS = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
            31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101,
            103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167,
            173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239,
            241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307 };

    /**
     * fastest cache method
     */
    private static int[] getPrimeNumbers_small(int upperLimit) {
        for (int i = 0; i < PRIME_NUMBERS.length; i++) {
            if (PRIME_NUMBERS[i] > upperLimit) {
                int[] ret = new int[i];
                for (int j = 0; j < ret.length; j++) {
                    ret[j] = PRIME_NUMBERS[j];
                }
                return ret;
            }
        }
        throw new InternalError("Large? : " + upperLimit);
    }

    /**
     * general array index method
     */
    private static int[] getPrimeNumbers_index(int upperLimit) {
        if (upperLimit < 2)
            return new int[0];
        boolean[] index = new boolean[upperLimit];
        index[0] = true;// not prime number
        for (int i = 2; i <= upperLimit; i++) {
            if (index[i - 1] == true) {
                continue;
            }
            for (int j = i * 2; j <= upperLimit; j += i) {
                index[j - 1] = true;
            }
        }
        int length = 0;
        for (int i = 1; i < upperLimit; i++) {
            if (index[i] == false)
                length++;
        }
        int[] ret = new int[length];
        int count = 0;
        for (int i = 1; i < upperLimit; i++) {
            if (index[i] == false) {
                ret[count++] = i + 1;
            }
        }
        return ret;
    }

    /**
     * slow method...
     */
    private static int[] getPrimeNumbers_general(int upperLimit) {
        if (upperLimit < 2)
            return new int[0];
        ArrayList numberList = new ArrayList();
        numberList.add(new Integer(2));
        if (upperLimit >= 3) {
            primeNumberLoop: for (int i = 3; i <= upperLimit; i++) {
                for (int j = 0; j < numberList.size(); j++) {
                    int pn = ((Integer) numberList.get(j)).intValue();
                    if ((i % pn) == 0) {
                        continue primeNumberLoop;
                    }
                }
                numberList.add(new Integer(i));
            }
        }
        int[] ret = new int[numberList.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ((Integer) numberList.get(i)).intValue();
        }
        return ret;
    }

    /**
     * factorization into prime factors
     */
    public static int[] getPrimeFactors(int number) {
        int[] primeNumbers = getPrimeNumbers(number);
        ArrayList factorList = new ArrayList();
        int currentPNIndex = 0;
        int currentNumber = number;
        while (true) {
            if (currentNumber <= 1) {
                break;
            }
            int pn = primeNumbers[currentPNIndex];
            if ((currentNumber % pn) == 0) {
                currentNumber /= pn;
                factorList.add(new Integer(pn));
                continue;
            }
            currentPNIndex++;
            if (currentPNIndex >= primeNumbers.length) {
                throw new InternalError("failed factorization. : " + number);
            }
        }
        int[] ret = new int[factorList.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ((Integer) factorList.get(i)).intValue();
        }
        return ret;
    }

    /*
     * simple and faster method
     */
    public static int getGreatestCommon(int a, int b) {
        if (a <= 0 || b <= 0) {
            return 0;
        }
        int small = a, large = b;
        if (a > b) {
            small = b;
            large = a;
        }
        for (int i = small; i > 1; i--) {
            if ((large % i) == 0) {
                return i;
            }
        }
        return 1;
    }

    /*
     * public static void main(String [] args) { for (int i=1;i<=9;i++) { int
     * sample = 2<<i; long stime = System.currentTimeMillis(); int num =
     * 100000; for (int j=0;j<num;j++) { getGreatestCommon2(
     * (int)(Math.random()*sample), (int)(Math.random()*sample)); } long simple =
     * (System.currentTimeMillis()-stime);
     * 
     * stime = System.currentTimeMillis(); for (int j=0;j<num;j++) {
     * getGreatestCommon( (int)(Math.random()*sample),
     * (int)(Math.random()*sample)); } long factor =
     * (System.currentTimeMillis()-stime);
     * 
     * System.out.println(""+sample+" "+simple+" "+factor); } }
     */
}
