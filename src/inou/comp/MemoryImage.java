/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import inou.math.TriangleFunctions;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;

/**
 * Handle an image object made from integer array.
 * 
 * Note: Drawing time... drawing line ; background < memory << direct fill
 * polygon ; direct << background = memory
 */
public class MemoryImage {

    private MemoryImageSource source;
    private int width, height;
    private int[] pixel;
    private int[] scanBufferMin;
    private int[] scanBufferMax;

    final private static int BIT = 8;
    final private static int HIGH = 1 << 8;

    public MemoryImage(int width, int height) {
        pixel = new int[width * height];
        scanBufferMax = new int[height];
        scanBufferMin = new int[height];
        for (int i = 0; i < height; i++) {
            scanBufferMax[i] = Integer.MIN_VALUE;
            scanBufferMin[i] = Integer.MAX_VALUE;
        }
        this.width = width;
        this.height = height;
    }

    public void setMode(int mode) {
    }

    /**
     * translate 2D coordinate (x,y) into linear address in memry array.
     */
    final private int address(int x, int y) {
        return x + width * y;
    }

    /**
     * fill line along X axis
     * 
     * @param x
     *            x start position
     * @param y
     *            y position
     * @param w
     *            width to fill
     * @param rgb
     *            fill color
     */
    final private void fillLineX(int x, int y, int ex, int rgb) {
        if ((ex - x) < 0) {
            throw new RuntimeException("Minus width : " + (ex - x) + " at ("
                    + x + "," + y + ")");
            // System.err.println("Minus width : "+w);
        }
        if (x > width || ex < 0 || y >= height || y < 0) {
            return;
        }
        if (x < 0) {
            x = 0;
        }
        if (ex >= width) {
            ex = width - 1;
        }

        /* try { */
        fill(address(x, y), moreThan1(ex - x + 1), rgb);
        /*
         * }catch (RuntimeException e) { System.err.println("x:"+x);
         * System.err.println("ex:"+ex); System.err.println("w:"+(ex-x)); throw
         * e; }
         */
    }

    final private int getAlpha(int rgb) {
        return (rgb >> 24) & 0xff;
    }

    final private int getTrueRGB(int rgb) {
        return rgb & 0xffffff;
    }

    /**
     * fill line along X axis
     * 
     * @param x
     *            x start position
     * @param y
     *            y position
     * @param h
     *            height to fill
     * @param rgb
     *            fill color and alpha parameter
     */
    final private void fillLineY(int x, int y, int h, int rgb) {
        if (h < 0)
            throw new RuntimeException("Minus height : " + h + " at (" + x
                    + "," + y + ")");
        if (x >= width || x < 0 || y >= height || y < 0) {
            return;
        }
        int startAddress = address(x, y);
        int alpha = getAlpha(rgb);
        rgb = getTrueRGB(rgb);
        if (alpha == 255) {
            rgb |= 0xff000000;
            for (int i = 0; i < h && (i + y) < height; i++) {
                pixel[startAddress + i * width] = rgb;
            }
        } else {
            int malpha = 256 - alpha;
            int sr = ((rgb >> 16) & 255) * alpha;
            int sg = ((rgb >> 8) & 255) * alpha;
            int sb = (rgb & 255) * alpha;
            for (int i = 0; i < h && (i + y) < height; i++) {
                int address = startAddress + i * width;
                int original = pixel[address];
                int or = ((original >> 16) & 255) * malpha;
                int og = ((original >> 8) & 255) * malpha;
                int ob = (original & 255) * malpha;
                pixel[address] = (((sr + or) & 0xff00) << 8)
                        | ((sg + og) & 0xff00) | (((sb + ob) & 0xff00) >> 8)
                        | 0xff000000;
            }
        }
    }

