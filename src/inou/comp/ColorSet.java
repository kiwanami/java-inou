/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Color;

/** make color index */
public class ColorSet {

    // geographics color set
    public static Color[] geographIndex = { new Color(0xcc00cc), Color.blue,
            Color.cyan, Color.yellow, Color.green, Color.red };

    // rainbow color set
    public static Color[] rainbowIndex = { new Color(0xcc00cc), Color.blue,
            Color.green, Color.yellow, Color.orange, Color.red };

    private int alpha = 255;

    private Color[] genIndex;

    private int number;

    private Color[] index;

    private boolean rev = false;

    /**
     * @param zeroc
     *            color of first index
     * @param endc
     *            color of last index
     * @param num
     *            number of index
     */
    public ColorSet(Color zeroc, Color endc, int num) {
        genIndex = new Color[] { zeroc, endc };
        number = num;
    }

    /**
     * Construct color index, re-indexing colors divided given number.
     * 
     * @param colors
     *            color index (you can use "geographIndex" or "rainbowIndex")
     * @param num
     *            number of index
     */
    public ColorSet(Color[] colors, int num) {
        genIndex = colors;
        number = num;
    }

    /**
     * @param colors
     *            color index
     */
    public ColorSet(Color[] colors) {
        genIndex = colors;
        number = colors.length;
    }

    public Color[] getColors() {
        return genIndex;
    }

    /** make gradation color index */
    public static Color[] makeColorIndex(Color[] set, int num) {
        return makeColorIndex(set, num, 0xff);
    }

    /** make gradation color index */
    public static Color[] makeColorIndex(Color[] set, int num, int alpha) {
        Color[] colorIndex = new Color[num];
        if (set.length == 1) {
            for (int i = 0; i < num; i++) {
                colorIndex[i] = set[0];
            }
            return colorIndex;
        }
        double vc, rh, rl;
        Color low, hi;
        int cc, ivc;
        double fr, fg, fb, br, bg, bb;
        for (int i = 0; i < num; i++) {
            vc = ((double) (set.length - 1) / num) * i;
            ivc = (int) vc;
            low = set[ivc];
            hi = set[ivc + 1];
            rh = vc - ivc;
            rl = 1. - rh;
            br = low.getRed();
            bg = low.getGreen();
            bb = low.getBlue();
            fr = hi.getRed();
            fg = hi.getGreen();
            fb = hi.getBlue();
            cc = (int) (br * rl + fr * rh) * 65536 + (int) (bg * rl + fg * rh)
                    * 256 + (int) (bb * rl + fb * rh) + (alpha << 24);
            if (alpha == 255) {
                colorIndex[i] = new Color(cc);
            } else {
                colorIndex[i] = new Color(cc, true);
            }
        }
        return colorIndex;
    }

    public void setReversible(boolean b) {
        rev = b;
    }

    public void setAlpha(int a) {
        alpha = a;
    }

    public int getNumberOfColors() {
        return number;
    }

    /**
     * @param i
     *            color index number
     * @return suitable color
     */
    public Color getColor(int i) {
        if (index == null) {
            index = makeColorIndex(genIndex, number, alpha);
        }
        if (i < 0) {
            if (rev) {
                i = -i;
            } else {
                i = 0;
            }
        }
        if (index.length <= i) {
            i = index.length - 1;
        }
        return index[i];
    }

    /**
     * @param d
     *            input value (0.0 <= d <= 1.0)
     * @return suitable color
     */
    public Color getColor(double d) {
        return getColor((int) Math.rint(d * number));
    }

    /**
     * @param d
     *            input value (0.0 <= d <= 1.0)
     * @return suitable color index as integer value
     */
    public int getRGB(double d) {
        int colorNumber = number - 1;
        int lowIndex = (int) (d * colorNumber);
        Color low = getColor(lowIndex);
        Color hi = getColor(lowIndex + 1);
        double discreteSize = 1. / colorNumber;
        double hiMix = d / discreteSize - ((double) lowIndex);
        double lowMix = 1. - hiMix;
        return (int) (low.getRed() * lowMix + hi.getRed() * hiMix) * 65536
                + (int) (low.getGreen() * lowMix + hi.getGreen() * hiMix) * 256
                + (int) (low.getBlue() * lowMix + hi.getBlue() * hiMix)
                + (alpha << 24);
    }

}