/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

/** paint background image */
public class ImagePainter extends RectPainter {

    public static final int CENTER = 0;

    public static final int FILL = 1;

    public static final int FIT = 2;

    public static final int LEFTTOP = 3;

    protected Image image;

    protected int type = CENTER;

    protected ImageObserver parent;

    // ================================
    // constructor
    // ================================

    /**
     * constructor
     * 
     * @param i
     *            source image object
     * @param t
     *            paint type (CENTER,FILL,FIT,LEFTTOP)
     * @param p
     *            ImageObserver object
     */
    public ImagePainter(Image i, int t, ImageObserver p) {
        image = i;
        type = t;
        parent = p;
    }

    /**
     * constructor
     * 
     * @param path
     *            source image path or URL
     * @param t
     *            paint type (CENTER,FILL,FIT,LEFTTOP)
     * @param p
     *            ImageObserver object
     */
    public ImagePainter(String path, int t, ImageObserver p) {
        loadImage(path);
        type = t;
        parent = p;
    }

    /**
     * constructor (use CENTER paint mode)
     * 
     * @param i
     *            source image object
     * @param p
     *            ImageObserver object
     */
    public ImagePainter(Image i, ImageObserver p) {
        this(i, CENTER, p);
    }

    /**
     * constructor (use CENTER paint mode)
     * 
     * @param i
     *            source image path or URL
     * @param p
     *            ImageObserver object
     */
    public ImagePainter(String path, ImageObserver p) {
        this(path, CENTER, p);
    }

    private void loadImage(String path) {
        try {
            image = Toolkit.getDefaultToolkit().getImage(path);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + " : " + e.getMessage());
            System.err.println("Cannot load image. [" + path + "]");
            // e.printStackTrace();
        }
    }

    // ================================
    // access
    // ================================

    public void setImage(String path) {
        loadImage(path);
    }

    public void setImage(Image i) {
        image = i;
    }

    public Image getImage() {
        return image;
    }

    public void setType(int i) {
        type = i;
    }

    public int getType() {
        return type;
    }

    // ================================
    // operation
    // ================================

    public void filter(Color c) {
        ImageFilter f = new ColoredFilter(c);
        ImageProducer pro = new FilteredImageSource(image.getSource(), f);
        image = Toolkit.getDefaultToolkit().createImage(pro);

    }

    // ================================
    // private
    // ================================

    protected void work(Graphics g, Rectangle r) {
        int x = r.x, y = r.y;
        int w = image.getWidth(parent);
        int h = image.getHeight(parent);
        switch (type) {
        case FILL:
            if (w <= 0 || h <= 0)
                return;
            while (y < (r.height + r.y)) {
                x = r.x;
                while (x < (r.width + r.x)) {
                    g.drawImage(image, x, y, parent);
                    x += w;
                }
                y += h;
            }
            break;
        case FIT:
            g.drawImage(image, r.x, r.y, r.width, r.height, parent);
            break;
        case LEFTTOP:
            g.drawImage(image, r.x, r.y, parent);
            break;
        case CENTER:
        default:
            x = (r.width - w) >> 1;
            y = (r.height - h) >> 1;
            g.drawImage(image, r.x + x, r.y + y, parent);
            break;
        }
    }

    class ColoredFilter extends RGBImageFilter {
        int rr, gg, bb;

        public ColoredFilter(Color c) {
            // The filter's operation does not depend on the
            // pixel's location, so IndexColorModels can be
            // filtered directly.
            canFilterIndexColorModel = true;
            rr = c.getRed();
            gg = c.getGreen();
            bb = c.getBlue();
        }

        public int filterRGB(int x, int y, int rgb) {
            int a = (rgb & 0xff000000);
            int g = (rgb & 0x00ff00) >> 8;
            int r = (rgb & 0xff0000) >> 16;
            int b = (rgb & 0x0000ff);
            g = (gg + g) >> 1;
            r = (rr + r) >> 1;
            b = (b + bb) >> 1;
            return (r << 16) + (g << 8) + b + a;
        }
    }

}