    /**
     * draw a dot.
     */
    final public void poke(int x, int y, int rgb) {
        if (x >= width || x < 0 || y >= height || y < 0) {
            return;
        }
        int alpha = getAlpha(rgb);
        rgb = getTrueRGB(rgb);
        if (alpha == 255) {
            pixel[address(x, y)] = rgb | 0xff000000;
        } else {
            int malpha = 256 - alpha;
            int sr = ((rgb >> 16) & 255) * alpha;
            int sg = ((rgb >> 8) & 255) * alpha;
            int sb = (rgb & 255) * alpha;
            int address = address(x, y);
            int original = pixel[address];
            int or = ((original >> 16) & 255) * malpha;
            int og = ((original >> 8) & 255) * malpha;
            int ob = (original & 255) * malpha;
            pixel[address] = (((sr + or) & 0xff00) << 8) | ((sg + og) & 0xff00)
                    | (((sb + ob) & 0xff00) >> 8) | 0xff000000;
        }
    }

    /**
     * read a dot
     */
    final public int peek(int x, int y) {
        if (x >= width || x < 0 || y >= height || y < 0) {
            return -1;
        }
        return pixel[address(x, y)];
    }

    final private int round(int in) {
        return in >> BIT;
    }

    /**
     * create real Image object. Never call this method in paint(Graphics)
     * method.
     * 
     * @param parent
     *            parent Component
     */
    final public Image createImage(Component parent) {
        return createImage(parent, false);
    }

    /**
     * create real Image object. Never call this method in paint(Graphics)
     * method.
     * 
     * @param parent
     *            parent Component
     * @param anime
     *            animation awitch (see java.awt.image.MemoryImageSource)
     */
    final public Image createImage(Component parent, boolean anime) {
		if (parent != null) {
			source = new MemoryImageSource(width, height, pixel, 0, width);
			if (anime) {
				source.setAnimated(true);
				source.setFullBufferUpdates(true);
			}
			return parent.createImage(source);
		} else {
			throw new RuntimeException("MemoryImage needs Component object to create new Image.");
		}
    }

    /**
     * return the internal memory array.
     */
    final public int[] getMemoryArray() {
        return pixel;
    }

    /**
     * Send update message to parent component. When you finished drawing some
     * images into memory array, you call this method to update real image on
     * the component.
     */
    final public void update() {
		if (source != null) {
			source.newPixels();
		}
    }

    /**
     * @param x
     *            x-position at left-top of the rectangle
     * @param y
     *            y-position at left-top of the rectangle
     * @param w
     *            width of the rectangle
     * @param h
     *            height of the rectangle
     * @param rgb
     *            color value
     */
    final public void drawRect(int x, int y, int w, int h, int rgb) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        fillLineX(x, y, w + x, rgb);
        fillLineY(x, y, h, rgb);
        fillLineX(x, y + h, w + x, rgb);
        fillLineY(x + w, y, h, rgb);
    }

    /**
     * @param x
     *            x-position at left-top of the rectangle
     * @param y
     *            y-position at left-top of the rectangle
     * @param w
     *            width of the rectangle
     * @param h
     *            height of the rectangle
     * @param rgb
     *            color value
     */
    final public void fillRect(int x, int y, int w, int h, int rgb) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        for (int i = 0; i < h; i++) {
            fillLineX(x, y + i, w + x, rgb);
        }
    }

    final public void drawLine(int sx, int sy, int ex, int ey, int rgb) {
        try {
            if (ex == sx) {
                int ssy = min(sy, ey);
                int eey = max(sy, ey);
                fillLineY(sx, ssy, eey - ssy, rgb);
                return;
            }

            if (ey == sy) {
                int ssx = min(sx, ex);
                int eex = max(sx, ex);
                fillLineX(ssx, sy, eex, rgb);
                return;
            }
            drawLine_presenham(sx, sy, ex, ey, rgb);
            // drawLine_old(sx,sy,ex,ey,rgb);
        } catch (RuntimeException e) {
            System.err.println("Bug (" + e.getClass().getName() + ":"
                    + e.getMessage() + "): [sx:" + sx + ", sy:" + sy + "] [ex:"
                    + ex + ", ey:" + ey + "]");
            e.printStackTrace();
            System.exit(0);
        }
    }

    // faster algorythm
    final public void drawLine_presenham(int sx, int sy, int ex, int ey, int rgb) {
        int x = ex - sx;
        int addX = 1;
        if (x < 0) {
            x *= -1;
            addX = -1;
        }

        int y = ey - sy;
        int addY = 1;
        if (y < 0) {
            y *= -1;
            addY = -1;
        }

        if (x == 0 || y == 0) {
            if (sy >= 0 && sy < height)
                poke(sx, sy, rgb);
            return;
        }

        int tx = sx, ty = sy, z = 0;
        if (x >= y) {
            addY = ((ey - sy) << BIT) / x;
            y = sy << BIT;
            for (; z <= x; z++) {
                ty = round(y);
                poke(tx, ty, rgb);
                tx += addX;
                y += addY;
            }
        } else {
            addX = ((ex - sx) << BIT) / y;
            x = sx << BIT;
            for (; z <= y; z++) {
                tx = round(x);
                poke(tx, ty, rgb);
                ty += addY;
                x += addX;
            }
        }
    }

    final static private int min(int a, int b) {
        return Math.min(a, b);
    }

    final static private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    final static private int max(int a, int b) {
        return Math.max(a, b);
    }

    final static private int max(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    final static private int minIndex(int[] x, int a, int b) {
        return ((x[a] < x[b]) ? a : b);
    }

    final static private int maxIndex(int[] x, int a, int b) {
        return ((x[a] > x[b]) ? a : b);
    }

    final static private int minIndex(int[] a) {
        if (a[0] < a[1]) {
            if (a[2] < a[0])
                return 2;
            return 0;
        } else {
            // a1 < a0
            if (a[2] < a[1])
                return 2;
            return 1;
        }
    }

    final private void fill(int startAddress, int w, int rgb) {
        int alpha = getAlpha(rgb);
        rgb = getTrueRGB(rgb);
        if (alpha == 255) {
            rgb |= 0xff000000;
            java.util.Arrays.fill(pixel,startAddress,startAddress+w,rgb);
        } else {
            int endAddress = startAddress + w;
            int malpha = 256 - alpha;
            int sr = ((rgb >> 16) & 255) * alpha;
            int sg = ((rgb >> 8) & 255) * alpha;
            int sb = (rgb & 255) * alpha;
            for (int i = startAddress; i < endAddress; i++) {
                int original = pixel[i];
                int or = ((original >> 16) & 255) * malpha;
                int og = ((original >> 8) & 255) * malpha;
                int ob = (original & 255) * malpha;
                pixel[i] = (((sr + or) & 0xff00) << 8) | ((sg + og) & 0xff00)
                        | (((sb + ob) & 0xff00) >> 8) | 0xff000000;
            }
        }
    }

    final public void clear(int rgb) {
        fill(0, pixel.length, rgb);
    }

    /**
     * @param sx
     *            x positions (assume that the number array is 3)
     * @param sy
     *            y positions (assume that the number array is 3)
     * @param rgb
     *            color value
     */
    final public void fillPolygon(int[] sx, int[] sy, int rgb) {
        fillPolygon(sx, sy, sx.length, rgb);
    }

    /**
     * @param sx
     *            x positions (assume that the number array is 3)
     * @param sy
     *            y positions (assume that the number array is 3)
     * @param rgb
     *            color value
     */
    final public void fillPolygon(int[] sx, int[] sy, int length, int rgb) {
        try {
            if (sy[0] == sy[1] && sy[1] == sy[2] && length == 3) {
                int x = min(sx[0], sx[1], sx[2]);
                fillLineX(x, sy[0], max(sx[0], sx[1], sx[2]), rgb);
                return;
            }
            if (sx[0] == sx[1] && sx[1] == sx[2] && length == 3) {
                int y = min(sy[0], sy[1], sy[2]);
                fillLineY(sx[0], y, max(sy[0], sy[1], sy[2]) - y, rgb);
                return;
            }

            fillPolygon_presenham(sx, sy, length, rgb);
        } catch (RuntimeException e) {
            System.err.println("Bug (" + e.getClass().getName() + ":"
                    + e.getMessage() + "):" + " [x:" + sx[0] + ", y:" + sy[0]
                    + "] " + " [x:" + sx[1] + ", y:" + sy[1] + "] " + " [x:"
                    + sx[2] + ", y:" + sy[2] + "] ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    // =====================================================
    // Slower but general algorythm (see fillPolygon_old)
    // ========

    final private void fillPolygon_presenham(int[] sx, int[] sy, int length,
            int rgb) {
        try {
            fillPolygonAll_presenham(sx, sy, length, rgb);
        } catch (RuntimeException e) {
            for (int i = 0; i < height; i++) {
                System.err.println("## " + i + " : " + scanBufferMin[i] + " ,"
                        + scanBufferMax[i]);
            }
            System.err.println("Bug (" + e.getClass().getName() + ":"
                    + e.getMessage() + "):" + " [x:" + sx[0] + ", y:" + sy[0]
                    + "] " + " [x:" + sx[1] + ", y:" + sy[1] + "] " + " [x:"
                    + sx[2] + ", y:" + sy[2] + "] ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    final private void fillPolygonScanBuffer_presenham(int sx, int sy, int ex,
            int ey) {
        int x = ex - sx;
        int addX = 1;
        if (x < 0) {
            x *= -1;
            addX = -1;
        }

        int y = ey - sy;
        int addY = 1;
        if (y < 0) {
            y *= -1;
            addY = -1;
        }

        if (x == 0 && y == 0) {
            if (sy >= 0 && sy < height) {
                if (scanBufferMax[sy] < sx)
                    scanBufferMax[sy] = sx;
                if (scanBufferMin[sy] > sx)
                    scanBufferMin[sy] = sx;
            }
            return;
        }

        int tx = sx, ty = sy, z = 0;
        if (x >= y) {
            addY = ((ey - sy) << BIT) / x;
            y = sy << BIT;
            for (; z <= x; z++) {
                ty = round(y);
                if (ty >= 0 && ty < height) {
                    if (scanBufferMax[ty] < tx) {
                        scanBufferMax[ty] = tx;
                    }
                    if (scanBufferMin[ty] > tx) {
                        scanBufferMin[ty] = tx;
                    }
                }
                tx += addX;
                y += addY;
            }
        } else {
            addX = ((ex - sx) << BIT) / y;
            x = sx << BIT;
            for (; z < y; z++) {
                tx = round(x);
                if (ty >= 0 && ty < height) {
                    if (scanBufferMax[ty] < tx) {
                        scanBufferMax[ty] = tx;
                    }
                    if (scanBufferMin[ty] > tx) {
                        scanBufferMin[ty] = tx;
                    }
                }
                ty += addY;
                x += addX;
            }
        }
    }

    final private void fillPolygonFillBuffer(int top, int bottom, int rgb) {
        int min, max;
        for (int i = top; i <= bottom; i++) {
            min = scanBufferMin[i];
            max = scanBufferMax[i];
            if (min < scanBufferMax[i] && (min < width || max > 0)) {
                /*
                 * try {
                 */
                fillLineX(min, i, max, rgb);
                /*
                 * } catch (RuntimeException e) {
                 * System.err.println("top:"+top);
                 * System.err.println("bottm:"+bottom);
                 * System.err.println("i:"+i);
                 * System.err.println("scanmax[i]:"+scanBufferMax[i]);
                 * System.err.println("scanmin[i]:"+scanBufferMin[i]);
                 * e.printStackTrace(); throw e; }
                 */
            }
            scanBufferMax[i] = Integer.MIN_VALUE;
            scanBufferMin[i] = Integer.MAX_VALUE;
        }
    }

    final private void fillPolygonAll_presenham(int[] sx, int[] sy, int length,
            int rgb) {
        int lineNum = length;
        int cx, cy, px = sx[lineNum - 1], py = sy[lineNum - 1];
        int top = Math.min(Math.max(0, py), height - 1);
        int bottom = Math.min(Math.max(0, py), height - 1);
        for (int i = 0; i < lineNum; i++) {
            cy = sy[i];
            cx = sx[i];
            if (top > cy) {
                top = Math.max(cy, 0);
            }
            if (bottom < cy) {
                bottom = Math.min(cy, height - 1);
            }
            fillPolygonScanBuffer_presenham(px, py, cx, cy);
            px = cx;
            py = cy;
        }
        fillPolygonFillBuffer(top, bottom, rgb);
    }

    // ===============================================
    // Faster but draw only triangle (these routines have bug)
    // ========
    final private void fillPolygon_old(int[] sx, int[] sy, int rgb) {
        if (sy[0] == sy[1]) {
            fillPolygonBase(sx, sy, minIndex(sx, 0, 1), maxIndex(sx, 0, 1), 2,
                    rgb);
        } else if (sy[1] == sy[2]) {
            fillPolygonBase(sx, sy, minIndex(sx, 1, 2), maxIndex(sx, 1, 2), 0,
                    rgb);
        } else if (sy[0] == sy[2]) {
            fillPolygonBase(sx, sy, minIndex(sx, 0, 2), maxIndex(sx, 0, 2), 1,
                    rgb);
        } else {
            fillPolygonAll(sx, sy, rgb);
        }
    }

    final private void fillPolygonBase(int[] sx, int[] sy, int p1, int p2,
            int p3, int rgb) {
        if (sy[p3] < sy[p1])
            fillPolygonBaseUp(sx, sy, p1, p2, p3, rgb);
        else
            fillPolygonBaseDown(sx, sy, p1, p2, p3, rgb);
    }

    final private void fillPolygonBaseUp(int[] sx, int[] sy, int p1, int p2,
            int p3, int rgb) {
        int leftDx = sx[p3] - sx[p1];
        int rightDx = sx[p3] - sx[p2];
        int leftSign = 1;
        int rightSign = 1;
        if (leftDx < 0) {
            leftDx *= -1;
            leftSign = -1;
        }
        leftDx++;
        if (rightDx < 0) {
            rightDx *= -1;
            rightSign = -1;
        }
        rightDx++;
        int leftSlope = (leftDx << BIT) / (sy[p3] - sy[p1]);
        int rightSlope = (rightDx << BIT) / (sy[p3] - sy[p2]);
        int currentLeftX = sx[p1] << BIT;
        int currentRightX = sx[p2] << BIT;
        int end = sy[p3];
        for (int i = sy[p1]; i > end; i--) {
            fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
            currentLeftX -= leftSlope * leftSign;
            currentRightX -= rightSlope * rightSign;
        }
        poke(sx[p3], end, rgb);
    }

    final private void fillPolygonBaseDown(int[] sx, int[] sy, int p1, int p2,
            int p3, int rgb) {
        int leftSlope = ((sx[p3] - sx[p1]) << BIT) / (sy[p3] - sy[p1]);
        int rightSlope = ((sx[p3] - sx[p2]) << BIT) / (sy[p3] - sy[p2]);
        int currentLeftX = sx[p1] << BIT;
        int currentRightX = sx[p2] << BIT;
        int end = sy[p3];
        for (int i = sy[p1]; i < end; i++) {
            fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
            currentLeftX += leftSlope;
            currentRightX += rightSlope;
        }
        poke(sx[p3], end, rgb);
    }

    final static private void arrayIndex(int[] x, int[] results) {
        if (x[0] < x[1]) {
            if (x[2] < x[0]) {
                results[0] = 2;
                // results[1] = 0;
                results[2] = 1;
                return;
            }
            if (x[1] < x[2]) {
                // results[0] = 0;
                results[1] = 1;
                results[2] = 2;
                return;
            }
            // results[0] = 0;
            results[1] = 2;
            results[2] = 1;
            return;
        }

        // x1 < x0
        if (x[2] < x[1]) {
            results[0] = 2;
            results[1] = 1;
            // results[2] = 0;
            return;
        }
        if (x[0] < x[2]) {
            results[0] = 1;
            // results[1] = 0;
            results[2] = 2;
            return;
        }
        results[0] = 1;
        results[1] = 2;
        // results[2] = 0;
    }

    final private void fillPolygonAll(int[] sx, int[] sy, int rgb) {
        int[] index = { 0, 0, 0 };
        arrayIndex(sy, index);
        int x1 = sx[index[0]];
        int y1 = sy[index[0]];
        int x2 = sx[index[1]];
        int y2 = sy[index[1]];
        int x3 = sx[index[2]];
        int y3 = sy[index[2]];
        int longSlope = ((x3 - x1) << BIT) / (y3 - y1);
        int slopeUp = ((x2 - x1) << BIT) / (y2 - y1);
        int slopeDown = ((x3 - x2) << BIT) / (y3 - y2);
        int currentRightX = x1 << BIT;
        int currentLeftX = x1 << BIT;
        if (longSlope > slopeUp) {
            for (int i = y1; i < y2; i++) {
                fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
                currentLeftX += slopeUp;
                currentRightX += longSlope;
            }
            currentLeftX = x2 << BIT;
            for (int i = y2; i < y3; i++) {
                fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
                currentLeftX += slopeDown;
                currentRightX += longSlope;
            }
            poke(x3, y3, rgb);
        } else {
            for (int i = y1; i < y2; i++) {
                fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
                currentLeftX += longSlope;
                currentRightX += slopeUp;
            }
            currentRightX = x2 << BIT;
            for (int i = y2; i < y3; i++) {
                fillLineX(round(currentLeftX), i, round(currentRightX), rgb);
                currentLeftX += longSlope;
                currentRightX += slopeDown;
            }
            poke(x3, y3, rgb);
        }
    }

    final private static int moreThan1(int a) {
        return Math.max(a, 0);
    }

    public void drawCircle(int x, int y, int radious, int rgb) {
        int div = radious / 2;
        if (div < 2)
            div = 2;
        int xx1 = x + radious, xx2 = x + radious, xx3 = x - radious, xx4 = x
                - radious;
        int yy1 = y, yy2 = y, yy3 = y, yy4 = y;
        for (int i = 0; i <= div; i++) {
            int rad = i * 64 / div;
            int xx = TriangleFunctions.cos(rad, radious);
            int yy = TriangleFunctions.sin(rad, radious);
            int x1 = x + xx;
            int y1 = y - yy;
            drawLine(xx1, yy1, x1, y1, rgb);
            xx1 = x1;
            yy1 = y1;
            int x2 = x + xx;
            int y2 = y + yy;
            drawLine(xx2, yy2, x2, y2, rgb);
            xx2 = x2;
            yy2 = y2;
            int x3 = x - xx;
            int y3 = y - yy;
            drawLine(xx3, yy3, x3, y3, rgb);
            xx3 = x3;
            yy3 = y3;
            int x4 = x - xx;
            int y4 = y + yy;
            drawLine(xx4, yy4, x4, y4, rgb);
            xx4 = x4;
            yy4 = y4;
        }
    }

    public void drawPolygon(int[] sx, int[] sy, int rgb) {
        if (sx.length < 2)
            return;
        for (int i = 0; i < (sx.length - 1); i++) {
            drawLine(sx[i], sy[i], sx[i + 1], sy[i + 1], rgb);
        }
        drawLine(sx[sx.length - 1], sy[sx.length - 1], sx[0], sy[0], rgb);
    }

    private int[] ax = new int[10];

    private int[] ay = new int[10];

    public void fillCircle(int x, int y, int radious, int rgb) {
        int div = radious / 2;
        if (div < 2)
            div = 2;
        int num = div * 4;
        int hnum = div * 2;
        if (ax.length < num) {
            ax = new int[num];
            ay = new int[num];
        }
        for (int i = 0; i < div; i++) {
            int rad = (i * 64) / div;
            int xx = TriangleFunctions.cos(rad, radious);
            int yy = TriangleFunctions.sin(rad, radious);
            ax[i] = x + xx;
            ay[i] = y - yy;
            ax[hnum - i - 1] = x - xx;
            ay[hnum - i - 1] = y - yy;
            ax[i + hnum] = x - xx;
            ay[i + hnum] = y + yy;
            ax[num - i - 1] = x + xx;
            ay[num - i - 1] = y + yy;
        }
        fillPolygon(ax, ay, num, rgb);
    }
}
